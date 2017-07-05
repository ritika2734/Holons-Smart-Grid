package com.htc.action;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import com.htc.hibernate.pojo.Disaster;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.utilities.CommonUtilities;

/**
 * This class contains all action methods related to the disaster module.
 */
public class DisasterAction extends CommonUtilities {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DisasterAction.class);
	
	/**
	 * This method is used to create disaster object in database
	 */
	public void createDisasterCircle() {
		try {
			Disaster disaster= new Disaster();
			Double latitudeOfDisasterCircle = getRequest().getParameter("latitude")!=null?Double.parseDouble(getRequest().getParameter("latitude")):0D;
			Double longitudeOfDisasterCircle = getRequest().getParameter("longitude")!=null?Double.parseDouble(getRequest().getParameter("longitude")):0D;
			Double radiusOfDisasterCircle = getRequest().getParameter("radius")!=null?Double.parseDouble(getRequest().getParameter("radius")):0D;
			LatLng disasterCircleLatLng= new LatLng(latitudeOfDisasterCircle,longitudeOfDisasterCircle);
			Integer disasterCircleLocationId = saveLocation(disasterCircleLatLng);//Save the Location in the Lat Lng table
			LatLng disasterCircleLocationId2 = getLatLngService().findById(disasterCircleLocationId);
			disaster.setCenter(disasterCircleLocationId2);
			disaster.setRadius(radiusOfDisasterCircle);
			Integer disasterId= getDisasterService().persist(disaster);//Saving disaster object in database
			Disaster disaster2= getDisasterService().findById(disasterId);
			Map<Integer, PowerLine> mapOfAllPowerLinesInsideCircle= getListOfAllPowerLineIdsInsideCircle(latitudeOfDisasterCircle,longitudeOfDisasterCircle,radiusOfDisasterCircle);
			for(Integer powerLineId : mapOfAllPowerLinesInsideCircle.keySet()) {
				PowerLine powerLine = getPowerLineService().findById(powerLineId);
				powerLine.setDisaster(disaster2);
				getPowerLineService().merge(powerLine);
			}
			StringBuffer createDisasterResponse = new StringBuffer();
			//Creating response with disaster center location to be used by functions at client side to show disaster circle.
			createDisasterResponse.append(disaster2.getId()+"*"+disaster2.getCenter().getLatitude()+"*"+disaster2.getCenter().getLongitude());
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(createDisasterResponse.toString());
		} catch(Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in getAllPointsInsideCircle()");
		}
	}
	
	/**
	 * This method is used to fetch all disaster objects from database.
	 */
	public void getAllSavedDisasters(){
		try{
			ArrayList<Disaster> disasterList = getDisasterService().getAllDisasterCircles(); //Fetching disaster list from database
			StringBuffer disasterListBuffer = new StringBuffer();
			if(disasterList != null) {
				for(Disaster disaster1 : disasterList){
					Integer disasterId=disaster1.getId();
					Double radius=disaster1.getRadius();
					Double latitude= disaster1.getCenter().getLatitude();
					Double longitude= disaster1.getCenter().getLongitude();
					disasterListBuffer.append(disasterId+"^"+radius+"^"+latitude+"^"+longitude+"*");
				}
				if(disasterListBuffer.length() > 0) {
					disasterListBuffer = disasterListBuffer.deleteCharAt(disasterListBuffer.lastIndexOf("*"));
				}
			}
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(disasterListBuffer.toString());
		} catch (Exception e) {
			log.info("Exception "+e.getMessage()+" occurred in action getAllSavedDisasters()");
		}
	}
	
	/**
	 * This method is used to delete all disaster objects from database. 
	 */
	public void deleteAllDisasterCircleFromDatabase(){
		try{
			ArrayList<PowerLine> listDisasterPowerLine= getPowerLineService().getAllPowerLineIdsHavingDisaster();
			Map<Integer, Disaster> disasterIdMap = new TreeMap<Integer, Disaster>();
			ArrayList<Disaster> allDisasterList = getDisasterService().getAllDisasterCircles();
			for(Disaster disaster : allDisasterList) {
				disasterIdMap.put(disaster.getId(), disaster);
			}
			String disasterResponse = deleteDisasterMode(listDisasterPowerLine, disasterIdMap);
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(disasterResponse);
		}catch(Exception e){
			log.info("Exception in deleteAllDisasterCircleFromDatabase()");
		}
	}
	
	/**
	 * This method is used to delete a particular disaster circle from database.
	 */
	public void deleteDisasterCircleFromDatabase() {
		Integer disasterId = getRequest().getParameter("disasterId")!=null && getRequest().getParameter("disasterId") != ""?Integer.parseInt(getRequest().getParameter("disasterId")):0;
		String disasterResponse;
		Disaster disaster = getDisasterService().findById(disasterId); //Fetching the disaster object to remove
		Map<Integer, Disaster> disasterIdMap = new TreeMap<Integer, Disaster>();
		disasterIdMap.put(disasterId, disaster);
		try {
			ArrayList<PowerLine> disasterPowerLines = disaster!=null?getPowerLineService().getAllPowerLinesWithDisasterId(disaster):null;
			disasterResponse= disasterPowerLines!=null?deleteDisasterMode(disasterPowerLines, disasterIdMap):"failure";
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(disasterResponse);
		} catch(Exception e) {
			log.info("Exception in deleteDisasterCircleFromDatabase");
		}
	}
	
	/**
	 * This method is used by deleteAllDisasterCircleFromDatabase() and deleteDisasterCircleFromDatabase() methods to delete disaster objects from database.
	 * It first sets all disaster references in power line objects to null before deleting the disaster object. 
	 * @param disasterPowerLines Array list of power lines which are a part of disaster.
	 * @param disasterIdMap A map containing disaster IDs and disaster objects.
	 * @return  a list of disaster IDs that have been removed from database so that they can also be removed from map at client side.
	 */
	public String deleteDisasterMode(ArrayList<PowerLine> disasterPowerLines, Map<Integer, Disaster> disasterIdMap){
		StringBuffer listofDisasterIdsAsResponse= new StringBuffer();
		try{
			//Setting all power lines' disaster reference to null before deleting all disaster objects
			for(PowerLine powerLine: disasterPowerLines){
				powerLine.setDisaster(null);
				getPowerLineService().merge(powerLine);
			}
			//Removing all disaster objects from database
			for(Integer disasterId : disasterIdMap.keySet()) {
				getDisasterService().delete(disasterIdMap.get(disasterId));
				listofDisasterIdsAsResponse.append(disasterId+"*");
			}
			if(listofDisasterIdsAsResponse.length() > 0) {
				listofDisasterIdsAsResponse = listofDisasterIdsAsResponse.deleteCharAt(listofDisasterIdsAsResponse.lastIndexOf("*"));
			}
		} catch(Exception e){
			log.info("Error in delete Disaster Mode");
		}
		return listofDisasterIdsAsResponse.toString();
	}
	
}
