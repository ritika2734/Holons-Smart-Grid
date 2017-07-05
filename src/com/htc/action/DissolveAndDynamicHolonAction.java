package com.htc.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.pojo.Supplier;
import com.htc.utilities.CommonUtilities;
import com.htc.utilities.ConstantValues;

/**
 * This class contains all methods that are used in dissolve holon module and start dynamic holon module.
 */
public class DissolveAndDynamicHolonAction extends CommonUtilities {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DissolveAndDynamicHolonAction.class);

	/**
	 *  This method is used to dissolve an entire holon into a suitable holon which fulfills the energy requirements of the holon that wants to dissolve.
	 *  Only Holon Coordinator has access to this module.
	 *  Holon will dissolve only if the flexibility of current holon is zero and current energy requirement is greater than zero.
	 */
	public void dissolveHolon() {
		try {
			//Fetching holon coordinator from database
			Integer holonCoordinatorId = getRequest().getParameter("holonCoordinatorId")!=null?Integer.parseInt(getRequest().getParameter("holonCoordinatorId")):0;
			String responseDissolveHolon = "false";
			StringBuffer responseDissolveHolonTrue = new StringBuffer();
			if(holonCoordinatorId > 0) {
				HolonObject holonCoordinator = getHolonObjectService().findById(holonCoordinatorId);
				Integer currentEnergyRequiredHolon = 0;
				Integer flexibilityHolon = 0;
				Map<String, String> holonEnergyDetails = getHolonEnergyDetails(holonCoordinator);
				currentEnergyRequiredHolon = Integer.parseInt(holonEnergyDetails.get("currentEnergyRequiredHolon"));
				flexibilityHolon = Integer.parseInt(holonEnergyDetails.get("flexibilityHolon"));
				//Holon will dissolve only if the flexibility of current holon is zero and current energy requirement is greater than zero
				if(flexibilityHolon == 0 && currentEnergyRequiredHolon > 0) {
					PowerLine powerLine = getPowerLineService().getPowerLineByHolonObject(holonCoordinator);
					if(powerLine != null) {
						ArrayList<HolonObject> connectedHolonObjectsOfAllHolons = getHolonObjectListByConnectedPowerLinesOfAllHolons(powerLine, "common");
						//Function call to get list of holon coordinators of all holons to find a suitable new holon based on energy requirements
						Map<String, ArrayList<HolonObject>> mapOfHolonCoordinatorsOfAllHolons = getHolonCoordinatorsOfAllHolons(connectedHolonObjectsOfAllHolons);
						ArrayList<HolonObject> redHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("redCoordinators");
						ArrayList<HolonObject> yellowHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("yellowCoordinators");
						ArrayList<HolonObject> blueHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("blueCoordinators");
						ArrayList<HolonObject> greenHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("greenCoordinators");
						HolonObject redHolonCoordinator = null;
						HolonObject yellowHolonCoordinator = null;
						HolonObject blueHolonCoordinator = null;
						HolonObject greenHolonCoordinator = null;
						if(redHolonCoordinatorsList!=null && redHolonCoordinatorsList.size() == 1) {
							redHolonCoordinator = redHolonCoordinatorsList.get(0);
						}
						if(yellowHolonCoordinatorsList!=null && yellowHolonCoordinatorsList.size() == 1) {
							yellowHolonCoordinator = yellowHolonCoordinatorsList.get(0);
						}
						if(blueHolonCoordinatorsList!=null && blueHolonCoordinatorsList.size() == 1) {
							blueHolonCoordinator = blueHolonCoordinatorsList.get(0);
						}
						if(greenHolonCoordinatorsList!=null && greenHolonCoordinatorsList.size() == 1) {
							greenHolonCoordinator = greenHolonCoordinatorsList.get(0);
						}
						HolonObject bestHolonCoordinatorMatch = null;
						Integer redBenchmarkEnergy = 0;
						Integer yellowBenchmarkEnergy = 0;
						Integer greenBenchmarkEnergy = 0;
						Integer blueBenchmarkEnergy = 0;
						
						if(redHolonCoordinator != null) {
							Integer currentEnergyRequiredHolonTemp = 0;
							Integer flexibilityHolonTemp = 0;
							Map<String, String> holonEnergyDetailsTemp = getHolonEnergyDetails(redHolonCoordinator);
							currentEnergyRequiredHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("currentEnergyRequiredHolon"));
							flexibilityHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("flexibilityHolon"));
							redBenchmarkEnergy = (flexibilityHolonTemp-currentEnergyRequiredHolonTemp)-currentEnergyRequiredHolon;
							if(redBenchmarkEnergy >= 0) {
								//Assigning the first possible best holon coordinator match. System will not check further for more suitable coordinators.
								bestHolonCoordinatorMatch = redHolonCoordinator;
							}
						}
						//System will check further for suitable coordinators only if bestHolonCoordinatorMatch is null 
						if(bestHolonCoordinatorMatch == null && yellowHolonCoordinator != null) {
							Integer currentEnergyRequiredHolonTemp = 0;
							Integer flexibilityHolonTemp = 0;
							Map<String, String> holonEnergyDetailsTemp = getHolonEnergyDetails(yellowHolonCoordinator);
							currentEnergyRequiredHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("currentEnergyRequiredHolon"));
							flexibilityHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("flexibilityHolon"));
							yellowBenchmarkEnergy = (flexibilityHolonTemp-currentEnergyRequiredHolonTemp)-currentEnergyRequiredHolon;
							if(yellowBenchmarkEnergy > redBenchmarkEnergy && yellowBenchmarkEnergy > 0) {
								//Assigning the first possible best holon coordinator match. System will not check further for more suitable coordinators.
								bestHolonCoordinatorMatch = yellowHolonCoordinator;
							}
						}
						//System will check further for suitable coordinators only if bestHolonCoordinatorMatch is null
						if(bestHolonCoordinatorMatch == null && greenHolonCoordinator != null) {
							Integer currentEnergyRequiredHolonTemp = 0;
							Integer flexibilityHolonTemp = 0;
							Map<String, String> holonEnergyDetailsTemp = getHolonEnergyDetails(greenHolonCoordinator);
							currentEnergyRequiredHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("currentEnergyRequiredHolon"));
							flexibilityHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("flexibilityHolon"));
							greenBenchmarkEnergy = (flexibilityHolonTemp-currentEnergyRequiredHolonTemp)-currentEnergyRequiredHolon;
							if(greenBenchmarkEnergy > yellowBenchmarkEnergy && greenBenchmarkEnergy > 0) {
								//Assigning the first possible best holon coordinator match. System will not check further for more suitable coordinators.
								bestHolonCoordinatorMatch = greenHolonCoordinator;
							}
						}
						//System will check further for suitable coordinators only if bestHolonCoordinatorMatch is null
						if(bestHolonCoordinatorMatch == null && blueHolonCoordinator != null) {
							Integer currentEnergyRequiredHolonTemp = 0;
							Integer flexibilityHolonTemp = 0;
							Map<String, String> holonEnergyDetailsTemp = getHolonEnergyDetails(blueHolonCoordinator);
							currentEnergyRequiredHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("currentEnergyRequiredHolon"));
							flexibilityHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("flexibilityHolon"));
							blueBenchmarkEnergy = (flexibilityHolonTemp-currentEnergyRequiredHolonTemp)-currentEnergyRequiredHolon;
							if(blueBenchmarkEnergy > greenBenchmarkEnergy && blueBenchmarkEnergy > 0) {
								//Assigning the first possible best holon coordinator match. System will not check further for more suitable coordinators.
								bestHolonCoordinatorMatch = blueHolonCoordinator;
							}
						}
						if(bestHolonCoordinatorMatch != null) {
							if(bestHolonCoordinatorMatch.getIsCoordinator() && bestHolonCoordinatorMatch.getHolon()!=null) {
								HolonObject newHolonCoordinator = null;
								responseDissolveHolon = "true";
								Holon newHolon = bestHolonCoordinatorMatch.getHolon();
								Holon oldHolon = holonCoordinator.getHolon();
								//Leadership election between holon coordinators
								BigDecimal competencyTrust1 = bestHolonCoordinatorMatch.getCoordinatorCompetency().multiply(bestHolonCoordinatorMatch.getTrustValue());
								BigDecimal competencyTrust2 = holonCoordinator.getCoordinatorCompetency().multiply(holonCoordinator.getTrustValue());
								if(competencyTrust1.compareTo(competencyTrust2) == -1) {
									responseDissolveHolonTrue.append(bestHolonCoordinatorMatch.getId()+"!"+newHolon.getColor()+"*");
									bestHolonCoordinatorMatch.setIsCoordinator(false);
									getHolonObjectService().merge(bestHolonCoordinatorMatch);
									newHolonCoordinator = holonCoordinator;
								} else {
									responseDissolveHolonTrue.append(holonCoordinator.getId()+"!"+newHolon.getColor()+"*");
									holonCoordinator.setIsCoordinator(false);
									newHolonCoordinator = bestHolonCoordinatorMatch;
								}
								//Updating power sources connected to the current holon
								ArrayList<PowerSource> listOfPowerSources = getPowerSourceService().findByHolonCoordinator(holonCoordinator);
								for(PowerSource powerSource : listOfPowerSources) {
									powerSource.setHolonCoordinator(newHolonCoordinator);
									getPowerSourceService().merge(powerSource);
								}
								//Setting holon objects of current holon in list to set color on ajaz request completion.
								for(HolonObject holonObject : connectedHolonObjectsOfAllHolons) {
									if(holonObject.getHolon().getId() == holonCoordinator.getHolon().getId()) {
										responseDissolveHolonTrue.append(holonObject.getId()+"~");
										holonObject.setHolon(newHolon);
										getHolonObjectService().merge(holonObject);
									}
								}
								holonCoordinator.setHolon(newHolon);
								getHolonObjectService().merge(holonCoordinator);
								log.info("Old Holon --> "+oldHolon.getName());
								log.info("New Holon --> "+newHolon.getName());
							}
						} else {
							responseDissolveHolon = "noOtherHolonFound";
						}
					}
					
				}
			}
			//Calling the response function and setting the content type of response.
			getResponse().setContentType("text/html");
			if(responseDissolveHolon.equalsIgnoreCase("true")) {
				getResponse().getWriter().write(responseDissolveHolonTrue.toString());
			} else {
				getResponse().getWriter().write(responseDissolveHolon);
			}
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action dissolveHolon()");
		}
	}
	
	/**
	 * This method is used by start dynamic holon module and it checks for the current energy requirement of the holon object. 
	 */
	public void checkDynamicCurrentEnergyRequired() {
		try {
			Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
			HolonObject holonObject = getHolonObjectService().findById(holonObjectId);//Fetching holon object from database
			Integer currentEnergyRequired = 0;
			Integer originalEnergyRequiredAfterCurrentProduction = 0;
			if(holonObject != null) {
				//Function call to fetch energy details of the holon object
				Map<String, Integer> holonObjectEnergyDetails = getHolonObjectEnergyDetails(holonObject);
				currentEnergyRequired = holonObjectEnergyDetails.get("currentEnergyRequired");
				originalEnergyRequiredAfterCurrentProduction = holonObjectEnergyDetails.get("originalEnergyRequiredAfterCurrentProduction");
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(currentEnergyRequired+"~"+originalEnergyRequiredAfterCurrentProduction);
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action checkDynamicCurrentEnergyRequired()");
		}
	}
	
	/**
	 * This method is part of start dynamic holon module and it merges the holon object with a suitable holon. 
	 */
	public void startDynamicHolonMerger() {
		try {
			Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
			Integer originalEnergyRequiredAfterCurrentProduction = getRequest().getParameter("originalEnergyRequiredAfterCurrentProduction")!=null?
					Integer.parseInt(getRequest().getParameter("originalEnergyRequiredAfterCurrentProduction")):0;
			HolonObject holonObject = getHolonObjectService().findById(holonObjectId);
			String startDynamicHolonMergerResponse = "false";
			StringBuffer startDynamicHolonMergerResponseBuffer = new StringBuffer();
			
			if(holonObject != null && originalEnergyRequiredAfterCurrentProduction > 0) {
				PowerLine powerLine = getPowerLineService().getPowerLineByHolonObject(holonObject);
				if(powerLine != null) {
					//Fetching connected holon objects of all holons
					ArrayList<HolonObject> connectedHolonObjectsOfAllHolons = getHolonObjectListByConnectedPowerLinesOfAllHolons(powerLine, "common");
					Map<String, ArrayList<HolonObject>> mapOfHolonCoordinatorsOfAllHolons = getHolonCoordinatorsOfAllHolons(connectedHolonObjectsOfAllHolons);
					ArrayList<HolonObject> redHolonCoordinatorsList = null;
					ArrayList<HolonObject> yellowHolonCoordinatorsList = null;
					ArrayList<HolonObject> blueHolonCoordinatorsList = null;
					ArrayList<HolonObject> greenHolonCoordinatorsList = null;
					String currentHolonColor = holonObject.getHolon()!=null?holonObject.getHolon().getColor():"black";
					if(!currentHolonColor.equalsIgnoreCase("red")) {
						redHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("redCoordinators");
					}
					if(!currentHolonColor.equalsIgnoreCase("blue")) {
						blueHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("blueCoordinators");
					}
					if (!currentHolonColor.equalsIgnoreCase("green")) {
						greenHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("greenCoordinators");
					}
					if(!currentHolonColor.equalsIgnoreCase("yellow")) {
						yellowHolonCoordinatorsList = mapOfHolonCoordinatorsOfAllHolons.get("yellowCoordinators");
					}
					HolonObject redHolonCoordinator = null;
					HolonObject yellowHolonCoordinator = null;
					HolonObject blueHolonCoordinator = null;
					HolonObject greenHolonCoordinator = null;
					if(redHolonCoordinatorsList!=null && redHolonCoordinatorsList.size() == 1) {
						redHolonCoordinator = redHolonCoordinatorsList.get(0);
					}
					if(yellowHolonCoordinatorsList!=null && yellowHolonCoordinatorsList.size() == 1) {
						yellowHolonCoordinator = yellowHolonCoordinatorsList.get(0);
					}
					if(blueHolonCoordinatorsList!=null && blueHolonCoordinatorsList.size() == 1) {
						blueHolonCoordinator = blueHolonCoordinatorsList.get(0);
					}
					if(greenHolonCoordinatorsList!=null && greenHolonCoordinatorsList.size() == 1) {
						greenHolonCoordinator = greenHolonCoordinatorsList.get(0);
					}
					HolonObject bestHolonCoordinatorMatch = null;
					Integer redBenchmarkEnergy = 0;
					Integer yellowBenchmarkEnergy = 0;
					Integer greenBenchmarkEnergy = 0;
					Integer blueBenchmarkEnergy = 0;
					//If conditions to find the best holon coordinator match
					if(redHolonCoordinator != null) {
						Integer currentEnergyRequiredHolonTemp = 0;
						Integer flexibilityHolonTemp = 0;
						Map<String, String> holonEnergyDetailsTemp = getHolonEnergyDetails(redHolonCoordinator);
						currentEnergyRequiredHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("currentEnergyRequiredHolon"));
						flexibilityHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("flexibilityHolon"));
						redBenchmarkEnergy = (flexibilityHolonTemp-currentEnergyRequiredHolonTemp)-originalEnergyRequiredAfterCurrentProduction;
						if(redBenchmarkEnergy >= 0) {
							bestHolonCoordinatorMatch = redHolonCoordinator;
						}
					}
					if(bestHolonCoordinatorMatch == null && yellowHolonCoordinator != null) {
						Integer currentEnergyRequiredHolonTemp = 0;
						Integer flexibilityHolonTemp = 0;
						Map<String, String> holonEnergyDetailsTemp = getHolonEnergyDetails(yellowHolonCoordinator);
						currentEnergyRequiredHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("currentEnergyRequiredHolon"));
						flexibilityHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("flexibilityHolon"));
						yellowBenchmarkEnergy = (flexibilityHolonTemp-currentEnergyRequiredHolonTemp)-originalEnergyRequiredAfterCurrentProduction;
						if(yellowBenchmarkEnergy > redBenchmarkEnergy && yellowBenchmarkEnergy > 0) {
							bestHolonCoordinatorMatch = yellowHolonCoordinator;
						}
					}
					if(bestHolonCoordinatorMatch == null && greenHolonCoordinator != null) {
						Integer currentEnergyRequiredHolonTemp = 0;
						Integer flexibilityHolonTemp = 0;
						Map<String, String> holonEnergyDetailsTemp = getHolonEnergyDetails(greenHolonCoordinator);
						currentEnergyRequiredHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("currentEnergyRequiredHolon"));
						flexibilityHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("flexibilityHolon"));
						greenBenchmarkEnergy = (flexibilityHolonTemp-currentEnergyRequiredHolonTemp)-originalEnergyRequiredAfterCurrentProduction;
						if(greenBenchmarkEnergy > yellowBenchmarkEnergy && greenBenchmarkEnergy > 0) {
							bestHolonCoordinatorMatch = greenHolonCoordinator;
						}
					}
					if(bestHolonCoordinatorMatch == null && blueHolonCoordinator != null) {
						Integer currentEnergyRequiredHolonTemp = 0;
						Integer flexibilityHolonTemp = 0;
						Map<String, String> holonEnergyDetailsTemp = getHolonEnergyDetails(blueHolonCoordinator);
						currentEnergyRequiredHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("currentEnergyRequiredHolon"));
						flexibilityHolonTemp = Integer.parseInt(holonEnergyDetailsTemp.get("flexibilityHolon"));
						blueBenchmarkEnergy = (flexibilityHolonTemp-currentEnergyRequiredHolonTemp)-originalEnergyRequiredAfterCurrentProduction;
						if(blueBenchmarkEnergy > greenBenchmarkEnergy && blueBenchmarkEnergy > 0) {
							bestHolonCoordinatorMatch = blueHolonCoordinator;
						}
					}
					if(bestHolonCoordinatorMatch != null) {
						if(bestHolonCoordinatorMatch.getIsCoordinator() && bestHolonCoordinatorMatch.getHolon()!=null) {
							startDynamicHolonMergerResponse = "true";
							Holon newHolon = bestHolonCoordinatorMatch.getHolon();
							Holon oldHolon = holonObject.getHolon();
							if(holonObject.getIsCoordinator()) {
								//Leadership election between holon coordinators
								BigDecimal competencyTrust1 = bestHolonCoordinatorMatch.getCoordinatorCompetency().multiply(bestHolonCoordinatorMatch.getTrustValue());
								BigDecimal competencyTrust2 = holonObject.getCoordinatorCompetency().multiply(holonObject.getTrustValue());
								if(competencyTrust1.compareTo(competencyTrust2) == -1) {
									startDynamicHolonMergerResponseBuffer.append(bestHolonCoordinatorMatch.getId()+"!"+newHolon.getColor()+"!"+holonObjectId);
									bestHolonCoordinatorMatch.setIsCoordinator(false);
									getHolonObjectService().merge(bestHolonCoordinatorMatch);
								} else {
									startDynamicHolonMergerResponseBuffer.append(holonObject.getId()+"!"+newHolon.getColor()+"!"+holonObjectId);
									holonObject.setIsCoordinator(false);
								}
							} else {
								startDynamicHolonMergerResponseBuffer.append(0+"!"+newHolon.getColor()+"!"+holonObjectId);
							}
							ArrayList<Supplier> listSupplier = getSupplierService().getSupplierListForConsumerOrProducer(holonObject);
							for(Supplier supplier : listSupplier) {
								//Before joining a new holon, resetting previous communications between holon objects of old holon
								supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
								getSupplierService().merge(supplier);
							}
							holonObject.setHolon(newHolon);
							getHolonObjectService().merge(holonObject);
							log.info("Old Holon --> "+oldHolon.getName());
							log.info("New Holon --> "+newHolon.getName());
						}
					}
				}
			}
			getResponse().setContentType("text/html");
			if(startDynamicHolonMergerResponse.equalsIgnoreCase("true")) {
				getResponse().getWriter().write(startDynamicHolonMergerResponseBuffer.toString());
			} else {
				getResponse().getWriter().write(startDynamicHolonMergerResponse);
			}
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action startDynamicHolonMerger()");
		}
	}
	
}
