package com.htc.factory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonElementState;
import com.htc.hibernate.pojo.HolonElementType;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.HolonObjectType;
import com.htc.hibernate.pojo.LatLng;
import com.htc.utilities.CommonUtilities;

/**
 * This class contains functions that actually create holon objects and holon elements in database using the input from client side.
 */
public class FactoryUtilities extends CommonUtilities{
	
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(FactoryUtilities.class);

	/** This method creates holon objects and holon elements in database
	 * @param holonObjectType_ProbabilityMap the map containing probability of holon object types that need to be created
	 */
	public void sendDataToFactory(Map<Integer, String> holonObjectType_ProbabilityMap) {
		int cumulativeProbability = 0;
		for(Integer holonObjectPriority: holonObjectType_ProbabilityMap.keySet()) {
			cumulativeProbability += Integer.parseInt(holonObjectType_ProbabilityMap.get(holonObjectPriority).split(":")[1]);
		}
		//Variables to be used inside the loop for data factory declared outside the loop so that we don't have to create a new object for entity.
		HolonObjectType holonObjectType = null;
		HolonObject holonObject = null;
		Double latNE = null;
		Double lngNE = null;
		Double latSW = null;
		Double lngSW = null;
		LatLng NorthlatLng = null;
		LatLng SouthlatLng = null;
		Integer NorthlocationId = null;
		Integer SouthlocationId = null;
		LatLng NorthlatLng2 = null;
		LatLng SouthlatLng2 = null;
		Integer newHolonObjectId = null;
		//Calling function that contains the algorithm for priority and probability concept
		Map<Integer, Integer> finalMapAfterFactoryAlgorithm = algorithmForPriorityProbability(holonObjectType_ProbabilityMap, cumulativeProbability);
		for(Integer holonObjectTypeId: finalMapAfterFactoryAlgorithm.keySet()) {
			holonObjectType = getHolonObjectTypeService().findById(holonObjectTypeId);
			log.info("Holon Object Type : "+holonObjectType.getName()+", No of Objects = "+finalMapAfterFactoryAlgorithm.get(holonObjectTypeId));
			int noOfObjects = finalMapAfterFactoryAlgorithm.get(holonObjectTypeId);
			while(noOfObjects > 0){
				latNE = Double.parseDouble(FactoryHolonObjectsLocation.mapLocations.get(0).split("~")[0].split("!")[0]);
				lngNE = Double.parseDouble(FactoryHolonObjectsLocation.mapLocations.get(0).split("~")[0].split("!")[1]);
				latSW = Double.parseDouble(FactoryHolonObjectsLocation.mapLocations.get(0).split("~")[1].split("!")[0]);
				lngSW = Double.parseDouble(FactoryHolonObjectsLocation.mapLocations.get(0).split("~")[1].split("!")[1]);
				FactoryHolonObjectsLocation.mapLocations.remove(0);
				
				NorthlatLng = new LatLng(latNE, lngNE);
				SouthlatLng = new LatLng(latSW, lngSW);
				NorthlocationId = saveLocation(NorthlatLng);
				SouthlocationId = saveLocation(SouthlatLng);
				NorthlatLng2 = getLatLngService().findById(NorthlocationId);
				SouthlatLng2 = getLatLngService().findById(SouthlocationId);

				holonObject = new HolonObject();
				holonObject.setLatLngByNeLocation(NorthlatLng2);
				holonObject.setLatLngBySwLocation(SouthlatLng2);
				holonObject.setHolonObjectType(holonObjectType);
				holonObject.setLineConnectedState(false);
				holonObject.setCanCommunicate(true);
				holonObject.setCreatedFactory(true);
				holonObject.setIsCoordinator(false);
				holonObject.setFlexibility(0);
				holonObject.setCoordinatorCompetency(new BigDecimal(Math.random()));
				holonObject.setTrustValue(new BigDecimal(Math.random()));
				//Calling service method to save the object in database and saving the auto-incremented ID in an integer
				newHolonObjectId = getHolonObjectService().persist(holonObject);
				HolonObject holonObject2 = getHolonObjectService().findById(newHolonObjectId);
				log.info("New HolonObject ID = "+newHolonObjectId);

				//Adding 10 Holon Elements to the newly generated Holon Object
				for(int i=0; i<10; i++) {
					Integer holonElementTypeId = randomNumber(1, 6);
					Integer holonElementStateId = 1;//Setting state to ON
					HolonElementType holonElementType = getHolonElementTypeService().findById(holonElementTypeId);
					HolonElementState holonElementState = getHolonElementStateService().findById(holonElementStateId);

					//Current capacity is a random number between maximum and minimum holon element capacity.
					Integer currentCapacity = randomNumber(holonElementType.getMinCapacity(), holonElementType.getMaxCapacity());
					
					HolonElement holonElement = new HolonElement();
					holonElement.setCurrentCapacity(currentCapacity);
					holonElement.setHolonElementState(holonElementState);
					holonElement.setHolonElementType(holonElementType);
					holonElement.setHolonObject(holonObject2);
					//Calling service method to save the Element in database and saving the auto-incremented ID in an integer
					Integer newHolonElementID = getHolonElementService().persist(holonElement);
					log.info("NewLy Generated Holon Element ID --> "+newHolonElementID);
				}
				noOfObjects --;
			}
		}
	}
	
