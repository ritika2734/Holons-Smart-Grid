package com.htc.dao;

import java.util.ArrayList;
import com.htc.hibernate.pojo.Holon;

/**
 * This interface contains all functions related to holon
 */
public interface HolonDao {

	/**
	 * This function is used to save holon in database
	 * @param transientInstance the holon
	 * @return newly created holon instance
	 */
	public Integer persist(Holon transientInstance);

	/**
	 * This function is used to update holon instance in database
	 * @param detachedInstance the holon instance
	 * @return the updated holon instance
	 */
	public Holon merge(Holon detachedInstance);

	/**
	 * This function is used to find a holon instance in database using holon ID
	 * @param holonId the holon ID
	 * @return the holon instance
	 */
	public Holon findById(int holonId);
	
	/**
	 * This function is used to delete holon instance from database
	 * @param persistentInstance the holon instance
	 * @return the delete status
	 */
	public boolean delete(Holon persistentInstance);
	
	/**
	 * This function is used to get all holon instances from database
	 * @return array list of holon instance
	 */
	public ArrayList<Holon> getAllHolon();

}
