package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerSource;

/**
 * Home object for domain model class PowerSource.
 * @see .PowerSource
 */
public class PowerSourceHome {
	static Logger log = Logger.getLogger(PowerSourceHome.class);
	
	/**
	 * This function is used to save power source object in database
	 * @param transientInstance the power source object
	 * @return newly created power source ID
	 */
	public Integer persist(PowerSource transientInstance) {
		Integer powerSource_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			powerSource_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return powerSource_id;
	}
	
	/**
	 * This function is used to update power source object in database
	 * @param detachedInstance the power source object
	 * @return the updated power source object
	 */
	public PowerSource merge(PowerSource detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			PowerSource result = (PowerSource) session.merge(detachedInstance);
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
	 * This function is used to find power source object in database using power source ID
	 * @param powerSourceId the power source ID
	 * @return the power source object
	 */
	public PowerSource findById(int id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			PowerSource instance = (PowerSource) session.get(PowerSource.class, id);
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
	 * This function is used to delete power source object from database
	 * @param persistentInstance the power source object
	 * @return the delete status
	 */
	public boolean delete(PowerSource persistentInstance) {
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
	 * This function is used to get all power source objects from database
	 * @return list of power source objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PowerSource> getAllPowerSource() {
		Session session = null;
		Transaction tx = null;
		ArrayList<PowerSource> listPowerSource = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listPowerSource = (ArrayList<PowerSource>) session.createQuery("from PowerSource p").list();
			tx.commit();
			return listPowerSource;
		} catch (RuntimeException re) {
			log.info("get Power Source list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listPowerSource;
	}

	/**
	 * This function is used to find all power source objects of a particular holon using the holon coordinator object(1st parameter)
	 * @param holonCoordinator the holon coordinator
	 * @return list of power source objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PowerSource> findByHolonCoordinator(HolonObject holonCoordinator) {
		Session session = null;
		Transaction tx = null;
		ArrayList<PowerSource> listPowerSource = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query qr= session.createQuery("from PowerSource p where p.holonCoordinator=:holonCoordinator");
			qr.setEntity("holonCoordinator", holonCoordinator);
			listPowerSource =  (ArrayList<PowerSource>) qr.list();
			tx.commit();
			return listPowerSource;
		} catch (RuntimeException re) {
			log.info("Exception --> "+re.getMessage());
			re.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listPowerSource;
	}
	
	/**
	 * This function is used to delete all power source objects from database
	 * @return the delete status
	 */
	public int deleteAllPowerSources() {
		Session session = null;
		Transaction tx = null;
		int deleteResponse = 0;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("delete PowerSource");
			deleteResponse = query.executeUpdate();
			tx.commit();
			return deleteResponse;
		} catch (RuntimeException re) {
			log.info("deleteAllPowerSources failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return deleteResponse;
	}

}
