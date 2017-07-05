package com.htc.service;

import com.opensymphony.xwork2.ActionSupport;
import com.htc.serviceImpl.DisasterServiceImpl;
import com.htc.serviceImpl.HolonElementServiceImpl;
import com.htc.serviceImpl.HolonElementStateServiceImpl;
import com.htc.serviceImpl.HolonElementTypeServiceImpl;
import com.htc.serviceImpl.HolonObjectServiceImpl;
import com.htc.serviceImpl.HolonObjectTypeServiceImpl;
import com.htc.serviceImpl.HolonServiceImpl;
import com.htc.serviceImpl.LatLngServiceImpl;
import com.htc.serviceImpl.PowerLineServiceImpl;
import com.htc.serviceImpl.PowerSourceServiceImpl;
import com.htc.serviceImpl.PowerSwitchServiceImpl;
import com.htc.serviceImpl.SupplierServiceImpl;

/**
 * This is the parent class of all action classes and also CommonUtilities class. All Service implementations are instantiated here.
 * All actions use these service objects to call DAO layer functions which further interacts with hibernate session.
 * 
 */
public abstract class ServiceAware extends ActionSupport {
	
	private static final long serialVersionUID = 1L;

	private HolonService holonService = new HolonServiceImpl();
	private HolonElementService holonElementService = new HolonElementServiceImpl();
	private HolonElementStateService holonElementStateService = new HolonElementStateServiceImpl();
	private HolonElementTypeService holonElementTypeService = new HolonElementTypeServiceImpl();
	private HolonObjectService holonObjectService = new HolonObjectServiceImpl();
	private HolonObjectTypeService holonObjectTypeService = new HolonObjectTypeServiceImpl();
	private LatLngService latLngService = new LatLngServiceImpl();
	private PowerLineService powerLineService = new PowerLineServiceImpl();
	private PowerSwitchService powerSwitchService = new PowerSwitchServiceImpl();
	private PowerSourceService powerSourceService = new PowerSourceServiceImpl();
	private SupplierService supplierService = new SupplierServiceImpl();
	private DisasterService disasterService = new DisasterServiceImpl();
	
	public HolonService getHolonService() {
		return holonService;
	}
	public void setHolonService(HolonService holonService) {
		this.holonService = holonService;
	}
	public HolonElementService getHolonElementService() {
		return holonElementService;
	}
	public void setHolonElementService(HolonElementService holonElementService) {
		this.holonElementService = holonElementService;
	}
	public HolonElementStateService getHolonElementStateService() {
		return holonElementStateService;
	}
	public void setHolonElementStateService(
			HolonElementStateService holonElementStateService) {
		this.holonElementStateService = holonElementStateService;
	}
	public HolonElementTypeService getHolonElementTypeService() {
		return holonElementTypeService;
	}
	public void setHolonElementTypeService(
			HolonElementTypeService holonElementTypeService) {
		this.holonElementTypeService = holonElementTypeService;
	}
	public HolonObjectService getHolonObjectService() {
		return holonObjectService;
	}
	public void setHolonObjectService(HolonObjectService holonObjectService) {
		this.holonObjectService = holonObjectService;
	}
	public HolonObjectTypeService getHolonObjectTypeService() {
		return holonObjectTypeService;
	}
	public void setHolonObjectTypeService(
			HolonObjectTypeService holonObjectTypeService) {
		this.holonObjectTypeService = holonObjectTypeService;
	}
	public LatLngService getLatLngService() {
		return latLngService;
	}
	public void setLatLngService(LatLngService latLngService) {
		this.latLngService = latLngService;
	}
	public PowerLineService getPowerLineService() {
		return powerLineService;
	}
	public void setPowerLineService(PowerLineService powerLineService) {
		this.powerLineService = powerLineService;
	}
	public PowerSwitchService getPowerSwitchService() {
		return powerSwitchService;
	}
	public void setPowerSwitchService(PowerSwitchService powerSwitchService) {
		this.powerSwitchService = powerSwitchService;
	}
	public PowerSourceService getPowerSourceService() {
		return powerSourceService;
	}
	public void setPowerSourceService(PowerSourceService powerSourceService) {
		this.powerSourceService = powerSourceService;
	}
	public SupplierService getSupplierService() {
		return supplierService;
	}
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}
	public DisasterService getDisasterService() {
		return disasterService;
	}
	public void setDisasterService(DisasterService disasterService) {
		this.disasterService = disasterService;
	}

}
