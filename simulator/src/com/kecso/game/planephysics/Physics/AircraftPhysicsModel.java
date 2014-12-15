package com.kecso.game.planephysics.Physics;

import com.kecso.game.planephysics.Aircrafts.Aircraft;
import com.kecso.game.planephysics.IO.InputParameters;
import com.kecso.game.planephysics.IO.OutputParameters;

public class AircraftPhysicsModel {

    public static enum Difficulty {

        /**
         * The most unrealistic model. Secondary effect of yawing and propeller
         * stream is not considered. Automatic elevator trim is enabled
         */
        EASY,
        /**
         * Secondary effect of yawing and propeller stream is not considered.
         * Automatic elevator trim is disabled.
         */
        HARD,
        /**
         * All the forces are counted - secondary effect of yawing, also the
         * propeller stream. Automatic elevator trim is disabled.
         */
        REALISTIC;
    }
    private Difficulty difficulty;
    // 1. Creating claas Aircraft, where the properties are defined 
    private Aircraft aircraft;
    private OutputParameters output;

    /**
     * This class symbolizes simple vector with x,y,z coordinates.
     *
     * @author Jan Kozlovsk� - AI3-P (email: jan.kozlovsky@centrum.cz)
     */
    public static class Vector {

        @SuppressWarnings("javadoc")
        public double x;
        @SuppressWarnings("javadoc")
        public double y;
        @SuppressWarnings("javadoc")
        public double z;
    }
    /**
     * A vector of a gravity force. The coordinates are related to the aircraft
     * frame (pilot's view).
     */
    public Vector gravityVector = new Vector();
    // 2. Creating speed constants to achieve realistic behavior (this shouldnt be used, it is necessary now because 
    // the model doesnt count with moments of rotations) 
    /**
     * A constant for normalizing the gravity vector. If the y coordinate of the
     * vectors azimuth, zenith or norm is larger than this coeficient, the
     * gravity vector is set to the right position (downto, upto, to the left,to
     * the right, to the front, to the rear)
     */
    private double normLimit = 0.998;
    /**
     * A constant for recovery from fall. If an aft goes through the head-first
     * (CZ: "st�emhlav") postition, (Fg vector is pointing to the front, so the
     * transformation matrix gives the result for the "x" coordinate larger than
     * "gettingOutOfFallLimit" coeficient) wings get airstream again and
     * aircraft fly.
     */
    private double gettingOutOfFall_Limit = 0.9;
    /**
     * When speed reaches this limit, the aircraft is falling. Aerodynamic
     * doestn take effect at all, there is only gravitation acting.
     */
    private double gettingIntoFall_SpeedLimit = 10;
    /**
     * When speed reaches speed of gettingIntoFall_SpeedLimit + realisticConst,
     * the aft begins to loose effect of aerodynami forces. The elevator, rudder
     * and alierons are loosing effectivity.
     */
    private double realisticConst = 17;
    /**
     * Auxiliary variable. It symbolizes a ratio, how much the aerodynamic
     * forces take effect on the rotations of an aft, and how much the
     * gravitation force take effect on the rotations of an aft.
     */
    private double fallRatio = 0;
    // 3. Variables of an aircraft
    /**
     * A total speed of an aircraft. [m/s]
     */
    private double speed;
    /**
     * An angle of elevator trim. If it is 0 then the elevator is not trimmed at
     * all. <br />
     * Maximum value is 12�,<br />
     * Minimum value is -12�<br />
     */
    private double trimElevatorAoa;
    /**
     * An altitude of an aircraft. [m]
     */
    private double alt;
    /**
     * Symbolizes the increment of an altitude during the time interval delta T.
     * [m]
     */
    private double deltaAlt = 0;
    /**
     * An actual engine force of the aircraft [N]
     */
    private double engineForce;
    private double engineForceMin = 900;
    /**
     * A wingspan of an aircraft in meters [m]
     */
    private double wingSpan;
    /**
     * A weight of an aircraft. [kg]
     */
    private double weight;
    /**
     * An area of a main aircraft's wing. [m2]
     */
    private double wingArea;
    /**
     * An area of aircraft's rudder. [m2]
     */
    private double rudderArea;
    /**
     * An area of aircraft's elevator. [m2]
     */
    private double elevatorArea;
    /**
     * An area of aircraft's one of two ailerons. [m2]
     */
    private double aileronArea;
    /**
     * A distance of aircraft's point of mass (POM) from aerodynamic center. At
     * aerodynamically stable aircraft the number is > 0.
     */
    private double pomDistance;
    /**
     * A moment of inertia for roll movement (around x axis). The bigger number,
     * the smaller effect the controls have and aircraft is less manoeuvrable.
     * Reduced moment of inertia is counted as sum of (mass * distace from axis
     * of rotation^2).<br />
     * <br />
     * For the library's default aircraft Cessna 172 the rmi_for_roll =
     * weight*2*2.
     */
    private double miForRoll;
    /**
     * A moment of inertia for pitch and yaw movement (around y and z axis). The
     * bigger number, the smaller effect the controls have and aircraft is less
     * manoeuvrable. Reduced moment of inertia is counted as sum of (mass *
     * distace from axis of rotation^2).<br />
     * <br />
     * For the library's default aircraft Cessna 172 the rmi_for_pitch_and_yaw =
     * weight * 1.2 * 1.2.
     */
    private double miForPitchAndYaw;
    /**
     * A maximal engine power in Newtons. We do not take into account a
     * dependency between power and RPM (engine rounds per minute) or
     * altitude.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the max_engine_power =
     * 3500.
     */
    private double maxEnginePower;
    // 4. Angles of attack = AOA
    /**
     * An angle of attack of the main wing. [�]
     */
    private double aoa = 0;
    /**
     * An angle of attack of the rudder. [�]
     */
    private double rudderAoa = 0;
    /**
     * An angle of attack of the ailerons. [�]
     */
    private double aileronAoa = 0;
    /**
     * An angle of attack of the elevator. [�]
     */
    private double elevatorAoa = trimElevatorAoa;
    /**
     * A sideslip angle during sideslip. [�]
     */
    private double sideSlipAngle = 0;
    /**
     * Auxiliary variable. Old angle of attack to count the increment. [�]
     */
    private double oldAoa = 0;
    /**
     * Auxiliary variable. The increment of an angle of attack. [�]
     */
    private double deltaAoa = 0;
    /**
     * Auxiliary variable. Old sideslip angle to count the increment. [�]
     */
    private double oldSideSlipAngle = 0;
    /**
     * Auxiliary variable. The increment of a sideslip angle. [�]
     */
    private double deltaSideslipAngle = 0;
    /**
     * An angle of attack, integer. [�]
     */
    private int intAoa = 0;
    /**
     * An elevator angle of attack, integer. [�]
     */
    private int intElevatorAoa;
    // 5. Pitch, Roll, Yaw angles 
    /**
     * Pitch angle during the time interval delta T. [rad]
     */
    private double theta = 0;
    /**
     * Rolling angle during the time interval delta T. [rad]
     */
    private double fi = 0;
    /**
     * Yawing angle during the time interval delta T. [rad]
     */
    private double psi = 0;
    // 6. Constants of an aircraft and environemt
    // gravitational acceleration, air density
    /**
     * A gravity (gravitation acceleration). We assume that the gravitational
     * acceleration is the same all the time.
     */
    private double g = 9.823;
    /**
     * A force caused by gravity. It is counted as Fg = m * g. [N]
     */
    private double fg;
    /**
     * An air density. [kg/m3]
     */
    private double ro = 1.29;
    /**
     * For counting the forces and moments caused by the wings, a reference
     * point on the wing has to be determined. In this point all the forces take
     * effect:<br />
     * 1. The force of ailerons,<br />
     * 2. The force caused by YAW rotation.<br />
     * <br />
     * It simply means a distance of this reference point from the center of the
     * aircraft [m]. We use it in countings as an arm (M = F * arm).<br />
     * <br />
     * This point is also taken as a reference for counting the circumferential
     * speed of the wing during YAW rotation and as the place where ailerons
     * take place.
     */
    private double wingRadius;
    /**
     * Auxiliary variable. Symbolizes the difference of circumferential speeds
     * of one and second (left and right) wing during YAW rotation. [m/s]
     */
    private double deltaWingSpeed = 0;
    // 6. Forces
    /**
     * The force that makes an aft yaw [N]. It is caused by:<br />
     * 1. The force of rudder,<br />
     * 2. The force of curved airstream from the propeller (only in difficulty
     * model "realistic"). <br />
     */
    private double f_yaw = 0;
    /**
     * The force that makes an aft roll [N]. It is caused by ailerons.
     */
    private double f_roll = 0;
    /**
     * The lift force of an elevator [N]. This force generates an angle of
     * attack of the main wing.
     */
    private double f_elevator = 0;
    /**
     * The drag force of a wing [N]. It is counted as F = cd * 0.5 * ro *
     * wingArea* speed * speed.<br />
     * <br />
     * The "cd" coeficient is a drag coeficient for an actual angle of attack.
     * The coeficient is obtained from the wing polar curve.
     */
    private double f_d = 0;
    /**
     * The lift force of a wing [N]. It is counted as F = cl * 0.5 * ro *
     * wingArea* speed * speed.<br />
     * <br />
     * The "cl" coeficient is a lift coeficient for an actual angle of attack.
     * The coeficient is obtained from the wing polar curve.
     */
    private double f_l = 0;
    /**
     * The drag force of aircraft fuselage [N]. It is counted as F = cd * 0.5 *
     * ro * fuselage_front_area * speed * speed. <br />
     * <br />
     * The fuselage_front_area is a constant that determines an area of the
     * fuselage from the front view. Default value is 1 m2.
     */
    private double fd_fuselage = 0;
    /**
     * The increment of the lift force of the left wing caused by yaw rotation
     * [N].
     */
    private double fl_left_wing = 0;
    /**
     * The increment of the lift force of the right wing caused by yaw rotation
     * [N].
     */
    private double fl_right_wing = 0;
    /**
     * Total difference between lift forces of left and right wing during yaw
     * rotation [N].
     */
    private double f_roll_delta = 0;
    // forces in relation the pilot
    /**
     * Total force on the aft in the x axis direction (x axis of the aircraft).
     * [N]
     */
    private double f_x = 0;
    /**
     * Total force on the aft in the z axis direction (z axis of the aircraft).
     * [N]
     */
    private double f_z = 0;
    /**
     * Total force on the aft in the y axis direction (y axis of the aircraft).
     * [N]
     */
    private double f_y = 0;
    // forces of gravitation in relation to pilot
    /**
     * The Fg force of the aft is decomposed into x, y and z axis of an aft.
     * <br /> <br />
     * f_g_x [N] is a component of vector Fg acting in the direction of
     * aircraft's x axis.
     */
    private double f_g_x = 0;
    /**
     * The Fg force of the aft is decomposed into x, y and z axis of an aft.
     * <br /> <br />
     * f_g_y [N] is a component of vector Fg acting in the direction of
     * aircraft's y axis.
     */
    private double f_g_y = 0;
    /**
     * The Fg force of the aft is decomposed into x, y and z axis of an aft.
     * <br /> <br />
     * f_g_z [N] is a component of vector Fg acting in the direction of
     * aircraft's z axis.
     */
    private double f_g_z = 0;
    // 7. Speeds and accelerations
    /**
     * The speed of an aircraft in the direction of it's x axis. [m/s] <br />
     * <br />
     * In the normal flight, speedX is very similar to the total speed. The
     * difference is when the aircraft flies under a certain angle of attack or
     * sideslip angle. Then speedX = cos(sideslipAngle) * cos(angleOfAttack) *
     * speed;
     */
    private double speedX = 0;
    /**
     * The speed of an aircraft in the direction of it's x axis. [m/s] <br />
     * <br />
     * In the normal flight, speedY is zero. The difference is when the aircraft
     * flies under a certain sideslip angle. Then speedY = sin(sideslipAngle) *
     * speed;
     */
    private double speedY = 0;
    /**
     * The speed of an aircraft in the direction of it's z axis. [m/s] <br />
     * <br />
     * In the normal flight, speedZ is zero. The difference is when the aircraft
     * flies under a certain angle of attack. Then speedZ = sin(angleOfAttack) *
     * speed;
     */
    private double speedZ = 0;
    /**
     * Vertical speed of an aft [m/s]. It is counted for measuring the altitude.
     * Also it can be used for a vario (vertical speed indicator), which is one
     * of the instruments in the cockpit.
     */
    private double verticalSpeed = 0;
    /**
     * An increment of speed of an aft in the direction of aft's x axis. [m/s]
     */
    private double delta_v_x = 0;
    /**
     * An increment of speed of an aft in the direction of aft's z axis. [m/s]
     */
    private double delta_v_z = 0;
    /**
     * An increment of speed of an aft in the direction of aft's y axis. [m/s]
     */
    private double delta_v_y = 0;
    /**
     * An increment of an aft in the direction of aft's x axis. [m/s2]
     */
    private double a_x = 0;
    /**
     * An acceleration of an aft in the direction of aft's z axis. [m/s2]
     */
    private double a_z = 0;
    /**
     * An acceleration of an aft in the direction of aft's y axis. [m/s2]
     */
    private double a_y = 0;
    /**
     * An angular acceleration of an aft in the roll rotation (around x axis).
     * [rad/s2]
     */
    private double a_roll = 0;
    /**
     * An angular acceleration of an aft in the pitch rotation (around y axis)
     * during fall. [rad/s2]
     */
    private double pitchAcceleration_fall = 0;
    /**
     * An increment of an angular speed of an aft in the pitch rotation (around
     * y axis) during fall. [rad/s] <br /> <br />
     * It is counted as pitchDeltaSpeed_fall = pitchAcceleration_fall * deltaT.
     */
    private double pitchDeltaSpeed_fall = 0;
    /**
     * An angular speed of pitch rotation during fall. [rad/s]
     */
    private double pitchSpeed_fall = 0;
    /**
     * An angular acceleration of an aft in the yaw rotation (around z axis)
     * during fall. [rad/s2]
     */
    private double yawAcceleration_fall = 0;
    /**
     * An increment of an angular speed of an aft in the yaw rotation (around z
     * axis) during fall. [rad/s] <br /> <br />
     * It is counted as yawDeltaSpeed_fall = yawAcceleration_fall * deltaT.
     */
    private double yawDeltaSpeed_fall = 0;
    /**
     * An angular speed of yaw rotation during fall. [rad/s]
     */
    private double yawSpeed_fall = 0;
    /**
     * If the aircraft is falling then fall = true.<br />
     * <br />
     * Aircraft is falling if speedX is smaller then gettingIntoFall_SpeedLimit.
     */
    private boolean fall = false;
    /**
     * Auxiliary variable. If the aircraft is getting out of fall. This variable
     * ensures that the recover from fall into normal flight is smooth. <br />
     * <br />
     * An aft gets from fall if it goes through the head-first (CZ: "st�emhlav")
     * position (f_g_x / fg > gettingOutOfFall_Limit).
     */
    private boolean from_fall = false;
    /**
     * Sets the automatic trim function on or off. If easy model of difficulty
     * is selected, automatic trim function is turned on.
     */
    private boolean automaticTrim = false;
    // 8. Coeficients of lift and drag
    /**
     * A drag coeficient of the main wing. It is obtained by an interpolation of
     * cd1 and cd2 coeficients. [-]
     */
    private double cd = 0;
    /**
     * A drag coeficient of the main wing, the first one for the interpolation.
     * It is obtained from aerodynamic polar. [-]
     */
    private double cd1 = 0;
    /**
     * A drag coeficient of the main wing, the second one for the interpolation.
     * It is obtained from aerodynamic polar. [-]
     */
    private double cd2 = 0;
    /**
     * A lift coeficient of the main wing, the first one for the interpolation.
     * It is obtained from aerodynamic polar. [-]
     */
    private double cl1 = 0;
    /**
     * A lift coeficient of the main wing, the second one for the interpolation.
     * It is obtained from aerodynamic polar. [-]
     */
    private double cl2 = 0;
    /**
     * A lift coeficient of the main wing. It is obtained by an interpolation of
     * cl1 and cl2 coeficients. [-]
     */
    private double cl = 0;
    /**
     * A lift coeficient of the rudder. It is obtained from aerodynamic polar of
     * the rudder. [-]
     */
    private double cRudder = 0;
    /**
     * A lift coeficient of the aileron. It is obtained from aerodynamic polar
     * of the aileron. [-]
     */
    private double cAileron = 0;
    /**
     * A lift coeficient of the elevator. It is obtained by an interpolation of
     * cElevator1 and cElevator2 coeficients. [-]
     */
    private double cElevator = 0;
    /**
     * A lift coeficient of the elevator. The first one for the interpolation.
     * It is obtained from aerodynamic polar. [-]
     */
    private double cElevator1 = 0;
    /**
     * A lift coeficient of the elevator. The second one for the interpolation.
     * It is obtained from aerodynamic polar. [-]
     */
    private double cElevator2 = 0;
    // 9. Variables for optimalization
    /**
     * A variable stands for: 0.5 * ro * wingArea <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_wingArea;
    /**
     * A variable stands for: 0.5 * ro * wingArea * speed * speed <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_wingArea_v_v;
    /**
     * A variable stands for: 0.5 * ro * rudderArea <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_rudderArea;
    /**
     * A variable stands for: 0.5 * ro * elevatorArea <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_elevatorArea;
    /**
     * A variable stands for: speed * speed <br />
     * and is used for counting aerodynamic forces.
     */
    private double v_v;
    /**
     * A variable stands for: 0.5 * ro * rudderArea * speed * speed <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_rudderArea_v_v;
    /**
     * A variable stands for: 0.5 * ro * elevatorArea * speed * speed <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_elevatorArea_v_v;
    /**
     * A variable stands for: 0.5 * ro * aileronArea <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_aileronArea;
    /**
     * A variable stands for: 0.5 * ro * aileronArea * speed * speed <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_aileronArea_v_v = 0;
    /**
     * A variable stands for the function: sin(theta). <br />
     * Theta is a pitch angle during the time delta T [rad]. This variable is
     * used in transformation matrix.
     */
    private double sin_theta;
    /**
     * A variable stands for the function: cos(theta). <br />
     * Theta is a pitch angle during the time delta T [rad]. This variable is
     * used in transformation matrix.
     */
    private double cos_theta;
    /**
     * A variable stands for the function: sin(fi). <br />
     * Fi is a roll angle during the time delta T [rad]. This variable is used
     * in transformation matrix.
     */
    private double sin_fi;
    /**
     * A variable stands for the function: cos(fi). <br />
     * Fi is a roll angle during the time delta T [rad]. This variable is used
     * in transformation matrix.
     */
    private double cos_fi;
    /**
     * A variable stands for the function: sin(aoa). <br />
     * Aoa is an angle of attack during the time delta T [rad]. This variable is
     * used for calculating speedZ.
     */
    private double sin_aoa;
    /**
     * A variable stands for the function: cos(aoa). <br />
     * Aoa is an angle of attack during the time delta T [rad]. This variable is
     * used for calculating speedX.
     */
    private double cos_aoa;
    /**
     * A variable stands for the function: sin(sideslipAngle). <br />
     * SideslipAngle is an angle of a side slip [rad]. This variable is used for
     * calculating speedY.
     */
    private double sin_sideslip_angle;
    /**
     * A variable stands for the function: cos(sideslipAngle). <br />
     * SideslipAngle is an angle of a side slip [rad]. This variable is used for
     * calculating speedX.
     */
    private double cos_sideslip_angle;
    /**
     * A variable stands for the function: sin(psi). <br />
     * Psi is a yaw angle during the time delta T [rad]. This variable is used
     * in transformation matrix.
     */
    private double sin_psi;
    /**
     * A variable stands for the function: cos(psi). <br />
     * Psi is a yaw angle during the time delta T [rad]. This variable is used
     * in transformation matrix.
     */
    private double cos_psi;
    /**
     * A variable stands for: 0.5 * ro * fuselageArea <br />
     * and is used for counting aerodynamic forces.
     */
    private double half_ro_fuselageArea;
    /**
     * A variable stands for: 0.5 * ro * oneWingArea <br />
     * and is used for counting aerodynamic forces.<br />
     * <br />
     * OneWingArea is the area of one wing, so wingArea / 2 [m2].
     */
    private double half_ro_oneWingArea;
    /**
     * Delta T. Is the time interval between each method call. The main method
     * is doComputations(InputParameters input).
     */
    private double dt = 0;
    /**
     * This coeficient was guessed experimentally by testing the behavior of the
     * model. It is used to transform the force of an elevator into the angle of
     * attack of the main wing. <br />
     * <br />
     * If You are going to create Your own aircraft with the different
     * properties, please be advised that You will also probably have to change
     * this coeficient. The smaller the number is, the more sensitive the plane
     * is (for the same elevator force will be bigger angle of attack).
     */
    private double elevatorForceIntoAoa_Coefficient = 100;
    /**
     * This coeficient was guessed experimentally by testing the behavior of the
     * model. It is used to transform the force of a rudder into the sideslip
     * angle. <br />
     * <br />
     * If You are going to create Your own aircraft with the different
     * properties, please be advised that You will also probably have to change
     * this coeficient. The smaller the number is, the more sensitive the plane
     * is (for the same rudder force will be bigger sideslip angle).
     */
    private double rudderForceIntoSideslipAngle_Coefficient = 450;
    private double analogControlLowerLimitLow = -0.05;
    private double analogControlUpperLimitLow = 0.05;
    private double analogControlLowerLimitUp = -1;
    private double analogControlUpperLimitUp = 1;

