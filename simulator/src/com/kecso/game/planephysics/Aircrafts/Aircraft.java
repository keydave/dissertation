/**
 * Abstract class Aircraft is a superclass for every flyable aircraft. For make
 * a flight all the variables are needed.
 */
package com.kecso.game.planephysics.Aircrafts;

/**
 * Abstract class for all aircraft used with this physics library
 *
 * @author Jan Kozlovsk� - AI3-P (email: jan.kozlovsky@centrum.cz)
 */
public abstract class Aircraft {

    /**
     * This two-dimensional array symbolizes a positive part of aircraft's polar
     * curve (the part above x axis in chart). <br />
     * <br />
     * 1 - The first argument is an angle of attack in degrees. <br />
     * 2 - The second argument is Cd, the coefficient of drag. <br />
     * 3 - The third argument is Cl, the coefficient of lift. <br />
     * <br />
     * IMPORTANT: The array should be of size double[24][3]. So the maximal
     * angle of attack is 23�.<br />
     * <br />
     * The example of using the array might be: <br />
     * - polar_positive[5][0] = 5 - The angle of attack is 5�.<br />
     * - polar_positive[5][1] = 0.033; - The coefficient of drag is 0.033 during
     * angle of attack 5�. <br />
     * - polar_positive[5][2] = 0.41; - The coefficient of lift is 0.41 during
     * angle of attack 5�. <br />
     * <br />
     * - polar_positive[6][0] = 6 - The angle of attack is 6�.<br />
     * - polar_positive[6][1] = 0.039; - The coefficient of drag is 0.039 during
     * angle of attack 6�. <br />
     * - polar_positive[6][2] = 0.49; - The coefficient of lift is 0.49 during
     * angle of attack 6�. <br />
     * <br />
     * - polar_positive[19][2] = 1.49;- The maximum coefficient of lift is 1.49
     * during angle of attack 19�. <br />
     * - polar_positive[23][1] = 0.360; - The maximum coefficient of drag is
     * 0.360 during maximum angle of attack 23�. <br />
     * etc. <br />
     * <br />
     * This is the polar curve of this library's default aircraft Cessna 172:<br
     * />
     * <br />
     * for(int i = 0; i <= 23; i++) {<br /> polar_positive[i][0] = i; //This
     * fills in an angle of attack <br />
     * }<br />
     * polar_positive[0][1] = 0.02; polar_positive[0][2] = 0; <br />
     * polar_positive[1][1] = 0.021; polar_positive[1][2] = 0.08; <br />
     * polar_positive[2][1] = 0.023; polar_positive[2][2] = 0.170; <br />
     * polar_positive[3][1] = 0.025; polar_positive[3][2] = 0.25;<br />
     * polar_positive[4][1] = 0.029; polar_positive[4][2] = 0.33;<br />
     * polar_positive[5][1] = 0.033; polar_positive[5][2] = 0.41; <br />
     * polar_positive[6][1] = 0.039; polar_positive[6][2] = 0.49; <br />
     * polar_positive[7][1] = 0.046; polar_positive[7][2] = 0.57; <br />
     * polar_positive[8][1] = 0.054; polar_positive[8][2] = 0.65; <br />
     * polar_positive[9][1] = 0.063; polar_positive[9][2] = 0.73; <br />
     * polar_positive[10][1] = 0.073; polar_positive[10][2] = 0.81; <br />
     * polar_positive[11][1] = 0.084; polar_positive[11][2] = 0.9; <br />
     * polar_positive[12][1] = 0.098; polar_positive[12][2] = 0.98; <br />
     * polar_positive[13][1] = 0.112; polar_positive[13][2] = 1.06; <br />
     * polar_positive[14][1] = 0.129; polar_positive[14][2] = 1.14; <br />
     * polar_positive[15][1] = 0.146; polar_positive[15][2] = 1.23; <br />
     * polar_positive[16][1] = 0.167; polar_positive[16][2] = 1.3; <br />
     * polar_positive[17][1] = 0.190; polar_positive[17][2] = 1.37; <br />
     * polar_positive[18][1] = 0.217; polar_positive[18][2] = 1.44; <br />
     * polar_positive[19][1] = 0.247; polar_positive[19][2] = 1.49; <br />
     * polar_positive[20][1] = 0.282; polar_positive[20][2] = 1.5; <br />
     * polar_positive[21][1] = 0.330; polar_positive[21][2] = 1.3; <br />
     * polar_positive[22][1] = 0.350; polar_positive[22][2] = 1; <br />
     * polar_positive[23][1] = 0.360; polar_positive[23][2] = 0.0; <br />
     */
    public double[][] polarPositive;
    /**
     * This two-dimensional array symbolizes a negative part of aircraft's polar
     * curve (the part under x axis in chart). This polar curve is needed when
     * an aircraft flies with negative angle of attack. <br />
     * IMPORTANT: The array should be of size double[24][3]. So the maximal
     * angle of attack is -23�.<br />
     * <br />
     * 1 - The first argument is an angle of attack in degrees. <br />
     * 2 - The second argument is Cd, the coefficient of drag. <br />
     * 3 - The third argument is Cl, the coefficient of lift. <br />
     * <br />
     * The example of using the array might be: <br />
     * - polar_negative[5][0] = 5 - The angle of attack is -5�. -
     * polar_negative[5][1] = 0.033; - The coefficient of drag is 0.033 during
     * angle of attack -5�. <br />
     * - polar_negative[5][2] = -0.136; - The coefficient of lift is -0.136
     * during angle of attack -5�. <br />
     * <br />
     * - polar_negative[6][1] = 0.039; - The coefficient of drag is 0.039 during
     * angle of attack -6�. <br />
     * - polar_negative[6][2] = -0.163; - The coefficient of lift is -0.163
     * during angle of attack -6�. <br />
     * etc. <br />
     * <br />
     * This is the polar curve of this library's default aircraft Cessna 172:<br
     * />
     * <br />
     * for(int i = 0; i <= 23; i++) {<br /> polar_negative[i][0] = i; //This
     * fills in an angle of attack <br />
     * }<br />
     * polar_negative[0][1] = 0.02; polar_negative[0][2] = 0; <br />
     * polar_negative[1][1] = 0.021; polar_negative[1][2] = -0.027; <br />
     * polar_negative[2][1] = 0.023; polar_negative[2][2] = -0.057; <br />
     * polar_negative[3][1] = 0.025; polar_negative[3][2] = -0.083;<br />
     * polar_negative[4][1] = 0.029; polar_negative[4][2] = -0.11;<br />
     * polar_negative[5][1] = 0.033; polar_negative[5][2] = -0.136; <br />
     * polar_negative[6][1] = 0.039; polar_negative[6][2] = -0.163; <br />
     * polar_negative[7][1] = 0.046; polar_negative[7][2] = -0.19; <br />
     * polar_negative[8][1] = 0.054; polar_negative[8][2] = -0.216; <br />
     * polar_negative[9][1] = 0.063; polar_negative[9][2] = -0.243; <br />
     * polar_negative[10][1] = 0.073; polar_negative[10][2] = -0.27; <br />
     * polar_negative[11][1] = 0.084; polar_negative[11][2] = -0.3; <br />
     * polar_negative[12][1] = 0.098; polar_negative[12][2] = -0.326; <br />
     * polar_negative[13][1] = 0.112; polar_negative[13][2] = -0.353; <br />
     * polar_negative[14][1] = 0.129; polar_negative[14][2] = -0.38; <br />
     * polar_negative[15][1] = 0.146; polar_negative[15][2] = -0.41; <br />
     * polar_negative[16][1] = 0.167; polar_negative[16][2] = -0.43; <br />
     * polar_negative[17][1] = 0.190; polar_negative[17][2] = -0.456; <br />
     * polar_negative[18][1] = 0.217; polar_negative[18][2] = -0.48; <br />
     * polar_negative[19][1] = 0.247; polar_negative[19][2] = -0.496; <br />
     * polar_negative[20][1] = 0.282; polar_negative[20][2] = -0.5; <br />
     * polar_negative[21][1] = 0.330; polar_negative[21][2] = -0.4;<br />
     * polar_negative[22][1] = 0.350; polar_negative[22][2] = -0.2; <br />
     * polar_negative[23][1] = 0.360; polar_negative[23][2] = -0.1; <br />
     */
    public double[][] polarNegative;
    /**
     * This two-dimensional array symbolizes aircraft's rudder polar curve.
     * Because rudder behaves like a wing, it has to have its own polar curve.
     * Here we need just a lift coefficient. The drag of rudder is neglected.
     * Only positive part of polar curve is needed because for negative and
     * positive angle of attack the coefficients are the same.<br />
     * <br />
     * IMPORTANT: The array should be of size double[16][2]. So the maximal
     * angle of attack is 16�.<br />
     * <br />
     * 1 - The first argument is an angle of attack in degrees. <br />
     * 2 - The second argument is Cl, the coefficient of lift. <br />
     * <br />
     * The example of using the array might be: <br />
     * - polar_Rudder[5][0] = 5 - The angle of attack is 5� or -5�. -
     * polar_Rudder[5][1] = 0.65; - The coefficient of lift is 0.65 during angle
     * of attack 5� and -5�. <br />
     * - polar_Rudder[6][0] = 6 - The angle of attack is 6� or -6�. -
     * polar_Rudder[6][1] = 0.78; - The coefficient of lift is 0.78 during angle
     * of attack 6� and -6�. <br />
     * etc. <br />
     * <br />
     * This is the polar curve of this library's default aircraft Cessna 172:<br
     * />
     * <br />
     * for(int i = 0; i <= 15; i++) {<br /> polar_Rudder[i][0] = i; // This
     * fills in angles of attack <br />
     * }<br />
     * polar_Rudder[0][1] = 0; <br />
     * polar_Rudder[1][1] = 0.13; <br />
     * polar_Rudder[2][1] = 0.26; <br />
     * polar_Rudder[3][1] = 0.39;<br />
     * polar_Rudder[4][1] = 0.52;<br />
     * polar_Rudder[5][1] = 0.65;<br />
     * polar_Rudder[6][1] = 0.78;<br />
     * polar_Rudder[7][1] = 0.91; <br />
     * polar_Rudder[8][1] = 1.04; <br />
     * polar_Rudder[9][1] = 1.17; <br />
     * polar_Rudder[10][1] = 1.3; <br />
     * polar_Rudder[11][1] = 1.43; <br />
     * polar_Rudder[12][1] = 1.56; <br />
     * polar_Rudder[13][1] = 1.69; <br />
     * polar_Rudder[14][1] = 1.82;<br />
     * polar_Rudder[15][1] = 1.95; <br />
     */
    public double[][] polarRudder;
    /**
     * This two-dimensional array symbolizes aircraft's elevator polar curve.
     * Because rudder behaves like a wing, it has to have its own polar curve.
     * Here we need just a lift coefficient. The drag of rudder is neglected.
     * Only positive part of polar curve is needed because for negative and
     * positive angle of attack the coefficients are the same.<br />
     * <br />
     * IMPORTANT: The array should be of size double[22][2]. So the maximal
     * angle of attack is 21�.<br />
     * <br />
     * 1 - The first argument is an angle of attack in degrees. <br />
     * 2 - The second argument is Cl, the coefficient of lift. <br />
     * <br />
     * The example of using the array might be: <br />
     * - polar_Elevator[5][0] = 5 - The angle of attack is 5� or -5�. -
     * polar_Elevator[5][1] = 0.41; - The coefficient of lift is 0.41 during
     * angle of attack 5� and -5�. <br />
     * - polar_Elevator[6][0] = 6 - The angle of attack is 6� or -6�. -
     * polar_Elevator[6][1] = 0.49; - The coefficient of lift is 0.49 during
     * angle of attack 6� and -6�. <br />
     * etc. <br />
     * This is the polar curve of this library's default aircraft Cessna 172:<br
     * />
     * <br />
     * for(int i = 0; i <= 21; i++) {<br /> polar_Elevator[i][0] = i; // This
     * fills in angles of attack	<br />
     * }<br />
     * polar_Elevator[0][1] = 0; <br />
     * polar_Elevator[1][1] = 0.08; <br />
     * polar_Elevator[2][1] = 0.170; <br />
     * polar_Elevator[3][1] = 0.25;<br />
     * polar_Elevator[4][1] = 0.33;<br />
     * polar_Elevator[5][1] = 0.41; <br />
     * polar_Elevator[6][1] = 0.49; <br />
     * polar_Elevator[7][1] = 0.57; <br />
     * polar_Elevator[8][1] = 0.65; <br />
     * polar_Elevator[9][1] = 0.73;<br />
     * polar_Elevator[10][1] = 0.81;<br />
     * polar_Elevator[11][1] = 0.9; <br />
     * polar_Elevator[12][1] = 0.98; <br />
     * polar_Elevator[13][1] = 1.06; <br />
     * polar_Elevator[14][1] = 1.14; <br />
     * polar_Elevator[15][1] = 1.23; <br />
     * polar_Elevator[16][1] = 1.3; <br />
     * polar_Elevator[17][1] = 1.37; <br />
     * polar_Elevator[18][1] = 1.44; <br />
     * polar_Elevator[19][1] = 1.49; <br />
     * polar_Elevator[20][1] = 1.5; <br />
     * polar_Elevator[21][1] = 1.51; <br />
     */
    public double[][] polarElevator;
    /**
     * This two-dimensional array symbolizes aircraft's ailerons polar curve.
     * Because ailerons change wing profile, it has to have its own polar curve.
     * Here we need just a lift coefficient. The drag of rudder is neglected.
     * Here it is also assumed that fot negative and positive angle of attack
     * the coefficients are the same, so only positive part of polar curve is
     * needed.<br />
     * <br />
     * IMPORTANT: The array should be of size double[22][2]. So the maximal
     * angle of attack is 21�.<br />
     * <br />
     * 1 - The first argument is an angle of attack in degrees. <br />
     * 2 - The second argument is Cl, the coefficient of lift. <br />
     * <br />
     * The example of using the array might be: <br />
     * - polar_Aileron[5][0] = 5 - The angle of attack is 5� or -5�. -
     * polar_Aileron[5][1] = 0.205; - The coefficient of lift is 0.205 during
     * angle of attack 5� and -5�. <br />
     * - polar_Aileron[6][0] = 6 - The angle of attack is 6� or -6�. -
     * polar_Aileron[6][1] = 0.245; - The coefficient of lift is 0.245 during
     * angle of attack 6� and -6�. <br />
     * etc. <br />
     * This is the polar curve of this library's default aircraft Cessna 172:<br
     * />
     * <br />
     * for(int i = 0; i <= 21; i++) {<br /> polar_Aileron[i][0] = i; // This
     * fills in angles of attack <br />
     * }<br />
     * polar_Aileron[0][1] = 0;<br />
     * polar_Aileron[1][1] = 0.04; <br />
     * polar_Aileron[2][1] = 0.085; <br />
     * polar_Aileron[3][1] = 0.125;<br />
     * polar_Aileron[4][1] = 0.165;<br />
     * polar_Aileron[5][1] = 0.205; <br />
     * polar_Aileron[6][1] = 0.245; <br />
     * polar_Aileron[7][1] = 0.285; <br />
     * polar_Aileron[8][1] = 0.325; <br />
     * polar_Aileron[9][1] = 0.365; <br />
     * polar_Aileron[10][1] = 0.405; <br />
     * polar_Aileron[11][1] = 0.45; <br />
     * polar_Aileron[12][1] = 0.49; <br />
     * polar_Aileron[13][1] = 0.53; <br />
     * polar_Aileron[14][1] = 0.57;<br />
     * polar_Aileron[15][1] = 0.615; <br />
     * polar_Aileron[16][1] = 0.65; <br />
     * polar_Aileron[17][1] = 0.685;<br />
     * polar_Aileron[18][1] = 0.72; <br />
     * polar_Aileron[19][1] = 0.745; <br />
     * polar_Aileron[20][1] = 0.75; <br />
     * polar_Aileron[21][1] = 0.755; <br />
     */
    public double[][] polarAileron;
    /**
     * Set here a wing span of an aircraft in meters. This variable is used for
     * counting a rolling moment.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the wingSpan = 11.
     */
    public double wingSpan;
    /**
     * Set here a weight of an aircraft in kilograms. This variable is used for
     * counting all the accelerations.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the weight = 750.
     */
    public double weight;
    /**
     * Set here a wing area of an aircraft in square meters. This variable is
     * used for counting a lift and drag.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the wingArea = 17.
     */
    public double wingArea;
    /**
     * Set here an area of aircraft's rudder in square meters. This variable is
     * used for counting yaw moment and sideslip angle.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the rudder_area = 1.
     */
    public double rudderArea;
    /**
     * Set here an area of aircraft's elevator in square meters. This variable
     * is used for counting pitch moment and angle of attack.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the elevator_area = 1.
     */
    public double elevatorArea;
    /**
     * Set here an area of aircraft's one (left or right) aileron in square
     * meters. This variable is used for counting roll moment.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the aileron_area = 1.
     */
    public double aileronArea;
    /**
     * Set here a distance of gravity center from aerodynamic center. For
     * aerodynamically stable aircrafts the value should be > 0.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the
     * center_of_gravity_distance = 0.1.
     */
    public double centerOfGravityDistance;
    /**
     * Set here a reduced moment of inertia for roll movement (around x axis).
     * The bigger number, the smaller effect the controls have and aircraft is
     * less manoeuvrable. Reduced moment of inertia is counted as sum of (mass *
     * distace from axis of rotation ^2).<br />
     * <br />
     * For the library's default aircraft Cessna 172 the rmi_for_roll =
     * weight*2*2.
     */
    public double miForRoll;
    /**
     * Set here a reduced moment of inertia for pitch and yaw movement (around y
     * and z axis). The bigger number, the smaller effect the controls have and
     * aircraft is less manoeuvrable. Reduced moment of inertia is counted as
     * sum of (mass * distace from axis of rotation).<br />
     * <br />
     * For the library's default aircraft Cessna 172 the rmi_for_pitch_and_yaw =
     * weight * 1.2 * 1.2.
     */
    public double miForPitchAndYaw;
    /**
     * Set here a maximal engine power in Newtons. We do not take into account a
     * dependency between power and RPM (engine rounds per minute) or
     * altitude.<br />
     * <br />
     * For the library's default aircraft Cessna 172 the max_engine_power =
     * 3000.
     */
    public double maxEnginePower;

