package com.htc.service;

import java.util.ArrayList;
import com.htc.hibernate.pojo.Disaster;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;

/**
 * This interface contains all functions related to power line
 */
public interface PowerLineService {

	/**
	 * This function is used to save power line object in database
	 * @param transientInstance the power line object
	 * @return newly created power line ID
	 */
	public Integer persist(PowerLine transientInstance);
	
	/**
	 * This function is used to update an existing power line object in database
	 * @param detachedInstance the power line object
	 * @return the updated power line object
	 */
	public PowerLine merge(PowerLine detachedInstance);
	
	/**
	 * This function is used to find power line object from database using power line id
	 * @param powerLineId the power line ID
	 * @return the power line object
	 */
	public PowerLine findById(int powerLineId);
	
	/**
	 * This function is used to delete power line object from database
	 * @param persistentInstance the power line object
	 * @return the delete status
	 */
	public boolean delete(PowerLine persistentInstance);

	/**
	 * This function is used to get all power line objects from database
	 * @return list of power line objects
	 */
	public ArrayList<PowerLine> getAllPowerLine();
	
	/**
	 * This function is used to get power line objects which are connected to the power line passed in the parameter
	 * @param powerLine the power line object
	 * @return list of power line objects 
	 */
	public ArrayList<PowerLine> getConnectedPowerLines(PowerLine powerLine);
	
	/**
	 * This function is used to get the power line object which is connected directly to the holon object passed in the parameter
	 * @param holonObject the holon object
	 * @return the power line object
	 */
	public PowerLine getPowerLineByHolonObject(HolonObject holonObject);
	
	/**
	 * This function is used to get the power line object which is connected directly to the power source object passed in the parameter 
	 * @param powerSource the power source object
	 * @return the power line object
	 */
	public PowerLine getPowerLineByPowerSource(PowerSource powerSource);
	
	/**
	 * This function is used to get the power line object using the location passed in the parameter
	 * @param latLng the location object
	 * @return list of power lines
	 */
	public ArrayList<PowerLine> getPowerLineFromLatLng(LatLng latLng);
	
	/**
	 * This function is used to get all power line IDs which are part of any disaster
	 * @return array list of power lines
	 */
	public ArrayList<PowerLine> getAllPowerLineIdsHavingDisaster();
	
	/**
	 * This function gets list of all power line objects which are part of a particular disaster object passed in the parameter
	 * @param disaster the disaster object
	 * @return array list of power lines
	 */
	public ArrayList<PowerLine> getAllPowerLinesWithDisasterId(Disaster disaster);

	/**
	 * This function is used to delete all power line objects from database
	 * @return the delete status
	 */
	public int deleteAllPowerLines();
	
}
