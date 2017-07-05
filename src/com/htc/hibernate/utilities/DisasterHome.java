package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.Disaster;

public class DisasterHome {
static Logger log = Logger.getLogger(DisasterHome.class);
	
	/**
	 * This function is used to save the disaster instance in database 
	 * @param transientInstance the disaster instance
	 * @return newly created disaster instance ID
	 */
	public Integer persist(Disaster transientInstance) {
		Integer disaster_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			disaster_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return disaster_id;
	}

	/**
	 * This function is used to update an existing disaster instance in database
	 * @param detachedInstance the disaster instance
	 * @return the updated disaster instance
	 */
	public Disaster merge(Disaster detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Disaster result = (Disaster) session.merge(detachedInstance);
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
	 * This function is used to find the disaster instance in database using disaster ID
	 * @param disasterId the disaster ID
	 * @return the disaster instance
	 */
	public Disaster findById(int id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Disaster instance = (Disaster) session.get(Disaster.class, id);
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
	 * This function is used to delete a disaster instance from database
	 * @param persistentInstance the disaster instance
	 * @return the delete status
	 */
	public boolean delete(Disaster persistentInstance) {
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
	 * This function is used to get all disaster instances from database
	 * @return array list of disaster instances
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Disaster> getAllDisasterCircles() {
		Session session = null;
		Transaction tx = null;
		ArrayList<Disaster> listDisaster = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listDisaster = (ArrayList<Disaster>) session.createQuery("from Disaster d").list();
			tx.commit();
			return listDisaster;
		} catch (RuntimeException re) {
			log.info("get DisasterCircles list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listDisaster;
	}
	
	/**
	 * This function is used to delete all disaster instances from database
	 * @return the delete status
	 */
	public int deleteAllDisasters() {
		Session session = null;
		Transaction tx = null;
		int deleteResponse = 0;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("delete Disaster");
			deleteResponse = query.executeUpdate();
			tx.commit();
			return deleteResponse;
		} catch (RuntimeException re) {
			log.info("deleteAllDisasters failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return deleteResponse;
	}

}
