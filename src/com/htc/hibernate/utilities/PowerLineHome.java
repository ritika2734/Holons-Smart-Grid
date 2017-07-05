package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.Disaster;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.utilities.ConstantValues;

/**
 * Home object for domain model class PowerLine.
 * @see .PowerLine
 */
public class PowerLineHome {
	
	static Logger log = Logger.getLogger(PowerLineHome.class);
	
	/**
	 * This function is used to save power line object in database
	 * @param transientInstance the power line object
	 * @return newly created power line ID
	 */
	public Integer persist(PowerLine transientInstance) {
		Integer powerLine_id=null;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();//Getting Hibernate session from factory
			tx = session.beginTransaction();// active transaction session
			powerLine_id = (Integer)session.save(transientInstance);
			tx.commit();// Committing transaction changes
		} catch (Exception exp){
			exp.printStackTrace();
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return powerLine_id;
	}
	
	/**
	 * This function is used to update an existing power line object in database
	 * @param detachedInstance the power line object
	 * @return the updated power line object
	 */
	public PowerLine merge(PowerLine detachedInstance) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			PowerLine result = (PowerLine) session.merge(detachedInstance);
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
	 * This function is used to find power line object from database using power line id
	 * @param powerLineId the power line ID
	 * @return the power line object
	 */
	public PowerLine findById(int powerLineId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			PowerLine instance = (PowerLine) session.get(PowerLine.class, powerLineId);
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
	 * This function is used to delete power line object from database
	 * @param persistentInstance the power line object
	 * @return the delete status
	 */
	public boolean delete(PowerLine persistentInstance) {
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
	 * This function is used to get all power line objects from database
	 * @return list of power line objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PowerLine> getAllPowerLine() {
		Session session = null;
		Transaction tx = null;
		ArrayList<PowerLine> listPowerLine = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listPowerLine = (ArrayList<PowerLine>) session.createQuery("from PowerLine p").list();
			tx.commit();
			return listPowerLine;
		} catch (RuntimeException re) {
			log.info("get Power Line list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listPowerLine;
	}
	
	/**
	 * This function is used to get power line objects which are connected to the power line passed in the parameter
	 * @param powerLine the power line object
	 * @return list of power line objects 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PowerLine> getConnectedPowerLines(PowerLine powerLine) {
		Session session = null;
		Transaction tx = null;
		ArrayList<PowerLine> connectedPowerLines = new ArrayList<PowerLine>();
		Disaster selfDisaster = powerLine.getDisaster();
		try {
			if (selfDisaster == null) {
				session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
				tx = session.beginTransaction();
				LatLng latLngBySource = powerLine.getLatLngBySource();
				LatLng latLngByDestination = powerLine.getLatLngByDestination();
				Integer powerLineId = powerLine.getId();
				Query query = session.createQuery("from PowerLine p where (p.latLngBySource=:latLngBySource or p.latLngBySource=:latLngByDestination"
						+ " or p.latLngByDestination=:latLngByDestination or p.latLngByDestination=:latLngBySource) and p.id !=:id and p.disaster=null");
				query.setEntity("latLngBySource", latLngBySource);
				query.setEntity("latLngByDestination", latLngByDestination);
				query.setInteger("id", powerLineId);
				connectedPowerLines =  (ArrayList<PowerLine>) query.list();
				tx.commit();
				return connectedPowerLines;
			}
		} catch (RuntimeException re) {
			log.info("get Connected Power Line list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return connectedPowerLines;
	}
	
	/**
	 * This function is used to get the power line object which is connected directly to the holon object passed in the parameter
	 * @param holonObject the holon object
	 * @return the power line object
	 */
	public PowerLine getPowerLineByHolonObject(HolonObject holonObject) {
		Session session = null;
		Transaction tx = null;
		PowerLine powerLine = null;
		String powerLineType = ConstantValues.SUBLINE;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from PowerLine p where p.type=:powerLineType and p.holonObject =:holonObject");
			query.setString("powerLineType", powerLineType);
			query.setEntity("holonObject", holonObject);
			powerLine =  (PowerLine)query.uniqueResult();
			tx.commit();
		} catch (RuntimeException re) {
			log.info("getPowerLineByHolonObject failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return powerLine;
	}
	
	/**
	 * This function is used to get the power line object which is connected directly to the power source object passed in the parameter 
	 * @param powerSource the power source object
	 * @return the power line object
	 */
	public PowerLine getPowerLineByPowerSource(PowerSource powerSource) {
		Session session = null;
		Transaction tx = null;
		PowerLine powerLine = null;
		String powerLineType = ConstantValues.POWERSUBLINE;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from PowerLine p where p.type=:powerLineType and p.powerSource =:powerSource");
			query.setString("powerLineType", powerLineType);
			query.setEntity("powerSource", powerSource);
			powerLine =  (PowerLine)query.uniqueResult();
			tx.commit();
		} catch (RuntimeException re) {
			log.info("getPowerLineByPowerSource failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return powerLine;
	}

	/**
	 * This function is used to get the power line object using the location passed in the parameter
	 * @param latLng the location object
	 * @return list of power lines
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PowerLine> getPowerLineFromLatLng(LatLng latLng){
		Session session = null;
		Transaction tx = null;
		ArrayList<PowerLine> listPowerLine = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from PowerLine p where p.latLngBySource=:latLng or p.latLngByDestination =:latLng");
			query.setEntity("latLng", latLng);
			listPowerLine = (ArrayList<PowerLine>) query.list();
			tx.commit();
		} catch (RuntimeException re) {
			log.info("getPowerLineFromLatLngId failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listPowerLine;
	}
	
	/**
	 * This function is used to get all power line IDs which are part of any disaster
	 * @return array list of power lines
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PowerLine> getAllPowerLineIdsHavingDisaster(){
		Session session = null;
		Transaction tx = null;
		ArrayList<PowerLine> listPowerLine = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from PowerLine p where p.disaster is not null");
			listPowerLine = (ArrayList<PowerLine>) query.list();
			tx.commit();
		} catch (RuntimeException re) {
			log.info("getPowerLineFromLatLngId failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listPowerLine;
	}
	
	/**
	 * This function gets list of all power line objects which are part of a particular disaster object passed in the parameter
	 * @param disaster the disaster object
	 * @return array list of power lines
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PowerLine> getAllPowerLinesWithDisasterId(Disaster disaster){
		Session session = null;
		Transaction tx = null;
		ArrayList<PowerLine> listPowerLine = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from PowerLine p where p.disaster=:disaster");
			query.setEntity("disaster", disaster);
			listPowerLine = (ArrayList<PowerLine>) query.list();
			tx.commit();
		} catch (RuntimeException re) {
			log.info("getPowerLineFromLatLngId failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listPowerLine;
	}

	/**
	 * This function is used to delete all power line objects from database
	 * @return the delete status
	 */
	public int deleteAllPowerLines() {
		Session session = null;
		Transaction tx = null;
		int deleteResponse = 0;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("delete PowerLine");
			deleteResponse = query.executeUpdate();
			tx.commit();
			return deleteResponse;
		} catch (RuntimeException re) {
			log.info("deleteAllPowerLines failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return deleteResponse;
	}
	
}