    /**
     * This is a main method for AircraftPhysicsModel. This method counts the
     * physics of an aft once in the time interval delta T (dt).<br />
     * <br />
     * Every forces, accelerations and vectors are counted in relative to aft's
     * frame (pilot's view).
     *
     * @param input {@link InputParameters} instance
     * @return {@link OutputParameters} output
     */
    public OutputParameters doComputations(InputParameters input) {
        dt = input.getDt(); // the time in miliseconds
        Float pitchRate = input.getPitchRate();
        Float rollRate = input.getRollRate();
        Float yawRate = input.getYawRate();


        if (pitchRate == null) {
            if (input.isPitch_down()) {
                if (elevatorAoa > -20) {
                    elevatorAoa -= 0.5;
                }
            }
            if (input.isPitch_up()) {
                if (elevatorAoa < 20) {
                    elevatorAoa += 0.5;
                }
            }
        } else {
            if (pitchRate > 0 && pitchRate >= analogControlUpperLimitLow && elevatorAoa < 20) {
                elevatorAoa += (pitchRate > analogControlUpperLimitUp) ? 0.5 : pitchRate/2f;
            }
            if (pitchRate < 0 && pitchRate <= analogControlLowerLimitLow && elevatorAoa > -20) {
                elevatorAoa += (pitchRate < analogControlLowerLimitUp) ? (-0.5) : pitchRate/2f;
            }
        }
        if (rollRate == null) {
            if (input.isRoll_left()) {
                if (aileronAoa < 21 && f_roll < 3000) {
                    aileronAoa += 1;
                }
            }
            if (input.isRoll_right()) {
                if (aileronAoa > -21 && f_roll > -3000) {
                    aileronAoa -= 1;
                }
            }
        } else {
            if (rollRate > 0 && rollRate >= analogControlUpperLimitLow && aileronAoa < 21 && f_roll < 3000) {
                aileronAoa += rollRate > analogControlUpperLimitUp ? 1 : rollRate;
            }
            if (rollRate < 0 && rollRate <= analogControlLowerLimitLow && aileronAoa > -21 && f_roll > -3000) {
                aileronAoa += rollRate < analogControlLowerLimitUp ? - 1 : rollRate;
            }
        }
        if (yawRate == null) {
            if (input.isYaw_left()) {
                if (rudderAoa < 15) {
                    rudderAoa += 1;
                }
            }
            if (input.isYaw_right()) {
                if (rudderAoa > -15) {
                    rudderAoa -= 1;
                }
            }
        } else {
            if (yawRate > 0 && yawRate >= analogControlUpperLimitLow && rudderAoa < 15) {
                rudderAoa += yawRate > analogControlUpperLimitUp ? 1 : yawRate;
            }
            if (yawRate < 0 && yawRate <= analogControlLowerLimitLow && rudderAoa > -15) {
                rudderAoa += yawRate < analogControlLowerLimitUp ? - 1 : yawRate;
            }
        }

        if ((pitchRate != null && pitchRate > analogControlLowerLimitLow && pitchRate < analogControlUpperLimitLow) || (pitchRate == null && (!input.isPitch_down()) && (!input.isPitch_up()))) {
            if (elevatorAoa > trimElevatorAoa) {
                if (elevatorAoa - trimElevatorAoa < 0.1) {
                    elevatorAoa = trimElevatorAoa;
                } else {
                    if (elevatorAoa - trimElevatorAoa < 0.5) {
                        elevatorAoa = elevatorAoa - 0.1;
                    } else {
                        elevatorAoa = elevatorAoa - 0.5;
                    }
                }
            }
            if (elevatorAoa < trimElevatorAoa) {
                if (trimElevatorAoa - elevatorAoa < 0.1) {
                    elevatorAoa = trimElevatorAoa;
                } else {
                    if (trimElevatorAoa - elevatorAoa < 0.5) {
                        elevatorAoa = elevatorAoa + 0.1;
                    } else {
                        elevatorAoa = elevatorAoa + 0.5;
                    }
                }
            }

            if (automaticTrim) {
                if (elevatorAoa == trimElevatorAoa) {
                    if (theta > 0.0003) {
                        if (trimElevatorAoa > -12) {

                            if (theta > 0.003) {
                                trimElevatorAoa = trimElevatorAoa - 0.15;
                                //System.out.println("true");
                            }
                            trimElevatorAoa = trimElevatorAoa - 0.05;
                        }
                    }
                    if (theta < -0.0003) {
                        if (trimElevatorAoa < 12) {

                            if (theta < -0.003) {
                                trimElevatorAoa = trimElevatorAoa + 0.15;
                                //System.out.println("true");
                            }
                            trimElevatorAoa = trimElevatorAoa + 0.05;
                        }
                    }
                }
            }
        }

        if ((yawRate != null && yawRate > analogControlLowerLimitLow && yawRate < analogControlUpperLimitLow) || ((yawRate == null && !input.isYaw_right()) && (!input.isYaw_left()))) {
            if (rudderAoa > 0) {
                rudderAoa = rudderAoa - 1;
            }
            if (rudderAoa < 0) {
                rudderAoa = rudderAoa + 1;
            }
        }
        if ((rollRate != null && rollRate > analogControlLowerLimitLow && rollRate < analogControlUpperLimitLow) || ((rollRate == null && !input.isRoll_left()) && (!input.isRoll_right()))) {
            if (aileronAoa > 0) {
                aileronAoa = aileronAoa - 1;
            }
            if (aileronAoa < 0) {
                aileronAoa = aileronAoa + 1;
            }
        }
        if (input.getThrottleRate() == null) {
            if (input.isThrottle_inc()) {
                if (engineForce < maxEnginePower) {
                    engineForce = engineForce + 50;
                } else {
                    engineForce = maxEnginePower;
                }

            }
            if (input.isThrottle_dec()) {
                if (engineForce > engineForceMin) {
                    engineForce = engineForce - 50;
                } else {
                    engineForce = engineForceMin;
                }
            }
        } else {
            engineForce = engineForceMin + (maxEnginePower - engineForceMin) * (input.getThrottleRate() / 100F);
        }
        if (input.isTrim_up()) {
            if (automaticTrim) {
            } else {
                if (trimElevatorAoa < 12) {
                    trimElevatorAoa = trimElevatorAoa + 0.1;
                }
            }
        }
        if (input.isTrim_down()) {
            if (automaticTrim) {
            } else {
                if (trimElevatorAoa > -12) {
                    trimElevatorAoa = trimElevatorAoa - 0.1;
                }
            }
        }

        v_v = speed * speed;
        half_ro_wingArea_v_v = half_ro_wingArea * v_v;
        half_ro_rudderArea_v_v = half_ro_rudderArea * v_v;
        half_ro_elevatorArea_v_v = half_ro_elevatorArea * v_v;
        half_ro_aileronArea_v_v = half_ro_aileronArea * v_v;
        // --------------------------------------------------------------------
        // Getting elevator coeficient ----------------------------------------
        if (elevatorAoa < 0) {
            // cElevator interpolation
            intElevatorAoa = (int) (elevatorAoa + 0.0000001);
            cElevator1 = -aircraft.getCElevator(-intElevatorAoa);
            cElevator2 = -aircraft.getCElevator(-(intElevatorAoa - 1));
            cElevator = cElevator1 - (cElevator2 - cElevator1) * (elevatorAoa - intElevatorAoa);
        } else {
            // cElevator interpolation
            intElevatorAoa = (int) (elevatorAoa - 0.0000001);
            cElevator1 = aircraft.getCElevator(intElevatorAoa);
            cElevator2 = aircraft.getCElevator(intElevatorAoa + 1);
            cElevator = cElevator1 + (cElevator2 - cElevator1) * (elevatorAoa - intElevatorAoa);
        }
        f_elevator = half_ro_elevatorArea_v_v * cElevator;

        // Getting the main AOA coeficient from the force of elevator---------
        aoa = f_elevator / elevatorForceIntoAoa_Coefficient;
        if (aoa > 22.9) {
            aoa = 22.9;
        }
        if (aoa < -22.9) {
            aoa = -22.9;
        }
        intAoa = (int) aoa;

        if (aoa < 0) {
            // cx and cy interpolation
            // Note: (do budoucna mohu napsat funkci, ktera po zadani nekolika vrcholovych bodu dokaze
            // interpolovat celou polaru a ulozit do trojrozmerneho pole - namisto rucniho vypisovani)
            cd1 = aircraft.getNegativeCx((int) intAoa);
            cd2 = aircraft.getNegativeCx((int) intAoa - 1);
            cl1 = aircraft.getNegativeCy((int) intAoa);
            cl2 = aircraft.getNegativeCy((int) intAoa - 1);
            cd = cd1 + (cd2 - cd1) * (aoa - intAoa);
            cl = cl1 + (cl2 - cl1) * (aoa - intAoa);
        } else {
            cd1 = aircraft.getPositiveCx((int) intAoa);
            cd2 = aircraft.getPositiveCx((int) intAoa + 1);
            cl1 = aircraft.getPositiveCy((int) intAoa);
            cl2 = aircraft.getPositiveCy((int) intAoa + 1);
            cd = cd1 + (cd2 - cd1) * (aoa - intAoa);
            cl = cl1 + (cl2 - cl1) * (aoa - intAoa);
        }
        // -------------------------------------------------------------------
        // Getting the rudder coeficient and force----------------------------
        if (rudderAoa < 0) {
            cRudder = -aircraft.getCRudder(-(int) rudderAoa);
        } else {
            cRudder = aircraft.getCRudder((int) rudderAoa);
        }
        if (difficulty == Difficulty.REALISTIC) {
            // the force caused by rudder and the stream of air from the propeller
            f_yaw = 2 * half_ro_rudderArea_v_v * cRudder + engineForce / 3;
        }
        if (difficulty == Difficulty.HARD) {
            // the force caused by rudder
            f_yaw = 2 * half_ro_rudderArea_v_v * cRudder;
        }
        if (difficulty == Difficulty.EASY) {
            // the force caused by rudder
            f_yaw = 2 * half_ro_rudderArea_v_v * cRudder;
        }
        // -------------------------------------------------------------------
        // Getting aileron coeficient and force-------------------------------
        if (aileronAoa < 0) {
            cAileron = -aircraft.getCAileron(-(int) aileronAoa);
        } else {
            cAileron = aircraft.getCAileron((int) aileronAoa);
        }

        // Transformation matrix for transforming Fg force into the aircraft's frame
        gravityVector = transformGravityVector(theta, fi, psi);
        f_g_x = gravityVector.x;
        f_g_y = gravityVector.y; // na pravem uchu je fgy < 0
        f_g_z = gravityVector.z;

        // counting forces related to pilot, if we fall, there is nothing to count
        if (fall == false) {
            // counting powers Drag and Lift
            f_d = cd * half_ro_wingArea_v_v;
            fd_fuselage = 0.5 * half_ro_fuselageArea * v_v;
            f_l = cl * half_ro_wingArea_v_v;

            f_x = engineForce - f_d - fd_fuselage + f_g_x;
            f_y = f_g_y + f_yaw;
            f_z = f_l - f_g_z;

            // accelerations
            a_x = f_x / weight;
            a_y = f_y / weight;
            a_z = f_z / weight;

            // speed rates of change
            delta_v_x = a_x * dt;
            delta_v_y = a_y * dt;
            delta_v_z = a_z * dt;
        }

        // flying
        if (((speed + delta_v_x <= gettingIntoFall_SpeedLimit) && (f_g_x / fg < 0)) || fall) { // f_g_x / fg < 0 cumak je nad horizontem
			/*
             * if aircraft is in vertical position and speed is near zero it can
             * be taken also as "fall". pitch_acceleration_fall is angular
             * acceleration of aircraft during fall
             */
            fall = true;

            if (f_g_z == 0) { // the plane would stuck in the air, we dont want this
                f_g_z = -0.001;
            }

            pitchAcceleration_fall = (f_g_z * pomDistance) / miForPitchAndYaw;
            pitchDeltaSpeed_fall = pitchAcceleration_fall * dt / 10;
            pitchSpeed_fall = pitchSpeed_fall - pitchDeltaSpeed_fall;
            yawAcceleration_fall = (f_g_y * pomDistance) / miForPitchAndYaw;
            yawDeltaSpeed_fall = yawAcceleration_fall * dt / 10;
            yawSpeed_fall = yawSpeed_fall + yawDeltaSpeed_fall;

            psi = yawSpeed_fall;
            fi = 0;
            theta = pitchSpeed_fall;

            if (speed > 0) {
                // If the speed is smaller than gettingIntoFall_SpeedLimit but I am still climbing by some minimal speed that is > 0.

                //System.out.println("fall, speed > 0");
                speed = speed - g * dt;
                speedX = speed * Math.cos(Math.toRadians(aoa)) * Math.cos(Math.toRadians(sideSlipAngle));
                speedY = -speed * Math.sin(Math.toRadians(sideSlipAngle));
                speedZ = -speed * Math.sin(Math.toRadians(aoa)); // = almost 0

                verticalSpeed = -(f_g_x / fg) * cos_aoa * cos_sideslip_angle * speed; // [m/s]
                deltaAlt = verticalSpeed * dt; // [m]
                alt = alt + deltaAlt; // [m]

            } else {
                // If the plane is really falling already, it is moving only in the direction of gravitation force.

                //System.out.println("fall, speed < 0");
                speed = speed - g * dt;
                speedX = -speed * f_g_x / fg;
                speedY = -speed * f_g_y / fg;
                speedZ = speed * f_g_z / fg;

                verticalSpeed = speed; // [m/s]
                deltaAlt = verticalSpeed * dt; // [m]
                alt = alt + deltaAlt; // [m]
            }

            // If the aft goes through the head-first (CZ: "st�emhlav") position, the wings get airstreem again and we recover from fall.
            if ((f_g_x / fg) > gettingOutOfFall_Limit) {
                fall = false;
                from_fall = true;
                speed = -speed;
            }

        } else {
            /*
             * normal state when we dont fall, speed is more than 36 km/h and
             * aircraft is not in vertical position
             */

            // counting angle of attack and slideslip angle for rotation
            deltaAoa = aoa - oldAoa;
            oldAoa = aoa;
            sideSlipAngle = f_yaw / rudderForceIntoSideslipAngle_Coefficient;
            deltaSideslipAngle = sideSlipAngle - oldSideSlipAngle;
            oldSideSlipAngle = sideSlipAngle;

            speed = speed + delta_v_x;
            if (speed == 0) { // we dont wanna divide by zero
                speed = -0.0001;
            }

            psi = Math.atan(delta_v_y / speed)
                    + Math.toRadians(deltaSideslipAngle);
            theta = Math.atan(delta_v_z / speed) + Math.toRadians(deltaAoa);

            if ((speed < realisticConst + gettingIntoFall_SpeedLimit) && (from_fall == false)) {
                /*
                 * if speed is less than realisticConst + getting_into_fall_limit [m/s] then the auto rotating effect is loosing 
                 * the strength. We need to loose efect completely when the speed = getting_into_fall_limit. 
                 * 
                 * Here the aerodynamic forces slowly disappears and the plane is moved only by the gravitation force.
                 */
                pitchAcceleration_fall = (f_g_z * pomDistance) / miForPitchAndYaw;
                pitchDeltaSpeed_fall = pitchAcceleration_fall * dt / 10;
                pitchSpeed_fall = pitchSpeed_fall - pitchDeltaSpeed_fall;
                yawAcceleration_fall = (f_g_y * pomDistance) / miForPitchAndYaw;
                yawDeltaSpeed_fall = yawAcceleration_fall * dt / 10;
                yawSpeed_fall = yawSpeed_fall + yawDeltaSpeed_fall;

                fallRatio = (realisticConst + gettingIntoFall_SpeedLimit - speed) * (realisticConst + gettingIntoFall_SpeedLimit - speed) / (realisticConst * realisticConst);
                psi = psi * (1 - fallRatio) + yawSpeed_fall * fallRatio;
                theta = theta * (1 - fallRatio) + pitchSpeed_fall * fallRatio;

                //System.out.println("pre-fall, speed > 0" + speed);
            } else {
                pitchSpeed_fall = pitchSpeed_fall * 0.8;
                yawSpeed_fall = yawSpeed_fall * 0.8;

                if (from_fall) {
                    psi = psi + yawSpeed_fall;
                    theta = theta + pitchSpeed_fall;

                    //System.out.println("from-fall, speed > 0");
                }

                if ((Math.abs(pitchSpeed_fall) < 0.0001) && (Math.abs(yawSpeed_fall) < 0.0001)) {
                    from_fall = false;
                    pitchSpeed_fall = 0;
                    yawSpeed_fall = 0;

                    //System.out.println("we got off fall completely, speed > 0");
                }
            }

            /*
             * Counting the ROLL movement plus efect caused by YAW movement:
             * ------------------------------------------------------------
             * psi*(1/dt) is transforming angular speed to [rad/s], because all rates are in [rad/dt] (because of the time interval).
             * We gets the distance that one wing travels during the dt interval during YAW rotation. It is also a speed [m / one second].
             * We add this speed to the speed of one wing, and subtract from the speed of another wing. 
             */
            // If yaw angle is 0 we dont have to count this
            if (psi == 0) {
                f_roll_delta = 0;
            } else {
                if (difficulty == Difficulty.REALISTIC) {
                    // jakekoli cislo nazacatku (0,25) je koeficient pro
                    // realisticnost
                    deltaWingSpeed = wingRadius * psi * (1 / dt);
                    // jako soucinitel vztlaku pouzijeme druhy z pol�ry cca 0.1,
                    // pokud vezmeme aktu�ln� podle �hlu n�b�hu, chov� se nere�ln�
                    // (p�ehnan�)
                    fl_left_wing = (cl / 1.7) * half_ro_oneWingArea
                            * (speed - deltaWingSpeed) * (speed - deltaWingSpeed);
                    fl_right_wing = (cl / 1.7) * half_ro_oneWingArea
                            * (speed + deltaWingSpeed) * (speed + deltaWingSpeed);
                    f_roll_delta = fl_right_wing - fl_left_wing;
                }
                if (difficulty == Difficulty.HARD) {
                    f_roll_delta = 0;
                }
                if (difficulty == Difficulty.EASY) {
                    f_roll_delta = 0;
                }
            }
            // sila vznikl� rollem, toto je s�la jednoho k��dla kter� p�sob� na
            // rameno r - na k��dlo. f_roll_delta je s�la vznikaj�c� z pohybu
            // yaw. 0.4 na zacatku je koeficient realisticnosti
            f_roll = 0.4 * (2 * half_ro_aileronArea_v_v * cAileron + f_roll_delta);
            // a_roll is an angular acceleration
            a_roll = (f_roll * wingRadius) / miForRoll;
            fi = a_roll * dt;

            speed = Math.sqrt(speed * speed + delta_v_z * delta_v_z + delta_v_y
                    * delta_v_y); // vypocteni celkove rychlosti

            // speed counting
            sin_aoa = Math.sin(Math.toRadians(aoa));
            cos_aoa = Math.cos(Math.toRadians(aoa));
            sin_sideslip_angle = Math.sin(Math.toRadians(sideSlipAngle));
            cos_sideslip_angle = Math.cos(Math.toRadians(sideSlipAngle));

            speedX = speed * cos_aoa * cos_sideslip_angle;
            speedY = -speed * sin_sideslip_angle;
            speedZ = -speed * sin_aoa;

            verticalSpeed = -f_g_x / fg * speedX + f_g_z / fg * speedZ
                    - f_g_y / fg * speedY; // [m/s]
            deltaAlt = verticalSpeed * dt; // [m]
            alt = alt + deltaAlt; // [m]

        }

        output = new OutputParameters();

        output.setPitchRate(theta);
        output.setRollRate(fi);
        output.setYawRate(-psi);
        output.setAltitude(alt);
        output.setVerticalSpeed(verticalSpeed);
        output.setRpm(engineForce);
        output.setSpeed(speed*5);
        output.setSpeedX(speedX);
        output.setSpeedY(speedY);
        output.setSpeedZ(speedZ);
        output.setTrimElevatorAoa(trimElevatorAoa);
        output.setAileronAoa(aileronAoa);
        output.setElevatorAoa(elevatorAoa);
        output.setRudderAoa(rudderAoa);
        output.setMaxEnginePower(maxEnginePower);
        return output;
    }

