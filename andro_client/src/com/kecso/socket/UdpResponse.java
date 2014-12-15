package com.kecso.socket;

import java.io.Serializable;

public class UdpResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1814959023274484646L;
	/**
	 * 
	 */
	private float speed;
	private float verticalSpeed;
	private float altitude;
	private float rpm;

	public UdpResponse() {
	}

	public UdpResponse(float speed, float verticalSpeed, float altitude,
			float rpm) {
		this.speed = speed;
		this.verticalSpeed = verticalSpeed;
		this.altitude = altitude;
		this.rpm = rpm;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getVerticalSpeed() {
		return verticalSpeed;
	}

	public void setVerticalSpeed(float verticalSpeed) {
		this.verticalSpeed = verticalSpeed;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	public float getRpm() {
		return rpm;
	}

	public void setRpm(float rpm) {
		this.rpm = rpm;
	}

}
