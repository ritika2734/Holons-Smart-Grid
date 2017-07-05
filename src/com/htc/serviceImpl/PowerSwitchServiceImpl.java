package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSwitch;
import com.htc.service.DaoAware;
import com.htc.service.PowerSwitchService;

/**
 * This class is the implementation of PowerSwitchService interface and calls respective functions of the next DAO layer.
 *
 */
public class PowerSwitchServiceImpl extends DaoAware implements  PowerSwitchService{

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSwitchService#persist(com.htc.hibernate.pojo.PowerSwitch)
	 */
	@Override
	public Integer persist(PowerSwitch transientInstance) {
		return getPowerSwitchDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSwitchService#merge(com.htc.hibernate.pojo.PowerSwitch)
	 */
	@Override
	public PowerSwitch merge(PowerSwitch detachedInstance) {
		return getPowerSwitchDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSwitchService#findById(int)
	 */
	@Override
	public PowerSwitch findById(int powerSwitchId) {
		return getPowerSwitchDao().findById(powerSwitchId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSwitchService#delete(com.htc.hibernate.pojo.PowerSwitch)
	 */
	@Override
	public boolean delete(PowerSwitch persistentInstance) {
		return getPowerSwitchDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSwitchService#getAllPowerSwitch()
	 */
	@Override
	public ArrayList<PowerSwitch> getAllPowerSwitch() {
		return getPowerSwitchDao().getAllPowerSwitch();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSwitchService#checkSwitchStatusBetweenPowerLines(com.htc.hibernate.pojo.PowerLine, com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public PowerSwitch checkSwitchStatusBetweenPowerLines(PowerLine powerLineA, PowerLine powerLineB) {
		return getPowerSwitchDao().checkSwitchStatusBetweenPowerLines(powerLineA, powerLineB);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerSwitchService#deleteAllPowerSwitches()
	 */
	@Override
	public int deleteAllPowerSwitches() {
		return getPowerSwitchDao().deleteAllPowerSwitches();
	}
	
}