    /**
     * The constructor of AircraftPhysicsModel with parameters: <br />
     * Aircraft aircraft - the aft model with its properties. <br />
     * Difficulty: EASY, HARD, REALISTIC.
     *
     * @param aircraft {@link Aircraft} instance
     * @param difficulty {@link Difficulty} type
     */
    public AircraftPhysicsModel(Aircraft aircraft, Difficulty difficulty) {
        this.aircraft = aircraft;
        this.difficulty = difficulty;
        if (this.difficulty == Difficulty.EASY) {
            this.automaticTrim = true;
        }

        this.wingSpan = aircraft.getWingSpan();
        this.weight = aircraft.getWeight();
        this.wingArea = aircraft.getWingArea();
        this.rudderArea = aircraft.getRudderArea();
        this.elevatorArea = aircraft.getElevatorArea();
        this.aileronArea = aircraft.getAileronArea();
        this.pomDistance = aircraft.getpOM_distance();
        this.miForRoll = aircraft.getMi_for_roll();
        this.miForPitchAndYaw = aircraft.getMi_for_pitch();
        this.maxEnginePower = aircraft.getMax_engine_power();

        //counting auxiliary variables 
        this.fg = weight * g;
        this.wingRadius = wingSpan * 2.7 / 8;

        this.half_ro_wingArea = 0.5 * ro * wingArea;
        this.half_ro_rudderArea = 0.5 * ro * rudderArea;
        this.half_ro_elevatorArea = 0.5 * ro * elevatorArea;
        this.half_ro_aileronArea = 0.5 * ro * aileronArea;
        this.half_ro_fuselageArea = 0.5 * ro * 1;
        this.half_ro_oneWingArea = 0.5 * ro * wingArea * 0.5;
        this.f_g_z = fg;

        //initial values
        this.speed = 45;
        this.trimElevatorAoa = 3;
        this.alt = 50;
        this.engineForce = 2000;
        this.elevatorAoa = trimElevatorAoa;
    }

