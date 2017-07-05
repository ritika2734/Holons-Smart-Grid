package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.SupplierDao;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.pojo.Supplier;
import com.htc.hibernate.utilities.SupplierHome;

/**
 * This class is the implementation of interface SupplierDao and calls respective functions of the next layer.
 *
 */
public class SupplierDaoImpl implements SupplierDao {
	
	/**
	 * Object of SupplierHome class to access the functions defined in that class
	 */
	private SupplierHome supplierHome = new SupplierHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#persist(com.htc.hibernate.pojo.Supplier)
	 */
	@Override
	public Integer persist(Supplier transientInstance) {
		return supplierHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#merge(com.htc.hibernate.pojo.Supplier)
	 */
	@Override
	public Supplier merge(Supplier detachedInstance) {
		return supplierHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#findById(int)
	 */
	@Override
	public Supplier findById(int supplierId) {
		return supplierHome.findById(supplierId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#delete(com.htc.hibernate.pojo.Supplier)
	 */
	@Override
	public boolean delete(Supplier persistentInstance) {
		return supplierHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#getAllSupplier()
	 */
	@Override
	public ArrayList<Supplier> getAllSupplier() {
		return supplierHome.getAllSupplier();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#getSupplierListForProducer(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForProducer(HolonObject holonObject) {
		return supplierHome.getSupplierListForProducer(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#getSupplierListForConsumer(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForConsumer(HolonObject holonObject) {
		return supplierHome.getSupplierListForConsumer(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#getListOfSimilarRequests(java.lang.Integer)
	 */
	@Override
	public ArrayList<Supplier> getListOfSimilarRequests(Integer requestId) {
		return supplierHome.getListOfSimilarRequests(requestId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#getSupplierListForProducerOrderByConsumerPriority(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForProducerOrderByConsumerPriority(HolonObject holonObject) {
		return supplierHome.getSupplierListForProducerOrderByConsumerPriority(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#getSupplierListForProducerPowerSource(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForProducerPowerSource(PowerSource powerSource) {
		return supplierHome.getSupplierListForProducerPowerSource(powerSource);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#getSupplierListHolonCoordinator(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListHolonCoordinator(Holon holon) {
		return supplierHome.getSupplierListHolonCoordinator(holon);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#getSupplierListForConsumerOrProducer(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForConsumerOrProducer(HolonObject holonObject) {
		return supplierHome.getSupplierListForConsumerOrProducer(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.SupplierDao#deleteAllSuppliers()
	 */
	@Override
	public int deleteAllSuppliers() {
		return supplierHome.deleteAllSuppliers();
	}

}
