package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.PowerSourceDao;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.utilities.PowerSourceHome;

/**
 * This class is the implementation of PowerSourceDao and calls respective functions of the next layer.
 *
 */
public class PowerSourceDaoImpl implements PowerSourceDao {
	PowerSourceHome powerSourceHome = new PowerSourceHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSourceDao#persist(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public Integer persist(PowerSource transientInstance) {
		return powerSourceHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSourceDao#merge(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public PowerSource merge(PowerSource detachedInstance) {
		return powerSourceHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSourceDao#findById(int)
	 */
	@Override
	public PowerSource findById(int powerSourceId) {
		return powerSourceHome.findById(powerSourceId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSourceDao#delete(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public boolean delete(PowerSource persistentInstance) {
		return powerSourceHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSourceDao#getAllPowerSource()
	 */
	@Override
	public ArrayList<PowerSource> getAllPowerSource() {
		return powerSourceHome.getAllPowerSource();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSourceDao#findByHolonCoordinator(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<PowerSource> findByHolonCoordinator(HolonObject holonCoordinator) {
		return powerSourceHome.findByHolonCoordinator(holonCoordinator);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSourceDao#deleteAllPowerSources()
	 */
	@Override
	public int deleteAllPowerSources() {
		return powerSourceHome.deleteAllPowerSources();
	}

}
