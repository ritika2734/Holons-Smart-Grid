package com.htc.action;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonElementType;
import com.htc.utilities.CommonUtilities;

/**
 * This class contains all actions related to holon element type
 */
public class HolonElementTypeAction extends CommonUtilities {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(HolonElementTypeAction.class);

	/**
	 * This action creates a transient object of holonElementType and 
	 * saves it in the database and then returns the ID of newly created row.
	 */
	public void createHolonElementType(){

		HolonElementType holonElementType = new HolonElementType(); // Creating HolonElementType object to store values
		String holonElementTypeName = getRequest().getParameter("holonElementTypeName")!=null?
				getRequest().getParameter("holonElementTypeName"):"Default Value";//Getting HE name value from JSP
		holonElementType.setName(holonElementTypeName); // Setting values in HE type object
		//Calling service method to save the object in database and saving the auto-incremented ID in an integer
		Integer newHolonElementTypeID = getHolonElementTypeService().persist(holonElementType);
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write("<tr><td onclick='deleteHolonElement("+newHolonElementTypeID+")'>"+newHolonElementTypeID+"</td><td>"+holonElementTypeName+"</td></tr>");
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action createHolonElementType()");
			e.printStackTrace();
		}
	}

	/**
	 * This action merges an existing object of holonElementType and 
	 * merges it in the database and then returns the merged object.
	 */
	public void editHolonElementType(){
		Integer holonElementTypeId = getRequest().getParameter("holonElementTypeId")!=null?
				Integer.parseInt(getRequest().getParameter("holonElementTypeId")):0;//Getting HE ID value from JSP
		String holonElementTypeName = getRequest().getParameter("holonElementTypeName")!=null?
						getRequest().getParameter("holonElementTypeName"):"Default Value";//Getting HE name value from JSP
		HolonElementType holonElementType = getHolonElementTypeService().findById(holonElementTypeId); // Fetching holon element type from database
		holonElementType.setName(holonElementTypeName); // Setting new values in HE type object
		//Editing holon element type object and saving in database 
		HolonElementType holonElementType2 = getHolonElementTypeService().merge(holonElementType);
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write("Updated Name from DB = "+holonElementType2.getName());
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action editHolonElementType()");
			e.printStackTrace();
		}
	}
	
	/**
	 * This action deletes an existing object of holonElementType from database.
	 */
	public void deleteHolonElementType(){
		Integer holonElementTypeId = getRequest().getParameter("holonElementTypeId")!=null?
				Integer.parseInt(getRequest().getParameter("holonElementTypeId")):0;//Getting HE ID value from JSP
		HolonElementType holonElementType = getHolonElementTypeService().findById(holonElementTypeId); // Fetching holon element type from database
		//Deleting holon element type object 
		boolean deleteStatus = getHolonElementTypeService().delete(holonElementType);
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write("Delete Status --> "+deleteStatus);
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action editHolonElementType()");
			e.printStackTrace();
		}
	}
	
	/**
	 * This action fetches list of all holon element types from database.
	 */
	public void getListHolonElementType(){
		//Fetching holon element type objects from database
		ArrayList<HolonElementType> holonElementTypes = getHolonElementTypeService().getAllHolonElementType();
		StringBuffer holonElementTypeNameList = new StringBuffer();
		String holonElementTypeInfo;
		for(HolonElementType holonElementType:holonElementTypes){
			if(holonElementType.getProducer()) {
				holonElementTypeInfo = holonElementType.getName().
					concat(" : (Max. Capacity:"+holonElementType.getMaxCapacity()).concat(", Min. Capacity:"+holonElementType.getMinCapacity()+")").concat(" : [Producer]");
			} else {
				holonElementTypeInfo = holonElementType.getName().
						concat(" : (Max. Capacity:"+holonElementType.getMaxCapacity()).concat(", Min. Capacity:"+holonElementType.getMinCapacity()+")").concat(" : [Consumer]");
			}
			holonElementTypeNameList.append(holonElementType.getId()+" - "+holonElementTypeInfo+"*\n");
			log.info(holonElementTypeInfo);
		}
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write(holonElementTypeNameList.toString());
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action getListHolonElementType()");
			e.printStackTrace();
		}
	}

}