	/**
	 * This is the algorithm that takes into consideration the priority and probability(entered by user) of the holon object type that needs to be created
	 * @param holonObjectType_ProbabilityMap the map containing probability of holon object type
	 * @param cumulativeProbability the combined probability of all holon object type (enteerd by user)
	 * @return
	 */
	public Map<Integer, Integer> algorithmForPriorityProbability(Map<Integer, String> holonObjectType_ProbabilityMap, int cumulativeProbability) {
		Map<Integer, Integer> mapHolonObjectTypesOccurences = new TreeMap<Integer, Integer>();
		int mapLocationSize = FactoryHolonObjectsLocation.mapLocations.size();
		
		try {
				while(mapLocationSize > 0 && cumulativeProbability > 0) {
					for(Integer holonObjectPriority : holonObjectType_ProbabilityMap.keySet()) {
						int noOfObjects = 0;
						double calculatedProbability = 0.0;
						if(mapLocationSize > 0 && cumulativeProbability > 0) {
							Integer holonObjectTypeId = Integer.parseInt(holonObjectType_ProbabilityMap.get(holonObjectPriority).split(":")[0]);
							Integer holonObjectProbability = Integer.parseInt(holonObjectType_ProbabilityMap.get(holonObjectPriority).split(":")[1]);
							if(holonObjectPriority.equals(1)){
								if(holonObjectProbability >= 10) {
									calculatedProbability = (3/4.0)*(holonObjectProbability);
									noOfObjects = (int)(Math.ceil(calculatedProbability*(mapLocationSize)/100.0));
								} else {
									noOfObjects = (int)(Math.ceil((holonObjectProbability)*(mapLocationSize)/100.0));
									}
							} else if(holonObjectPriority.equals(2)) {
								if(holonObjectProbability >= 10) {
									calculatedProbability = (1/2.0)*(holonObjectProbability);
									noOfObjects = (int)(Math.ceil(calculatedProbability*(mapLocationSize)/100.0));
								} else {
									noOfObjects = (int)(Math.ceil((holonObjectProbability)*(mapLocationSize)/100.0));
									}
							} else if (holonObjectPriority.equals(3)) {
								if(holonObjectProbability >= 10) {
									calculatedProbability = (1/3.0)*(holonObjectProbability);
									noOfObjects = (int)(Math.ceil(calculatedProbability*(mapLocationSize)/100.0));
								} else {
									noOfObjects = (int)(Math.ceil((holonObjectProbability)*(mapLocationSize)/100.0));
									}
							} else if (holonObjectPriority.equals(4)) {
								if(holonObjectProbability >= 10) {
									calculatedProbability = (1/4.0)*(holonObjectProbability);
									noOfObjects = (int)(Math.ceil(calculatedProbability*(mapLocationSize)/100.0));
								} else {
									noOfObjects = (int)(Math.ceil((holonObjectProbability)*(mapLocationSize)/100.0));
									}
							}
							if(mapLocationSize > 0 && cumulativeProbability > 0) {
								holonObjectType_ProbabilityMap.put(holonObjectPriority, holonObjectTypeId+":"+((int)(holonObjectProbability-calculatedProbability)));
								if(mapHolonObjectTypesOccurences.size() > 0 && mapHolonObjectTypesOccurences.containsKey(holonObjectTypeId)) {
									mapHolonObjectTypesOccurences.put(holonObjectTypeId, mapHolonObjectTypesOccurences.get(holonObjectTypeId)+noOfObjects);
								} else {
									mapHolonObjectTypesOccurences.put(holonObjectTypeId, noOfObjects);
								}
							}
							cumulativeProbability -= calculatedProbability;
							mapLocationSize -= noOfObjects;
						}
					}
					
				}
		} catch (NumberFormatException e) {
			log.info("Exception "+e.getMessage()+" occurred in algorithmForPriorityProbability()");
		}
		return mapHolonObjectTypesOccurences;
	}
	
	public int randomNumber(int min, int max) {
		return (min + (int)(Math.random()*((max-min)+1)));
	}

}
