package com.htc.action;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.utilities.CommonUtilities;

/**
 * This class contains functions related to holon coordinator
 */
public class HolonCoordinatorAction extends CommonUtilities{

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(HolonCoordinatorAction.class);

	/**
	 * This method just sets true flag in response 
	 */
	public void updateCoordinator() {
		try {
			String response = "true";
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(response);
		} catch (IOException e) {
			log.info("Exception "+e.getMessage()+" occurred in action updateCoordinator()");
		}
	}
	
	/**
	 *  This method is used to get Holon coordinators from database and send their IDs in response, so that Holon Coordinator icons can be placed accordingly.
	 */
	public void getHoCoIcons() {
		try {
			ArrayList<HolonObject> holonCoordinatorList = getHolonObjectService().findAllHolonCoordinators();//Fetching all holon coordinators from database.
			StringBuffer response = new StringBuffer();
			for(HolonObject holonCordinator : holonCoordinatorList) {
				response.append(holonCordinator.getId()+"~"+holonCordinator.getHolon().getColor()+"*");
			}
			if(response.length() > 0) {
				response = response.deleteCharAt(response.lastIndexOf("*"));
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(response.toString());
		} catch (IOException e) {
			log.info("Exception "+e.getMessage()+" occurred in action getHoCoIcons()");
		}
	}

}
