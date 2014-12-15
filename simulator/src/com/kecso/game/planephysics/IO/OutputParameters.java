package com.kecso.game.planephysics.IO;

/**
 * A Class of output parameters of the flight physics library
 *
 * @author Jan Kozlovsk� - AI3-P (email: jan.kozlovsky@centrum.cz)
 */
public class OutputParameters {

    private double pitchRate;
    private double yawRate;
    private double rollRate;
    private double speed;
    private double speedX;
    private double speedY;
    private double speedZ;
    private double verticalSpeed;
    private double altitude;
    private double rpm;
    private double trimElevatorAoa;
    private double elevatorAoa;
    private double rudderAoa;
    private double aileronAoa;
    private double maxEnginePower;

    /**
     * @return the pitchRate
     */
    public double getPitchRate() {
        return pitchRate;
    }

    /**
     * @param pitchRate the pitchRate to set
     */
    public void setPitchRate(double pitchRate) {
        this.pitchRate = pitchRate;
    }

    /**
     * @return the yawRate
     */
    public double getYawRate() {
        return yawRate;
    }

    /**
     * @param yawRate the yawRate to set
     */
    public void setYawRate(double yawRate) {
        this.yawRate = yawRate;
    }

    /**
     * @return the rollRate
     */
    public double getRollRate() {
        return rollRate;
    }

    /**
     * @param rollRate the rollRate to set
     */
    public void setRollRate(double rollRate) {
        this.rollRate = rollRate;
    }

    /**
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @return the speedX
     */
    public double getSpeedX() {
        return speedX;
    }

    /**
     * @param speedX the speedX to set
     */
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    /**
     * @return the speedY
     */
    public double getSpeedY() {
        return speedY;
    }

    /**
     * @param speedY the speedY to set
     */
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    /**
     * @return the speedZ
     */
    public double getSpeedZ() {
        return speedZ;
    }

    /**
     * @param speedZ the speedZ to set
     */
    public void setSpeedZ(double speedZ) {
        this.speedZ = speedZ;
    }

    /**
     * @return the verticalSpeed
     */
    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    /**
     * @param verticalSpeed the verticalSpeed to set
     */
    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    /**
     * @return the altitude
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
     * @return the rpm
     */
    public double getRpm() {
        return rpm;
    }

    /**
     * @param rpm the rpm to set
     */
    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    /**
     * @return the trimElevatorAoa
     */
    public double getTrimElevatorAoa() {
        return trimElevatorAoa;
    }

    /**
     * @param trimElevatorAoa the trimElevatorAoa to set
     */
    public void setTrimElevatorAoa(double trimElevatorAoa) {
        this.trimElevatorAoa = trimElevatorAoa;
    }

    /**
     * @return the elevatorAoa
     */
    public double getElevatorAoa() {
        return elevatorAoa;
    }

    /**
     * @param elevatorAoa the elevatorAoa to set
     */
    public void setElevatorAoa(double elevatorAoa) {
        this.elevatorAoa = elevatorAoa;
    }

    /**
     * @return the rudderAoa
     */
    public double getRudderAoa() {
        return rudderAoa;
    }

    /**
     * @param rudderAoa the rudderAoa to set
     */
    public void setRudderAoa(double rudderAoa) {
        this.rudderAoa = rudderAoa;
    }

    /**
     * @return the aileronAoa
     */
    public double getAileronAoa() {
        return aileronAoa;
    }

    /**
     * @param aileronAoa the aileronAoa to set
     */
    public void setAileronAoa(double aileronAoa) {
        this.aileronAoa = aileronAoa;
    }

    /**
     * @return the maxEnginePower
     */
    public double getMaxEnginePower() {
        return maxEnginePower;
    }

    /**
     * @param maxEnginePower the maxEnginePower to set
     */
    public void setMaxEnginePower(double maxEnginePower) {
        this.maxEnginePower = maxEnginePower;
    }

    @Override
    public String toString() {
        return "OutputParameters{" + "pitchRate=" + pitchRate + ", yawRate=" + yawRate + ", rollRate=" + rollRate + ", speed=" + speed + ", speedX=" + speedX + ", speedY=" + speedY + ", speedZ=" + speedZ + ", verticalSpeed=" + verticalSpeed + ", altitude=" + altitude + ", rpm=" + rpm + ", trimElevatorAoa=" + trimElevatorAoa + ", elevatorAoa=" + elevatorAoa + ", rudderAoa=" + rudderAoa + ", aileronAoa=" + aileronAoa + ", maxEnginePower=" + maxEnginePower + '}';
    }
}