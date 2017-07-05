package com.htc.service;

import java.util.ArrayList;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSwitch;

/**
 * This interface contains all functions related to power switch
 */
public interface PowerSwitchService {

	/**
	 * This function is used to save power switch object in database
	 * @param transientInstance the power switch object
	 * @return newly created power switch ID
	 */
	public Integer persist(PowerSwitch transientInstance);
	
	/**
	 * This function is used to update an existing power switch object in database
	 * @param detachedInstance the power switch object
	 * @return updated power switch object
	 */
	public PowerSwitch merge(PowerSwitch detachedInstance);
	
	/**
	 * This function is used to find power switch object in database using power switch ID 
	 * @param powerSwitchId the power switch ID
	 * @return the power switch object
	 */
	public PowerSwitch findById(int powerSwitchId);
	
	/**
	 * This function is used to delete power switch object from database
	 * @param persistentInstance the power switch object
	 * @return the delete status
	 */
	public boolean delete(PowerSwitch persistentInstance);
	
	/**
	 * This function is used to get list of all power switch objects from database
	 * @return list of power switch objects
	 */
	public ArrayList<PowerSwitch> getAllPowerSwitch();
	
	/**
	 * This function checks whether a power switch exists between two power lines or not, if yes, it return the objects of that power switch
	 * @param powerLineA the power line object A
	 * @param powerLineB the power line object B
	 * @return the power switch object
	 */
	public PowerSwitch checkSwitchStatusBetweenPowerLines(PowerLine powerLineA, PowerLine powerLineB);
	
	/**
	 * This function deletes all power switch objects from database
	 * @return the delete status
	 */
	public int deleteAllPowerSwitches();
	
}
