package com.htc.service;

import java.util.ArrayList;

import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.pojo.Supplier;

public interface SupplierService {
	
	/**
	 * This function is used to save supplier object in database
	 * @param transientInstance the supplier object
	 * @return Newly generated supplier ID
	 */
	public Integer persist(Supplier transientInstance);

	/**
	 * This function is used to update an existing supplier object in database
	 * @param detachedInstance the supplier object with updated values
	 * @return the updated supplier object
	 */
	public Supplier merge(Supplier detachedInstance);
	
	/**
	 * This function is used to find an existing supplier object in database based on supplier ID
	 * @param supplierId the supplier ID
	 * @return the supplier object
	 */
	public Supplier findById(int supplierId);
	
	/**
	 * This function is used to delete supplier object from database
	 * @param persistentInstance the supplier object that needs to be deleted
	 * @return the delete status
	 */
	public boolean delete(Supplier persistentInstance);
	
	/**
	 * This function is used to get all supplier objects from database
	 * @return array list of supplier objects
	 */
	public ArrayList<Supplier> getAllSupplier();
	
	/**
	 * This method is used to get only those supplier objects where the holon object(1st parameter) is a producer in supplier table  
	 * @param holonObject the holon object producer
	 * @return array list of supplier objects
	 */
	public ArrayList<Supplier> getSupplierListForProducer(HolonObject holonObject);

	/**
	 * This method is used to get only those supplier objects where the holon object(1st parameter) is a consumer in supplier table
	 * @param holonObject the holon object consumer
	 * @return array list of supplier objects
	 */
	public ArrayList<Supplier> getSupplierListForConsumer(HolonObject holonObject);

	/**
	 * This function gets list of similar requests from supplier table based on request ID.
	 * This is to avoid processing duplicate requests.
	 * @param requestId the request id
	 * @return array list of supplier
	 */
	public ArrayList<Supplier> getListOfSimilarRequests(Integer requestId);

	/**
	 * This method is used to get only those supplier objects where the holon object(1st parameter) is a producer in supplier table.
	 * Also the list is sorted based on the consumer holon object priority so that top priority consumers can be processed first.
	 * @param holonObject the holon object producer
	 * @return array list of supplier objects
	 */
	public ArrayList<Supplier> getSupplierListForProducerOrderByConsumerPriority(HolonObject holonObject);

	/**
	 * This function is used to get only those supplier objects where the power source object(1st parameter) is a producer in supplier table
	 * @param powerSource the power source producer
	 * @return array list of supplier objects
	 */
	public ArrayList<Supplier> getSupplierListForProducerPowerSource(PowerSource powerSource);

	/**
	 * This function is used to get only those supplier objects where holon object consumer is part of holon passed as a parameter
	 * and the communication mode is COMMUNICATION_MODE_COORDINATOR
	 * @param holon the holon object
	 * @return array list of supplier objects
	 */
	public ArrayList<Supplier> getSupplierListHolonCoordinator(Holon holon);
	
	/**
	 * This function is used to get only those supplier objects where the holon object(1st parameter)
	 * is either a consumer or a producer in the supplier table
	 * @param holonObject the holon object
	 * @return array list of supplier objects
	 */
	public ArrayList<Supplier> getSupplierListForConsumerOrProducer(HolonObject holonObject);
	
	/**
	 * This function deletes all supplier objects from database
	 * @return
	 */
	public int deleteAllSuppliers();
	
}