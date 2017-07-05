package com.htc.action;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonElementState;
import com.htc.hibernate.pojo.HolonElementType;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.Supplier;
import com.htc.utilities.CommonUtilities;
import com.htc.utilities.ConstantValues;

/**
 * This class contains all actions related to Holon Elements
 */
public class HolonElementAction extends CommonUtilities {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(HolonElementAction.class);

	/**
	 * This method is used to create holon element in database
	 */
	public void createHolonElement() {
		try {
				//Fetching request parameters sent by client side functions
				Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):0;
				Integer holonElementTypeId = getRequest().getParameter("holonElementTypeId")!=null?Integer.parseInt(getRequest().getParameter("holonElementTypeId")):0;
				Integer holonElementStateId = getRequest().getParameter("holonElementStateId")!=null?Integer.parseInt(getRequest().getParameter("holonElementStateId")):0;
				Integer currentCapacity = getRequest().getParameter("currentCapacity")!=null && getRequest().getParameter("currentCapacity")!=""?
						Integer.parseInt(getRequest().getParameter("currentCapacity")):0;
				HolonObject holonObject = getHolonObjectService().findById(holonObjectId);
				HolonElementType holonElementType = getHolonElementTypeService().findById(holonElementTypeId);
				Integer hoCoObjIdOld=0;
				PowerLine powerLine = getPowerLineService().getPowerLineByHolonObject(holonObject);
				HolonObject oldHC = null;
				if(holonObject.getHolon() != null) {
					oldHC = findConnectedHolonCoordinatorByHolon(holonObject.getHolon(), powerLine);
				}
				if(oldHC!=null) {
					hoCoObjIdOld = holonObject.getId();
				}
				HolonElementState holonElementState = getHolonElementStateService().findById(holonElementStateId);
				HolonElement holonElement = new HolonElement(); // Creating HolonElement Element to store values
				holonElement.setCurrentCapacity(currentCapacity);
				holonElement.setHolonElementState(holonElementState);
				holonElement.setHolonElementType(holonElementType);
				holonElement.setHolonObject(holonObject);
				//Calling service method to save the Element in database and saving the auto-incremented ID in an integer
				Integer newHolonElementID = getHolonElementService().persist(holonElement);
				log.info("NewLy Generated Holon Element ID --> "+newHolonElementID);
				//Calling the response function and setting the content type of response.
				getResponse().setContentType("text/html");
				String dbResponse = "false";
				if(newHolonElementID > 0 ) {
					dbResponse = "true";
				}
				String response = dbResponse+"*"+holonObjectId+"*"+hoCoObjIdOld;
				if(holonElementType.getProducer()) {
					//On addition of a producer holon element, reset connection in Supplier table for consumer.
					ArrayList<Supplier> listConsumer = getSupplierService().getSupplierListForConsumer(holonObject);
					for(Supplier supplier : listConsumer) {
						if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
							supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
							getSupplierService().merge(supplier);
						}
					}
				}
				getResponse().getWriter().write(response);
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action createHolonElement()");
		}
	}

	/**
	 *  This method is used to edit a holon element and save in database
	 */
	public void editHolonElement(){

		try {
				Integer holonElementId = getRequest().getParameter("holonElementId")!=null?Integer.parseInt(getRequest().getParameter("holonElementId")):0;
				Integer holonElementTypeId = getRequest().getParameter("holonElementTypeId")!=null?Integer.parseInt(getRequest().getParameter("holonElementTypeId")):0;
				Integer holonElementStateId = getRequest().getParameter("holonElementStateId")!=null?Integer.parseInt(getRequest().getParameter("holonElementStateId")):0;
				Integer currentCapacity = getRequest().getParameter("currentCapacity")!=null?Integer.parseInt(getRequest().getParameter("currentCapacity")):0;
				HolonElementType holonElementType = getHolonElementTypeService().findById(holonElementTypeId);
				HolonElementState holonElementState = getHolonElementStateService().findById(holonElementStateId);
				HolonElement holonElement = getHolonElementService().findById(holonElementId);
				Integer holonObjectId = holonElement.getHolonObject().getId();
				PowerLine powerLine = getPowerLineService().getPowerLineByHolonObject(holonElement.getHolonObject());
				HolonObject hoc=  null;
				if(powerLine != null) {
					//Fetching connected holon coordinator of the same holon as holon object
					hoc=  findConnectedHolonCoordinatorByHolon(holonElement.getHolonObject().getHolon(), powerLine);
				}
				Integer hoCoObjIdOld=0;
				if(hoc!=null) {
					hoCoObjIdOld = getHolonObjectService().findById(holonObjectId).getId();//Fetching holon coordinator from database
				}
				holonElement.setCurrentCapacity(currentCapacity);
				holonElement.setHolonElementState(holonElementState);
				holonElement.setHolonElementType(holonElementType);
				HolonElement holonElement2 = getHolonElementService().merge(holonElement);
				String dbResponse = "false";
				if(holonElement2 != null) {
					dbResponse = "true";
				}
				String response = dbResponse+"*"+holonObjectId+"*"+hoCoObjIdOld;
				HolonObject holonObject = holonElement2.getHolonObject();
				if(holonElementType.getProducer()) {
					//On editing of a producer holon element, reset connection in Supplier table for consumer and producer.
					ArrayList<Supplier> listConsumer = getSupplierService().getSupplierListForConsumer(holonObject);
					for(Supplier supplier : listConsumer) {
						if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
							supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
							getSupplierService().merge(supplier);
						}
					}
					ArrayList<Supplier> listProducer = getSupplierService().getSupplierListForProducer(holonObject);
					for(Supplier supplier : listProducer) {
						if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
							supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
							getSupplierService().merge(supplier);
						}
					}
				}
	
				//Calling the response function and setting the content type of response.
				getResponse().setContentType("text/html");
				getResponse().getWriter().write(response);
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action editHolonElement()");
		}
	}

	/**
	 * This method is used to show all holon elements in database of a particular holon object
	 * @throws IOException
	 */
	public void showHolonElements() throws IOException {
		try {
				Integer holonObjectId = getRequest().getParameter("holonObjectId")!=null?Integer.parseInt(getRequest().getParameter("holonObjectId")):13;
				ArrayList<HolonElement> holonElementList = getHolonElementService().getHolonElements(getHolonObjectService().findById(holonObjectId));
				StringBuffer response = new StringBuffer();
				response = listHolonElementsAsPerUI(holonElementList, holonObjectId);//Function call to list holon elements as per UI
				getResponse().setContentType("text/html");
				getResponse().getWriter().write(response.toString());
		} catch (Exception e) {
			getResponse().getWriter().write("Error occurred in getting Holon Elements. Please check application Logs for more information.");
			log.info("Error occurred in getting Holon Elements::"+e.getMessage()+" Please check application Logs for more information.");
		}
	}
	
	/**
	 * This method lists all holon elements as per UI
	 * @param holonElementsList raw list of holon elements
	 * @param holonObjectId The holon object ID of the holon elements
	 * @return Response containing modified list of holon elements to be used by client side functions
	 */
	private StringBuffer listHolonElementsAsPerUI(ArrayList<HolonElement> holonElementsList, int holonObjectId) {
		StringBuffer listElements = new StringBuffer();
		if(holonElementsList != null && holonElementsList.size() > 0) {
			for(HolonElement holonElement : holonElementsList) {
				listElements.append("<tr>");
				listElements.append("<td>"+holonElement.getHolonElementType().getName()+"</td>");
				String producer=holonElement.getHolonElementType().getProducer()==true?"Yes":"No";
				listElements.append("<td>"+producer+"</td>");
				listElements.append("<td>"+holonElement.getHolonElementType().getMaxCapacity()+"</td>");
				listElements.append("<td>"+holonElement.getHolonElementType().getMinCapacity()+"</td>");
				listElements.append("<td>"+holonElement.getHolonElementState().getName()+"</td>");
				listElements.append("<td>"+holonElement.getCurrentCapacity()+"</td>");
				listElements.append("<td title='Consumption Graph'><i class=\"fa fa-line-chart\" onclick=\"showConsumptionGraph("+holonElement.getId()+","+holonObjectId+","+holonElement.getCurrentCapacity()+")\"></i></td>");
				listElements.append("<td title='Delete Element'><i class=\"fa fa-remove\" onclick=\"deleteHolonElement("+holonElement.getId()+","+holonObjectId+")\"></i></td>");
				listElements.append("<td title='Edit Element'><i class=\"fa fa-edit\" onclick=\"editHolonElement("+holonElement.getId()+","+holonElement.getHolonElementType().getId()+","+holonElement.getHolonElementState().getId()+","+holonElement.getCurrentCapacity()+","+holonObjectId+")\"></i></td>");
				listElements.append("</tr>");
			}
		} else {
			listElements.append("<tr><td colspan=\"10\" class=\"noData\">No Elements found! Please click on Add Holon Element to add some.</td> ");
		}
		return listElements;
	}

	/**
	 * This action deletes an existing object of holonElement from database.
	 */
	public void deleteHolonElement(){
		Integer holonElementId = getRequest().getParameter("holonElementId")!=null?Integer.parseInt(getRequest().getParameter("holonElementId")):0;
		HolonElement holonElement = getHolonElementService().findById(holonElementId);
		Integer holonElementTypeId = holonElement.getHolonElementType().getId();
		Integer holonObjectId = holonElement.getHolonObject().getId();
		// Since we are deleting this HE, we can't take the reference of HO from this. We need to take it from database.
		HolonObject holonObject = getHolonObjectService().findById(holonObjectId);
		PowerLine powerLine = getPowerLineService().getPowerLineByHolonObject(holonObject);
		HolonObject hoc = findConnectedHolonCoordinatorByHolon(holonObject.getHolon(), powerLine);
		Integer hoCoObjIdOld=0;
		if(hoc!=null) {
			hoCoObjIdOld = hoc.getId();
		}
		//Deleting holonElement object
		boolean deleteStatus = getHolonElementService().delete(holonElement);
		HolonElementType holonElementType = getHolonElementTypeService().findById(holonElementTypeId);
		if(holonElementType.getProducer()) {
			//On deletion of a producer holon element, reset connection in Supplier table for consumer and producer.
			ArrayList<Supplier> listConsumer = getSupplierService().getSupplierListForConsumer(holonObject);
			for(Supplier supplier : listConsumer) {
				if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
					supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
					getSupplierService().merge(supplier);
				}
			}
			ArrayList<Supplier> listProducer = getSupplierService().getSupplierListForProducer(holonObject);
			for(Supplier supplier : listProducer) {
				if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
					supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
					getSupplierService().merge(supplier);
				}
			}
		}
		//Calling the response function and setting the content type of response.
		getResponse().setContentType("text/html");
		try {
			String response = deleteStatus+"*"+holonObjectId+"*"+hoCoObjIdOld;
			getResponse().getWriter().write(response);
		} catch (Exception e) {
			log.debug("Exception "+e.getMessage()+" occurred in action deleteHolonElement()");
		}
	}

}
