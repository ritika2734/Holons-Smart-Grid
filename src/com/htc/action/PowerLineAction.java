package com.htc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.utilities.CommonUtilities;
import com.htc.utilities.ConstantValues;

/**
 * This class contains all methods related to power lines
 *
 */
public class PowerLineAction extends CommonUtilities {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(PowerLineAction.class);

	/**
	 * This method is used to save a new power line to database and also perform other checks, for e.g., leadership election, 
	 * when a newly created main line joins two sets of power lines together or when a SUBLINE/POWERLINE connects holon object and power source respectively to main line.
	 */
	public void drawPowerLine() {
		try {
			//Fetching request parameters sent by client side functions
			Boolean isConnected = getRequest().getParameter("isConnected")!=null?Boolean.parseBoolean(getRequest().getParameter("isConnected")):false;
			Integer maxCapacity = getRequest().getParameter("maxCapacity")!=null && getRequest().getParameter("maxCapacity") != ""?Integer.parseInt(getRequest().getParameter("maxCapacity")):0;
			String powerLineType = getRequest().getParameter("powerLineType")!=null?getRequest().getParameter("powerLineType"):ConstantValues.MAINLINE;
			String reasonDown = getRequest().getParameter("reasonDown")!=null?getRequest().getParameter("reasonDown"):"";
			Double latStart = getRequest().getParameter("latStart")!=null?Double.parseDouble(getRequest().getParameter("latStart")):0D;
			Double lngStart = getRequest().getParameter("lngStart")!=null?Double.parseDouble(getRequest().getParameter("lngStart")):0D;
			Double latEnd = getRequest().getParameter("latEnd")!=null?Double.parseDouble(getRequest().getParameter("latEnd")):0D;
			Double lngEnd = getRequest().getParameter("lngEnd")!=null?Double.parseDouble(getRequest().getParameter("lngEnd")):0D;
			Integer powerLineForSubLine = getRequest().getParameter("powerLineForSubLine")!=null && getRequest().getParameter("powerLineForSubLine")!=""?
					Integer.parseInt(getRequest().getParameter("powerLineForSubLine")):0;
			Integer subLineHolonObjId=0;
			LatLng StartLatLng = new LatLng(latStart, lngStart);
			LatLng EndLatLng = new LatLng(latEnd, lngEnd);
			/*Function call saveLocation() to save LatLng in DB. A new object is saved in DB only if there is no
			corresponding entry which has the same sets of latitudes and longitudes.*/
			int newStartLatLngId = saveLocation(StartLatLng);
			int newEndLatLngId = saveLocation(EndLatLng);
			LatLng savedStartLatLng=getLatLngService().findById(newStartLatLngId);
			LatLng savedEndLatLng=getLatLngService().findById(newEndLatLngId);
			int currentCapacity= 0;
			//Preparing new power line object to be saved in Database.
			PowerLine powerLine = new PowerLine();
			powerLine.setCurrentCapacity(currentCapacity);
			powerLine.setIsConnected(isConnected);
			powerLine.setLatLngByDestination(savedEndLatLng);
			powerLine.setLatLngBySource(savedStartLatLng);
			powerLine.setMaximumCapacity(maxCapacity);
			powerLine.setReasonDown(reasonDown);
			powerLine.setType(powerLineType);
			PowerLine powerLineA = null, powerLineB = null;
			String colorOfHolonObject="";
			StringBuffer newCoordinatorsList = new StringBuffer();
			StringBuffer oldCoordinatorsList = new StringBuffer();
			ArrayList<HolonObject> connectedHolonObjects= new ArrayList<HolonObject>();
			if(powerLineType.equals(ConstantValues.SUBLINE)) {//Setting holon object in power line object when type is SUBLINE
				subLineHolonObjId=getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
				powerLine.setHolonObject(getHolonObjectService().findById(subLineHolonObjId));
				/*This code is not required as per new DB changes. Recursive reference of power line has now been removed.
				 * powerLine.setPowerLine(getPowerLineService().findById(powerLineIdForsubLine));*/
				Map<String, PowerLine> powerLineMap = splitPowerLineByLocation(powerLineForSubLine,savedEndLatLng);
				powerLineA = powerLineMap.get("powerLineA");
				powerLineB = powerLineMap.get("powerLineB");
						
			} else if(powerLineType.equals(ConstantValues.POWERSUBLINE)) {//Setting power source object in power line object when type is POWERSUBLINE
				subLineHolonObjId=getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
				powerLine.setPowerSource(getPowerSourceService().findById(subLineHolonObjId));
				/*This code is not required as per new DB changes. Recursive reference of power line has now been removed.
				 * powerLine.setPowerLine(getPowerLineService().findById(powerLineIdForsubLine));*/
				Map<String, PowerLine> powerLineMap = splitPowerLineByLocation(powerLineForSubLine,savedEndLatLng);
				powerLineA = powerLineMap.get("powerLineA");
				powerLineB = powerLineMap.get("powerLineB");
			}
			Integer newPowerLineID = getPowerLineService().persist(powerLine);//Saving power line object in database
			PowerLine powerLine2 = getPowerLineService().findById(newPowerLineID);
			String color = CommonUtilities.getLineColor(CommonUtilities.getPercent(currentCapacity,maxCapacity));//Getting line color for the newly created power line
			log.info("NewLy Generated powerLine  ID --> "+newPowerLineID);
			getResponse().setContentType("text/html");
			StringBuffer plResponse = new StringBuffer();
			//Preparing response to be used by client side functions
			plResponse.append(newPowerLineID+"!");
			plResponse.append(color);
			if(powerLineA != null && powerLineB != null) {
				plResponse.append("!"+powerLineA.getId()+"!");
				plResponse.append(powerLineB.getId());
			}
			
			//Code to make the current flow on addition of Sub line or Power line and Assign Holon Coordinator
			if(powerLineType.equals(ConstantValues.SUBLINE) || powerLineType.equals(ConstantValues.POWERSUBLINE)) {
				updateHolonObjectsAndPowerSources(newPowerLineID);
			}
			// Code to add color to holon object.
			if(powerLineType.equals(ConstantValues.SUBLINE)) {
				colorOfHolonObject= getHolonObjectService().findById(subLineHolonObjId).getHolon().getColor();
				plResponse.append("!"+colorOfHolonObject+"!"+subLineHolonObjId);
				HolonObject holonObject = getHolonObjectService().findById(subLineHolonObjId);
				HolonObject holonCoordinator = findConnectedHolonCoordinatorByHolon(holonObject.getHolon(), powerLine2);
				connectedHolonObjects =  getHolonObjectListByConnectedPowerLines(powerLine2,holonCoordinator);
				if(connectedHolonObjects != null){
					if(connectedHolonObjects.size()==0){
						//It is coordinator, show icon on it
						plResponse.append("!"+"Yes");
					} else {
						log.info("Start leadership Elections");
						HolonObject newCoordinator= getHolonCoordinatorByElectionUsingPowerLineId(powerLine2, holonObject);
						plResponse.append("!YesCoordinator"+"!"+newCoordinator.getId()+"~!"+holonCoordinator.getId()+"~");
					}
				}
				
			}
			//More code based on leadership election to get list of old and new coordinators. Old coordinator list is required to remove coordinator icon from map
			if(powerLineType.equals(ConstantValues.MAINLINE)){
				ArrayList<PowerLine> connectedToMainLine=connectedPowerLines(powerLine.getId());
				if(connectedToMainLine.size()>0){
					Map<String, ArrayList<HolonObject>> mapOfNewAndOldCoordinators = getHolonCoordinatorByElectionUsingForMainLineAndSwitch(powerLine2, "common");
					ArrayList<HolonObject> newCoordinators = mapOfNewAndOldCoordinators.get("newCoordinators");
					for(HolonObject holonObject : newCoordinators) {
						System.out.println("New Coordinator ID --> "+holonObject.getId());
						newCoordinatorsList.append(holonObject.getId()+"~");
					}
					ArrayList<HolonObject> oldCoordinators = mapOfNewAndOldCoordinators.get("oldCoordinators");
					for(HolonObject holonObject : oldCoordinators) {
						System.out.println("Old Coordinator ID --> "+holonObject.getId());
						oldCoordinatorsList.append(holonObject.getId()+"~");
					}
					
					if(oldCoordinatorsList!= null && oldCoordinatorsList.toString().length()>0 && newCoordinatorsList!=null && newCoordinatorsList.toString().length() > 0 ){
						plResponse.append("!0!0!null!null"+"!YesCoordinatoorMainLine"+"!"+newCoordinatorsList.toString()+"!"+oldCoordinatorsList.toString());
					}
				}
			}
			getResponse().getWriter().write(plResponse.toString());	
		}catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action drawPowerLine()");
		}
	}
	
	/**
	 * This method is used to fetch all power lines from database and prepare a response to be displayed in the power line info window.
	 */
	public void showPowerLines(){
		try {
			ArrayList<PowerLine> powerLineList = getPowerLineService().getAllPowerLine();//Fetch all power lines from database
			ArrayList<String> powerLineListArray = new ArrayList<String>();
			String startLocation;
			String endLocation;
			String color;
			if(powerLineList != null) {
				for(int i=0; i<powerLineList.size();i++){
					PowerLine  powerLine = powerLineList.get(i);
					startLocation = powerLine.getLatLngBySource().getLatitude()+"~"+powerLine.getLatLngBySource().getLongitude();
					endLocation = powerLine.getLatLngByDestination().getLatitude()+"~"+powerLine.getLatLngByDestination().getLongitude();
					color= CommonUtilities.getLineColor(CommonUtilities.getPercent(powerLine.getCurrentCapacity(),powerLine.getMaximumCapacity())); 				
					powerLineListArray.add(startLocation+"^"+endLocation+"!"+color+"!"+powerLine.getId()+"*");
				}
			}
			//Calling the response function and setting the content type of response.
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(powerLineListArray.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action showPowerLine()");
		}
	}
	
	/**
	 * This method is used to fetch power line info from database and then prepare response to be displayed in the info window of power line on map
	 */
	public void getPowerLineInfo () {
		try {
				Integer powerLineId = getRequest().getParameter("powerLineId")!=null?Integer.parseInt(getRequest().getParameter("powerLineId")):0;
				PowerLine  powerLine = getPowerLineService().findById(powerLineId);//Fetching power line object from database
				ArrayList<PowerLine> connectedPowerLines = connectedPowerLines(powerLineId); //Fetching list of all connected power lines
				int indexToRemove = -1;
				for(int i=0; i < connectedPowerLines.size(); i++) {
					if(connectedPowerLines.get(i).getId().equals(powerLineId)) {
						indexToRemove = i;		
					}
				}
				if(indexToRemove !=-1) {
					connectedPowerLines.remove(indexToRemove);//Removing self power line link
				}
				StringBuffer powerLineIds = new StringBuffer();
				powerLineIds.append("");
				double middleLatitude = 0L;
				double middleLongitude = 0L;
				for(PowerLine powerLine2 : connectedPowerLines) {
					middleLatitude = (powerLine2.getLatLngBySource().getLatitude()+powerLine2.getLatLngByDestination().getLatitude())/2;
					middleLongitude = (powerLine2.getLatLngBySource().getLongitude()+powerLine2.getLatLngByDestination().getLongitude())/2;
					powerLineIds.append(powerLine2.getId()+"!"+middleLatitude+"^"+middleLongitude+"~");
				}
				if(powerLineIds.length() > 0) {
					powerLineIds = powerLineIds.deleteCharAt(powerLineIds.lastIndexOf("~"));
				}
				HolonObject subLineHolonObject = powerLine.getHolonObject();
				PowerSource subLinePowerSrc=powerLine.getPowerSource();
				Integer subLineHolonObjectId= 0;
				Integer subLinePowerSrcId=0;
				if(subLineHolonObject!=null) {
					subLineHolonObjectId = subLineHolonObject.getId();
				}
				if(subLinePowerSrc!=null) {
					subLinePowerSrcId=subLinePowerSrc.getId();
				}
				String color = CommonUtilities.getLineColor(CommonUtilities.getPercent(powerLine.getCurrentCapacity(),powerLine.getMaximumCapacity()));
				boolean isConnectedToHolonOrPower=checkConnectedStatusForLine(connectedPowerLines,powerLine);
				//Calling the response function and setting the content type of response.
				StringBuffer respStr= new StringBuffer("");
				respStr.append(isConnectedToHolonOrPower+"*");
				respStr.append(powerLine.getId()+"*");
				respStr.append(powerLine.getMaximumCapacity()+"*");
				respStr.append(powerLine.getCurrentCapacity()+"*");
				respStr.append(powerLine.getType()+"*");
				respStr.append(powerLine.getLatLngBySource().getLatitude()+"~"+powerLine.getLatLngBySource().getLongitude()+"*");
				respStr.append(powerLine.getLatLngByDestination().getLatitude()+"~"+powerLine.getLatLngByDestination().getLongitude()+"*");
				respStr.append(subLineHolonObjectId+"*");
				respStr.append(subLinePowerSrcId+"*");
				respStr.append(color+"*");
				respStr.append(powerLineIds);
				
				getResponse().setContentType("text/html");
				getResponse().getWriter().write(respStr.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action getPowerLineInfo()");
		}
	}
	
	/**
	 * This method is used to split an existing power line into two lines based on the switch location passed as 2nd parameter.
	 * @param powerLineId The power line that needs to be split into two. 
	 * @param switchLatLng2 Location of switch, on the basis of which power line is split
	 * @return 
	 */
	public Map<String, PowerLine> splitPowerLineByLocation(Integer powerLineId, LatLng switchLatLng2) {
		PowerLine powerLineA = getPowerLineService().findById(powerLineId);//Fetch power line from database
		LatLng powerLineAEnd=switchLatLng2;
		LatLng powerLineBStart=switchLatLng2;
		LatLng powerLineBEnd = powerLineA.getLatLngByDestination();		
		powerLineA.setLatLngByDestination(powerLineAEnd);
		getPowerLineService().merge(powerLineA);//Saving updated power line object in database
		PowerLine powerLineB = new PowerLine();
		powerLineB.setCurrentCapacity(powerLineA.getCurrentCapacity());
		powerLineB.setIsConnected(powerLineA.isIsConnected());
		powerLineB.setLatLngByDestination(powerLineBEnd);
		powerLineB.setLatLngBySource(powerLineBStart);
		powerLineB.setMaximumCapacity(powerLineA.getMaximumCapacity());
		powerLineB.setReasonDown(powerLineA.getReasonDown());
		powerLineB.setType(powerLineA.getType());
		powerLineB.setPowerSource(powerLineA.getPowerSource());	
		getPowerLineService().persist(powerLineB);//Saving newly created power line in database
		//Update any switch connected to new Powerline
		new PowerSwitchAction().setNewPowerLineForExistingSwitch(powerLineB);		
		Map<String, PowerLine> powerLineMap = new HashMap<String, PowerLine>();
		powerLineMap.put("powerLineA", powerLineA);
		powerLineMap.put("powerLineB", powerLineB);
		return powerLineMap;
	}
	
	
	/**
	 * This method is used to update power line object with color of the line and send response to client side functions
	 */
	public void updatePowerLine() {
		try {
			String startLocation;
			String endLocation;
			String color;
			Integer powerLineId = getRequest().getParameter("powerLineId")!=null?Integer.parseInt(getRequest().getParameter("powerLineId")):0;
			PowerLine  powerLine =  getPowerLineService().findById(powerLineId);//Fetching power line object from database.
			startLocation = powerLine.getLatLngBySource().getLatitude()+"~"+powerLine.getLatLngBySource().getLongitude();
			endLocation = powerLine.getLatLngByDestination().getLatitude()+"~"+powerLine.getLatLngByDestination().getLongitude();
			color= CommonUtilities.getLineColor(CommonUtilities.getPercent(powerLine.getCurrentCapacity(),powerLine.getMaximumCapacity()));
			String resp=startLocation+"^"+endLocation+"!"+color+"!"+powerLine.getId()+"!"+powerLine.getMaximumCapacity();
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(resp);
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action updatePowerLine()");
		}
	}
	
	/**
	 * This method is used to edit a power line and save in database 
	 */
	public void editPowerLine() {
		try {
			Integer maxCapacity = getRequest().getParameter("maxCapacity")!=null?Integer.parseInt(getRequest().getParameter("maxCapacity")):0;
			Integer powerLineId = getRequest().getParameter("powerLineId")!=null?Integer.parseInt(getRequest().getParameter("powerLineId")):0;
			PowerLine  powerLine =  getPowerLineService().findById(powerLineId);
			powerLine.setMaximumCapacity(maxCapacity);
			powerLine.setCurrentCapacity(0);
			getPowerLineService().merge(powerLine);//Saving updated power line object in database
			ArrayList<PowerLine> connectedPowerLines = connectedPowerLines(powerLineId);
			int indexToRemove = -1;
			for(int i=0; i < connectedPowerLines.size(); i++) {
				if(connectedPowerLines.get(i).getId().equals(powerLineId)) {
					indexToRemove = i;		
				}
			}
			if(indexToRemove !=-1) {
				connectedPowerLines.remove(indexToRemove);//Removing self power line link
			}
			StringBuffer powerLineIds = new StringBuffer();
			powerLineIds.append("");
			double middleLatitude = 0L;
			double middleLongitude = 0L;
			for(PowerLine powerLine2 : connectedPowerLines) {
				middleLatitude = (powerLine2.getLatLngBySource().getLatitude()+powerLine2.getLatLngByDestination().getLatitude())/2;
				middleLongitude = (powerLine2.getLatLngBySource().getLongitude()+powerLine2.getLatLngByDestination().getLongitude())/2;
				powerLineIds.append(powerLine2.getId()+"!"+middleLatitude+"^"+middleLongitude+"~");
			}
			if(powerLineIds.length() > 0) {
				powerLineIds = powerLineIds.deleteCharAt(powerLineIds.lastIndexOf("~"));
			}
			boolean isConnectedToHolonOrPower=checkConnectedStatusForLine(connectedPowerLines,powerLine);
			String color=CommonUtilities.getLineColor(CommonUtilities.getPercent(powerLine.getCurrentCapacity(),powerLine.getMaximumCapacity()));
			//Preparing response for client side functions
			StringBuffer respStr= new StringBuffer("");
			respStr.append(isConnectedToHolonOrPower+"*");
			respStr.append(powerLine.getId()+"*");
			respStr.append(powerLine.getMaximumCapacity()+"*");
			respStr.append(powerLine.getCurrentCapacity()+"*");
			respStr.append(powerLine.getType()+"*");
			respStr.append(powerLine.getLatLngBySource().getLatitude()+"~"+powerLine.getLatLngBySource().getLongitude()+"*");
			respStr.append(powerLine.getLatLngByDestination().getLatitude()+"~"+powerLine.getLatLngByDestination().getLongitude()+"*");
			int holonObjectId = 0;
			int powerSourceId= 0;
			if(powerLine.getHolonObject() != null){
				holonObjectId = powerLine.getHolonObject().getId();
			}
			if(powerLine.getPowerSource() != null){
				powerSourceId = powerLine.getPowerSource().getId();
			}
			respStr.append(holonObjectId+"*");
			respStr.append(powerSourceId+"*");
			respStr.append(color+"*");
			respStr.append(powerLineIds);
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(respStr.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action editPowerLine()");
		}
	}
	
}