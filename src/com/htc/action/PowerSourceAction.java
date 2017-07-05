package com.htc.action;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.pojo.Supplier;
import com.htc.utilities.CommonUtilities;
import com.htc.utilities.ConstantValues;

/**
 * This class contains all methods related to Power Source
 *
 */
public class PowerSourceAction  extends CommonUtilities{

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(PowerSourceAction.class);
	
	/**
	 * This method creates a new power source object in database 
	 */
	public void createPowerSourceObject(){
		try {
				//Fetching request parameters sent by client side functions
				Integer psMaxProdCap = getRequest().getParameter("psMaxProdCap")!=null?Integer.parseInt(getRequest().getParameter("psMaxProdCap")):0;
				Integer psStatus = getRequest().getParameter("psStatus")!=null?Integer.parseInt(getRequest().getParameter("psStatus")):0;
				Double radius = getRequest().getParameter("radius")!=null?Double.parseDouble(getRequest().getParameter("radius")):0D;
				Double latCenter = getRequest().getParameter("latCenter")!=null?Double.parseDouble(getRequest().getParameter("latCenter")):0D;
				Double lngCenter = getRequest().getParameter("lngCenter")!=null?Double.parseDouble(getRequest().getParameter("lngCenter")):0D;
				LatLng centerLatLng = new LatLng(latCenter, lngCenter);
				Integer centerLatLngId = saveLocation(centerLatLng);
				PowerSource pwSrc = new PowerSource(); // Creating HolonObject object to store values
				pwSrc.setCenter(getLatLngService().findById(centerLatLngId));
				pwSrc.setCurrentProduction(0);
				pwSrc.setRadius(radius);
				pwSrc.setMaxProduction(psMaxProdCap);
				pwSrc.setMinProduction(0);
				pwSrc.setStatus((psStatus==0?false:true));
				pwSrc.setFlexibility(0);
				Integer newPsId = getPowerSourceService().persist(pwSrc);//Saving power source object in database
				log.info("NewLy Generated Power Source ID --> "+newPsId);
				PowerSource pwSrc2 = getPowerSourceService().findById(newPsId);
				//Calling the response function and setting the content type of response.
				StringBuffer psResponse = new StringBuffer();
				psResponse.append(pwSrc2.getId()+"!");
				psResponse.append(psStatus);
				getResponse().setContentType("text/html");
				getResponse().getWriter().write(psResponse.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action createPowerSourceObject()");
		}
	}
	
	
	/**
	 * This method is used to edit an already existing power source object
	 */
	public void editPowerSourceObject() {
		try {
				//Fetching request parameters from client side.
				Integer psMaxProdCap = getRequest().getParameter("psMaxProdCap")!=null?Integer.parseInt(getRequest().getParameter("psMaxProdCap")):0;
				Integer psStatus = getRequest().getParameter("psStatus")!=null?Integer.parseInt(getRequest().getParameter("psStatus")):0;
				Integer pSourceId = getRequest().getParameter("hiddenPowerObjectId")!=null?Integer.parseInt(getRequest().getParameter("hiddenPowerObjectId")):0;		
				PowerSource pwSrcOld = getPowerSourceService().findById(pSourceId);
				pwSrcOld.setMaxProduction(psMaxProdCap);
				pwSrcOld.setStatus((psStatus==1?true:false));
				pwSrcOld.setCurrentProduction((psStatus==1?psMaxProdCap:0));
				pwSrcOld.setFlexibility(psMaxProdCap);
				getPowerSourceService().merge(pwSrcOld);//Updating power source object in database
				PowerSource pwSrc2 = getPowerSourceService().findById(pSourceId);
				PowerLine powerLine = getPowerLineService().getPowerLineByPowerSource(pwSrc2);
				//Update all holon objects
				if(powerLine != null) {
					updateHolonObjectsAndPowerSources(powerLine.getId());
				}
				//Calling the response function and setting the content type of response.
				getResponse().setContentType("text/html");
				StringBuffer psResponse = new StringBuffer();
				psResponse.append(pwSrc2.getId()+"!");
				psResponse.append(psStatus);
				log.info(psResponse.toString());
				getResponse().getWriter().write(psResponse.toString());
			} catch (Exception e) {
				log.info("Exception "+e.getMessage()+" occurred in action createPowerSourceObject()");
			}
	}
	
	
	/**
	 * This method prepares the response to be displayed in the info window of the power source on map 
	 */
	public void getPsObjectInfoWindow() {
		try {
			Integer powerSourceId = getRequest().getParameter("psId")!=null?Integer.parseInt(getRequest().getParameter("psId")):0;
			PowerSource powerSource = getPowerSourceService().findById(powerSourceId);//Fetching power source object from database
			//Function call to get list of all power sources which are supplying energy to some holon objects
			ArrayList<Supplier> listOfProducerPowerSource = getSupplierService().getSupplierListForProducerPowerSource(powerSource);
			for(Supplier supplier : listOfProducerPowerSource) {
				//Checking connectivity between holon object and power source. If they are not connected, then set message status to CONNECTION_RESET
				if(!checkConnectivityBetweenHolonObjectAndPowerSource(supplier.getHolonObjectConsumer(), powerSource)
						&& supplier.getMessageStatus().equals(ConstantValues.ACCEPTED)) {
					supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
					getSupplierService().merge(supplier);
					powerSource.setFlexibility(powerSource.getFlexibility() + supplier.getPowerGranted());
					getPowerSourceService().merge(powerSource);
				}
			}
			PowerSource pwSrc = getPowerSourceService().findById(powerSourceId); 
			Integer MaxProdCap=pwSrc.getMaxProduction();
			Integer currProd=pwSrc.getCurrentProduction();
			Integer minProd=pwSrc.getMinProduction();
			Boolean status = pwSrc.getStatus();
			Integer flexibility = pwSrc.getFlexibility();
			double latCenter=pwSrc.getCenter().getLatitude();
			double lngCenter=pwSrc.getCenter().getLongitude();
			double pwSrcRad=pwSrc.getRadius();
			HolonObject hc= null;
			Holon holon = null;
			PowerLine powerLine = getPowerLineService().getPowerLineByPowerSource(pwSrc);
			if(pwSrc.getHolonCoordinator() != null && pwSrc.getHolonCoordinator().getHolon() != null) {
				holon = pwSrc.getHolonCoordinator().getHolon();
			}
			if(powerLine != null && holon != null) {
				hc = findConnectedHolonCoordinatorByHolon(holon, powerLine);
			}
			pwSrc.setHolonCoordinator(hc);
			getPowerSourceService().merge(pwSrc);
			Integer CoHolonId=0;
			String coHoLoc="";
			String CoHolonName="";
			if(hc!=null) {
				CoHolonId=hc.getId();
				CoHolonName= hc.getHolon().getName();
				coHoLoc=hc.getLatLngByNeLocation().getLatitude()+"~"+hc.getLatLngByNeLocation().getLongitude();
			}
			//Calling the response function and setting the content type of response.
			getResponse().setContentType("text/html");
			StringBuffer psResponse = new StringBuffer();
			//Preparing response to be sent to the client side
			psResponse.append(powerSourceId+"!");
			psResponse.append(MaxProdCap+"!");
			psResponse.append(currProd+"!");
			psResponse.append(CoHolonId+"!");
			psResponse.append(coHoLoc+"!");
			psResponse.append(status+"!");
			psResponse.append(minProd+"!");
			psResponse.append(latCenter+"!");
			psResponse.append(lngCenter+"!");
			psResponse.append(pwSrcRad+"!");
			psResponse.append(CoHolonName+"!");
			psResponse.append(flexibility);
			getResponse().getWriter().write(psResponse.toString());
			} catch (Exception e) {
				log.info("Exception "+e.getMessage()+" occurred in action getPsObjectInfoWindow()");
			}
		}
	
	/**
	 * This method is used to fetch details of all power sources in database and prepare a suitable response to be sent to the client side
	 */
	public void showPowerSources(){
		try {			
			ArrayList<String> psListArray = new ArrayList<String>();
			PowerSource pwSrc = null;
			ArrayList<PowerSource> psObjectList = getPowerSourceService().getAllPowerSource();//Fetching all power sources from database
			if(psObjectList != null) {
				for(int i=0; i<psObjectList.size();i++) {
					boolean status=false;
					String center;
					double radius;
					Integer psObjectId;
					pwSrc = psObjectList.get(i);
					center = pwSrc.getCenter().getLatitude()+"~"+pwSrc.getCenter().getLongitude();
					radius = pwSrc.getRadius();
					status=pwSrc.getStatus();
					psObjectId=pwSrc.getId();
					psListArray.add(psObjectId+"#"+radius+"#"+center+"#"+status+"*");
				}
			}
			//Calling the response function and setting the content type of response.
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(psListArray.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action showPowerSources()");
		}
	}
	
	/**
	 * This method is used to toggle the power source object between on and off
	 */
	public void powerSourceOnOff() {
		try {			
			Integer powerSrcId = getRequest().getParameter("powerSrcId")!=null?Integer.parseInt(getRequest().getParameter("powerSrcId")):0;
			PowerSource powerSource = getPowerSourceService().findById(powerSrcId); 
			boolean pwOldStatus=powerSource.getStatus();
			int maxProd = powerSource.getMaxProduction();
			int minProd = powerSource.getMinProduction();
			int currentProd = powerSource.getCurrentProduction();
			Integer flexibility = powerSource.getFlexibility();
			boolean pwNewStatus=true;
			Integer pwNewIntStatus=1;
			if(pwOldStatus) {
				pwNewStatus=false;
				pwNewIntStatus=0;
				powerSource.setCurrentProduction(0);
				powerSource.setFlexibility(0);
			} else {
				powerSource.setCurrentProduction(powerSource.getMaxProduction());
				powerSource.setFlexibility(powerSource.getMaxProduction());
			}
			powerSource.setStatus(pwNewStatus);
			HolonObject hc= null;
			Holon holon = null;
			PowerLine powerLine = getPowerLineService().getPowerLineByPowerSource(powerSource);
			if(powerSource.getHolonCoordinator() != null && powerSource.getHolonCoordinator().getHolon() != null) {
				holon = powerSource.getHolonCoordinator().getHolon();
			}
			if(powerLine != null && holon != null) {
				hc = findConnectedHolonCoordinatorByHolon(holon, powerLine);
			}
			powerSource.setHolonCoordinator(hc);
			getPowerSourceService().merge(powerSource);//Saving updated power source object in database
			Integer CoHolonId=0;
			String coHoLoc="";
			if(hc!=null) {
				CoHolonId=hc.getId();
				coHoLoc=hc.getLatLngByNeLocation().getLatitude()+"~"+hc.getLatLngByNeLocation().getLongitude();
			}
			//Preparing response to be used by client side functions
			StringBuffer respStr=new StringBuffer();
			respStr.append(CoHolonId+"!");
			respStr.append(coHoLoc+"!");
			respStr.append(pwNewIntStatus+"!");
			respStr.append(maxProd+"!");
			respStr.append(minProd+"!");
			respStr.append(currentProd+"!");
			respStr.append(flexibility);
			if(!powerSource.getStatus()) {
				//Setting message status to CONNECTION_RESET if power source is turned off
				ArrayList<Supplier> supplierPowerSource = getSupplierService().getSupplierListForProducerPowerSource(powerSource);
				for(Supplier supplier : supplierPowerSource) {
					if(supplier.getMessageStatus().equalsIgnoreCase(ConstantValues.ACCEPTED)) {
						supplier.setMessageStatus(ConstantValues.CONNECTION_RESET);
						getSupplierService().merge(supplier);
					}
				}
			}
			//Calling the response function and setting the content type of response.
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(respStr.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action powerSourceOnOff()");
		}
	}

}
