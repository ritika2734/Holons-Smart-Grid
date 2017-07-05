package com.htc.factory;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonElementType;
import com.htc.hibernate.pojo.HolonObjectType;
import com.htc.utilities.CommonUtilities;

/**
 * This class contains functions that are used to take client input to create holon objects from factory.
 *
 */
public class FactoryDataGeneratorAction extends CommonUtilities {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(FactoryDataGeneratorAction.class);

	/**
	 * This method is used to take request parameters from client side and then call the necessary functions to create holon objects 
	 */
	public void factoryDataGenerator() {
		try {
			//Fetching request parameters
			Integer totalHolonObjectTypes = getRequest().getParameter("totalHolonObjectTypes")!=null?Integer.parseInt(getRequest().getParameter("totalHolonObjectTypes")):0;
			String htmlIdHolonObjectTypes = getRequest().getParameter("htmlIdHolonObjectTypes")!=null?getRequest().getParameter("htmlIdHolonObjectTypes"):"";
			String htmlValuesHolonObjectTypes = getRequest().getParameter("htmlValuesHolonObjectTypes")!=null?getRequest().getParameter("htmlValuesHolonObjectTypes"):"";
			String holonObjectTypesPriorities = getRequest().getParameter("holonObjectTypesPriorities")!=null?getRequest().getParameter("holonObjectTypesPriorities"):"";

			log.info("No of HolonObject Types = "+totalHolonObjectTypes);
			log.info("htmlIdHolonObjectTypes = "+htmlIdHolonObjectTypes);
			log.info("htmlValuesHolonObjectTypes = "+htmlValuesHolonObjectTypes);

			String[] holonObjectTypesIdsList = htmlIdHolonObjectTypes.replaceAll("holonObjectType_", "").split("~~");
			String[] holonObjectTypesValues = htmlValuesHolonObjectTypes.split("~~");
			String[] holonObjectTypesPrioritiesList = holonObjectTypesPriorities.split("~");
			
			Map<Integer, String> holonObjectTypeProbabilityMap = new TreeMap<Integer, String>();
			for(int i=0;i<holonObjectTypesIdsList.length;i++) {
				Integer holonObjectTypeId = Integer.parseInt(holonObjectTypesIdsList[i]);
				Integer objectTypeProbability = Integer.parseInt(holonObjectTypesValues[i]);
				Integer holonObjectTypesPriority = Integer.parseInt(holonObjectTypesPrioritiesList[i]);
				holonObjectTypeProbabilityMap.put(holonObjectTypesPriority, holonObjectTypeId+":"+objectTypeProbability);
			}
			FactoryUtilities factoryUtilities = new FactoryUtilities();
			factoryUtilities.sendDataToFactory(holonObjectTypeProbabilityMap);//Function call to send the manipulated client data to factory
			getResponse().setContentType("text/html");
			getResponse().getWriter().write("Holon Objects created successfully from factory.");
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in factoryDataGenerator()");
		}
	}	

	/**
	 * This function is used to get list of all holon object types from database and send the response to client
	 */
	public void factoryListHolonObjectType() {
		try {
			ArrayList<HolonObjectType> holonObjectTypes = getHolonObjectTypeService().getAllHolonObjectType();
			StringBuffer holonObjectTypeList = new StringBuffer();
			for(HolonObjectType holonObjectType:holonObjectTypes){
				holonObjectTypeList.append(holonObjectType.getId()+"~");
				holonObjectTypeList.append(holonObjectType.getName()+"~");
				holonObjectTypeList.append(holonObjectType.getPriority());
				holonObjectTypeList.append("*");
			}
			holonObjectTypeList.replace(holonObjectTypeList.length()-1, holonObjectTypeList.length(), "");
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(holonObjectTypeList.toString());
		} catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in factoryListHolonObjectType()");
		}
	}

	/**
	 * This function is used to get all holon elements types from database and send the response to client 
	 */
	public void factoryListHolonElementType() {
		ArrayList<HolonElementType> holonElementTypes = getHolonElementTypeService().getAllHolonElementType();
		StringBuffer holonElementTypeNameList = new StringBuffer();
		String holonElementTypeInfo;
		int sNo = 0;
		for(HolonElementType holonElementType:holonElementTypes) {
			if(holonElementType.getProducer()) {
				holonElementTypeInfo = holonElementType.getName().
					concat(" : (Max. Capacity:"+holonElementType.getMaxCapacity()).concat(", Min. Capacity:"+holonElementType.getMinCapacity()+")").concat(" : [Producer]");
			} else {
				holonElementTypeInfo = holonElementType.getName().
						concat(" : (Max. Capacity:"+holonElementType.getMaxCapacity()).concat(", Min. Capacity:"+holonElementType.getMinCapacity()+")").concat(" : [Consumer]");
			}
			holonElementTypeNameList.append((sNo+1)+" - "+holonElementTypeInfo+"~~");
			sNo++;
			log.info(holonElementTypeInfo);
		}
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write(holonElementTypeNameList.toString());
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action factoryListHolonElementType()");
		}
	}

}
