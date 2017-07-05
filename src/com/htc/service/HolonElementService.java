package com.htc.service;

import java.util.ArrayList;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonObject;

/**
 * This interface contains all functions related to holon elements
 */
public interface HolonElementService {

	/**
	 * This function is used to save holon element in database
	 * @param transientInstance the holon element
	 * @return newly created holon element ID
	 */
	public Integer persist(HolonElement transientInstance);
	
	/**
	 * This function is used to update holon element in database
	 * @param detachedInstance the holon element
	 * @return the updated holon element
	 */
	public HolonElement merge(HolonElement detachedInstance);

	/**
	 * This method is used to find holon element object from database using holon element ID  
	 * @param holonElementId the holon element ID
	 * @return the holon element instance
	 */
	public HolonElement findById(int holonElementId);
	
	/**
	 * This function is used to delete holon element instance from database
	 * @param persistentInstance the holon element instance
	 * @return the delete status
	 */
	public boolean delete(HolonElement persistentInstance);
	
	/**
	 * This function is used to get all holon elements from database
	 * @return array list of holon elements
	 */
	public ArrayList<HolonElement> getAllHolonElement();
	
	/**
	 * This function is used to get all holon elements of a particular holon object
	 * @param holonObject the holon object
	 * @return array list of holon elements
	 */
	public ArrayList<HolonElement> getHolonElements(HolonObject holonObject);
	
	/**
	 * This function is used to delete all holon elements from database
	 * @return the delete status
	 */
	public int deleteAllHolonElements();

}
