package com.htc.service;

import java.util.ArrayList;

import com.htc.hibernate.pojo.LatLng;

/**
 * This interface contains all functions related to location(LatLng)
 */
public interface LatLngService {

	/**
	 * This function is used to save LatLng object in database
	 * @param transientInstance the LatLng object
	 * @return newly created LatLng ID
	 */
	public Integer persist(LatLng transientInstance);
	
	/**
	 * This function is used to update LatLng object in database
	 * @param detachedInstance the LatLng object
	 * @return the updated LatLng object
	 */
	public LatLng merge(LatLng detachedInstance);
	
	/**
	 * This function is used to find LatLng object in database using the latLngId
	 * @param latLngId the latLng Id
	 * @return the LatLng object
	 */
	public LatLng findById(int latLngId);
	
	/**
	 * This function is used to delete a latLng object from database
	 * @param persistentInstance the latLng object
	 * @return the delete status
	 */
	public boolean delete(LatLng persistentInstance);
	
	/**
	 * This function is used to get all latLng objects from database
	 * @return array list of latLng objects
	 */
	public ArrayList<LatLng> getAllLatLng();
	
	/**
	 * This function is used to find latLng object in database using the latitude and longitude passed in the parameter.
	 * @param lat the latitude
	 * @param lng the longitude
	 * @return array list of latLng objects
	 */
	public ArrayList<LatLng> findByLocation(Double lat, Double lng);
	
	/**
	 * This function is used to find all latLng objects in database that lie inside the disaster circle
	 * @param lat the latitude
	 * @param lng the longitude
	 * @param radius the radius of the disaster circle
	 * @return list of latLng objects
	 */
	public ArrayList<LatLng> findAllLatLngInsideTheCircle(Double lat, Double lng,Double radius);
	
	/**
	 * This function deletes all latLng objects from database
	 * @return the delete status
	 */
	public int deleteAllLatLngs();

}
