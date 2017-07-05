package com.htc.action;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.utilities.CommonUtilities;

public class ConsumptionGraphAction extends CommonUtilities {

	/**
	 * This class contains functions which are used by high charts at client side
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(ConsumptionGraphAction.class);
	
	/**
	 * This method is used by high charts at client side to show consumption graph. 
	 * The output of this function is the list of holon elements current capacity and the holon object to which these holon elements belong to.
	 */
	public void showConsumptionGraph() {
		try {
			Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
			HolonObject holonObject = getHolonObjectService().findById(holonObjectId);		 
			ArrayList<HolonElement> holonElementList = getHolonElementService().getHolonElements(holonObject);
			int response=0;
			for(int i=0;i< holonElementList.size();i++) {
				response=response+holonElementList.get(i).getCurrentCapacity();
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(response+"*"+holonObjectId);
		}
		catch(Exception e) {
			log.debug("Error occurred in consumption graph action. Please check application Logs for more information.");
		}
	}

}