    /**
     * @return wingSpan of aircraft's wing in meters.
     */
    public double getWingSpan() {
        return wingSpan;
    }

    /**
     * @return weight of aircraft in kilograms.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @return wing area of aircfraft's wing in square meters.
     */
    public double getWingArea() {
        return wingArea;
    }

    /**
     *
     * @return area of aircfraft's rudder in square meters.
     */
    public double getRudderArea() {
        return rudderArea;
    }

    /**
     *
     * @return area of aircfraft's elevator in square meters.
     */
    public double getElevatorArea() {
        return elevatorArea;
    }

    /**
     *
     * @return area of aircfraft's aileron in square meters.
     */
    public double getAileronArea() {
        return aileronArea;
    }

    /**
     *
     * @return distance of center of gravity from aerodynamic center in meters.
     * At aerodynamically stable aircraft this value should be > 0.
     */
    public double getpOM_distance() {
        return centerOfGravityDistance;
    }

    /**
     *
     * @return reduced moment of inertia for roll movement.
     */
    public double getMi_for_roll() {
        return miForRoll;
    }

    /**
     *
     * @return reduced moment of inertia for yaw and pitch movement.
     */
    public double getMi_for_pitch() {
        return miForPitchAndYaw;
    }

    /**
     *
     * @return maximal engine power in newtons
     */
    public double getMax_engine_power() {
        return maxEnginePower;
    }

