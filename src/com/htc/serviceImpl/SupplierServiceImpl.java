package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.pojo.Supplier;
import com.htc.service.DaoAware;
import com.htc.service.SupplierService;

/**
 * This class is the implementation of interface SupplierService and calls respective functions of the next DAO layer. 
 *
 */
public class SupplierServiceImpl extends DaoAware implements  SupplierService{

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#persist(com.htc.hibernate.pojo.Supplier)
	 */
	@Override
	public Integer persist(Supplier transientInstance) {
		return getSupplierDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#merge(com.htc.hibernate.pojo.Supplier)
	 */
	@Override
	public Supplier merge(Supplier detachedInstance) {
		return getSupplierDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#findById(int)
	 */
	@Override
	public Supplier findById(int supplierId) {
		return getSupplierDao().findById(supplierId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#delete(com.htc.hibernate.pojo.Supplier)
	 */
	@Override
	public boolean delete(Supplier persistentInstance) {
		return getSupplierDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#getAllSupplier()
	 */
	@Override
	public ArrayList<Supplier> getAllSupplier() {
		return getSupplierDao().getAllSupplier();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#getSupplierListForProducer(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForProducer(HolonObject holonObject) {
		return getSupplierDao().getSupplierListForProducer(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#getSupplierListForConsumer(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForConsumer(HolonObject holonObject) {
		return getSupplierDao().getSupplierListForConsumer(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#getListOfSimilarRequests(java.lang.Integer)
	 */
	@Override
	public ArrayList<Supplier> getListOfSimilarRequests(Integer requestId) {
		return getSupplierDao().getListOfSimilarRequests(requestId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#getSupplierListForProducerOrderByConsumerPriority(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForProducerOrderByConsumerPriority(HolonObject holonObject) {
		return getSupplierDao().getSupplierListForProducerOrderByConsumerPriority(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#getSupplierListForProducerPowerSource(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForProducerPowerSource(PowerSource powerSource) {
		return getSupplierDao().getSupplierListForProducerPowerSource(powerSource);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#getSupplierListHolonCoordinator(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListHolonCoordinator(Holon holon) {
		return getSupplierDao().getSupplierListHolonCoordinator(holon);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#getSupplierListForConsumerOrProducer(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<Supplier> getSupplierListForConsumerOrProducer(HolonObject holonObject) {
		return getSupplierDao().getSupplierListForConsumerOrProducer(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.SupplierService#deleteAllSuppliers()
	 */
	@Override
	public int deleteAllSuppliers() {
		return getSupplierDao().deleteAllSuppliers();
	}
	
}