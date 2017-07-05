package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.DisasterDao;
import com.htc.hibernate.pojo.Disaster;
import com.htc.hibernate.utilities.DisasterHome;

/**
 * This class is the implementation of DisasterDao and calls respective functions of the next layer.
 *
 */
public class DisasterDaoImpl  implements DisasterDao {
	private DisasterHome disasterHome = new DisasterHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.DisasterDao#persist(com.htc.hibernate.pojo.Disaster)
	 */
	@Override
	public Integer persist(Disaster transientInstance) {
		return disasterHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.DisasterDao#merge(com.htc.hibernate.pojo.Disaster)
	 */
	@Override
	public Disaster merge(Disaster detachedInstance) {
		return disasterHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.DisasterDao#findById(int)
	 */
	@Override
	public Disaster findById(int disasterId) {
		return disasterHome.findById(disasterId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.DisasterDao#delete(com.htc.hibernate.pojo.Disaster)
	 */
	@Override
	public boolean delete(Disaster persistentInstance) {
		return disasterHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.DisasterDao#getAllDisasterCircles()
	 */
	@Override
	public ArrayList<Disaster> getAllDisasterCircles() {
		return disasterHome.getAllDisasterCircles();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.DisasterDao#deleteAllDisasters()
	 */
	@Override
	public int deleteAllDisasters() {
		return disasterHome.deleteAllDisasters();
	}

}
