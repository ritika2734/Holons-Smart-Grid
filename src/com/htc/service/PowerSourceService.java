package com.htc.service;

import java.util.ArrayList;

import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerSource;

/**
 * This interface contains all functions related to power source
 */
public interface PowerSourceService {

	/**
	 * This function is used to save power source object in database
	 * @param transientInstance the power source object
	 * @return newly created power source ID
	 */
	public Integer persist(PowerSource transientInstance);
	
	/**
	 * This function is used to update power source object in database
	 * @param detachedInstance the power source object
	 * @return the updated power source object
	 */
	public PowerSource merge(PowerSource detachedInstance);
	
	/**
	 * This function is used to find power source object in database using power source ID
	 * @param powerSourceId the power source ID
	 * @return the power source object
	 */
	public PowerSource findById(int powerSourceId);
	
	/**
	 * This function is used to delete power source object from database
	 * @param persistentInstance the power source object
	 * @return the delete status
	 */
	public boolean delete(PowerSource persistentInstance);
	
	/**
	 * This function is used to get all power source objects from database
	 * @return list of power source objects
	 */
	public ArrayList<PowerSource> getAllPowerSource();
	
	/**
	 * This function is used to find all power source objects of a particular holon using the holon coordinator object(1st parameter)
	 * @param holonCoordinator the holon coordinator
	 * @return list of power source objects
	 */
	public ArrayList<PowerSource> findByHolonCoordinator(HolonObject holonCoordinator);
	
	/**
	 * This function is used to delete all power source objects from database
	 * @return the delete status
	 */
	public int deleteAllPowerSources();

}
