package com.htc.hibernate.pojo;

import java.util.HashSet;
import java.util.Set;

/**
 * PowerSource generated by hbm2java
 */
public class PowerSource implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	private int id;
	private HolonObject holonCoordinator;
	private LatLng center;
	private String name;
	private Integer minProduction;
	private Integer maxProduction;
	private Integer currentProduction;
	private Double radius;
	private Boolean status;
	private Integer flexibility;
	private Set<?> powerLines = new HashSet<Object>(0);
	private Set<?> suppliers = new HashSet<Object>(0);
	

	public PowerSource(int id, HolonObject holonCoordinator, LatLng center,
			String name, Integer minProduction, Integer maxProduction,
			Integer currentProduction, Double radius, Boolean status,
			Integer flexibility, Set<?> powerLines, Set<?> suppliers) {
		super();
		this.id = id;
		this.holonCoordinator = holonCoordinator;
		this.center = center;
		this.name = name;
		this.minProduction = minProduction;
		this.maxProduction = maxProduction;
		this.currentProduction = currentProduction;
		this.radius = radius;
		this.status = status;
		this.flexibility = flexibility;
		this.powerLines = powerLines;
		this.suppliers = suppliers;
	}

	public PowerSource() {
	}

	public PowerSource(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HolonObject getHolonCoordinator() {
		return this.holonCoordinator;
	}

	public void setHolonCoordinator(HolonObject holonCoordinator) {
		this.holonCoordinator = holonCoordinator;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMinProduction() {
		return this.minProduction;
	}

	public void setMinProduction(Integer minProduction) {
		this.minProduction = minProduction;
	}

	public Integer getMaxProduction() {
		return this.maxProduction;
	}

	public void setMaxProduction(Integer maxProduction) {
		this.maxProduction = maxProduction;
	}

	public Integer getCurrentProduction() {
		return this.currentProduction;
	}

	public void setCurrentProduction(Integer currentProduction) {
		this.currentProduction = currentProduction;
	}

	public Double getRadius() {
		return this.radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Boolean getStatus() {
		return this.status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Set<?> getPowerLines() {
		return this.powerLines;
	}

	public void setPowerLines(Set<?> powerLines) {
		this.powerLines = powerLines;
	}

	public LatLng getCenter() {
		return center;
	}

	public void setCenter(LatLng center) {
		this.center = center;
	}

	public Integer getFlexibility() {
		return flexibility;
	}

	public void setFlexibility(Integer flexibility) {
		this.flexibility = flexibility;
	}

	public Set<?> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(Set<?> suppliers) {
		this.suppliers = suppliers;
	}

}