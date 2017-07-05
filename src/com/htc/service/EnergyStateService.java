package com.htc.service;

import java.util.ArrayList;
import com.htc.hibernate.pojo.EnergyState;

/**
 * This interface contains all functions related to energy state
 */
public interface EnergyStateService {
	
	/**
	 * This function is used to find energy state instance in database using energy state ID
	 * @param energyStateId the energy state ID
	 * @return the energy state instance
	 */
	public EnergyState findById(int energyStateId);
	
	/**
	 * This function is used to get all energy state instances from database
	 * @return list of all energy state instances
	 */
	public ArrayList<EnergyState> getAllEnergyState();

}
