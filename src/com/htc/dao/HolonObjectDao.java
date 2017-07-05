package com.htc.dao;

import java.util.ArrayList;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;

/**
 * This interface contains all functions related to holon object
 */
public interface HolonObjectDao {

	/**
	 * This function is used to save holon object in database
	 * @param transientInstance the holon object
	 * @return newly created holon object ID
	 */
	public Integer persist(HolonObject transientInstance);
	
	/**
	 * This function is used to update holon object in database
	 * @param detachedInstance the holon object
	 * @return the updated holon object
	 */
	public HolonObject merge(HolonObject detachedInstance);
	
	/**
	 * This function is used to find the holon object in database using the ID provided in the parameter
	 * @param holonObjectId the holon object ID
	 * @return the holon object
	 */
	public HolonObject findById(int holonObjectId);
	
	/**
	 * This function is used to delete the holon object from database
	 * @param persistentInstance the holon object
	 * @return the delete status
	 */
	public boolean delete(HolonObject persistentInstance);
	
	/**
	 * This function is used to get all holon objects from database
	 * @return array list of holon objects
	 */
	public ArrayList<HolonObject> getAllHolonObject();
	
	/**
	 * This function is used to find all holon objects of a particular holon (passed in the parameter)
	 * @param holon the holon
	 * @return array list of holon objects
	 */
	public ArrayList<HolonObject> findByHolon(Holon holon);
	
	/**
	 * This function is used to find all holon objects from the database which are also holon coordinators
	 * @return list of all holon coordinators
	 */
	public ArrayList<HolonObject> findAllHolonCoordinators();
	
	/**
	 * This function is used to delete all holon objects from database
	 * @return the delete status
	 */
	public int deleteAllHolonObjects();
	
}
