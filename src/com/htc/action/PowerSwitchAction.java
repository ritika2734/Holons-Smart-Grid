package com.htc.action;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSwitch;
import com.htc.utilities.CommonUtilities;

/**
 * This class contains all methods related to power switches.
 */
public class PowerSwitchAction extends CommonUtilities {

	/**
	 * Global(to class only) member map and array list to be used by the recursive function connectedPowerLines(Integer powerLineId) 
	 */
	private Map<Integer, PowerLine> listOfAllConnectedPowerLines = new TreeMap<Integer, PowerLine>();
	private ArrayList<PowerLine> listOfAllNeighbouringConnectedPowerLines = new ArrayList<PowerLine>();
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(PowerSwitchAction.class);
	
	/**
	 * This method is used to create power switch in the database. 
	 */
	public void createPowerSwitch(){
		try{
			PowerSwitch powerSwitch= new PowerSwitch();
			Integer powerLineId= getRequest().getParameter("powerLineId")!=null?Integer.parseInt(getRequest().getParameter("powerLineId")):0;
			Double latSwitch = getRequest().getParameter("switchPositionLat")!=null?Double.parseDouble(getRequest().getParameter("switchPositionLat")):0D;
			Double lngSwitch = getRequest().getParameter("switchPositionLng")!=null?Double.parseDouble(getRequest().getParameter("switchPositionLng")):0D;
			LatLng switchLatLng = new LatLng(latSwitch, lngSwitch);
			Integer switchlocationId = saveLocation(switchLatLng);
			LatLng switchLatLng2 = getLatLngService().findById(switchlocationId);
			//This function call splits an existing power line into two based on the switch location and returns a map with objects of the two power lines.
			Map<String, PowerLine> powerLineMap = new PowerLineAction().splitPowerLineByLocation(powerLineId,switchLatLng2);
			PowerLine powerLineA = powerLineMap.get("powerLineA");
			PowerLine powerLineB = powerLineMap.get("powerLineB");
			powerSwitch.setLatLng(switchLatLng2);
			powerSwitch.setPowerLineByPowerLineA(powerLineA);
			powerSwitch.setPowerLineByPowerLineB(powerLineB);
			powerSwitch.setStatus(true);
			Integer newPowerSwitchId= getPowerSwitchService().persist(powerSwitch); //Saving power switch object in database
			//Creating response to be used by the functions at client side
			String resp= newPowerSwitchId.toString()+"*"+powerLineA.getId().toString()+"*"+powerLineB.getId();
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(resp);
		}
		catch(Exception e){
			log.info("Exception in createPowerSwitch ::"+ e.getMessage());
		}
	}
	
