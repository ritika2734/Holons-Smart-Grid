package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.HolonObjectType;

/**
 * Home object for domain model class HolonObjectType.
 * @see .HolonObjectType
 */
public class HolonObjectTypeHome {
	static Logger log = Logger.getLogger(HolonObjectTypeHome.class);

	/**
	 * This function is used to save holon object type in database
	 * @param transientInstance the holon object type instance
	 * @return newly created holon object type ID
	 */
	public Integer persist(HolonObjectType transientInstance) {
		Integer holonObjectType_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			holonObjectType_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return holonObjectType_id;
	}
	
	/**
	 * This function is used to update holon object type instance in database
	 * @param detachedInstance the holon object type
	 * @return the updated holon object type instance
	 */
	public HolonObjectType merge(HolonObjectType detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			HolonObjectType result = (HolonObjectType) session.merge(detachedInstance);
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
	 * This function is used to find holon object type in database using the id passed in the parameter
	 * @param holonObjectTypeId the holon object type id
	 * @return the holon object type instance
	 */
	public HolonObjectType findById(int id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			HolonObjectType instance = (HolonObjectType) session.get(HolonObjectType.class, id);
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
	 * This function is used to delete holon object type instance from database
	 * @param persistentInstance the holon object type instance
	 * @return the delete status
	 */
	public boolean delete(HolonObjectType persistentInstance) {
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
	 * This function is used to get all holon object type instances from the database
	 * @return array list of all holon object type instances
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HolonObjectType> getAllHolonObjectType() {
		Session session = null;
		Transaction tx = null;
		ArrayList<HolonObjectType> listHolonObjectType = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listHolonObjectType = (ArrayList<HolonObjectType>) session.createQuery("from HolonObjectType h").list();
			tx.commit();
			return listHolonObjectType;
		} catch (RuntimeException re) {
			log.info("get holon Object type list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listHolonObjectType;
	}

}
