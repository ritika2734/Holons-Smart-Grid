package com.htc.hibernate.pojo;

import java.util.HashSet;
import java.util.Set;

/**
 * PowerLine generated by hbm2java
 */
public class PowerLine implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private HolonObject holonObject;
	private LatLng latLngBySource;
	private LatLng latLngByDestination;
	private PowerSource powerSource;
	private String type;
	private int currentCapacity;
	private int maximumCapacity;
	private boolean isConnected;
	private String reasonDown;
	private Set<?> powerSwitchesForPowerLineA = new HashSet<Object>(0);
	private Set<?> powerSwitchesForPowerLineB = new HashSet<Object>(0);
	private Disaster disaster;

	public PowerLine(Integer id, HolonObject holonObject,
			LatLng latLngBySource, LatLng latLngByDestination,
			PowerSource powerSource, String type, int currentCapacity,
			int maximumCapacity, boolean isConnected, String reasonDown,
			Set<?> powerSwitchesForPowerLineA,
			Set<?> powerSwitchesForPowerLineB, Disaster disaster) {
		super();
		this.id = id;
		this.holonObject = holonObject;
		this.latLngBySource = latLngBySource;
		this.latLngByDestination = latLngByDestination;
		this.powerSource = powerSource;
		this.type = type;
		this.currentCapacity = currentCapacity;
		this.maximumCapacity = maximumCapacity;
		this.isConnected = isConnected;
		this.reasonDown = reasonDown;
		this.powerSwitchesForPowerLineA = powerSwitchesForPowerLineA;
		this.powerSwitchesForPowerLineB = powerSwitchesForPowerLineB;
		this.disaster = disaster;
	}

	public PowerLine() {
	}

	public PowerLine(String type, int currentCapacity, int maximumCapacity,
			boolean isConnected) {
		this.type = type;
		this.currentCapacity = currentCapacity;
		this.maximumCapacity = maximumCapacity;
		this.isConnected = isConnected;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public HolonObject getHolonObject() {
		return this.holonObject;
	}

	public void setHolonObject(HolonObject holonObject) {
		this.holonObject = holonObject;
	}

	public LatLng getLatLngBySource() {
		return this.latLngBySource;
	}

	public void setLatLngBySource(LatLng latLngBySource) {
		this.latLngBySource = latLngBySource;
	}

	public LatLng getLatLngByDestination() {
		return this.latLngByDestination;
	}

	public void setLatLngByDestination(LatLng latLngByDestination) {
		this.latLngByDestination = latLngByDestination;
	}

	public PowerSource getPowerSource() {
		return this.powerSource;
	}

	public void setPowerSource(PowerSource powerSource) {
		this.powerSource = powerSource;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCurrentCapacity() {
		return this.currentCapacity;
	}

	public void setCurrentCapacity(int currentCapacity) {
		this.currentCapacity = currentCapacity;
	}

	public int getMaximumCapacity() {
		return this.maximumCapacity;
	}

	public void setMaximumCapacity(int maximumCapacity) {
		this.maximumCapacity = maximumCapacity;
	}

	public boolean isIsConnected() {
		return this.isConnected;
	}

	public void setIsConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public String getReasonDown() {
		return this.reasonDown;
	}

	public void setReasonDown(String reasonDown) {
		this.reasonDown = reasonDown;
	}

	public Set<?> getPowerSwitchesForPowerLineA() {
		return this.powerSwitchesForPowerLineA;
	}

	public void setPowerSwitchesForPowerLineA(Set<?> powerSwitchesForPowerLineA) {
		this.powerSwitchesForPowerLineA = powerSwitchesForPowerLineA;
	}

	public Set<?> getPowerSwitchesForPowerLineB() {
		return this.powerSwitchesForPowerLineB;
	}

	public void setPowerSwitchesForPowerLineB(Set<?> powerSwitchesForPowerLineB) {
		this.powerSwitchesForPowerLineB = powerSwitchesForPowerLineB;
	}
	
	public Disaster getDisaster() {
		return disaster;
	}

	public void setDisaster(Disaster disaster) {
		this.disaster = disaster;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

}