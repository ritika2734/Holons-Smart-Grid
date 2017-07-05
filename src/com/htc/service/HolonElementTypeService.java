package com.htc.service;

import java.util.ArrayList;
import com.htc.hibernate.pojo.HolonElementType;

/**
 * This interface contains all functions related to holon element type
 */
public interface HolonElementTypeService {
	
	/**
	 * This function is used to save holon element type in database
	 * @param transientInstance the holon element type
	 * @return newly created holon element type ID
	 */
	public Integer persist(HolonElementType transientInstance);
	
	/**
	 * This function is used to update holon element type instance in database
	 * @param detachedInstance the holon element type
	 * @return the updated holon element type
	 */
	public HolonElementType merge(HolonElementType detachedInstance);
	
	/**
	 * This function is used to find holon element type from database using holon element type ID
	 * @param holonElementTypeId the holon element type ID
	 * @return the holon element type instance
	 */
	public HolonElementType findById(int holonElementTypeId);
	
	/**
	 * This function is used to delete holon element type instance form database
	 * @param persistentInstance the holon element type instance
	 * @return the delete status
	 */
	public boolean delete(HolonElementType persistentInstance);
	
	/**
	 * This function is used to get all holon element type from database
	 * @return array list of holon element type instances
	 */
	public ArrayList<HolonElementType> getAllHolonElementType();

}