    /**
     * This method resets the gravity vector. The direction of the gravity
     * vector is usually changed a little bit after long flight. It is caused by
     * the mistake that comes with sin and cos functions in the transformation
     * matrix.
     *
     * @param azimuth - It is x axis of the aircraft
     * @param zenith - It is z axis of the aircraft
     * @param norm - It is y axis of the aircraft
     */
    public void normalizeGravity(Vector azimuth, Vector zenith, Vector norm) {

        if (speed > 20) {
            if ((zenith.y < -0.9999)) {
                f_g_x = 0;
                f_g_y = 0;
                f_g_z = fg;  // we do not do this because of vertical speed indicator
                //System.out.println("jsem v norm�lu"); 

            }
            if (zenith.y > normLimit) {
                f_g_x = 0;
                f_g_y = 0;
                f_g_z = -fg;    // we do not do this because of vertical speed indicator
                //System.out.println("jsem na z�dech"); 

            }
            if (norm.y < -normLimit) { // na pravem uchu je norm.y < 0
                f_g_x = 0;
                f_g_y = -fg;
                f_g_z = 0;
                //System.out.println("na prav�m uchu");
            }
            if (norm.y > normLimit) {
                f_g_x = 0;
                f_g_y = fg;
                f_g_z = 0;
                //System.out.println("na lev�m uchu");
            }
            if (azimuth.y < -normLimit) {
                // nastav�me kvuli p�du po ocase, abychom se nezasekli
                f_g_x = -fg * 0.9998; // cos(0,5�) = 0.99996
                f_g_y = 0;
                f_g_z = fg * 0.00872; // sin (0,5�) = 0.00872
                //System.out.println("stoup�m po vertik�le");
            }
            if (azimuth.y > normLimit) {
                f_g_x = fg;
                f_g_y = 0;
                f_g_z = 0;
                //System.out.println("kles�m st�emhlav");
            }
        }
    }

