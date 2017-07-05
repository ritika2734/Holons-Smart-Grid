package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.PowerLineDao;
import com.htc.hibernate.pojo.Disaster;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.hibernate.utilities.PowerLineHome;

/**
 * This class is the implementation of PowerSourceDao and calls respective functions of the next layer.
 *
 */
public class PowerLineDaoImpl implements PowerLineDao {
	
	private PowerLineHome powerLineHome = new PowerLineHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#persist(com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public Integer persist(PowerLine transientInstance) {
		return powerLineHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#merge(com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public PowerLine merge(PowerLine detachedInstance) {
		return powerLineHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#findById(int)
	 */
	@Override
	public PowerLine findById(int powerLineId) {
		return powerLineHome.findById(powerLineId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#delete(com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public boolean delete(PowerLine persistentInstance) {
		return powerLineHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#getAllPowerLine()
	 */
	@Override
	public ArrayList<PowerLine> getAllPowerLine() {
		return powerLineHome.getAllPowerLine();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#getConnectedPowerLines(com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public ArrayList<PowerLine> getConnectedPowerLines(PowerLine powerLine) {
		return powerLineHome.getConnectedPowerLines(powerLine);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#getPowerLineByHolonObject(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public PowerLine getPowerLineByHolonObject(HolonObject holonObject) {
		return powerLineHome.getPowerLineByHolonObject(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#getPowerLineByPowerSource(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public PowerLine getPowerLineByPowerSource(PowerSource powerSource) {
		return powerLineHome.getPowerLineByPowerSource(powerSource);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#getPowerLineFromLatLng(com.htc.hibernate.pojo.LatLng)
	 */
	@Override
	public ArrayList<PowerLine> getPowerLineFromLatLng(LatLng latLng) {
		return powerLineHome.getPowerLineFromLatLng(latLng);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#getAllPowerLineIdsHavingDisaster()
	 */
	@Override
	public ArrayList<PowerLine> getAllPowerLineIdsHavingDisaster() {
		return powerLineHome.getAllPowerLineIdsHavingDisaster();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#getAllPowerLinesWithDisasterId(com.htc.hibernate.pojo.Disaster)
	 */
	@Override
	public ArrayList<PowerLine> getAllPowerLinesWithDisasterId(Disaster disaster) {
		return powerLineHome.getAllPowerLinesWithDisasterId(disaster);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.PowerLineDao#deleteAllPowerLines()
	 */
	@Override
	public int deleteAllPowerLines() {
		return powerLineHome.deleteAllPowerLines();
	}

}
