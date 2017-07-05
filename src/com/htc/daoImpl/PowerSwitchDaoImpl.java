package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.PowerSwitchDao;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSwitch;
import com.htc.hibernate.utilities.PowerSwitchHome;

/**
 * This class is the implementation of PowerSwitchDao and calls respective functions of the next layer.
 *
 */
public class PowerSwitchDaoImpl implements PowerSwitchDao {

	private PowerSwitchHome powerSwitchHome = new PowerSwitchHome();
	
	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSwitchDao#persist(com.htc.hibernate.pojo.PowerSwitch)
	 */
	@Override
	public Integer persist(PowerSwitch transientInstance) {
		return powerSwitchHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSwitchDao#merge(com.htc.hibernate.pojo.PowerSwitch)
	 */
	@Override
	public PowerSwitch merge(PowerSwitch detachedInstance) {
		return powerSwitchHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSwitchDao#findById(int)
	 */
	@Override
	public PowerSwitch findById(int powerSwitchId) {
		return powerSwitchHome.findById(powerSwitchId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSwitchDao#delete(com.htc.hibernate.pojo.PowerSwitch)
	 */
	@Override
	public boolean delete(PowerSwitch persistentInstance) {
		return powerSwitchHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSwitchDao#getAllPowerSwitch()
	 */
	@Override
	public ArrayList<PowerSwitch> getAllPowerSwitch() {
		return powerSwitchHome.getAllPowerSwitch();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSwitchDao#checkSwitchStatusBetweenPowerLines(com.htc.hibernate.pojo.PowerLine, com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public PowerSwitch checkSwitchStatusBetweenPowerLines(PowerLine powerLineA, PowerLine powerLineB) {
		return powerSwitchHome.checkSwitchStatusBetweenPowerLines(powerLineA, powerLineB);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerSwitchDao#deleteAllPowerSwitches()
	 */
	@Override
	public int deleteAllPowerSwitches() {
		return powerSwitchHome.deleteAllPowerSwitches();
	}

}
