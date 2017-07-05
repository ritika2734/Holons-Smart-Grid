package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.Disaster;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.pojo.PowerLine;
import com.htc.hibernate.pojo.PowerSource;
import com.htc.service.DaoAware;
import com.htc.service.PowerLineService;

/**
 * This class is the implementation of interface PowerLineService and calls respective functions of the next DAO layer.
 *
 */
public class PowerLineServiceImpl extends DaoAware implements PowerLineService{

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#persist(com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public Integer persist(PowerLine transientInstance) {
		return getPowerLineDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#merge(com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public PowerLine merge(PowerLine detachedInstance) {
		return getPowerLineDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#findById(int)
	 */
	@Override
	public PowerLine findById(int powerLineId) {
		return getPowerLineDao().findById(powerLineId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#delete(com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public boolean delete(PowerLine persistentInstance) {
		return getPowerLineDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#getAllPowerLine()
	 */
	@Override
	public ArrayList<PowerLine> getAllPowerLine() {
		return getPowerLineDao().getAllPowerLine();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#getConnectedPowerLines(com.htc.hibernate.pojo.PowerLine)
	 */
	@Override
	public ArrayList<PowerLine> getConnectedPowerLines(PowerLine powerLine) {
		return getPowerLineDao().getConnectedPowerLines(powerLine);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#getPowerLineByHolonObject(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public PowerLine getPowerLineByHolonObject(HolonObject holonObject) {
		return getPowerLineDao().getPowerLineByHolonObject(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#getPowerLineByPowerSource(com.htc.hibernate.pojo.PowerSource)
	 */
	@Override
	public PowerLine getPowerLineByPowerSource(PowerSource powerSource) {
		return getPowerLineDao().getPowerLineByPowerSource(powerSource);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#getPowerLineFromLatLng(com.htc.hibernate.pojo.LatLng)
	 */
	@Override
	public ArrayList<PowerLine> getPowerLineFromLatLng(LatLng latLng) {
		return getPowerLineDao().getPowerLineFromLatLng(latLng);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#getAllPowerLineIdsHavingDisaster()
	 */
	@Override
	public ArrayList<PowerLine> getAllPowerLineIdsHavingDisaster() {
		return getPowerLineDao().getAllPowerLineIdsHavingDisaster();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#getAllPowerLinesWithDisasterId(com.htc.hibernate.pojo.Disaster)
	 */
	@Override
	public ArrayList<PowerLine> getAllPowerLinesWithDisasterId(Disaster disaster) {
		return getPowerLineDao().getAllPowerLinesWithDisasterId(disaster);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.PowerLineService#deleteAllPowerLines()
	 */
	@Override
	public int deleteAllPowerLines() {
		return getPowerLineDao().deleteAllPowerLines();
	}

}
