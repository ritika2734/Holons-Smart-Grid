package com.htc.service;

import com.htc.dao.DisasterDao;
import com.htc.dao.EnergyStateDao;
import com.htc.dao.HolonDao;
import com.htc.dao.HolonElementDao;
import com.htc.dao.HolonElementStateDao;
import com.htc.dao.HolonElementTypeDao;
import com.htc.dao.HolonObjectDao;
import com.htc.dao.HolonObjectTypeDao;
import com.htc.dao.LatLngDao;
import com.htc.dao.PowerLineDao;
import com.htc.dao.PowerSourceDao;
import com.htc.dao.PowerSwitchDao;
import com.htc.dao.SupplierDao;
import com.htc.daoImpl.DisasterDaoImpl;
import com.htc.daoImpl.EnergyStateDaoImpl;
import com.htc.daoImpl.HolonDaoImpl;
import com.htc.daoImpl.HolonElementDaoImpl;
import com.htc.daoImpl.HolonElementStateDaoImpl;
import com.htc.daoImpl.HolonElementTypeDaoImpl;
import com.htc.daoImpl.HolonObjectDaoImpl;
import com.htc.daoImpl.HolonObjectTypeDaoImpl;
import com.htc.daoImpl.LatLngDaoImpl;
import com.htc.daoImpl.PowerLineDaoImpl;
import com.htc.daoImpl.PowerSourceDaoImpl;
import com.htc.daoImpl.PowerSwitchDaoImpl;
import com.htc.daoImpl.SupplierDaoImpl;

/**
 * This is the parent class of all service implementation classes. All DAO implementations are instantiated here.
 * 
 */
public class DaoAware {
	
	private HolonDao holonDao = new HolonDaoImpl();
	private HolonElementDao holonElementDao = new HolonElementDaoImpl();
	private HolonElementStateDao holonElementStateDao = new HolonElementStateDaoImpl() ;
	private HolonElementTypeDao holonElementTypeDao = new HolonElementTypeDaoImpl() ;
	private HolonObjectDao holonObjectDao = new HolonObjectDaoImpl();
	private HolonObjectTypeDao holonObjectTypeDao = new HolonObjectTypeDaoImpl() ;
	private LatLngDao latLngDao = new LatLngDaoImpl();
	private PowerLineDao powerLineDao = new PowerLineDaoImpl();
	private PowerSwitchDao powerSwitchDao = new PowerSwitchDaoImpl();
	private PowerSourceDao powerSourceDao = new PowerSourceDaoImpl();
	private EnergyStateDao energyStateDao = new EnergyStateDaoImpl();
	private SupplierDao supplierDao = new SupplierDaoImpl();
	private DisasterDao disasterDao= new DisasterDaoImpl();
	
	public HolonDao getHolonDao() {
		return holonDao;
	}
	public void setHolonDao(HolonDao holonDao) {
		this.holonDao = holonDao;
	}
	public HolonElementDao getHolonElementDao() {
		return holonElementDao;
	}
	public void setHolonElementDao(HolonElementDao holonElementDao) {
		this.holonElementDao = holonElementDao;
	}
	public HolonElementStateDao getHolonElementStateDao() {
		return holonElementStateDao;
	}
	public void setHolonElementStateDao(HolonElementStateDao holonElementStateDao) {
		this.holonElementStateDao = holonElementStateDao;
	}
	public HolonElementTypeDao getHolonElementTypeDao() {
		return holonElementTypeDao;
	}
	public void setHolonElementTypeDao(HolonElementTypeDao holonElementTypeDao) {
		this.holonElementTypeDao = holonElementTypeDao;
	}
	public HolonObjectDao getHolonObjectDao() {
		return holonObjectDao;
	}
	public void setHolonObjectDao(HolonObjectDao holonObjectDao) {
		this.holonObjectDao = holonObjectDao;
	}
	public HolonObjectTypeDao getHolonObjectTypeDao() {
		return holonObjectTypeDao;
	}
	public void setHolonObjectTypeDao(HolonObjectTypeDao holonObjectTypeDao) {
		this.holonObjectTypeDao = holonObjectTypeDao;
	}
	public LatLngDao getLatLngDao() {
		return latLngDao;
	}
	public void setLatLngDao(LatLngDao latLngDao) {
		this.latLngDao = latLngDao;
	}
	public PowerLineDao getPowerLineDao() {
		return powerLineDao;
	}
	public void setPowerLineDao(PowerLineDao powerLineDao) {
		this.powerLineDao = powerLineDao;
	}
	public PowerSwitchDao getPowerSwitchDao() {
		return powerSwitchDao;
	}
	public void setPowerSwitchDao(PowerSwitchDao powerSwitchDao) {
		this.powerSwitchDao = powerSwitchDao;
	}
	public PowerSourceDao getPowerSourceDao() {
		return powerSourceDao;
	}
	public void setPowerSourceDao(PowerSourceDao powerSourceDao) {
		this.powerSourceDao = powerSourceDao;
	}
	public EnergyStateDao getEnergyStateDao() {
		return energyStateDao;
	}
	public void setEnergyStateDao(EnergyStateDao energyStateDao) {
		this.energyStateDao = energyStateDao;
	}
	public SupplierDao getSupplierDao() {
		return supplierDao;
	}
	public void setSupplierDao(SupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}
	public DisasterDao getDisasterDao() {
		return disasterDao;
	}
	public void setDisasterDao(DisasterDao disasterDao) {
		this.disasterDao = disasterDao;
	}
	
}