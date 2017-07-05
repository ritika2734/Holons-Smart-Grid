package com.htc.hibernate.pojo;

import java.util.HashSet;
import java.util.Set;

public class Disaster implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Double radius;
	private LatLng center;
	private Set<?> powerLines = new HashSet<Object>(0);
	
	public Disaster() {
	}
	public Disaster(Integer id, Double radius, LatLng center, Set<?> powerLines) {
		super();
		this.id = id;
		this.radius = radius;
		this.center = center;
		this.powerLines = powerLines;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	public LatLng getCenter() {
		return center;
	}
	public void setCenter(LatLng center) {
		this.center = center;
	}
	public Set<?> getPowerLines() {
		return powerLines;
	}
	public void setPowerLines(Set<?> powerLines) {
		this.powerLines = powerLines;
	}


}
