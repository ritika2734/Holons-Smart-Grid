package com.htc.hibernate.utilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htc.hibernate.config.HibernateSessionFactory;
import com.htc.hibernate.pojo.EnergyState;

public class EnergyStateHome {
	static Logger log = Logger.getLogger(EnergyStateHome.class);

	/**
	 * This function is used to find energy state instance in database using energy state ID
	 * @param energyStateId the energy state ID
	 * @return the energy state instance
	 */
	public EnergyState findById(int id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			EnergyState instance = (EnergyState) session.get(EnergyState.class, id);
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
	 * This function is used to get all energy state instances from database
	 * @return list of all energy state instances
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<EnergyState> getAllEnergyState() {
		Session session = null;
		Transaction tx = null;
		ArrayList<EnergyState> listEnergyState = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			listEnergyState = (ArrayList<EnergyState>) session.createQuery("from EnergyState h").list();
			tx.commit();
			return listEnergyState;
		} catch (RuntimeException re) {
			log.info("get holon element State list failed");
			if(tx!=null) { tx.rollback(); }
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return listEnergyState;
	}

}
