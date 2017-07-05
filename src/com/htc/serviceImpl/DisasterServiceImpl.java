package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.Disaster;
import com.htc.service.DaoAware;
import com.htc.service.DisasterService;

/**
 * This class is the implementation of interface DisasterService and calls respective functions of the next DAO layer.
 */
public class DisasterServiceImpl extends DaoAware implements DisasterService{

	/* (non-Javadoc)
	 * @see com.htc.service.DisasterService#persist(com.htc.hibernate.pojo.Disaster)
	 */
	@Override
	public Integer persist(Disaster transientInstance) {
		return getDisasterDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.DisasterService#merge(com.htc.hibernate.pojo.Disaster)
	 */
	@Override
	public Disaster merge(Disaster detachedInstance) {
		return getDisasterDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.DisasterService#findById(int)
	 */
	@Override
	public Disaster findById(int disasterId) {
		return getDisasterDao().findById(disasterId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.DisasterService#delete(com.htc.hibernate.pojo.Disaster)
	 */
	@Override
	public boolean delete(Disaster persistentInstance) {
		return getDisasterDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.DisasterService#getAllDisasterCircles()
	 */
	@Override
	public ArrayList<Disaster> getAllDisasterCircles() {
		return getDisasterDao().getAllDisasterCircles();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.DisasterService#deleteAllDisasters()
	 */
	@Override
	public int deleteAllDisasters() {
		return getDisasterDao().deleteAllDisasters();
	}

}
