package com.kecso.game.planephysics.IO;

/**
 * A Class of input parameters of the flight. It summarizes every possible
 * input, that we can transmit to the AircraftPhysicsModel class.
 *
 * @author Jan Kozlovsk� - AI3-P (email: jan.kozlovsky@centrum.cz)
 */
public class InputParameters {

    /**
     * Delta T. Is the time interval between each method call. The
     * AircraftPhysicsModel was created and tested with the interval 0.05
     * seconds (50 miliseconds).
     */
    private double dt;
    /**
     * A signal, whether the aircraft is ordered by user to pitch down.
     */
    private boolean pitchDown;
    /**
     * A signal, whether the aircraft is ordered by user to pitch up.
     */
    private boolean pitchUp;
    /**
     * A signal, whether the aircraft is ordered by user to roll left.
     */
    private boolean rollLeft;
    /**
     * A signal, whether the aircraft is ordered by user to roll right.
     */
    private boolean rollRight;
    /**
     * A signal, whether the aircraft is ordered by user to yaw left.
     */
    private boolean yawLeft;
    /**
     * A signal, whether the aircraft is ordered by user to yaw right.
     */
    private boolean yawRight;
    /**
     * A signal, whether the aircraft is ordered by user increase the throttle.
     */
    private boolean throttleInc;
    /**
     * A signal, whether the aircraft is ordered by user to decrease the
     * throttle.
     */
    private boolean throttleDec;
    /**
     * A signal, whether the aircraft is ordered by user to trim the elevator
     * up.
     */
    private boolean trimUp;
    /**
     * A signal, whether the aircraft is ordered by user to trim the elevator
     * down.
     */
    private boolean trimDown;
    private Float pitchRate;
    private Float rollRate;
    private Float yawRate;
    private Float throttleRate;
    private GearDir gearDir = GearDir.UP;

    /**
     * Getter for the dt variable. Delta T (dt) is the time interval between
     * each method call. The AircraftPhysicsModel was created and tested with
     * the interval 0.05 seconds (50 miliseconds).
     *
     * @return dt [seconds]
     */
    public double getDt() {
        return dt;
    }

    /**
     * Setter for the dt variable. Delta T (dt) is the time interval between
     * each method call. The AircraftPhysicsModel was created and tested with
     * the interval 0.05 seconds (50 miliseconds).
     *
     * @param dt [seconds]
     */
    public void setDt(double dt) {
        this.dt = dt;
    }

    /**
     * Pitch_down is a signal, whether the aircraft is ordered by user to pitch
     * down.
     *
     * @return pitch_down
     */
    public boolean isPitch_down() {
        return pitchDown;
    }

    /**
     * Pitch_down is a signal, whether the aircraft is ordered by user to pitch
     * down.
     *
     * @param pitch_down
     */
    public void setPitch_down(boolean pitch_down) {
        this.pitchDown = pitch_down;
    }

    /**
     * Pitch_up is a signal, whether the aircraft is ordered by user to pitch
     * up.
     *
     * @return pitch_up
     */
    public boolean isPitch_up() {
        return pitchUp;
    }

    /**
     * Pitch_up is a signal, whether the aircraft is ordered by user to pitch
     * up.
     *
     * @param pitch_up
     */
    public void setPitch_up(boolean pitch_up) {
        this.pitchUp = pitch_up;
    }

    /**
     * Roll_left is a signal, whether the aircraft is ordered by user to roll
     * left.
     *
     * @return roll_left
     */
    public boolean isRoll_left() {
        return rollLeft;
    }

    /**
     * Roll_left is a signal, whether the aircraft is ordered by user to roll
     * left.
     *
     * @param roll_left
     */
    public void setRoll_left(boolean roll_left) {
        this.rollLeft = roll_left;
    }

    /**
     * Roll_right is a signal, whether the aircraft is ordered by user to roll
     * right.
     *
     * @return roll_right
     */
    public boolean isRoll_right() {
        return rollRight;
    }

    /**
     * Roll_right is a signal, whether the aircraft is ordered by user to roll
     * right.
     *
     * @param roll_right
     */
    public void setRoll_right(boolean roll_right) {
        this.rollRight = roll_right;
    }