    /**
     * This method transforms the vector of gravity from the old position to the
     * new one. The position is changed due to theta, fi and psi angles.
     *
     * @param theta - pitch angle during the time interval delta T [rad].
     * @param fi - roll angle during the time interval delta T [rad].
     * @param psi - yaw angle during the time interval delta T [rad].
     * @return Vector vector - is new gravity vector transformed into the new
     * direction by theta, fi and psi angles.
     */
    private Vector transformGravityVector(double theta, double fi, double psi) {
        Vector transformedGravityVector = new Vector();

        // Pre-calculating values
        sin_theta = Math.sin(theta); // pitch
        cos_theta = Math.cos(theta); // pitch
        sin_fi = Math.sin(fi); // roll
        cos_fi = Math.cos(fi); // roll
        sin_psi = Math.sin(psi); // yaw
        cos_psi = Math.cos(psi); // yaw

        transformedGravityVector.x = f_g_x * (cos_theta * cos_psi) + f_g_y * (sin_psi * cos_theta) - f_g_z * sin_theta;
        transformedGravityVector.y = f_g_x * (sin_theta * sin_fi * cos_psi - sin_psi * cos_fi) + f_g_y * (sin_theta * sin_fi * sin_psi + cos_psi * cos_fi) + f_g_z * (cos_theta * sin_fi);
        transformedGravityVector.z = f_g_x * (cos_psi * sin_theta * cos_fi + sin_fi * sin_psi) + f_g_y * (sin_theta * cos_fi * sin_psi - sin_fi * cos_psi) + f_g_z * (cos_theta * cos_fi);

        return transformedGravityVector;
    }
}
