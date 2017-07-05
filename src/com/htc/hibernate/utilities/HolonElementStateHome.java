package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.HolonElementState;

/**
 * Home object for domain model class HolonElementState.
 * @see .HolonElementState
 */
public class HolonElementStateHome {
	static Logger log = Logger.getLogger(HolonElementStateHome.class);

	/**
	 * This function is used to save holon element state object in database
	 * @param transientInstance the holon element state instance
	 * @return newly created holon element state ID
	 */
	public Integer persist(HolonElementState transientInstance) {
		Integer holonElementState_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			holonElementState_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return holonElementState_id;
	}
	
	/**
	 * This function is used to update holon element state object in database 
	 * @param detachedInstance the holon element state object
	 * @return the updated holon element state object
	 */
	public HolonElementState merge(HolonElementState detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			HolonElementState result = (HolonElementState) session.merge(detachedInstance);
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
	 * This function is used to find holon element state object from database using holon element state ID
	 * @param holonElementStateId the holon element state ID
	 * @return the holon element state instance
	 */
	public HolonElementState findById(int id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			HolonElementState instance = (HolonElementState) session.get(HolonElementState.class, id);
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
	 * This function is used to delete holon element state object from database
	 * @param persistentInstance the holon element state object
	 * @return the delete status
	 */
	public boolean delete(HolonElementState persistentInstance) {
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
	 * This function is used to get all holon element state objects from database
	 * @return array list of holon element state objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HolonElementState> getAllHolonElementState() {
		Session session = null;
		Transaction tx = null;
		ArrayList<HolonElementState> listHolonElementState = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listHolonElementState = (ArrayList<HolonElementState>) session.createQuery("from HolonElementState h").list();
			tx.commit();
			return listHolonElementState;
		} catch (RuntimeException re) {
			log.info("get holon element State list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listHolonElementState;
	}

}