    /**
     * Yaw_left is a signal, whether the aircraft is ordered by user to yaw
     * left.
     *
     * @return yaw_left
     */
    public boolean isYaw_left() {
        return yawLeft;
    }

    /**
     * Yaw_left is a signal, whether the aircraft is ordered by user to yaw
     * left.
     *
     * @param yaw_left
     */
    public void setYaw_left(boolean yaw_left) {
        this.yawLeft = yaw_left;
    }

    /**
     * Yaw_right is a signal, whether the aircraft is ordered by user to yaw
     * right.
     *
     * @return yaw_right
     */
    public boolean isYaw_right() {
        return yawRight;
    }

    /**
     * Yaw_right is a signal, whether the aircraft is ordered by user to yaw
     * right.
     *
     * @param yaw_right
     */
    public void setYaw_right(boolean yaw_right) {
        this.yawRight = yaw_right;
    }

    /**
     * throttle_inc is a signal, whether the aircraft is ordered by user to
     * increase the throttle.
     *
     * @return throttle_inc
     */
    public boolean isThrottle_inc() {
        return throttleInc;
    }

    /**
     * throttle_inc is a signal, whether the aircraft is ordered by user to
     * increase the throttle.
     *
     * @param throttle_inc
     */
    public void setThrottle_inc(boolean throttle_inc) {
        this.throttleInc = throttle_inc;
    }

    /**
     * throttle_dec is a signal, whether the aircraft is ordered by user to
     * decrease the throttle.
     *
     * @return throttle_dec
     */
    public boolean isThrottle_dec() {
        return throttleDec;
    }

    /**
     * throttle_dec is a signal, whether the aircraft is ordered by user to
     * decrease the throttle.
     *
     * @param throttle_dec
     */
    public void setThrottle_dec(boolean throttle_dec) {
        this.throttleDec = throttle_dec;
    }

    /**
     * Trim_up is a signal, whether the aircraft is ordered by user to trim the
     * elevator up.
     *
     * @return trim_up
     */
    public boolean isTrim_up() {
        return trimUp;
    }

    /**
     * Trim_up is a signal, whether the aircraft is ordered by user to trim the
     * elevator up.
     *
     * @param trim_up
     */
    public void setTrim_up(boolean trim_up) {
        this.trimUp = trim_up;
    }

    /**
     * Trim_down is a signal, whether the aircraft is ordered by user to trim
     * the elevator down.
     *
     * @return trim_down
     */
    public boolean isTrim_down() {
        return trimDown;
    }

    /**
     * Trim_down is a signal, whether the aircraft is ordered by user to trim
     * the elevator down.
     *
     * @param trim_down
     */
    public void setTrim_down(boolean trim_down) {
        this.trimDown = trim_down;
    }

    /**
     * @return the pitchRate
     */
    public Float getPitchRate() {
        return pitchRate;
    }

    /**
     * @param pitchRate the pitchRate to set
     */
    public void setPitchRate(Float pitchRate) {
        this.pitchRate = pitchRate;
    }

    /**
     * @return the rollRate
     */
    public Float getRollRate() {
        return rollRate;
    }

    /**
     * @param rollRate the rollRate to set
     */
    public void setRollRate(Float rollRate) {
        this.rollRate = rollRate;
    }

    /**
     * @return the yawRate
     */
    public Float getYawRate() {
        return yawRate;
    }

    /**
     * @param yawRate the yawRate to set
     */
    public void setYawRate(Float yawRate) {
        this.yawRate = yawRate;
    }

    /**
     * @return the gearDir
     */
    public GearDir getGearDir() {
        return gearDir;
    }

    /**
     * @param gearDir the gearDir to set
     */
    public void setGearDir(GearDir gearDir) {
        this.gearDir = gearDir;
    }

    /**
     * @return the throttleRate [0,100]
     */
    public Float getThrottleRate() {
        return throttleRate;
    }

    /**
     * @param throttleRate the throttleRate to set [0,100]
     */
    public void setThrottleRate(Float throttleRate) {
        this.throttleRate = throttleRate;
    }

    public enum GearDir {

        UP, DOWN
    }
}
