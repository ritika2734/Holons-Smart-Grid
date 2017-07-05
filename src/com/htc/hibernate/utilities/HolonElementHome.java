package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonObject;

/**
 * Home object for domain model class HolonElement.
 * @see .HolonElement
 */
public class HolonElementHome {
	static Logger log = Logger.getLogger(HolonElementHome.class);

	/**
	 * This function is used to save holon element in database
	 * @param transientInstance the holon element
	 * @return newly created holon element ID
	 */
	public Integer persist(HolonElement transientInstance) {
		Integer holonElement_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			holonElement_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return holonElement_id;
	}
	
	/**
	 * This function is used to update holon element in database
	 * @param detachedInstance the holon element
	 * @return the updated holon element
	 */
	public HolonElement merge(HolonElement detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			HolonElement result = (HolonElement) session.merge(detachedInstance);
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
	 * This method is used to find holon element object from database using holon element ID  
	 * @param holonElementId the holon element ID
	 * @return the holon element instance
	 */
	public HolonElement findById(int id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			HolonElement instance = (HolonElement) session.get(HolonElement.class, id);
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
	 * This function is used to delete holon element instance from database
	 * @param persistentInstance the holon element instance
	 * @return the delete status
	 */
	public boolean delete(HolonElement persistentInstance) {
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
	 * This function is used to get all holon elements from database
	 * @return array list of holon elements
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HolonElement> getAllHolonElement() {
		Session session = null;
		Transaction tx = null;
		ArrayList<HolonElement> listHolonElement = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listHolonElement = (ArrayList<HolonElement>) session.createQuery("from HolonElement h").list();
			tx.commit();
			return listHolonElement;
		} catch (RuntimeException re) {
			log.info("get holon element list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listHolonElement;
	}

	/**
	 * This function is used to get all holon elements of a particular holon object
	 * @param holonObject the holon object
	 * @return array list of holon elements
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HolonElement> getHolonElements(HolonObject holonObject) {
		Session session = null;
		Transaction tx = null;
		ArrayList<HolonElement> listHolonElement = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from HolonElement h where h.holonObject=:holonObject");
			query.setEntity("holonObject", holonObject);
			listHolonElement = (ArrayList<HolonElement>) query.list();
			tx.commit();
			return listHolonElement;
		} catch (RuntimeException re) {
			log.info("get holon element list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listHolonElement;
	}
	
	/**
	 * This function is used to delete all holon elements from database
	 * @return the delete status
	 */
	public int deleteAllHolonElements() {
		Session session = null;
		Transaction tx = null;
		int deleteResponse = 0;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("delete HolonElement");
			deleteResponse = query.executeUpdate();
			tx.commit();
			return deleteResponse;
		} catch (RuntimeException re) {
			log.info("deleteAllHolonElements failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return deleteResponse;
	}
	
}
