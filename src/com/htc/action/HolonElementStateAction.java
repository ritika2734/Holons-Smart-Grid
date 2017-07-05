package com.htc.action;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonElementState;
import com.htc.utilities.CommonUtilities;

/**
 * This class contains all actions related to holon element state
 *
 */
public class HolonElementStateAction extends CommonUtilities {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(HolonElementStateAction.class);

	/**
	 * This action creates a transient object of holonElementState and 
	 * saves it in the database and then returns the ID of newly created row.
	 */
	public void createHolonElementState(){
		HolonElementState holonElementState = new HolonElementState(); // Creating HolonElementState object to store values
		String holonElementStateName = getRequest().getParameter("holonElementStateName")!=null?
				getRequest().getParameter("holonElementStateName"):"Default Value";//Getting HE name value from JSP
		holonElementState.setName(holonElementStateName); // Setting values in HE State object
		//Calling service method to save the object in database and saving the auto-incremented ID in an integer
		Integer newHolonElementStateID = getHolonElementStateService().persist(holonElementState);
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write("<tr><td onclick='deleteHolonElement("+newHolonElementStateID+")'>"+newHolonElementStateID+"</td><td>"+holonElementStateName+"</td></tr>");
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action createHolonElementState()");
			e.printStackTrace();
		}
	}

	/**
	 * This action merges an existing object of holonElementState and 
	 * merges it in the database and then returns the merged object.
	 */
	public void editHolonElementState() {
		Integer holonElementStateId = getRequest().getParameter("holonElementStateId")!=null?
				Integer.parseInt(getRequest().getParameter("holonElementStateId")):0;//Getting HE ID value from JSP
		String holonElementStateName = getRequest().getParameter("holonElementStateName")!=null?
				getRequest().getParameter("holonElementStateName"):"";//Getting HE Type value from JSP
		HolonElementState holonElementState = getHolonElementStateService().findById(holonElementStateId); // Fetching holon element State from database
		holonElementState.setName(holonElementStateName); // Setting new values in HE State object
		//Editing holon element State object and saving in database 
		HolonElementState holonElementState2 = getHolonElementStateService().merge(holonElementState);
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write("Updated Name from DB = "+holonElementState2.getName());
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action editHolonElementState()");
			e.printStackTrace();
		}
	}
	
	/**
	 * This action deletes an existing object of holonElementState from database.
	 */
	public void deleteHolonElementState(){
		Integer holonElementStateId = getRequest().getParameter("holonElementStateId")!=null?
				Integer.parseInt(getRequest().getParameter("holonElementStateId")):0;//Getting HE ID value from JSP
		HolonElementState holonElementState = getHolonElementStateService().findById(holonElementStateId); // Fetching holon element State from database
		//Deleting holon element State object 
		boolean deleteStatus = getHolonElementStateService().delete(holonElementState);
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write("Delete Status --> "+deleteStatus);
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action deleteHolonElementState()");
		}
	}
	
	/**
	 * This action fetches list of all holon element States from database.
	 */
	public void getListHolonElementState(){
		//Fetching holon element State objects from database 
		ArrayList<HolonElementState> holonElementStates = getHolonElementStateService().getAllHolonElementState();
		StringBuffer holonElementStateNameList = new StringBuffer();
		for(HolonElementState holonElementState:holonElementStates){
			holonElementStateNameList.append(holonElementState.getId()+" - "+holonElementState.getName()+"*");
		}
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			getResponse().getWriter().write(holonElementStateNameList.toString());
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action getListHolonElementState()");
		}
	}
	

}
