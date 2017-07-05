package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.HolonElementType;

/**
 * Home object for domain model class HolonElementType.
 * @see .HolonElementType
 */
public class HolonElementTypeHome {
	static Logger log = Logger.getLogger(HolonElementTypeHome.class);

	/**
	 * This function is used to save holon element type in database
	 * @param transientInstance the holon element type 
	 * @return newly created holon element type ID
	 */
	public Integer persist(HolonElementType transientInstance) {
		Integer holonElementType_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			holonElementType_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return holonElementType_id;
	}
	
	/**
	 * This function is used to update holon element type instance in database
	 * @param detachedInstance the holon element type instance
	 * @return the updated holon element type instance
	 */
	public HolonElementType merge(HolonElementType detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			HolonElementType result = (HolonElementType) session.merge(detachedInstance);
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
	 * This function is used to find holon element type instance in database using holon element type ID
	 * @param holonElementTypeId the holon element type ID
	 * @return the holon element type instance
	 */
	public HolonElementType findById(int id) {
		Session session = null;
		Transaction tx = null;
		log.info("Holon Element Type ID = "+id);
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			HolonElementType instance = (HolonElementType) session.get(HolonElementType.class, id);
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
	 * This function is used to delete holon element type instance from database
	 * @param persistentInstance the holon element type instance
	 * @return the delete status
	 */
	public boolean delete(HolonElementType persistentInstance) {
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
	 * This function gets a list of all holon element type from database
	 * @return array list of holon element type instances
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HolonElementType> getAllHolonElementType() {
		Session session = null;
		Transaction tx = null;
		ArrayList<HolonElementType> listHolonElementType = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listHolonElementType = (ArrayList<HolonElementType>) session.createQuery("from HolonElementType h order by h.name asc").list();
			tx.commit();
			return listHolonElementType;
		} catch (RuntimeException re) {
			log.info("get holon element type list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listHolonElementType;
	}

}
