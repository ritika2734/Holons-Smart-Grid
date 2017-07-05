package com.htc.service;

import java.util.ArrayList;

import com.htc.hibernate.pojo.HolonObjectType;

/**
 * This interface contains all functions related to holon object type
 */
public interface HolonObjectTypeService {

	/**
	 * This function is used to save holon object type in database
	 * @param transientInstance the holon object type instance
	 * @return newly created holon object type ID
	 */
	public Integer persist(HolonObjectType transientInstance);
	
	/**
	 * This function is used to update holon object type instance in database
	 * @param detachedInstance the holon object type
	 * @return the updated holon object type instance
	 */
	public HolonObjectType merge(HolonObjectType detachedInstance);
	
	/**
	 * This function is used to find holon object type in database using the id passed in the parameter
	 * @param holonObjectTypeId the holon object type id
	 * @return the holon object type instance
	 */
	public HolonObjectType findById(int holonObjectTypeId);
	
	/**
	 * This function is used to delete holon object type instance from database
	 * @param persistentInstance the holon object type instance
	 * @return the delete status
	 */
	public boolean delete(HolonObjectType persistentInstance);
	
	/**
	 * This function is used to get all holon object type instances from the database
	 * @return array list of all holon object type instances
	 */
	public ArrayList<HolonObjectType> getAllHolonObjectType();

}
