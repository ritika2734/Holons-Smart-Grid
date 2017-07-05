package com.htc.service;

import java.util.ArrayList;

import com.htc.hibernate.pojo.HolonElementState;

/**
 * This interface contains all functions related to holon element state
 */
public interface HolonElementStateService {

	/**
	 * This function is used to save holon element state object in database
	 * @param transientInstance the holon element state instance
	 * @return newly created holon element state ID
	 */
	public Integer persist(HolonElementState transientInstance);
	
	/**
	 * This function is used to update holon element state object in database 
	 * @param detachedInstance the holon element state object
	 * @return the updated holon element state object
	 */
	public HolonElementState merge(HolonElementState detachedInstance);

	/**
	 * This function is used to find holon element state object from database using holon element state ID
	 * @param holonElementStateId the holon element state ID
	 * @return the holon element state instance
	 */
	public HolonElementState findById(int holonElementStateId);
	
	/**
	 * This function is used to delete holon element state object from database
	 * @param persistentInstance the holon element state object
	 * @return the delete status
	 */
	public boolean delete(HolonElementState persistentInstance);
	
	/**
	 * This function is used to get all holon element state objects from database
	 * @return array list of holon element state objects
	 */
	public ArrayList<HolonElementState> getAllHolonElementState();

}
