package com.htc.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.HolonObjectType;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.pojo.Supplier;
import com.htc.utilities.CommonUtilities;
import com.htc.utilities.ConstantValues;

/**
 * This class contains all functions related to holon objects.
 */
public class HolonObjectAction extends CommonUtilities {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(HolonObjectAction.class);
	
	/**
	 * This method is used to create a new holon object in database.
	 */
	public void createHolonObject(){
		try {
			//Retrieving request parameters that were sent from client side.
			Integer holonObjectTypeId = getRequest().getParameter("holonObjectType")!=null?Integer.parseInt(getRequest().getParameter("holonObjectType")):0;
			Integer canCommunicate = getRequest().getParameter("canCommunicate")!=null?Integer.parseInt(getRequest().getParameter("canCommunicate")):0;
			Double latNE = getRequest().getParameter("latNE")!=null?Double.parseDouble(getRequest().getParameter("latNE")):0D;
			Double lngNE = getRequest().getParameter("lngNE")!=null?Double.parseDouble(getRequest().getParameter("lngNE")):0D;
			Double latSW = getRequest().getParameter("latSW")!=null?Double.parseDouble(getRequest().getParameter("latSW")):0D;
			Double lngSW = getRequest().getParameter("lngSW")!=null?Double.parseDouble(getRequest().getParameter("lngSW")):0D;
			BigDecimal coordinatorCompetency = new BigDecimal(Math.random());
			BigDecimal trustValue= new BigDecimal(Math.random());
			
			LatLng NorthlatLng = new LatLng(latNE, lngNE);
			LatLng SouthlatLng = new LatLng(latSW, lngSW);
			Integer NorthlocationId = saveLocation(NorthlatLng);
			Integer SouthlocationId = saveLocation(SouthlatLng);
			LatLng NorthlatLng2 = getLatLngService().findById(NorthlocationId);
			LatLng SouthlatLng2 = getLatLngService().findById(SouthlocationId);
			
			HolonObject holonObject = new HolonObject(); // Creating HolonObject object to store values
			HolonObjectType holonObjectType = getHolonObjectTypeService().findById(holonObjectTypeId);
			holonObject.setLatLngByNeLocation(NorthlatLng2);
			holonObject.setLatLngBySwLocation(SouthlatLng2);
			holonObject.setHolonObjectType(holonObjectType);
			holonObject.setLineConnectedState(false);
			holonObject.setCanCommunicate(canCommunicate==1?true:false);
			holonObject.setFlexibility(0);
			holonObject.setIsCoordinator(false);
			holonObject.setCoordinatorCompetency(coordinatorCompetency);
			holonObject.setTrustValue(trustValue);
			//Calling service method to save the object in database and saving the auto-incremented ID in an integer
			Integer newHolonObjectID = getHolonObjectService().persist(holonObject);
			HolonObject holonObject2 = getHolonObjectService().findById(newHolonObjectID);
			String hc_ne_location="";
			Integer noOfHolons=0;
			String holonColor="black";
			boolean isCoord=false;
			//Calling the response function and setting the content type of response.
			getResponse().setContentType("text/html");
			//Preparing response to be sent to client side
			StringBuffer hoResponse = new StringBuffer();
			hoResponse.append(holonObject2.getId()+"!");
			hoResponse.append(isCoord+"!");
			hoResponse.append(hc_ne_location+"!");
			hoResponse.append(noOfHolons.toString()+"!");
			hoResponse.append(holonColor);	
			getResponse().getWriter().write(hoResponse.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action createHolonObject()");
		}
	}
	
	/**
	 * This method is used to prepare the parameters that will be displayed in the holon object info window on map.
	 */
	public void getHolonObjectInfoWindow() {
		try {
				Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;			
				HolonObject holonObject2 = getHolonObjectService().findById(holonObjectId);
				String holonCoordinatorName_Holon="Not Part of any Holon";
				String holonColor="black";
				HolonObject hc = null;
				if(holonObject2.getHolon() != null) {
					PowerLine immediatePowerLine = getPowerLineService().getPowerLineByHolonObject(holonObject2);
					if(holonObject2.getIsCoordinator()) {
						hc = holonObject2;
					} else {
						hc = findConnectedHolonCoordinatorByHolon (holonObject2.getHolon(), immediatePowerLine);
					}
				}
				Integer coordinatorHolonId=0;
				String coOredNeLocation="";
				if(hc!=null) {
						holonCoordinatorName_Holon = holonObject2.getHolon().getName();
						holonColor = holonObject2.getHolon().getColor();
						coordinatorHolonId = hc.getId();
						coOredNeLocation= hc.getLatLngByNeLocation().getLatitude()+"~"+hc.getLatLngByNeLocation().getLongitude();
				}
				String coordinatorCompetency = holonObject2.getCoordinatorCompetency().toPlainString();
				String trustValue = holonObject2.getTrustValue().toPlainString();
				String holonObjectTypeName = holonObject2.getHolonObjectType().getName();
				String ne_location = holonObject2.getLatLngByNeLocation().getLatitude()+"~"+holonObject2.getLatLngByNeLocation().getLongitude();
				String sw_location = holonObject2.getLatLngBySwLocation().getLatitude()+"~"+holonObject2.getLatLngBySwLocation().getLongitude();			
				String lineConnectedState = holonObject2.getLineConnectedState()==true?"Yes":"No";
				String canCommunicate = holonObject2.getCanCommunicate()==true?"Yes":"No"; 
				Integer flexibility = holonObject2.getFlexibility();
				HolonObject holonObject = getHolonObjectService().findById(holonObjectId);
				//Function call to get holon object energy details and save the information in map.
				Map<String, Integer> holonObjectEnergyDetails = getHolonObjectEnergyDetails(holonObject);
				Integer noOfHolonElements = holonObjectEnergyDetails.get("noOfHolonElements");
				Integer minimumEnergyRequired = holonObjectEnergyDetails.get("minimumEnergyRequired");
				Integer maximumEnergyRequired = holonObjectEnergyDetails.get("maximumEnergyRequired");
				Integer originalEnergyRequired = holonObjectEnergyDetails.get("originalEnergyRequired");
				Integer minimumProductionCapacity = holonObjectEnergyDetails.get("minimumProductionCapacity");
				Integer maximumProductionCapacity = holonObjectEnergyDetails.get("maximumProductionCapacity");
				Integer currentProduction = holonObjectEnergyDetails.get("currentProduction");
				Integer currentEnergyRequired = holonObjectEnergyDetails.get("currentEnergyRequired");
				//Variables to capture Holon Details
				String noOfHolonObjects = "0";
				String minimumProductionCapacityHolon = "0";
				String maximumProductionCapacityHolon = "0";
				String currentProductionHolon = "0";
				String minimumEnergyRequiredHolon = "0";
				String maximumEnergyRequiredHolon = "0";
				String currentEnergyRequiredHolon = "0";
				String holonObjectList = "";
				String originalEnergyRequiredHolon = "0";
				String flexibilityHolon = "0";
				//This if condition will run only when the holon object is also a holon coordinator.
				if(hc != null && holonObjectId == hc.getId()) {
					//Function call to get energy details of entire holon and save the information in map.
					Map<String, String> holonEnergyDetails = getHolonEnergyDetails(hc);
					noOfHolonObjects = holonEnergyDetails.get("noOfHolonObjects");
					minimumProductionCapacityHolon = holonEnergyDetails.get("minimumProductionCapacityHolon");
					maximumProductionCapacityHolon = holonEnergyDetails.get("maximumProductionCapacityHolon");
					currentProductionHolon = holonEnergyDetails.get("currentProductionHolon");
					minimumEnergyRequiredHolon = holonEnergyDetails.get("minimumEnergyRequiredHolon");
					maximumEnergyRequiredHolon = holonEnergyDetails.get("maximumEnergyRequiredHolon");
					currentEnergyRequiredHolon = holonEnergyDetails.get("currentEnergyRequiredHolon");
					originalEnergyRequiredHolon = holonEnergyDetails.get("originalEnergyRequiredHolon");
					flexibilityHolon = holonEnergyDetails.get("flexibilityHolon");
					holonObjectList = holonEnergyDetails.get("holonObjectList");
				}
				//Calling the response function and setting the content type of response to be used by client side functions.
				getResponse().setContentType("text/html");
				StringBuffer hoResponse = new StringBuffer();
				hoResponse.append(holonObject2.getId()+"!");
				hoResponse.append(holonCoordinatorName_Holon+"!");
				hoResponse.append(holonObjectTypeName+"!");
				hoResponse.append(ne_location+"!");
				hoResponse.append(sw_location+"!");
				hoResponse.append(lineConnectedState+"!");
				hoResponse.append(holonColor+"!");
				hoResponse.append(coordinatorHolonId+"!");
				hoResponse.append(noOfHolonElements+"!");
				hoResponse.append(minimumEnergyRequired+"!");
				hoResponse.append(maximumEnergyRequired+"!");
				hoResponse.append(originalEnergyRequired+"!");
				hoResponse.append(minimumProductionCapacity+"!");
				hoResponse.append(maximumProductionCapacity+"!");
				hoResponse.append(currentProduction+"!");
				hoResponse.append(noOfHolonObjects+"!");
				hoResponse.append(minimumEnergyRequiredHolon+"!");
				hoResponse.append(maximumEnergyRequiredHolon+"!");
				hoResponse.append(currentEnergyRequiredHolon+"!");
				hoResponse.append(minimumProductionCapacityHolon+"!");
				hoResponse.append(maximumProductionCapacityHolon+"!");
				hoResponse.append(currentProductionHolon+"!");
				hoResponse.append(holonObjectList+"!");
				hoResponse.append(canCommunicate+"!");
				hoResponse.append(coOredNeLocation+"!");
				boolean createdFromFactory = holonObject2.getCreatedFactory() != null ? holonObject2.getCreatedFactory() : false;
				if(createdFromFactory) {
					hoResponse.append("Yes!");
				} else {
					hoResponse.append("No!");
				}
				hoResponse.append(currentEnergyRequired+"!");
				hoResponse.append(flexibility+"!");
				hoResponse.append(originalEnergyRequiredHolon+"!");
				hoResponse.append(flexibilityHolon+"!");
				hoResponse.append(coordinatorCompetency+"!");
				hoResponse.append(trustValue);
				getResponse().getWriter().write(hoResponse.toString());
			} catch (Exception e) {
				log.info("Exception "+e.getMessage()+" occurred in action createHolonObject()");
			}
	}
	
	/**
	 * This method is used to edit details of an existing holon object. 
	 */
	public void editHolonObject() {
		try {
				//Fetching request parameters from client.
				Integer holonObjectTypeId = getRequest().getParameter("holonObjectType")!=null?Integer.parseInt(getRequest().getParameter("holonObjectType")):0;
				Integer hiddenHolonObjectId = getRequest().getParameter("hiddenHolonObjectId")!=null?Integer.parseInt(getRequest().getParameter("hiddenHolonObjectId")):0;
				Integer canCommunicate = getRequest().getParameter("canCommunicate")!=null?Integer.parseInt(getRequest().getParameter("canCommunicate")):0;
				HolonObject holonObject = getHolonObjectService().findById(hiddenHolonObjectId);
				HolonObjectType holonObjectType = getHolonObjectTypeService().findById(holonObjectTypeId);
				holonObject.setHolonObjectType(holonObjectType);
				holonObject.setCanCommunicate(canCommunicate==1?true:false);
				//Calling service method to save the object in database and saving the auto-incremented ID in an integer
				getHolonObjectService().merge(holonObject); //Saving the updated holon object in database.
				HolonObject updatedHolonObject = getHolonObjectService().findById(hiddenHolonObjectId);
				Holon holon = updatedHolonObject!=null?updatedHolonObject.getHolon():null;
				String holonColor="black";
				if(holon!=null) {
					holonColor = holon.getColor();
				}
				StringBuffer hoResponse = new StringBuffer();
				hoResponse.append(holonColor+"!");
				hoResponse.append(holonObject.getId());
				//Calling the response function and setting the content type of response.
				getResponse().setContentType("text/html");		
				getResponse().getWriter().write(hoResponse.toString());
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action editHolonObject()");
		}
	}
	
	/**
	 * This method is used to fetch details of all holon objects from database and prepare response for client side functions.
	 */
	public void showHolonObjects(){
		try {			
			ArrayList<String> hoListArray = new ArrayList<String>();
			HolonObject holonObject = null;
			ArrayList<HolonObject> holonObjectList = getHolonObjectService().getAllHolonObject();//Fetching all holon objects from database
			if(holonObjectList != null) {
				for(int i=0; i<holonObjectList.size();i++){
					boolean isCoord=false;
					String ne_location;
					String sw_location;
					String holonColor;
					Integer holonObjectId;
				    holonObject = holonObjectList.get(i);
					ne_location = holonObject.getLatLngByNeLocation().getLatitude()+"~"+holonObject.getLatLngByNeLocation().getLongitude();
					sw_location = holonObject.getLatLngBySwLocation().getLatitude()+"~"+holonObject.getLatLngBySwLocation().getLongitude();
					holonColor ="black";
					if(holonObject.getHolon() != null) {
						holonColor = holonObject.getHolon().getColor();
					}
					holonObjectId = holonObject.getId();
					isCoord = holonObject.getIsCoordinator();
					log.info("The Color of the Holon is "+holonColor);
					hoListArray.add(holonObjectId+"#"+holonColor+"#"+ne_location+"^"+sw_location+"#"+isCoord+"*");
				}
			}
			//Calling the response function and setting the content type of response.
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(hoListArray.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action showHolonObjects()");
			e.printStackTrace();
		}
	}

	/**
	 * This method checks whether a holon object has any holon element that is a producer and then returns a boolean flag to indicate the same.
	 * @param holonObjectId The holon object for which necessary check has to be made.
	 * @return a boolean list containing variables that have power information.
	 */
	private List<Boolean> getHolonPowerProductionDetails(Integer holonObjectId) {
		List<Boolean> productionDetails = new ArrayList<Boolean>();
		List<HolonElement> holonElementList = getHolonElementService().getHolonElements(getHolonObjectService().findById(holonObjectId));
		boolean hasPower=false;
		boolean hasPowerOn=false;
		for(int i=0;i<holonElementList.size();i++) {
			if(holonElementList.get(i).getHolonElementType().getProducer()) {
				hasPower=true;
				if(hasPower && !hasPowerOn) {
					hasPowerOn=holonElementList.get(i).getHolonElementState().getId()==1?true:false;
				}
			}
		}
		productionDetails.add(hasPower);
		productionDetails.add(hasPowerOn);
		return productionDetails;
	}

	/**
	 * This function updates a power line with holon object.
	 * @param holonObject The holon obeject that needs to be set in the power line.
	 * @param powerLine The power line object that needs to be updated.
	 */
	public void updateConnectedPowerLine(HolonObject holonObject, PowerLine powerLine) {
		powerLine.setHolonObject(holonObject);
		getPowerLineService().merge(powerLine);
	}
	
	/**
	 * This method gets details regarding the power source icon of a holon object
	 */
	public void getDetailForPowerSourceIcon() {
		try {
			Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
			HolonObject hObject = getHolonObjectService().findById(holonObjectId);//Fetching holon object from database
			Double neLocationLat= hObject.getLatLngByNeLocation().getLatitude();
			Double swLocationLng=hObject.getLatLngBySwLocation().getLongitude();
			boolean hasPower= getHolonPowerProductionDetails(holonObjectId).get(0);
			boolean hasPowerOn= getHolonPowerProductionDetails(holonObjectId).get(1);
			//Preparing response to be used by client side functions.
			String responseVal =hasPower+"*"+hasPowerOn+"*"+neLocationLat+"*"+swLocationLng;
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(responseVal.toString());
			
		} catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in getDetailForPowerSourceIcon()");
		}
	}
	
	
	/**
	 * This method gets the data required for supplier window that is accessible from holon object info window on map. 
	 */
	public void getDataForSupplierDetails() {
		try {
			Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
			HolonObject holonObject = getHolonObjectService().findById(holonObjectId);//Fetching holon object from database
			ArrayList<Supplier> listConsumer = getSupplierService().getSupplierListForConsumer(holonObject);//Fetching supplier list where holon object is a consumer 
			StringBuffer responseStr = new StringBuffer();
			//Iterating supplier consumer list
			for(Supplier supplier : listConsumer) {
					//Checking the connection between consumer and producer, if there is no connectivity message status is updated accordingly to CONNECTION_RESET
					if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED) 
							|| supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.CONNECTION_RESET)) {
						if(supplier.getPowerSource() != null) {
							responseStr.append("Power Source~");
							responseStr.append(supplier.getPowerSource().getId()+"~");
						} else if(supplier.getHolonObjectProducer() != null) {
							responseStr.append("Holon Object~");
							responseStr.append(supplier.getHolonObjectProducer().getId()+"~");
						}
						responseStr.append(supplier.getPowerRequested()+"~");
						responseStr.append(supplier.getPowerGranted()+"~");
						responseStr.append(supplier.getMessageStatus()+"~");
						if(supplier.getPowerSource() != null) {
							responseStr.append(checkConnectivityBetweenHolonObjectAndPowerSource(holonObject, supplier.getPowerSource())+"~");
						} else if(supplier.getHolonObjectProducer() != null) {
							responseStr.append(checkConnectivityBetweenHolonObjects(holonObject, supplier.getHolonObjectProducer())+"~");
						}
						responseStr.append(supplier.getRequestId()+"~");
						responseStr.append(supplier.getCommunicationMode()+"!");
					}
			}
			if(responseStr.length() > 0) {
				responseStr = responseStr.deleteCharAt(responseStr.lastIndexOf("!"));
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(responseStr.toString());
		} catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in getDataForSupplierDetails()");
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to send energy messages to all connected holon objects which are producers.
	 * Energy messages are sent to immediate peers which then send to their peers and so on.  
	 */
	public void sendMessageToAllProducers() {
		try {
			Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
			HolonObject holonObjectConsumer = getHolonObjectService().findById(holonObjectId);//Fetching holon object from database. 
			Boolean canCommunicate = holonObjectConsumer.getCanCommunicate();
			String responseMessage = ConstantValues.SUCCESS;
			//Energy messages will be sent only if the canCommunicate flag is true
			if(canCommunicate) {
				//This function call fetches current energy details of holon object
				Integer currentEnergyRequired = getHolonObjectEnergyDetails(holonObjectConsumer).get("currentEnergyRequired");
				int requestId = 0;
				Integer newSupplierId = 0;
				PowerLine powerLine = getPowerLineService().getPowerLineByHolonObject(holonObjectConsumer);
				if(!(powerLine == null)) {
					ArrayList<HolonObject> connectedHolonObjects = getHolonObjectListByConnectedPowerLines(powerLine, holonObjectConsumer);
					Boolean statusDuplicateMessage = false;
					ArrayList<Supplier> checkDuplicateMessageList = getSupplierService().getAllSupplier();
					//Sending energy requirement message to all producers
					for(HolonObject holonObjectProducer : connectedHolonObjects) {
						for(Supplier supplier : checkDuplicateMessageList) {
							if(supplier.getHolonObjectProducer() != null && supplier.getHolonObjectProducer().getId() == holonObjectProducer.getId() 
									&& supplier.getHolonObjectConsumer().getId() == holonObjectConsumer.getId() 
									&& supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.PENDING)) {
								statusDuplicateMessage = true;
							}
						}
						if(!statusDuplicateMessage) {
							Integer flexibility = getHolonObjectEnergyDetails(holonObjectProducer).get("flexibility");
							//Messages will be send to producers only if flexibility is greater than zero and the producer can communicate.
							if(flexibility > 0 && holonObjectProducer.getCanCommunicate()) {
								holonObjectProducer.setFlexibility(flexibility);
								getHolonObjectService().merge(holonObjectProducer);
								Supplier supplier = new Supplier();
								supplier.setHolonObjectConsumer(holonObjectConsumer);
								supplier.setHolonObjectProducer(holonObjectProducer);
								supplier.setMessageStatus(ConstantValues.PENDING);
								supplier.setPowerSource(null);
								supplier.setPowerRequested(currentEnergyRequired);
								supplier.setPowerGranted(0);
								supplier.setRequestId(requestId);
								supplier.setCommunicationMode(ConstantValues.COMMUNICATION_MODE_DIRECT);
								newSupplierId = getSupplierService().persist(supplier);//Saving supplier object in database
								if(requestId == 0) {
									requestId  = newSupplierId;
								}
								Supplier tempSupplier = getSupplierService().findById(newSupplierId);
								tempSupplier.setRequestId(requestId);
								getSupplierService().merge(tempSupplier);
							}
						}
						statusDuplicateMessage = false;
					}
				} else {
					responseMessage = ConstantValues.FAILURE;
				}
			} else {
				responseMessage = ConstantValues.FAILURE;
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(responseMessage);
		} catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in sendMessageToAllProducers()");
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to prepare response for inbox of the holon object.   
	 */
	public void checkInbox() {
		try {
				Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
				String takeAction = getRequest().getParameter("takeAction")!=null?getRequest().getParameter("takeAction"):"";
				HolonObject holonObject = getHolonObjectService().findById(holonObjectId);//Fetching holon object details from database.
				//Fetching list of supplier objects based on holon object priority and where the holon object is a producer
				ArrayList<Supplier> producerList = getSupplierService().getSupplierListForProducerOrderByConsumerPriority(holonObject);
				Integer producerFlexibility = getHolonObjectEnergyDetails(holonObject).get("flexibility");
				Integer currentEnergyRequired = 0;
				Boolean canCommunicate = holonObject.getCanCommunicate();
				String responseMessage = ConstantValues.SUCCESS;
				
				if(takeAction.equalsIgnoreCase("yes") && canCommunicate) {
					for(Supplier supplier : producerList) {
						if(checkConnectivityBetweenHolonObjects(supplier.getHolonObjectProducer(), supplier.getHolonObjectConsumer())
								&& supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.PENDING)) {
							ArrayList<Supplier> similarRequests = getSupplierService().getListOfSimilarRequests(supplier.getRequestId());
							//Fetching current energy details for consumer holon object
							currentEnergyRequired = getHolonObjectEnergyDetails(supplier.getHolonObjectConsumer()).get("currentEnergyRequired");
							if(producerFlexibility >= supplier.getPowerRequested()) {
								if(currentEnergyRequired <= supplier.getPowerRequested()) {
									producerFlexibility = producerFlexibility - currentEnergyRequired;
									supplier.setPowerGranted(currentEnergyRequired);
									if(currentEnergyRequired == 0) {
										supplier.setMessageStatus(ConstantValues.PROCESSED);
									} else {
										for(Supplier supplier2 : similarRequests) {
												supplier2.setMessageStatus(ConstantValues.PROCESSED);
												getSupplierService().merge(supplier2);
										}
										if(supplier.getHolonObjectConsumer().getCanCommunicate()) {
											supplier.setMessageStatus(ConstantValues.ACCEPTED);
										}
									}
									getSupplierService().merge(supplier);
								} else {
									producerFlexibility = producerFlexibility - supplier.getPowerRequested();
									supplier.setPowerGranted(supplier.getPowerRequested());
									for(Supplier supplier2 : similarRequests) {
										supplier2.setMessageStatus(ConstantValues.PROCESSED);
										getSupplierService().merge(supplier2);
									}
									if(supplier.getHolonObjectConsumer().getCanCommunicate()) {
										supplier.setMessageStatus(ConstantValues.ACCEPTED);
									}
									getSupplierService().merge(supplier);
								}
							} else if(producerFlexibility > 0 && producerFlexibility < supplier.getPowerRequested()){
								if(producerFlexibility <= currentEnergyRequired ) {
									supplier.setPowerGranted(producerFlexibility);
									for(Supplier supplier2 : similarRequests) {
										supplier2.setMessageStatus(ConstantValues.PROCESSED);
										getSupplierService().merge(supplier2);
									}
									if(supplier.getHolonObjectConsumer().getCanCommunicate()) {
										supplier.setMessageStatus(ConstantValues.ACCEPTED);
									}
									getSupplierService().merge(supplier);
									producerFlexibility = 0;
								} else {
									producerFlexibility = producerFlexibility - currentEnergyRequired;
									supplier.setPowerGranted(currentEnergyRequired);
									if(currentEnergyRequired == 0) {
										supplier.setMessageStatus(ConstantValues.PROCESSED);
									} else {
										for(Supplier supplier2 : similarRequests) {
											supplier2.setMessageStatus(ConstantValues.PROCESSED);
											getSupplierService().merge(supplier2);
										}
										if(supplier.getHolonObjectConsumer().getCanCommunicate()) {
											supplier.setMessageStatus(ConstantValues.ACCEPTED);
										}
									}
									getSupplierService().merge(supplier);
								}
							} else {
								supplier.setMessageStatus(ConstantValues.REJECTED);
								getSupplierService().merge(supplier);//Saving supplier object in database
							}
						}
					}
				} else if(!canCommunicate) {
					responseMessage = ConstantValues.FAILURE;
				}
				StringBuffer responseStr =new StringBuffer("");
				ArrayList<Supplier> updatedProducerList = getSupplierService().getSupplierListForProducer(holonObject);
				//Preparing response for client side functions.
				for(Supplier supplier : updatedProducerList) {
					responseStr.append(supplier.getHolonObjectConsumer().getId()+"~");
					responseStr.append(supplier.getHolonObjectConsumer().getHolonObjectType().getName());
					responseStr.append(" ("+supplier.getHolonObjectConsumer().getHolonObjectType().getPriority()+")~");
					responseStr.append(supplier.getPowerRequested()+"~");
					responseStr.append(supplier.getPowerGranted()+"~");
					responseStr.append(supplier.getMessageStatus()+"~");
					responseStr.append(checkConnectivityBetweenHolonObjects(supplier.getHolonObjectProducer(), supplier.getHolonObjectConsumer())+"~");
					responseStr.append(supplier.getRequestId()+"~");
					responseStr.append(responseMessage+"~");
					responseStr.append(supplier.getCommunicationMode()+"!");
				}
				if(responseStr.length() > 0) {
					responseStr = responseStr.deleteCharAt(responseStr.lastIndexOf("!"));
				}
				getResponse().setContentType("text/html");
				getResponse().getWriter().write(responseStr.toString());
		} catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in checkInbox()");
		}
	}
	
	/**
	 * This method is used by holon coordinator to distribute energy among all holon objects of the same holon.  
	 */
	@SuppressWarnings("unchecked")
	public void distributeEnergyAmongHolonObjects() {
		try {
			Integer holonCoordinatorId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
			String takeAction = getRequest().getParameter("takeAction")!=null?getRequest().getParameter("takeAction"):"";
			HolonObject holonCoordinator = getHolonObjectService().findById(holonCoordinatorId);//Fetching holon coordinator from database
			PowerLine powerLine = getPowerLineService().getPowerLineByHolonObject(holonCoordinator);//Fetching the SUBLINE of holon coordinator
			//Fetching all holon objects of the same holon connected to the concerned SUBLINE
			ArrayList<HolonObject> listOfConnectedHolonObjects = getHolonObjectListByConnectedPowerLines(powerLine, holonCoordinator);
			ArrayList<HolonObject> listOfConsumers = new ArrayList<HolonObject>();
			StringBuffer responseStr =new StringBuffer("");
			if(!(takeAction.equalsIgnoreCase("yes"))) {//If condition when we don't have to distribute any energy
				for(HolonObject holonObject : listOfConnectedHolonObjects) {
					Integer currentEnergyRequired = getHolonObjectEnergyDetails(holonObject).get("currentEnergyRequired");
					if(currentEnergyRequired > 0) {//Preparing response to be used by client side functions
						responseStr.append(holonObject.getId()+"~");
						responseStr.append(holonObject.getHolonObjectType().getName()+" ("+holonObject.getHolonObjectType().getPriority()+")"+"~");
						responseStr.append("N/A~");
						responseStr.append("N/A~");
						responseStr.append(currentEnergyRequired+"~");
						responseStr.append("N/A~");
						responseStr.append("N/A~");
						responseStr.append("N/A~");
						responseStr.append("N/A!");
					}
				}
			} else {
				for(HolonObject holonObject : listOfConnectedHolonObjects) {
					Integer currentEnergyRequired = getHolonObjectEnergyDetails(holonObject).get("currentEnergyRequired");
					if(currentEnergyRequired > 0) {
						if(holonObject.getHolonObjectType().getPriority() == 1 && holonObject.getCanCommunicate()) {
							listOfConsumers.add(holonObject);
						}
					}
				}
				for(HolonObject holonObject : listOfConnectedHolonObjects) {
					Integer currentEnergyRequired = getHolonObjectEnergyDetails(holonObject).get("currentEnergyRequired");
					if(currentEnergyRequired > 0) {
						if(holonObject.getHolonObjectType().getPriority() == 2 && holonObject.getCanCommunicate()) {
							listOfConsumers.add(holonObject);
						}
					}
				}
				for(HolonObject holonObject : listOfConnectedHolonObjects) {
					Integer currentEnergyRequired = getHolonObjectEnergyDetails(holonObject).get("currentEnergyRequired");
					if(currentEnergyRequired > 0) {
						if(holonObject.getHolonObjectType().getPriority() == 3 && holonObject.getCanCommunicate()) {
							listOfConsumers.add(holonObject);
						}
					}
				}
				for(HolonObject holonObject : listOfConnectedHolonObjects) {
					Integer currentEnergyRequired = getHolonObjectEnergyDetails(holonObject).get("currentEnergyRequired");
					if(currentEnergyRequired > 0) {
						if(holonObject.getHolonObjectType().getPriority() == 4 && holonObject.getCanCommunicate()) {
							listOfConsumers.add(holonObject);
						}
					}
				}
				//Now we have a priority wise list of consumers
				
				ArrayList<PowerSource> listOfConnectedPowerSources = null;
				if(listOfConsumers.size() > 0) {
					listOfConnectedPowerSources = getPowerSourceListByConnectedPowerLines(powerLine, listOfConsumers.get(0));
				}
				//Distributing Energy via Power Sources
				for(HolonObject holonObjectConsumer : listOfConsumers) {
					for(PowerSource powerSource : listOfConnectedPowerSources) {
						Integer flexibility = powerSource.getFlexibility();
						Integer currentEnergyRequired = getHolonObjectEnergyDetails(holonObjectConsumer).get("currentEnergyRequired");
						if(flexibility > 0 && currentEnergyRequired > 0) {
								Supplier supplier = new Supplier();
								supplier.setCommunicationMode(ConstantValues.COMMUNICATION_MODE_COORDINATOR);
								supplier.setHolonObjectConsumer(holonObjectConsumer);
								supplier.setHolonObjectProducer(null);
								supplier.setMessageStatus(ConstantValues.ACCEPTED);
								if(flexibility <= currentEnergyRequired) {
									supplier.setPowerGranted(flexibility);
									powerSource.setFlexibility(0);
								} else {
									supplier.setPowerGranted(currentEnergyRequired);
									powerSource.setFlexibility(flexibility - currentEnergyRequired);
								}
								supplier.setPowerRequested(currentEnergyRequired);
								supplier.setPowerSource(getPowerSourceService().merge(powerSource));
								supplier.setRequestId(0);
								Integer newSupplierId = getSupplierService().persist(supplier);
								Supplier supplier2 = getSupplierService().findById(newSupplierId);
								supplier2.setRequestId(newSupplierId);
								getSupplierService().merge(supplier2);
						}
					}
				}
				
				//Distributing energy via Holon Objects (Producers)
				Collections.sort(listOfConnectedHolonObjects);
				for(HolonObject holonObjectConsumer: listOfConsumers) {
					for(HolonObject holonObjectProducer : listOfConnectedHolonObjects) {
						Integer flexibility = getHolonObjectEnergyDetails(holonObjectProducer).get("flexibility");
						Integer currentEnergyRequired = getHolonObjectEnergyDetails(holonObjectConsumer).get("currentEnergyRequired");
						/*Energy is distributed only if flexibility of producer is greater than zero and it can communicate.
						 * Also the current energy required for consumer must be greater than zero*/
						if(flexibility > 0 && holonObjectProducer.getCanCommunicate() && currentEnergyRequired > 0) {
							Supplier supplier = new Supplier();
							supplier.setCommunicationMode(ConstantValues.COMMUNICATION_MODE_COORDINATOR);
							supplier.setHolonObjectConsumer(holonObjectConsumer);
							supplier.setHolonObjectProducer(holonObjectProducer);
							supplier.setMessageStatus(ConstantValues.ACCEPTED);
							if(flexibility <= currentEnergyRequired) {
								supplier.setPowerGranted(flexibility);
							} else {
								supplier.setPowerGranted(currentEnergyRequired);
							}
							supplier.setPowerRequested(currentEnergyRequired);
							supplier.setPowerSource(null);
							supplier.setRequestId(0);
							Integer newSupplierId = getSupplierService().persist(supplier);
							Supplier supplier2 = getSupplierService().findById(newSupplierId);
							supplier2.setRequestId(newSupplierId);
							getSupplierService().merge(supplier2);
						}
					}
				}
			}

			if(responseStr.length() > 0) {
				responseStr = responseStr.deleteCharAt(responseStr.lastIndexOf("!"));
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(responseStr.toString());
		} catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in checkInbox()");
			e.printStackTrace();
		}
	}
	
	/**
	 * This method checks whether two holon objects are connected or not and then sets the response accordingly 
	 */
	public void getConnectedStatusForHolonObjects() {
		try {
			Integer firstHolonObject = getRequest().getParameter("firstHolonObjectId")!=null?Integer.parseInt(getRequest().getParameter("firstHolonObjectId")):0;
			Integer secondHolonObject = getRequest().getParameter("secondHolonObjectId")!=null?Integer.parseInt(getRequest().getParameter("secondHolonObjectId")):0;
			HolonObject holonObjectFirst = getHolonObjectService().findById(firstHolonObject);
			HolonObject holonObjectSecond = getHolonObjectService().findById(secondHolonObject);
			boolean response= false;
			if(firstHolonObject!=secondHolonObject){
				response = checkConnectivityBetweenHolonObjects(holonObjectFirst,holonObjectSecond);//Function call to check connectivity - defined in CommonUtilities.java
			} else {
				response=true;
			}
			String responseStr="Failure";
			if(response) {//Preparing response to be sent to client side function
				responseStr="Success"; 
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(responseStr);
		}
		catch(Exception e) {
			log.info("Exception occured at getConnectedStatusForHolonObjects::"+e.getMessage());
		}
	}
	
	/**
	 * This method prepares response for history module which is accessible from info window of holon coordinator.
	 */
	public void historyDistributeEnergyAmongHolonObjects() {
		try {
				Integer holonCoordinatorId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
				HolonObject holonCoordinator = getHolonObjectService().findById(holonCoordinatorId);//Fetching holon coordinator from database
				ArrayList<Supplier> listOfSuppliersHolonCoordinator = null;
				if(holonCoordinator.getHolon() != null) {
					listOfSuppliersHolonCoordinator = getSupplierService().getSupplierListHolonCoordinator(holonCoordinator.getHolon());
				}
				StringBuffer responseStr =new StringBuffer("");
				for(Supplier supplier : listOfSuppliersHolonCoordinator) {//Preparing response for client side function
						responseStr.append(supplier.getHolonObjectConsumer().getId()+"#");
						responseStr.append(supplier.getHolonObjectConsumer().getLatLngByNeLocation().getLatitude()+"~"+
								supplier.getHolonObjectConsumer().getLatLngByNeLocation().getLongitude()+"#");
						responseStr.append(supplier.getHolonObjectConsumer().getHolonObjectType().getName()
								+" ("+supplier.getHolonObjectConsumer().getHolonObjectType().getPriority()+")"+"#");
						if(supplier.getHolonObjectProducer() != null) {
							responseStr.append(supplier.getHolonObjectProducer().getId()+"#");
							responseStr.append("Holon Object#");
						} else {
							responseStr.append(supplier.getPowerSource().getId()+"#");
							responseStr.append("Power Source#");
						}
						responseStr.append(supplier.getPowerRequested()+"#");
						responseStr.append(supplier.getPowerGranted()+"#");
						responseStr.append(supplier.getMessageStatus()+"#");
						if(supplier.getPowerSource() != null) {
							responseStr.append(checkConnectivityBetweenHolonObjectAndPowerSource(supplier.getHolonObjectConsumer(), supplier.getPowerSource())+"#");
						} else {
							responseStr.append(checkConnectivityBetweenHolonObjects(supplier.getHolonObjectConsumer(), supplier.getHolonObjectProducer())+"#");
						}
						responseStr.append(supplier.getCommunicationMode()+"!");
				}
				if(responseStr.length() > 0) {
					responseStr = responseStr.deleteCharAt(responseStr.lastIndexOf("!"));
				}
				getResponse().setContentType("text/html");
				getResponse().getWriter().write(responseStr.toString());
		} catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in historyDistributeEnergyAmongHolonObjects()");
		}
	}
	
}