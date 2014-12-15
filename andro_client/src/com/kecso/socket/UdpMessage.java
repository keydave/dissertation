package com.kecso.socket;

import java.io.Serializable;

public class UdpMessage implements Serializable {

	/**
     *
     */
	static final long serialVersionUID = 140605814607823206L;
	/**
     *
     */
	private Float quatX;
	private Float quatY;
	private Float quatZ;
	private Float quatW;
	private Float throt;
	private boolean yaw;

	public UdpMessage() {
	}

	public UdpMessage(Float quatX, Float quatY, Float quatZ, Float quatW,
			Float throt, boolean yaw) {
		this.quatX = quatX;
		this.quatY = quatY;
		this.quatZ = quatZ;
		this.quatW = quatW;
		this.throt = throt;
		this.yaw = yaw;
	}

	public Float getQuatX() {
		return quatX;
	}

	public void setQuatX(Float quatX) {
		this.quatX = quatX;
	}

	public Float getQuatY() {
		return quatY;
	}

	public void setQuatY(Float quatY) {
		this.quatY = quatY;
	}

	public Float getQuatZ() {
		return quatZ;
	}

	public void setQuatZ(Float quatZ) {
		this.quatZ = quatZ;
	}

	public Float getQuatW() {
		return quatW;
	}

	public void setQuatW(Float quatW) {
		this.quatW = quatW;
	}

	public Float getThrot() {
		return throt;
	}

	public void setThrot(Float throt) {
		this.throt = throt;
	}

	public boolean isYaw() {
		return yaw;
	}

	public void setYaw(boolean yaw) {
		this.yaw = yaw;
	}
}
