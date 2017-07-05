package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.Holon;

/**
 * Home object for domain model class Holon.
 * @see .Holon
 */
public class HolonHome {
	static Logger log = Logger.getLogger(HolonHome.class);

	/**
	 * This function is used to save holon in database
	 * @param transientInstance the holon
	 * @return newly created holon instance
	 */
	public Integer persist(Holon transientInstance) {
		Integer holon_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			holon_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return holon_id;
	}
	
	/**
	 * This function is used to update holon instance in database
	 * @param detachedInstance the holon instance
	 * @return the updated holon instance
	 */
	public Holon merge(Holon detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Holon result = (Holon) session.merge(detachedInstance);
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
	 * This function is used to find a holon instance in database using holon ID
	 * @param holonId the holon ID
	 * @return the holon instance
	 */
	public Holon findById(int id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Holon instance = (Holon) session.get(Holon.class, id);
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
	 * This function is used to delete holon instance from database
	 * @param persistentInstance the holon instance
	 * @return the delete status
	 */
	public boolean delete(Holon persistentInstance) {
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
	 * This function is used to get all holon instances from database
	 * @return array list of holon instance
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Holon> getAllHolon() {
		Session session = null;
		Transaction tx = null;
		ArrayList<Holon> listHolon = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listHolon = (ArrayList<Holon>) session.createQuery("from Holon h").list();
			tx.commit();
			return listHolon;
		} catch (RuntimeException re) {
			log.info("get holon list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listHolon;
	}

}
