package com.htc.service;

import java.util.ArrayList;

import com.htc.hibernate.pojo.Disaster;

/**
 * This interface contains all functions related to disaster
 */
public interface DisasterService {
	
	/**
	 * This function is used to save the disaster instance in database 
	 * @param transientInstancethe disaster instance
	 * @return newly created disaster instance ID
	 */
	public Integer persist(Disaster transientInstance);

	/**
	 * This function is used to update an existing disaster instance in database
	 * @param detachedInstance the disaster instance
	 * @return the updated disaster instance
	 */
	public Disaster merge(Disaster detachedInstance);
	
	/**
	 * This function is used to find the disaster instance in database using disaster ID
	 * @param disasterId the disaster ID
	 * @return the disaster instance
	 */
	public Disaster findById(int disasterId);
	
	/**
	 * This function is used to delete a disaster instance from database
	 * @param persistentInstance the disaster instance
	 * @return the delete status
	 */
	public boolean delete(Disaster persistentInstance);
	
	/**
	 * This function is used to get all disaster instances from database
	 * @return array list of disaster instances
	 */
	public ArrayList<Disaster> getAllDisasterCircles();
	
	/**
	 * This function is used to delete all disaster instances from database
	 * @return the delete status
	 */
	public int deleteAllDisasters();
	
}