	/**
	 * This method is used to get list of all power switches in database.
	 */
	public void getListPowerSwitch(){
		try{
			ArrayList<PowerSwitch> powerSwitchList = getPowerSwitchService().getAllPowerSwitch();
			ArrayList<String> swListArray = new ArrayList<String>();
			PowerSwitch powerSwitch= null;
			for(int i=0;i<powerSwitchList.size();i++) {
				powerSwitch=powerSwitchList.get(i);
				Double switchLatitude= powerSwitch.getLatLng().getLatitude();
				Double switchLongitude= powerSwitch.getLatLng().getLongitude();
				Integer switchId = powerSwitch.getId();
				boolean statusBool=powerSwitch.getStatus();
				int status=0;
				if(statusBool){
				status=1;
				}
				swListArray.add(switchLatitude+"^"+switchLongitude+"^"+switchId+"^"+status+"*");
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(swListArray.toString());
		}
		catch(Exception e){
			log.info("Exception in getListPowerSwitch::"+e.getMessage());
		}
	}


	/**
	 * This method gets switch info from database based on the switch ID and returns the necessary info to client side functions.
	 */
	public void getSwitchInfo() {
	try {
			Integer powerSwitchId= getRequest().getParameter("powerSwitchId")!=null?Integer.parseInt(getRequest().getParameter("powerSwitchId")):0;
			PowerSwitch powerSwitch = getPowerSwitchService().findById(powerSwitchId);
			Double switchLatitude= powerSwitch.getLatLng().getLatitude();
			Double switchLongitude= powerSwitch.getLatLng().getLongitude();
			Integer switchId = powerSwitch.getId();
			Integer powerLineAId = powerSwitch.getPowerLineByPowerLineA().getId();
			Integer powerLineBId = powerSwitch.getPowerLineByPowerLineB().getId();
			boolean statusBool=powerSwitch.getStatus();
			int status=0;
			if(statusBool) {
				status=1;
			}
			String contentString=switchLatitude+"^"+switchLongitude+"^"+switchId+"^"+powerLineAId+"^"+powerLineBId+"^"+status;		
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(contentString);
		} catch(Exception e) {
				log.info("Exception in getListPowerSwitch");
			}
	}
	
	/**
	 * This method is used to toggle switch mode between on and off. 
	 * When a switch is toggled a leadership election takes place between various holon objects to find the new holon coordinator among themselves. 
	 */
	public void powerSwitchOnOff() {
		try {
				Integer powerSwitchId= getRequest().getParameter("powerSwitchId")!=null?Integer.parseInt(getRequest().getParameter("powerSwitchId")):0;
				PowerSwitch pwSw= getPowerSwitchService().findById(powerSwitchId); 
				StringBuffer newCoordinatorIds= new StringBuffer();
				StringBuffer oldCoordinatorIds = new StringBuffer();
				boolean psOldStatus=pwSw.getStatus();
				boolean psNewStatus=true;
				Integer psNewIntStatus=1;
				if(psOldStatus) {
					psNewStatus=false;
					//off
					psNewIntStatus=0;
				}
				pwSw.setStatus(psNewStatus);
				getPowerSwitchService().merge(pwSw);
				PowerSwitch powerSwitch2 = getPowerSwitchService().findById(powerSwitchId);
				if(!powerSwitch2.getStatus()) {
					PowerLine newPowerLineA = powerSwitch2.getPowerLineByPowerLineA();
					PowerLine newPowerLineB = powerSwitch2.getPowerLineByPowerLineB();
					//Function call to get list of new and old coordinators based on leadership election algorithm.
					Map<String, ArrayList<HolonObject>> mapOfNewAndOldCoordinators = getHolonCoordinatorByElectionUsingForMainLineAndSwitch(newPowerLineA, "common");
					ArrayList<HolonObject> newCoordinators = mapOfNewAndOldCoordinators.get("newCoordinators");
					for(HolonObject holonObject : newCoordinators) {
						newCoordinatorIds.append(holonObject.getId()+"!");
					}
					ArrayList<HolonObject> oldCoordinators = mapOfNewAndOldCoordinators.get("oldCoordinators");
					for(HolonObject holonObject : oldCoordinators) {
						oldCoordinatorIds.append(holonObject.getId()+"!");
					}
					//Function call to get list of new and old coordinators based on leadership election algorithm.
					Map<String, ArrayList<HolonObject>> mapOfNewAndOldCoordinatorsB = getHolonCoordinatorByElectionUsingForMainLineAndSwitch(newPowerLineB, "powerSwitch");
					ArrayList<HolonObject> newCoordinatorsB = mapOfNewAndOldCoordinatorsB.get("newCoordinators");
					for(HolonObject holonObject : newCoordinatorsB) {
						newCoordinatorIds.append(holonObject.getId()+"!");
					}
					ArrayList<HolonObject> oldCoordinatorsB = mapOfNewAndOldCoordinatorsB.get("oldCoordinators");
					for(HolonObject holonObject : oldCoordinatorsB) {
						oldCoordinatorIds.append(holonObject.getId()+"!");
					}
				} else {
					PowerLine newPowerLineA = powerSwitch2.getPowerLineByPowerLineA();
					//Function call to get list of new and old coordinators based on leadership election algorithm.
					Map<String, ArrayList<HolonObject>> mapOfNewAndOldCoordinators = getHolonCoordinatorByElectionUsingForMainLineAndSwitch(newPowerLineA, "common");
					ArrayList<HolonObject> newCoordinators = mapOfNewAndOldCoordinators.get("newCoordinators");
					for(HolonObject holonObject : newCoordinators) {
						newCoordinatorIds.append(holonObject.getId()+"!");
					}
					ArrayList<HolonObject> oldCoordinators = mapOfNewAndOldCoordinators.get("oldCoordinators");
					for(HolonObject holonObject : oldCoordinators) {
						oldCoordinatorIds.append(holonObject.getId()+"!");
					}
				}
				
				getResponse().setContentType("text/html");
				if(newCoordinatorIds.toString().length()>0  || oldCoordinatorIds.toString().length()>0){
					getResponse().getWriter().write(psNewIntStatus.toString()+"*"+newCoordinatorIds.toString()+"*"+oldCoordinatorIds.toString());
				}else{
					getResponse().getWriter().write(psNewIntStatus.toString());
				}
				
			} catch(Exception e) {
				log.info("Exception in powerSwitchOnOff::"+e.getMessage());
				}
	}

	/* (non-Javadoc)
	 * @see com.htc.utilities.CommonUtilities#connectedPowerLines(java.lang.Integer)
	 */
	public ArrayList<PowerLine> connectedPowerLines(Integer powerLineId) {
		PowerLine powerLine = null;
		if(powerLineId.compareTo(0) > 0 ){
			powerLine = getPowerLineService().findById(powerLineId);
		}
		ArrayList<PowerLine> connectedPowerLines = new ArrayList<PowerLine>();
		ArrayList<PowerLine> removeIndexListA = new ArrayList<PowerLine>();
		ArrayList<PowerLine> removeIndexListB = new ArrayList<PowerLine>();
		
		if(!(powerLine == null)) {
			connectedPowerLines = getPowerLineService().getConnectedPowerLines(powerLine);
		}
		PowerLine powerLine2 = null;
		PowerSwitch powerSwitch = null;
		for(int i =0; i< connectedPowerLines.size();i++) {
			powerLine2 = connectedPowerLines.get(i);
			powerSwitch = getPowerSwitchService().checkSwitchStatusBetweenPowerLines(powerLine, powerLine2);
			if(!(powerSwitch == null)){
				if(!powerSwitch.getStatus()) {
					removeIndexListA.add(powerLine2);
				}
			}
		}
		for(int i=0; i<removeIndexListA.size();i++) {
			connectedPowerLines.remove(removeIndexListA.get(i));
		}
		for(PowerLine powerLine3 : connectedPowerLines) {
			if(!(listOfAllConnectedPowerLines.containsKey(powerLine3.getId()))) {
				listOfAllConnectedPowerLines.put(powerLine3.getId(), powerLine3);
				listOfAllNeighbouringConnectedPowerLines.add(powerLine3);
			}
		}
		for(PowerLine powerLine3 : connectedPowerLines) {
			ArrayList<PowerLine> tempConnectedPowerLines = getPowerLineService().getConnectedPowerLines(powerLine3);
			int indexToRemove = -1;
			for(int i=0; i<tempConnectedPowerLines.size(); i++) {
				PowerLine powerLine4 = tempConnectedPowerLines.get(i);
				if(powerLine4.getId().equals(powerLineId)) {
					indexToRemove = i;
				}
			}
			if(indexToRemove>= 0 ) {
				tempConnectedPowerLines.remove(indexToRemove);
			}
			PowerLine powerLineTemp = null;
			PowerSwitch powerSwitchTemp = null;
			for(int i =0; i< tempConnectedPowerLines.size();i++) {
				powerLineTemp = tempConnectedPowerLines.get(i);
				powerSwitchTemp = getPowerSwitchService().checkSwitchStatusBetweenPowerLines(powerLine3, powerLineTemp);
				if(!(powerSwitchTemp == null)) {
					if(!powerSwitchTemp.getStatus()) {
						removeIndexListB.add(powerLineTemp);
					}
				}
			}
			for(int i=0; i<removeIndexListB.size();i++) {
				tempConnectedPowerLines.remove(removeIndexListB.get(i));
			}
			for(PowerLine powerLine4 : tempConnectedPowerLines) {
				if(!(listOfAllConnectedPowerLines.containsKey(powerLine4.getId()))) {
					connectedPowerLines(powerLine3.getId());//Recursive call to get list of neighbors of neighbor
				}
			}
		}
		return listOfAllNeighbouringConnectedPowerLines;
	}

	/**
	 * This method is used to set a new power line for an existing switch. 
	 * @param powerLineB Power line object that needs to be set.
	 */
	public void setNewPowerLineForExistingSwitch(PowerLine powerLineB) {
		ArrayList<PowerSwitch> powerSwitchList = getPowerSwitchService().getAllPowerSwitch();
		PowerSwitch powerSwitch= null;
		for(int i=0;i<powerSwitchList.size();i++) {
			PowerSwitch pSwitch=powerSwitchList.get(i);
			LatLng switchLocation= pSwitch.getLatLng();
			if(switchLocation.equals(powerLineB.getLatLngByDestination())) {
				powerSwitch =pSwitch;
				break;
			}				
		}
		if(powerSwitch!=null) {
			powerSwitch.setPowerLineByPowerLineA(powerLineB);
			getPowerSwitchService().merge(powerSwitch);
		}
	}

}