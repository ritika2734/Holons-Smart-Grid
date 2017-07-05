package com.htc.utilities;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import com.htc.action.PowerSwitchAction;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.pojo.PowerSwitch;
import com.htc.hibernate.pojo.Supplier;
import com.htc.service.ServiceAware;
import com.opensymphony.xwork2.ActionContext;

/**
 *	This class contains variables and functions that are used by all action classes.
 */
public class CommonUtilities extends ServiceAware {

	private static final long serialVersionUID = 1L;

	//Global(to class only) member map and array list to be used by the recursive function connectedPowerLines(Integer powerLineId) 
	private Map<Integer, PowerLine> listOfAllConnectedPowerLines = new TreeMap<Integer, PowerLine>();
	private ArrayList<PowerLine> listOfAllNeighbouringConnectedPowerLines = new ArrayList<PowerLine>();
	
	static Logger log = Logger.getLogger(CommonUtilities.class);
	protected HttpServletResponse response; //HTTP response object to be used by all AJAX calls
	protected HttpServletRequest request; // HTTP request object to be used by all AJAX calls
	protected HttpSession httpSession; // HTTP session object to be used by all AJAX calls

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	}
	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public HttpSession getHttpSession() {
		return getRequest().getSession();
	}
	
	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	
	/**
	 * This method calculates the percentage power capacity remaining for the Power Line
	 * @param x This is the current remaining power capacity of the power line
	 * @param y This is the maximum capacity of the power line
	 * @return It returns the percentage of the power capacity currently available in the power line 
	 */
	public static int getPercent(int x, int y) {
		float xF=new Float(x);
		float yF=new Float(y);
		float perc= ((xF/yF)*100);
		log.info("x is "+x);
		log.info("y is "+y);
		log.info("percent is "+perc);
		return (int) perc;
	}
	
	/**
	 * This method returns the color of the line based on the current power capacity of the power line.
	 * @param percentCap It is the current power capacity of the line in percent.
	 * @return The color of the line. 
	 */
	public static String getLineColor(int percentCap) {
		String color="brown";
		if(0<percentCap && percentCap<=20) {
			color = "red";
		} else if(20<percentCap && percentCap<=50) {
			color = "yellow";
		} else if(percentCap>50 && percentCap<100) {
			color ="green";
		} else if(percentCap==100) {
			color ="black";
		}
		return color;
	}
	

	/**
	 * This method calculates energy details of the holon object passed as parameter and returns the same in the form of a map.
	 * @param holonObject Holon Object for which energy needs to be calculated
	 * @return Map of various parameters containing holon object energy details.
	 */
	public Map<String, Integer> getHolonObjectEnergyDetails(HolonObject holonObject) {
		Map<String, Integer> holonObjectEnergyDetails = new TreeMap<String, Integer>();
		Integer minimumEnergyRequired = 0;
		Integer maximumEnergyRequired = 0;
		Integer originalEnergyRequired = 0;
		Integer minimumProductionCapacity = 0;
		Integer maximumProductionCapacity = 0;
		Integer currentProduction=0;
		Integer currentEnergyRequired = 0;
		Integer flexibility = 0;
		Integer originalEnergyRequiredAfterCurrentProduction = 0;

		ArrayList<HolonElement> holonElementList= getHolonElementService().getHolonElements(holonObject);
		for(HolonElement holonElement : holonElementList) {
			if(holonElement.getHolonElementType().getProducer()) {
				minimumProductionCapacity = minimumProductionCapacity + holonElement.getHolonElementType().getMinCapacity();
				maximumProductionCapacity = maximumProductionCapacity + holonElement.getHolonElementType().getMaxCapacity();
				if(holonElement.getHolonElementState().getId()==1) {
					currentProduction = currentProduction + holonElement.getCurrentCapacity();
				}
			} else {
				minimumEnergyRequired = minimumEnergyRequired+holonElement.getHolonElementType().getMinCapacity();
				maximumEnergyRequired = maximumEnergyRequired+holonElement.getHolonElementType().getMaxCapacity();
				if(holonElement.getHolonElementState().getId()==1) {
					originalEnergyRequired = originalEnergyRequired+holonElement.getCurrentCapacity();
				}
			}
		}
		currentEnergyRequired = originalEnergyRequired;
		if(originalEnergyRequired > 0) {
			if(currentProduction > 0 && currentProduction <= originalEnergyRequired) {
				currentEnergyRequired = originalEnergyRequired - currentProduction;
			} else if(currentProduction > 0 && currentProduction > originalEnergyRequired) {
				currentEnergyRequired = 0;
			}
		}
		originalEnergyRequiredAfterCurrentProduction = currentEnergyRequired;
		Map<String, Integer> flexibilityAndCurrentRequirement = getFlexibilityAndCurrentEnergyRequirementOfHolonObject(holonObject, currentEnergyRequired);
		currentEnergyRequired = flexibilityAndCurrentRequirement.get("currentEnergyRequired");
		flexibility = flexibilityAndCurrentRequirement.get("flexibility");
		
		holonObjectEnergyDetails.put("noOfHolonElements", holonElementList.size());
		holonObjectEnergyDetails.put("minimumEnergyRequired", minimumEnergyRequired);
		holonObjectEnergyDetails.put("maximumEnergyRequired", maximumEnergyRequired);
		holonObjectEnergyDetails.put("originalEnergyRequired", originalEnergyRequired);
		holonObjectEnergyDetails.put("minimumProductionCapacity", minimumProductionCapacity);
		holonObjectEnergyDetails.put("maximumProductionCapacity", maximumProductionCapacity);
		holonObjectEnergyDetails.put("currentProduction", currentProduction);
		holonObjectEnergyDetails.put("currentEnergyRequired", currentEnergyRequired);
		holonObjectEnergyDetails.put("flexibility", flexibility);
		holonObjectEnergyDetails.put("originalEnergyRequiredAfterCurrentProduction", originalEnergyRequiredAfterCurrentProduction);
		
		return holonObjectEnergyDetails;
	}
	
	/**
	 * This method calculates energy details of the entire holon represented by the holon coordinator passed as parameter.
	 * @param holonCoordinator Holon Coordinator for which holon energy details needs to be calculated.
	 * @return Map of various parameters containing holon energy details.
	 */
	public Map<String, String> getHolonEnergyDetails(HolonObject holonCoordinator) {
		Map<String, String> holonEnergyDetails = new TreeMap<String, String>();
		PowerLine powerLine = getPowerLineService().getPowerLineByHolonObject(holonCoordinator);
		ArrayList<HolonObject> holonObjectListByConnectedPowerLines = getHolonObjectListByConnectedPowerLines(powerLine, holonCoordinator);
		Integer noOfHolonObjects = 0;
		StringBuffer holonObjectList=new StringBuffer("");
		Integer minimumProductionCapacityHolon = 0;
		Integer maximumProductionCapacityHolon = 0;
		Integer currentProductionHolon = 0;
		Integer minimumEnergyRequiredHolon = 0;
		Integer maximumEnergyRequiredHolon = 0;
		Integer currentEnergyRequiredHolon = 0;
		Integer originalEnergyRequiredHolon = 0;
		Integer flexibilityHolon = 0;
		Integer flexibilityPowerSource = 0;
		Integer minimumProductionCapacityPowerSource = 0;
		Integer maximumProductionCapacityPowerSource = 0;
		Integer currentProductionPowerSource = 0;
		boolean checkFlagForCoordinator = false;
		if(holonObjectListByConnectedPowerLines .size() == 0 && holonCoordinator.getIsCoordinator()) {
			holonObjectListByConnectedPowerLines.add(holonCoordinator);
			checkFlagForCoordinator = true;
		} else if(holonObjectListByConnectedPowerLines.size() > 0) {
			for(HolonObject holonObject : holonObjectListByConnectedPowerLines) {
				if(holonObject.getId() == holonCoordinator.getId()) {
					checkFlagForCoordinator = true;
				}
			}
		}
		if(!checkFlagForCoordinator) {
			if(holonCoordinator.getIsCoordinator()) {
				holonObjectListByConnectedPowerLines.add(holonCoordinator);
			}
		}
		
		noOfHolonObjects = holonObjectListByConnectedPowerLines.size();
		Map<String, Integer> holonObjectEnergyDetails = null;
		holonObjectList.append("<option value=\"Select Holon\" id= \"infoWinOpt\" selected>Select Holon Object</option>");
		for(HolonObject holonObject : holonObjectListByConnectedPowerLines) {
			holonObjectEnergyDetails = getHolonObjectEnergyDetails(holonObject);
			minimumProductionCapacityHolon = minimumProductionCapacityHolon + holonObjectEnergyDetails.get("minimumProductionCapacity");
			maximumProductionCapacityHolon = maximumProductionCapacityHolon + holonObjectEnergyDetails.get("maximumProductionCapacity");
			currentProductionHolon = currentProductionHolon + holonObjectEnergyDetails.get("currentProduction");
			minimumEnergyRequiredHolon = minimumEnergyRequiredHolon + holonObjectEnergyDetails.get("minimumEnergyRequired");
			maximumEnergyRequiredHolon = maximumEnergyRequiredHolon + holonObjectEnergyDetails.get("maximumEnergyRequired");
			currentEnergyRequiredHolon = currentEnergyRequiredHolon + holonObjectEnergyDetails.get("currentEnergyRequired");
			if(holonObjectEnergyDetails.get("currentEnergyRequired") > 0) {
				holonObjectList.append("<option value="+holonObject.getId()+" id= \"infoWinOpt\">"+holonObject.getHolonObjectType().getName()+
						" (Id:"+holonObject.getId()+") - Requires "+holonObjectEnergyDetails.get("currentEnergyRequired")+" energy</option>");
			} else {
				holonObjectList.append("<option value="+holonObject.getId()+" id= \"infoWinOpt\">"+holonObject.getHolonObjectType().getName()+
						" (Id:"+holonObject.getId()+")"+"</option>");
			}

			originalEnergyRequiredHolon = originalEnergyRequiredHolon + holonObjectEnergyDetails.get("originalEnergyRequired");
			flexibilityHolon = flexibilityHolon + holonObject.getFlexibility();
		}

		//Code to get connected Power Sources
		ArrayList<PowerSource> listConnectedPowerSources = getPowerSourceListByConnectedPowerLines(powerLine, holonCoordinator);
		for(PowerSource powerSource : listConnectedPowerSources) {
			flexibilityPowerSource = flexibilityPowerSource + powerSource.getFlexibility();
			minimumProductionCapacityPowerSource = minimumProductionCapacityPowerSource + powerSource.getMinProduction();
			maximumProductionCapacityPowerSource = maximumProductionCapacityPowerSource + powerSource.getMaxProduction();
			currentProductionPowerSource = currentProductionPowerSource + powerSource.getCurrentProduction();
		}
		//Adding power source details to the holon.
		flexibilityHolon = flexibilityHolon + flexibilityPowerSource;
		minimumProductionCapacityHolon = minimumProductionCapacityHolon + minimumProductionCapacityPowerSource;
		maximumProductionCapacityHolon = maximumProductionCapacityHolon + maximumProductionCapacityPowerSource;
		currentProductionHolon = currentProductionHolon + currentProductionPowerSource;
		
		holonEnergyDetails.put("noOfHolonObjects", noOfHolonObjects.toString());
		holonEnergyDetails.put("minimumProductionCapacityHolon", minimumProductionCapacityHolon.toString());
		holonEnergyDetails.put("maximumProductionCapacityHolon", maximumProductionCapacityHolon.toString());
		holonEnergyDetails.put("currentProductionHolon", currentProductionHolon.toString());
		holonEnergyDetails.put("minimumEnergyRequiredHolon", minimumEnergyRequiredHolon.toString());
		holonEnergyDetails.put("maximumEnergyRequiredHolon", maximumEnergyRequiredHolon.toString());
		holonEnergyDetails.put("currentEnergyRequiredHolon", currentEnergyRequiredHolon.toString());
		holonEnergyDetails.put("originalEnergyRequiredHolon", originalEnergyRequiredHolon.toString());
		holonEnergyDetails.put("flexibilityHolon", flexibilityHolon.toString());
		holonEnergyDetails.put("holonObjectList", holonObjectList.toString());
		
		return holonEnergyDetails;
	}
	
	/**
	 * This method checks whether a power line is connected to a holon object or a power source or not and returns the status as true or false.
	 * @param connectedPowerLines Array list of connected power lines
	 * @param powerLine Object of powerLine POJO for which we need to check the connected status
	 * @return boolean connected status as true or false.
	 */
	public boolean checkConnectedStatusForLine(ArrayList<PowerLine> connectedPowerLines, PowerLine powerLine) {
		if(powerLine.getType().equals(ConstantValues.SUBLINE) || powerLine.getType().equals(ConstantValues.POWERSUBLINE)) {
			return true;
		} else {
			for(PowerLine powerLine2 : connectedPowerLines) {
				if(powerLine2.getType().equals(ConstantValues.SUBLINE) ||powerLine2.getType().equals(ConstantValues.POWERSUBLINE) ) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method takes LatLng object as a parameter and then stores it in database if there is no latitude and longitude of the same location.
	 * Otherwise, it just returns the already existing LatLng object and does not create a new one in database.
	 * This is to avoid duplicate entries in database.
	 * @param locationToSave The location to be saved in database.
	 * @return location ID or the id of LatLng object (either a new one or an existing one)
	 */
	public Integer saveLocation(LatLng locationToSave) {
		Integer locationid=0;
		Double lat =locationToSave.getLatitude();
		Double lng =locationToSave.getLongitude();
		ArrayList<LatLng> locationList=getLatLngService().findByLocation(lat,lng);
		log.info("Size of latLng Object list is "+locationList);
	
		if(locationList.size()==0) {
			log.info("Location is not in database ");
			locationid=getLatLngService().persist(locationToSave);
		} else {
			log.info("Location is already in database ");
			LatLng location = locationList.get(0);
			locationid=location.getId();
		}
		return locationid;
	}

	/**
	 * This method takes powerline ID as an input and finds all connected power lines.
	 * This method is used by almost all modules like dynamic holon, dissolve holon, distribute holon energy, leadership election for holon coordinator etc...   
	 * @param powerLineId ID of the power line for which we want to calculate connected power lines.
	 * @return Array list of connected power lines.
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
	 * This method updates all connected holon objects and power sources and is called whenever:
	 * 1 - Holon Object is added to a main line.
	 * 2 - Power Source is added to a main line.
	 * 3 - Switch is toggled.
	 * 4 - Power source is toggled.
	 * 5 - Power source is edited.
	 * 6 - Holon Element is deleted from a holon object.
	 * 7 - Holon Element is edited in such a way that its current capacity is less than the previous current capacity.
	 * @param powerLineId ID of the power line for which we want to update the connected holon objects and power sources.
	 */
	public void updateHolonObjectsAndPowerSources(Integer powerLineId) {
		PowerLine powerLine = getPowerLineService().findById(powerLineId);
		String powerLineType = powerLine.getType();
		PowerSource immediatePowerSource = null;
		HolonObject immediateHolonObject = null;
		Integer immediateHolonObjectId = 0;
		Holon existingHolon = null;
		if(powerLineType.equals(ConstantValues.SUBLINE)) {
			immediateHolonObject = powerLine.getHolonObject();
			immediateHolonObjectId = immediateHolonObject.getId();
		} else if(powerLineType.equals(ConstantValues.POWERSUBLINE)) {
			immediatePowerSource = powerLine.getPowerSource();
		}
		ArrayList<PowerLine> connectedPowerLines = connectedPowerLines(powerLineId);
		for(PowerLine powerLine2 : connectedPowerLines) {
			powerLineType = powerLine2.getType();
			if(powerLineType.equals(ConstantValues.SUBLINE)) {
				 if(powerLine2.getHolonObject() != null && powerLine2.getHolonObject().getHolon() != null){
					 existingHolon = powerLine2.getHolonObject().getHolon();
				 }
				//Condition to set holon coordinator for the newly joined holon object
				if(immediateHolonObject != null && immediateHolonObject.getIsCoordinator() == false) {
					if(powerLine2.getHolonObject().getIsCoordinator().booleanValue() == true) {
						immediateHolonObject.setIsCoordinator(false);
						immediateHolonObject.setHolon(powerLine2.getHolonObject().getHolon());
						getHolonObjectService().merge(immediateHolonObject);
					}
				}
				//Condition to set holon coordinator for the newly joined power source
				if(immediatePowerSource != null && immediatePowerSource.getHolonCoordinator() == null) {
					if(powerLine2.getHolonObject().getIsCoordinator().booleanValue() == true) {
						immediatePowerSource.setHolonCoordinator(powerLine2.getHolonObject());
						getPowerSourceService().merge(immediatePowerSource);
					}
				}
				
			} else if(powerLineType.equals(ConstantValues.POWERSUBLINE)) {
				if(powerLine2.getPowerSource() != null && powerLine2.getPowerSource().getHolonCoordinator() != null
						&& powerLine2.getPowerSource().getHolonCoordinator().getHolon() != null){
					existingHolon = powerLine2.getPowerSource().getHolonCoordinator().getHolon();
					}
				//Condition to set holon coordinator for the newly joined holon object
				if(immediateHolonObject != null && immediateHolonObject.getIsCoordinator().booleanValue() == false) {
					if(powerLine2.getPowerSource().getHolonCoordinator() != null) {
						immediateHolonObject.setIsCoordinator(false);
						immediateHolonObject.setHolon(powerLine2.getPowerSource().getHolonCoordinator().getHolon());
						getHolonObjectService().merge(immediateHolonObject);
					}
				}
				//Condition to set holon coordinator for the newly joined power source
				if(immediatePowerSource != null && immediatePowerSource.getHolonCoordinator() == null) {
					if(powerLine2.getPowerSource().getHolonCoordinator() != null) {
						immediatePowerSource.setHolonCoordinator(powerLine2.getPowerSource().getHolonCoordinator());
						getPowerSourceService().merge(immediatePowerSource);
					}
				}
				
			} 
			
		}//End of parent for loop
		
		immediateHolonObject = getHolonObjectService().findById(immediateHolonObjectId);
		/*Checking whether a holon coordinator has been assigned to the newly joined holon object or not. 
		 * If not, set a random holon and make this the new holon coordinator of that holon.*/
		if(immediateHolonObject != null && immediateHolonObject.getHolon() == null) {
			Integer randomHolonId = randomNumber(1, 4);
			Holon randomHolon = getHolonService().findById(randomHolonId);
			 if(existingHolon != null) {
				 immediateHolonObject.setHolon(existingHolon);
			 } else {
				 immediateHolonObject.setHolon(randomHolon);
			 }
			immediateHolonObject.setIsCoordinator(true);
			getHolonObjectService().merge(immediateHolonObject);
		}
		//Code to assign holonCoordinator to already existing power source
		for(PowerLine powerLine2 : connectedPowerLines) {
			if(powerLine2.getPowerSource() != null && powerLine2.getPowerSource().getHolonCoordinator() == null) {
				Holon holon = null;
				immediateHolonObject = getHolonObjectService().findById(immediateHolonObjectId);
				if(immediateHolonObject != null && immediateHolonObject.getHolon() != null) {
					holon = immediateHolonObject.getHolon();
					HolonObject holonCoordinator = findConnectedHolonCoordinatorByHolon(holon, powerLine2);
					PowerSource powerSource = powerLine2.getPowerSource();
					powerSource.setHolonCoordinator(holonCoordinator);
					getPowerSourceService().merge(powerSource);
				}
			}
		}

	}

	/**
	 * This method finds a random integer value between two integers passed as parameter. 
	 * @param min Minimum value of the random integer
	 * @param max Maximum value of the random integer
	 * @return Random integer value between 1st and 2nd parameter.
	 */
	public int randomNumber(int min, int max) {
		return (min + (int)(Math.random()*((max-min)+1)));
	}
	
	/**
	 * This method finds all connected holon objects to a single holon object
	 * @param powerLine This is the object of powerLine POJO and power line type is MAINLINE
	 * @param holonObject The holon object for which we want to find other connected holon objects
	 * @return ArrayList of connected holon objects
	 */
	public ArrayList<HolonObject> getHolonObjectListByConnectedPowerLines(PowerLine powerLine, HolonObject holonObject) {
		ArrayList<HolonObject> connectedHolonObjects = new ArrayList<HolonObject>();
		Integer powerLineId = 0;
		ArrayList<PowerLine> connectedPowerLines = new ArrayList<PowerLine>();
		if(powerLine != null) {
			powerLineId = powerLine.getId();
			connectedPowerLines = new CommonUtilities().connectedPowerLines(powerLineId);
		}
		for(PowerLine powerLine2 : connectedPowerLines) {
			if(powerLine2.getType().equalsIgnoreCase(ConstantValues.SUBLINE)) {
				if(powerLine2.getHolonObject() != null && powerLine2.getHolonObject().getHolon()!= null && holonObject != null &&holonObject.getHolon() != null &&
						powerLine2.getHolonObject().getHolon().getId() == holonObject.getHolon().getId()) {
					connectedHolonObjects.add(powerLine2.getHolonObject());
				}
			}
		}
		return connectedHolonObjects;
	}
	
	/**
	 * This method finds list of all power sources connected to a power line and of a particular holon represented by the 2nd parameter (holonObject).
	 * @param powerLine Power line object for which we need to find the connected power sources.
	 * @param holonObject The holon object which is used to find only those power sources which have the same holon as this holon object.
	 * @return Array list of power sources
	 */
	public ArrayList<PowerSource> getPowerSourceListByConnectedPowerLines(PowerLine powerLine, HolonObject holonObject) {
		ArrayList<PowerSource> connectedPowerSources = new ArrayList<PowerSource>();
		Integer powerLineId = powerLine!= null?powerLine.getId():null;
		ArrayList<PowerLine> connectedPowerLines = powerLineId!=null?connectedPowerLines(powerLineId):new ArrayList<PowerLine>();
		for(PowerLine powerLine2 : connectedPowerLines) {
			if(powerLine2.getType().equalsIgnoreCase(ConstantValues.POWERSUBLINE)) {
				if(powerLine2.getPowerSource().getStatus() && powerLine2.getPowerSource().getHolonCoordinator()!=null && powerLine2.getPowerSource().
						getHolonCoordinator().getHolon() != null && holonObject.getHolon() != null && powerLine2.getPowerSource().getHolonCoordinator().getHolon().getId()
						== holonObject.getHolon().getId()) {
					connectedPowerSources.add(powerLine2.getPowerSource());
				}
			}
		}
		return connectedPowerSources;
	}
	

	/**
	 * This method checks whether two holon objects are connected or not.
	 * @param holonObjectA 1st holon object for which we need to check the connectivity.
	 * @param holonObjectB 2nd holon object for which we need to check the connectivity.
	 * @return Connectivity status in the form of true or false.
	 */
	public boolean checkConnectivityBetweenHolonObjects(HolonObject holonObjectA, HolonObject holonObjectB) {
		Integer powerLineIdA = holonObjectA.getHolon()!=null?getPowerLineService().getPowerLineByHolonObject(holonObjectA).getId():null;
		Integer holonObjectBId = holonObjectB.getId();
		ArrayList<PowerLine> connectedPowerLines = powerLineIdA != null?connectedPowerLines(powerLineIdA):null;
		if(connectedPowerLines != null) {
			for(PowerLine powerLine2 : connectedPowerLines) {
				if(powerLine2.getType().equalsIgnoreCase(ConstantValues.SUBLINE)) {
					if(holonObjectBId.equals(powerLine2.getHolonObject().getId())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * This method checks whether a holon object and a power source are connected or not.
	 * @param holonObject The holon object that needs to be checked for connectivity.
	 * @param powerSource The power source that needs to be checked for connectivity.
	 * @return Connectivity status in the form of true or false.
	 */
	public boolean checkConnectivityBetweenHolonObjectAndPowerSource(HolonObject holonObject, PowerSource powerSource) {
		Integer powerLineId = getPowerLineService().getPowerLineByHolonObject(holonObject).getId();
		Integer powerSourceId = powerSource.getId();
		ArrayList<PowerLine> connectedPowerLines = connectedPowerLines(powerLineId);
		for(PowerLine powerLine : connectedPowerLines) {
			if(powerLine.getType().equalsIgnoreCase(ConstantValues.POWERSUBLINE)) {
				if(powerLine.getPowerSource().getStatus() && powerSourceId.equals(powerLine.getPowerSource().getId())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method calculates the flexibility and current energy required by a holon object by checking all possible scenarios.
	 * @param holonObject The holon object for which calculation needs to be done.
	 * @param currentEnergyRequired Current energy originally required by the holon object.
	 * @return Map containing values for flexibility and currentEnergyRequired.
	 */
	public Map<String, Integer> getFlexibilityAndCurrentEnergyRequirementOfHolonObject(HolonObject holonObject, Integer currentEnergyRequired) {
		Map<String, Integer> flexibilityAndCurrentEnergyRequiredMap = new TreeMap<String, Integer>();
		
		Integer originalEnergyRequired = 0;
		Integer currentProduction=0;
		Integer flexibility = 0;
		ArrayList<HolonElement> holonElementList= getHolonElementService().getHolonElements(holonObject);
		for(HolonElement holonElement : holonElementList) {
			if(holonElement.getHolonElementType().getProducer()) {
				if(holonElement.getHolonElementState().getId()==1) {
					currentProduction = currentProduction + holonElement.getCurrentCapacity();
				}
			} else {
				if(holonElement.getHolonElementState().getId()==1) {
					originalEnergyRequired = originalEnergyRequired+holonElement.getCurrentCapacity();
				}
			}
		}
		flexibility = currentProduction;
		if(originalEnergyRequired > 0) {
			if(currentProduction > 0 && currentProduction > originalEnergyRequired) {
				flexibility = currentProduction - originalEnergyRequired;
			} else if (currentProduction > 0 && currentProduction <= originalEnergyRequired) {
				flexibility = 0;
			}
		}

		//Scenario from producer's perspective
		ArrayList<Supplier> supplierListForProducer = getSupplierService().getSupplierListForProducer(holonObject);
		for(Supplier supplier : supplierListForProducer) {
			if(!checkConnectivityBetweenHolonObjects(holonObject, supplier.getHolonObjectConsumer()) 
					&& supplier.getMessageStatus().equals(ConstantValues.ACCEPTED)) {
				supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
				getSupplierService().merge(supplier);
			}
			if(checkConnectivityBetweenHolonObjects(holonObject, supplier.getHolonObjectConsumer())) {
				if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
					if(flexibility >= supplier.getPowerGranted()) {
						flexibility = flexibility - supplier.getPowerGranted();
					} else {
						flexibility = 0;
					}
				}
			}
		}
		
		//Scenario from consumer's perspective
		ArrayList<Supplier> supplierListForConsumer = getSupplierService().getSupplierListForConsumer(holonObject);
		for(Supplier supplier : supplierListForConsumer) {
			if(supplier.getHolonObjectProducer() != null) {
				if(!checkConnectivityBetweenHolonObjects(holonObject, supplier.getHolonObjectProducer()) 
						&& supplier.getMessageStatus().equals(ConstantValues.ACCEPTED)) {
					supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
					getSupplierService().merge(supplier);
				}
				if(checkConnectivityBetweenHolonObjects(holonObject, supplier.getHolonObjectProducer())) {
					if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
						if(currentEnergyRequired >= supplier.getPowerGranted()) {
							currentEnergyRequired = currentEnergyRequired - supplier.getPowerGranted();
						} else {
							currentEnergyRequired = 0;
						}
					}
				}
			} else if(supplier.getPowerSource() != null) {
				if(!checkConnectivityBetweenHolonObjectAndPowerSource(holonObject, supplier.getPowerSource()) 
						&& supplier.getMessageStatus().equals(ConstantValues.ACCEPTED)) {
					supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
					getSupplierService().merge(supplier);
					PowerSource powerSource = supplier.getPowerSource();
					powerSource.setFlexibility(powerSource.getFlexibility() + supplier.getPowerGranted());
					getPowerSourceService().merge(powerSource);
				}
				if(checkConnectivityBetweenHolonObjectAndPowerSource(holonObject, supplier.getPowerSource())) {
					if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
						if(currentEnergyRequired >= supplier.getPowerGranted()) {
							currentEnergyRequired = currentEnergyRequired - supplier.getPowerGranted();
						} else {
							currentEnergyRequired = 0;
						}
					}
				}
							
			}
		}
		
		holonObject.setFlexibility(flexibility);
		getHolonObjectService().merge(holonObject);
		flexibilityAndCurrentEnergyRequiredMap.put("flexibility", flexibility);
		flexibilityAndCurrentEnergyRequiredMap.put("currentEnergyRequired", currentEnergyRequired);
		return flexibilityAndCurrentEnergyRequiredMap;
	}

	/**
	 * This method finds the connected holon coordinator of the holon passed as parameter. Calculation are based on the power line object passed as 2nd parameter.
	 * @param holon The holon for which we need to find the holon coordinator.
	 * @param powerLine The power line object which is used to find the connected holon coordinator.
	 * @return Connected holon coordinator
	 */
	public HolonObject findConnectedHolonCoordinatorByHolon(Holon holon, PowerLine powerLine) {
		HolonObject holonCoordinator = null;
		Integer powerLineId =  powerLine!=null?powerLine.getId():0;
		ArrayList<PowerLine> connectedPowerLines = connectedPowerLines(powerLineId);
		for(PowerLine powerLine2 : connectedPowerLines) {
			if(powerLine2.getType().equalsIgnoreCase(ConstantValues.SUBLINE)) {
				HolonObject tempHolonObject = powerLine2.getHolonObject();
				if(tempHolonObject != null && tempHolonObject.getHolon() != null && tempHolonObject.getHolon().getId() == holon.getId() 
						&& tempHolonObject.getIsCoordinator() == true ) {
					holonCoordinator = tempHolonObject;
					return holonCoordinator;
				}
			}
		}
		return holonCoordinator;
	}
	
	/**
	 * This method is used to find the new holon coordinator by using leadership election algorithm.
	 * @param powerLine The power line which is used to find connected holon objects. 
	 * @param holonObject The holon object which wants to be a part of leadership election for holon coordinator. 
	 * @return The new holon coordinator
	 */
	public HolonObject getHolonCoordinatorByElectionUsingPowerLineId(PowerLine powerLine, HolonObject holonObject){
		ArrayList<HolonObject> connectedholonObject = new ArrayList<HolonObject>();
		HolonObject newCoordinator= null;
		BigDecimal coordinatorCompetency;
		BigDecimal trustValue;
		BigDecimal maxValue = new BigDecimal(0.00);
		BigDecimal currentValue ;
		MathContext mc = new MathContext(2);
		connectedholonObject = getHolonObjectListByConnectedPowerLines(powerLine, holonObject);
		//Adding own holon object to connected list for leadership election
		if(holonObject != null){
			connectedholonObject.add(holonObject);
		}
		
		for(int j=0;j<connectedholonObject.size();j++)
		{
			if(connectedholonObject.size() > 0){
				coordinatorCompetency= connectedholonObject.get(j).getCoordinatorCompetency();
				trustValue=connectedholonObject.get(j).getTrustValue();
				currentValue=coordinatorCompetency.multiply(trustValue,mc);
				if(maxValue.compareTo(currentValue) == -1){
					// -1 means 2nd value is greater
					maxValue=currentValue;
					newCoordinator= connectedholonObject.get(j);
				}
			}
			
		}
		
		for(int k=0;k<connectedholonObject.size();k++){
			
			if(connectedholonObject.size() > 0){
				if(connectedholonObject.get(k).getId() == newCoordinator.getId()){
					//Set isCoordinator true 
					newCoordinator.setIsCoordinator(true);
					getHolonObjectService().merge(newCoordinator);
				}
				else{
					HolonObject tempHolonObject = connectedholonObject.get(k);
					//Set isCoordinator false
					tempHolonObject.setIsCoordinator(false);
					getHolonObjectService().merge(tempHolonObject);
				}
			}
			
		}
		return newCoordinator;
	}
	
	/**
	 * This method calculates list of new and old coordinators when a main line is connected to another main line or when a switch is toggled.
	 * @param powerLine The power line object which is used for connectivity purpose.
	 * @param status The status value to find which method to call as there are two methods which perform the same function.
	 * @return Array list of new and old holon coordinators.
	 */
	public Map<String, ArrayList<HolonObject>> getHolonCoordinatorByElectionUsingForMainLineAndSwitch(PowerLine powerLine, String status){
		Map<String, ArrayList<HolonObject>> mapOfNewAndOldCoordinators = new TreeMap<String, ArrayList<HolonObject>>();
		ArrayList<HolonObject> connectedHolonObjectsOfAllHolons = getHolonObjectListByConnectedPowerLinesOfAllHolons(powerLine, status);
		Map<String, ArrayList<HolonObject>> mapOfHolonCoordinatorsOfAllHolons = getHolonCoordinatorsOfAllHolons(connectedHolonObjectsOfAllHolons);
		ArrayList<HolonObject> newCoordinators = new ArrayList<HolonObject>();
		ArrayList<HolonObject> oldCoordinators = new ArrayList<HolonObject>();
		ArrayList<HolonObject> redHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("redCoordinators");
		ArrayList<HolonObject> yellowHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("yellowCoordinators");
		ArrayList<HolonObject> blueHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("blueCoordinators");
		ArrayList<HolonObject> greenHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("greenCoordinators");
		
		//Holon Coordinator leadership election for RED holon.
		if(redHolonCoordinatorsList.size() > 1) {
			HolonObject coordinator1 = redHolonCoordinatorsList.get(0);
			HolonObject coordinator2 = redHolonCoordinatorsList.get(1);
			BigDecimal competencyTrust1 = coordinator1.getCoordinatorCompetency().multiply(coordinator1.getTrustValue());
			BigDecimal competencyTrust2 = coordinator2.getCoordinatorCompetency().multiply(coordinator2.getTrustValue());
			if(competencyTrust1.compareTo(competencyTrust2) == -1) {
				newCoordinators.add(coordinator2);
				oldCoordinators.add(coordinator1);
				coordinator1.setIsCoordinator(false);
				getHolonObjectService().merge(coordinator1);
			} else {
				newCoordinators.add(coordinator1);
				oldCoordinators.add(coordinator2);
				coordinator2.setIsCoordinator(false);
				getHolonObjectService().merge(coordinator2);
			}
		} else if(redHolonCoordinatorsList.size() == 1) {
			HolonObject coordinator1 = redHolonCoordinatorsList.get(0);
			newCoordinators.add(coordinator1);
			coordinator1.setIsCoordinator(true);
			getHolonObjectService().merge(coordinator1);
		}
		
		//Holon Coordinator leadership election for BLUE holon.
		if(blueHolonCoordinatorsList.size() > 1) {
			HolonObject coordinator1 = blueHolonCoordinatorsList.get(0);
			HolonObject coordinator2 = blueHolonCoordinatorsList.get(1);
			BigDecimal competencyTrust1 = coordinator1.getCoordinatorCompetency().multiply(coordinator1.getTrustValue());
			BigDecimal competencyTrust2 = coordinator2.getCoordinatorCompetency().multiply(coordinator2.getTrustValue());
			if(competencyTrust1.compareTo(competencyTrust2) == -1) {
				newCoordinators.add(coordinator2);
				oldCoordinators.add(coordinator1);
				coordinator1.setIsCoordinator(false);
				getHolonObjectService().merge(coordinator1);
			} else {
				newCoordinators.add(coordinator1);
				oldCoordinators.add(coordinator2);
				coordinator2.setIsCoordinator(false);
				getHolonObjectService().merge(coordinator2);
			}
		} else if(blueHolonCoordinatorsList.size() == 1) {
			HolonObject coordinator1 = blueHolonCoordinatorsList.get(0);
			newCoordinators.add(coordinator1);
			coordinator1.setIsCoordinator(true);
			getHolonObjectService().merge(coordinator1);
		}
		
		//Holon Coordinator leadership election for GREEN holon.
		if(greenHolonCoordinatorsList.size() > 1) {
			HolonObject coordinator1 = greenHolonCoordinatorsList.get(0);
			HolonObject coordinator2 = greenHolonCoordinatorsList.get(1);
			BigDecimal competencyTrust1 = coordinator1.getCoordinatorCompetency().multiply(coordinator1.getTrustValue());
			BigDecimal competencyTrust2 = coordinator2.getCoordinatorCompetency().multiply(coordinator2.getTrustValue());
			if(competencyTrust1.compareTo(competencyTrust2) == -1) {
				newCoordinators.add(coordinator2);
				oldCoordinators.add(coordinator1);
				coordinator1.setIsCoordinator(false);
				getHolonObjectService().merge(coordinator1);
			} else {
				newCoordinators.add(coordinator1);
				oldCoordinators.add(coordinator2);
				coordinator2.setIsCoordinator(false);
				getHolonObjectService().merge(coordinator2);
			}
		} else if(greenHolonCoordinatorsList.size() == 1) {
			HolonObject coordinator1 = greenHolonCoordinatorsList.get(0);
			newCoordinators.add(coordinator1);
			coordinator1.setIsCoordinator(true);
			getHolonObjectService().merge(coordinator1);
		}
		
		//Holon Coordinator leadership election for YELLOW holon.
		if(yellowHolonCoordinatorsList.size() > 1) {
			HolonObject coordinator1 = yellowHolonCoordinatorsList.get(0);
			HolonObject coordinator2 = yellowHolonCoordinatorsList.get(1);
			BigDecimal competencyTrust1 = coordinator1.getCoordinatorCompetency().multiply(coordinator1.getTrustValue());
			BigDecimal competencyTrust2 = coordinator2.getCoordinatorCompetency().multiply(coordinator2.getTrustValue());
			if(competencyTrust1.compareTo(competencyTrust2) == -1) {
				newCoordinators.add(coordinator2);
				oldCoordinators.add(coordinator1);
				coordinator1.setIsCoordinator(false);
				getHolonObjectService().merge(coordinator1);
			} else {
				newCoordinators.add(coordinator1);
				oldCoordinators.add(coordinator2);
				coordinator2.setIsCoordinator(false);
				getHolonObjectService().merge(coordinator2);
			}
		} else if(yellowHolonCoordinatorsList.size() == 1) {
			HolonObject coordinator1 = yellowHolonCoordinatorsList.get(0);
			newCoordinators.add(coordinator1);
			coordinator1.setIsCoordinator(true);
			getHolonObjectService().merge(coordinator1);
		}
		
		mapOfNewAndOldCoordinators.put("newCoordinators", newCoordinators);
		mapOfNewAndOldCoordinators.put("oldCoordinators", oldCoordinators);
		
		return mapOfNewAndOldCoordinators;
	}
	
	/**
	 * This method finds connected holon objects of all holons(red, blue, green and yellow). 
	 * @param powerLine The power line object which is used to check the connectivity.
	 * @param status The status value to find which method to call as there are two methods which perform the same function.
	 * @return Array list of connected holon objects of all holons.
	 */
	public ArrayList<HolonObject> getHolonObjectListByConnectedPowerLinesOfAllHolons(PowerLine powerLine, String status) {
		ArrayList<HolonObject> connectedHolonObjectsOfAllHolons = new ArrayList<HolonObject>();
		Integer powerLineId = 0;
		ArrayList<PowerLine> connectedPowerLines = null;
		if(powerLine != null) {
			powerLineId = powerLine.getId();
			if(status.equalsIgnoreCase("common")) {
				connectedPowerLines = connectedPowerLines(powerLineId);
			} else {
				connectedPowerLines = new PowerSwitchAction().connectedPowerLines(powerLineId);
			}
		}
		for(PowerLine powerLine2 : connectedPowerLines) {
			if(powerLine2.getType().equalsIgnoreCase(ConstantValues.SUBLINE)) {
				if(powerLine2.getHolonObject() != null) {
					connectedHolonObjectsOfAllHolons.add(powerLine2.getHolonObject());
				}
			}
		}
		return connectedHolonObjectsOfAllHolons;
	}

	/**
	 * This method finds all Holon Coordinators of all holons(red, blue, green, yellow)
	 * @param connectedHolonObjectsOfAllHolons Aray list of all connected holon objects of all holons(red, blue, green, yellow).
	 * @return Map containing holon coordinators of all holons.
	 */
	public Map<String, ArrayList<HolonObject>> getHolonCoordinatorsOfAllHolons(ArrayList<HolonObject> connectedHolonObjectsOfAllHolons) {
		Map<String, ArrayList<HolonObject>> mapOfCoordinatorsOfAllHolons= new TreeMap<String, ArrayList<HolonObject>>();
		ArrayList<HolonObject> redHolonCoordinatorsList = new ArrayList<HolonObject>();
		ArrayList<HolonObject> yellowHolonCoordinatorsList = new ArrayList<HolonObject>();
		ArrayList<HolonObject> blueHolonCoordinatorsList = new ArrayList<HolonObject>();
		ArrayList<HolonObject> greenHolonCoordinatorsList = new ArrayList<HolonObject>();
		Boolean redFlag = false, yellowFlag = false, greenFlag = false, blueFlag = false;
		for(HolonObject holonObject : connectedHolonObjectsOfAllHolons) {
			if(holonObject.getHolon()!=null) {
				if(holonObject.getHolon().getId() == ConstantValues.HOLON_CO_RED && holonObject.getIsCoordinator()) {
					redHolonCoordinatorsList.add(holonObject);
					redFlag = true;
				} else if(holonObject.getHolon().getId() == ConstantValues.HOLON_CO_BLUE  && holonObject.getIsCoordinator()) {
					blueHolonCoordinatorsList.add(holonObject);
					blueFlag = true;
				} else if(holonObject.getHolon().getId() == ConstantValues.HOLON_CO_GREEN  && holonObject.getIsCoordinator()) {
					greenHolonCoordinatorsList.add(holonObject);
					greenFlag = true;
				} else if(holonObject.getHolon().getId() == ConstantValues.HOLON_CO_YELLOW  && holonObject.getIsCoordinator()) {
					yellowHolonCoordinatorsList.add(holonObject);
					yellowFlag = true;
				}
			}
		}
		
		HolonObject newRedCoordinator = null;
		HolonObject newBlueCoordinator = null;
		HolonObject newGreenCoordinator = null;
		HolonObject newYellowCoordinator = null;
		
		for(HolonObject holonObject : connectedHolonObjectsOfAllHolons) {
			BigDecimal redMaxValue = new BigDecimal(0.0);
			BigDecimal redCurrentValue = new BigDecimal(0.0);
			BigDecimal blueMaxValue = new BigDecimal(0.0);
			BigDecimal blueCurrentValue = new BigDecimal(0.0);
			BigDecimal greenMaxValue = new BigDecimal(0.0);
			BigDecimal greenCurrentValue = new BigDecimal(0.0);
			BigDecimal yellowMaxValue = new BigDecimal(0.0);
			BigDecimal yellowCurrentValue = new BigDecimal(0.0);
			
			//Condition for red holon
			if(!redFlag) {
				if(holonObject.getHolon().getId() == ConstantValues.HOLON_CO_RED) {
					redCurrentValue = holonObject.getTrustValue().multiply(holonObject.getCoordinatorCompetency());
					if(redMaxValue.compareTo(redCurrentValue) == -1) {
						redMaxValue = redCurrentValue;
						newRedCoordinator = holonObject;
					}
				}
			}
			
			//Condition for blue holon
			if(!blueFlag) {
				if(holonObject.getHolon().getId() == ConstantValues.HOLON_CO_BLUE) {
					blueCurrentValue = holonObject.getTrustValue().multiply(holonObject.getCoordinatorCompetency());
					if(blueMaxValue.compareTo(blueCurrentValue) == -1) {
						blueMaxValue = blueCurrentValue;
						newBlueCoordinator = holonObject;
					}
				}
			}
			
			//Condition for green holon
			if(!greenFlag) {
				if(holonObject.getHolon().getId() == ConstantValues.HOLON_CO_GREEN) {
					greenCurrentValue = holonObject.getTrustValue().multiply(holonObject.getCoordinatorCompetency());
					if(greenMaxValue.compareTo(greenCurrentValue) == -1) {
						greenMaxValue = greenCurrentValue;
						newGreenCoordinator = holonObject;
					}
				}
			}
			
			//Condition for yellow holon
			if(!yellowFlag) {
				if(holonObject.getHolon().getId() == ConstantValues.HOLON_CO_YELLOW) {
					yellowCurrentValue = holonObject.getTrustValue().multiply(holonObject.getCoordinatorCompetency());
					if(yellowMaxValue.compareTo(yellowCurrentValue) == -1) {
						yellowMaxValue = yellowCurrentValue;
						newYellowCoordinator = holonObject;
					}
				}
			}	
		}
		if(newRedCoordinator != null) {
			redHolonCoordinatorsList.add(newRedCoordinator);
		}
		if(newBlueCoordinator != null) {
			blueHolonCoordinatorsList.add(newBlueCoordinator);
		}
		if(newGreenCoordinator != null) {
			greenHolonCoordinatorsList.add(newGreenCoordinator);
		}
		if(newYellowCoordinator != null) {
			yellowHolonCoordinatorsList.add(newYellowCoordinator);
		}
		
		mapOfCoordinatorsOfAllHolons.put("redCoordinators", redHolonCoordinatorsList);
		mapOfCoordinatorsOfAllHolons.put("blueCoordinators", blueHolonCoordinatorsList);
		mapOfCoordinatorsOfAllHolons.put("greenCoordinators", greenHolonCoordinatorsList);
		mapOfCoordinatorsOfAllHolons.put("yellowCoordinators", yellowHolonCoordinatorsList);

		return mapOfCoordinatorsOfAllHolons;
	}
	
	/**
	 * This method finds all power line IDs that lie inside a disaster circle.
	 * @param latitudeOfCircle Latitude of the disaster circle. 
	 * @param longitudeOfCircle Longitude of the disaster circle.
	 * @param radiusOfCircle Radius of the disaster circle.
	 * @return Map containing concerned power line IDs.
	 */
	public Map<Integer, PowerLine> getListOfAllPowerLineIdsInsideCircle(Double latitudeOfCircle,Double longitudeOfCircle,Double radiusOfCircle) {
		ArrayList<LatLng> listOfAllLatLngIdsInsideCircle= getLatLngService().findAllLatLngInsideTheCircle(latitudeOfCircle, longitudeOfCircle, radiusOfCircle);
		Map<Integer, PowerLine> mapOfAllPowerLinesInsideCircles = new TreeMap<Integer, PowerLine>();
		for(LatLng latLng : listOfAllLatLngIdsInsideCircle){
			ArrayList<PowerLine> powerLineListFromLatLng = getPowerLineService().getPowerLineFromLatLng(latLng);
			for(PowerLine powerLine : powerLineListFromLatLng){
				if(!mapOfAllPowerLinesInsideCircles.containsKey(powerLine.getId())) {
					mapOfAllPowerLinesInsideCircles.put(powerLine.getId(), powerLine);
				}
			}
		}
		return mapOfAllPowerLinesInsideCircles;
	}
	
}