    /**
     *
     * @param aoa - Angle of attack in degrees.
     * @return Cd - Coefficient of drag.
     */
    public double getNegativeCx(int aoa) {
        double cx = polarNegative[-aoa][1];
        return cx;
    }

    /**
     *
     * @param aoa - Angle of attack in degrees.
     * @return Cl - Coefficent of lift.
     */
    public double getNegativeCy(int aoa) {
        double cy = polarNegative[-aoa][2];
        return cy;
    }

    /**
     *
     * @param aoa - Angle of attack in degrees.
     * @return Cd - Coefficient of drag.
     */
    public double getPositiveCx(int aoa) {
        double cx = polarPositive[aoa][1];
        return cx;
    }

    /**
     *
     * @param aoa - Angle of attack in degrees.
     * @return Cl - Coefficient of lift.
     */
    public double getPositiveCy(int aoa) {
        double cy = polarPositive[aoa][2];
        return cy;
    }

    /**
     * @param yawAoa - Angle of attack in degrees.
     * @return double Cl - Coefficient of lift.
     */
    public double getCRudder(int yawAoa) {
        double cSOP = polarRudder[yawAoa][1];
        return cSOP;
    }

    /**
     *
     * @param pitchAoa - Angle of attack in degrees.
     * @return double Cl - Coefficient of lift.
     */
    public double getCElevator(int pitchAoa) {
        double cVOP = polarElevator[pitchAoa][1];
        return cVOP;
    }

    /**
     *
     * @param rollAoa - Angle of attack in degrees.
     * @return double Cl - Coefficient of lift.
     */
    public double getCAileron(int rollAoa) {
        double cAileron = polarAileron[rollAoa][1];
        return cAileron;
    }
}
