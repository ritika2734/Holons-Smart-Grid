package com.htc.hibernate.pojo;

public class Supplier implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private HolonObject holonObjectProducer;
	private HolonObject holonObjectConsumer;
	private PowerSource powerSource;
	private int powerRequested;
	private int powerGranted;
	private String messageStatus;
	private Integer requestId;
	private String communicationMode;

	public Supplier() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public HolonObject getHolonObjectProducer() {
		return holonObjectProducer;
	}

	public void setHolonObjectProducer(HolonObject holonObjectProducer) {
		this.holonObjectProducer = holonObjectProducer;
	}

	public HolonObject getHolonObjectConsumer() {
		return holonObjectConsumer;
	}

	public void setHolonObjectConsumer(HolonObject holonObjectConsumer) {
		this.holonObjectConsumer = holonObjectConsumer;
	}

	public PowerSource getPowerSource() {
		return powerSource;
	}

	public void setPowerSource(PowerSource powerSource) {
		this.powerSource = powerSource;
	}

	public int getPowerRequested() {
		return powerRequested;
	}

	public void setPowerRequested(int powerRequested) {
		this.powerRequested = powerRequested;
	}

	public int getPowerGranted() {
		return powerGranted;
	}

	public void setPowerGranted(int powerGranted) {
		this.powerGranted = powerGranted;
	}

	public String getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public String getCommunicationMode() {
		return communicationMode;
	}

	public void setCommunicationMode(String communicationMode) {
		this.communicationMode = communicationMode;
	}

}
