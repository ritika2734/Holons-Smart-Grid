package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.service.DaoAware;
import com.htc.service.PowerSourceService;

/**
 * This class is the implementation of interface PowerSourceService and calls respective functions of the next DAO layer.
 *
 */
public class PowerSourceServiceImpl extends DaoAware implements PowerSourceService {

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSourceService#persist(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public Integer persist(PowerSource transientInstance) {
		return getPowerSourceDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSourceService#merge(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public PowerSource merge(PowerSource detachedInstance) {
		return getPowerSourceDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSourceService#findById(int)
	 */
	@Override
	public PowerSource findById(int powerSourceId) {
		return getPowerSourceDao().findById(powerSourceId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSourceService#delete(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public boolean delete(PowerSource persistentInstance) {
		return getPowerSourceDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSourceService#getAllPowerSource()
	 */
	@Override
	public ArrayList<PowerSource> getAllPowerSource() {
		return getPowerSourceDao().getAllPowerSource();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSourceService#findByHolonCoordinator(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<PowerSource> findByHolonCoordinator(HolonObject holonCoordinator) {
		return getPowerSourceDao().findByHolonCoordinator(holonCoordinator);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSourceService#deleteAllPowerSources()
	 */
	@Override
	public int deleteAllPowerSources() {
		return getPowerSourceDao().deleteAllPowerSources();
	}

}
