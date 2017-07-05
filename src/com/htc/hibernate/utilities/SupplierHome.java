package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.pojo.Supplier;
import com.htc.utilities.ConstantValues;

/**
 * Home object for domain model class Supplier.
 * @see .Supplier
 */
public class SupplierHome {
	
	static Logger log = Logger.getLogger(SupplierHome.class);
	
	/**
	 * This function is used to save supplier object in database
	 * @param transientInstance the supplier object
	 * @return Newly generated supplier ID
	 */
	public Integer persist(Supplier transientInstance) {
		Integer supplier_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			supplier_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return supplier_id;
	}
	
	/**
	 * This function is used to update an existing supplier object in database
	 * @param detachedInstance the supplier object with updated values
	 * @return the updated supplier object
	 */
	public Supplier merge(Supplier detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Supplier result = (Supplier) session.merge(detachedInstance);
			tx.commit();
			return result;
		} catch (RuntimeException re) {
			log.info("Merge Failed...");
			if(tx!=null) { tx.rollback(); }
			throw re;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

	/**
	 * This function is used to find an existing supplier object in database based on supplier ID
	 * @param supplierId the supplier ID
	 * @return the supplier object
	 */
	public Supplier findById(int id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Supplier instance = (Supplier) session.get(Supplier.class, id);
			tx.commit();
			return instance;
		} catch (RuntimeException re) {
			log.info("Exception --> "+re.getMessage());
			if(tx!=null) { tx.rollback(); }
			throw re;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

	/**
	 * This function is used to delete supplier object from database
	 * @param persistentInstance the supplier object that needs to be deleted
	 * @return the delete status
	 */
	public boolean delete(Supplier persistentInstance) {
		Session session = null;
		Transaction tx = null;
		boolean deleteStatus = false;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(persistentInstance);
			tx.commit();
			deleteStatus = true;
			return deleteStatus;
		} catch (RuntimeException re) {
			log.info("Delete Failed...");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return deleteStatus;
	}
	
	/**
	 * This function is used to get all supplier objects from database
	 * @return array list of supplier objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Supplier> getAllSupplier() {
		Session session = null;
		Transaction tx = null;
		ArrayList<Supplier> listSupplier = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listSupplier = (ArrayList<Supplier>) session.createQuery("from Supplier s order by s.id DESC").list();
			tx.commit();
			return listSupplier;
		} catch (RuntimeException re) {
			log.info("get Supplier list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listSupplier;
	}
	
	/**
	 * This method is used to get only those supplier objects where the holon object(1st parameter) is a producer in supplier table  
	 * @param holonObject the holon object producer
	 * @return array list of supplier objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Supplier> getSupplierListForProducer(HolonObject holonObject) {
		Session session = null;
		Transaction tx = null;
		ArrayList<Supplier> listSupplier = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from Supplier s where s.holonObjectProducer=:holonObject order by s.id DESC");
			query.setEntity("holonObject", holonObject);
			listSupplier = (ArrayList<Supplier>) query.list();
			tx.commit();
			return listSupplier;
		} catch (RuntimeException re) {
			log.info("getSupplierListForProducer failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listSupplier;
	}
	
	/**
	 * This function is used to get only those supplier objects where the power source object(1st parameter) is a producer in supplier table
	 * @param powerSource the power source producer
	 * @return array list of supplier objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Supplier> getSupplierListForProducerPowerSource(PowerSource powerSource) {
		Session session = null;
		Transaction tx = null;
		ArrayList<Supplier> listSupplier = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from Supplier s where s.powerSource=:powerSource order by s.id DESC");
			query.setEntity("powerSource", powerSource);
			listSupplier = (ArrayList<Supplier>) query.list();
			tx.commit();
			return listSupplier;
		} catch (RuntimeException re) {
			log.info("getSupplierListForProducerPowerSource failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listSupplier;
	}
	
	/**
	 * This method is used to get only those supplier objects where the holon object(1st parameter) is a producer in supplier table.
	 * Also the list is sorted based on the consumer holon object priority so that top priority consumers can be processed first.
	 * @param holonObject the holon object producer
	 * @return array list of supplier objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Supplier> getSupplierListForProducerOrderByConsumerPriority(HolonObject holonObject) {
		Session session = null;
		Transaction tx = null;
		ArrayList<Supplier> listSupplier = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from Supplier s where s.holonObjectProducer=:holonObject order by s.holonObjectConsumer.holonObjectType.priority ASC");
			query.setEntity("holonObject", holonObject);
			listSupplier = (ArrayList<Supplier>) query.list();
			tx.commit();
			return listSupplier;
		} catch (RuntimeException re) {
			log.info("getSupplierListForProducer failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listSupplier;
	}

	/**
	 * This method is used to get only those supplier objects where the holon object(1st parameter) is a consumer in supplier table
	 * @param holonObject the holon object consumer
	 * @return array list of supplier objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Supplier> getSupplierListForConsumer(HolonObject holonObject) {
		Session session = null;
		Transaction tx = null;
		ArrayList<Supplier> listSupplier = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from Supplier s where s.holonObjectConsumer=:holonObject order by s.id DESC");
			query.setEntity("holonObject", holonObject);
			listSupplier = (ArrayList<Supplier>) query.list();
			tx.commit();
			return listSupplier;
		} catch (RuntimeException re) {
			log.info("getSupplierListForConsumer failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listSupplier;
	}
	
	/**
	 * This function gets list of similar requests from supplier table based on request ID.
	 * This is to avoid processing duplicate requests.
	 * @param requestId the request id
	 * @return array list of supplier
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Supplier> getListOfSimilarRequests(Integer requestId) {
		Session session = null;
		Transaction tx = null;
		ArrayList<Supplier> listSupplier = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from Supplier s where s.requestId=:requestId");
			query.setInteger("requestId", requestId);
			listSupplier = (ArrayList<Supplier>) query.list();
			tx.commit();
			return listSupplier;
		} catch (RuntimeException re) {
			log.info("getSupplierListForConsumer failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listSupplier;
	}
	
	/**
	 * This function is used to get only those supplier objects where holon object consumer is part of holon passed as a parameter
	 * and the communication mode is COMMUNICATION_MODE_COORDINATOR
	 * @param holon the holon object
	 * @return array list of supplier objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Supplier> getSupplierListHolonCoordinator(Holon holon) {
		Session session = null;
		Transaction tx = null;
		ArrayList<Supplier> listSupplier = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from Supplier s where s.holonObjectConsumer.holon=:holon and s.communicationMode=:communicationMode order by s.id DESC");
			query.setEntity("holon", holon);
			query.setString("communicationMode", ConstantValues.COMMUNICATION_MODE_COORDINATOR);
			listSupplier = (ArrayList<Supplier>) query.list();
			tx.commit();
			return listSupplier;
		} catch (RuntimeException re) {
			log.info("getSupplierListForConsumer failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listSupplier;
	}
	
	/**
	 * This function is used to get only those supplier objects where the holon object(1st parameter)
	 * is either a consumer or a producer in the supplier table
	 * @param holonObject the holon object
	 * @return array list of supplier objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Supplier> getSupplierListForConsumerOrProducer(HolonObject holonObject) {
		Session session = null;
		Transaction tx = null;
		ArrayList<Supplier> listSupplier = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from Supplier s where s.holonObjectConsumer=:holonObject or s.holonObjectProducer=:holonObject order by s.id DESC");
			query.setEntity("holonObject", holonObject);
			listSupplier = (ArrayList<Supplier>) query.list();
			tx.commit();
			return listSupplier;
		} catch (RuntimeException re) {
			log.info("getSupplierListForConsumer failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listSupplier;
	}
	
	/**
	 * This function deletes all supplier objects from database
	 * @return the delete status
	 */	public int deleteAllSuppliers() {
		Session session = null;
		Transaction tx = null;
		int deleteStatus = 0;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("delete Supplier");
			deleteStatus = query.executeUpdate();
			tx.commit();
			return deleteStatus;
		} catch (RuntimeException re) {
			log.info("deleteAllSuppliers failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return deleteStatus;
	}

}
