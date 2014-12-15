package com.kecso.game.planephysics.Aircrafts;

/**
 * Class contains Cesna 172 aircraft information
 *
 * @author Jan Kozlovsk� - AI3-P (email: jan.kozlovsky@centrum.cz)
 */
public class DefaultAircraft extends Aircraft {

    /**
     * Default plain constructor
     */
    public DefaultAircraft() {

        polarPositive = new double[24][3];
        polarNegative = new double[24][3];
        polarRudder = new double[16][2];
        polarElevator = new double[22][2];
        polarAileron = new double[22][2];

        wingSpan = 11;
        weight = 1000;
        wingArea = 17;
        rudderArea = 1;
        elevatorArea = 1;
        aileronArea = 1;
        centerOfGravityDistance = 0.1;
        miForRoll = weight * 2 * 2;
        miForPitchAndYaw = weight * 1.2 * 1.2;
        maxEnginePower = 4000;

        for (int i = 0; i <= 23; i++) {
            polarPositive[i][0] = i; //angle of attack	
        }

        polarPositive[0][1] = 0.02;
        polarPositive[0][2] = 0; //cx, cy 
        polarPositive[1][1] = 0.021;
        polarPositive[1][2] = 0.08;
        polarPositive[2][1] = 0.023;
        polarPositive[2][2] = 0.170;
        polarPositive[3][1] = 0.025;
        polarPositive[3][2] = 0.25;
        polarPositive[4][1] = 0.029;
        polarPositive[4][2] = 0.33;
        polarPositive[5][1] = 0.033;
        polarPositive[5][2] = 0.41; //cy
        polarPositive[6][1] = 0.039;
        polarPositive[6][2] = 0.49; //cy
        polarPositive[7][1] = 0.046;
        polarPositive[7][2] = 0.57; //cy
        polarPositive[8][1] = 0.054;
        polarPositive[8][2] = 0.65; //cy
        polarPositive[9][1] = 0.063;
        polarPositive[9][2] = 0.73; //cy
        polarPositive[10][1] = 0.073;
        polarPositive[10][2] = 0.81; //cy
        polarPositive[11][1] = 0.084;
        polarPositive[11][2] = 0.9; //cy
        polarPositive[12][1] = 0.098;
        polarPositive[12][2] = 0.98; //cy
        polarPositive[13][1] = 0.112;
        polarPositive[13][2] = 1.06; //cy
        polarPositive[14][1] = 0.129;
        polarPositive[14][2] = 1.14; //cy
        polarPositive[15][1] = 0.146;
        polarPositive[15][2] = 1.23; //cy
        polarPositive[16][1] = 0.167;
        polarPositive[16][2] = 1.3; //cy
        polarPositive[17][1] = 0.190;
        polarPositive[17][2] = 1.37; //cy
        polarPositive[18][1] = 0.217;
        polarPositive[18][2] = 1.44; //cy
        polarPositive[19][1] = 0.247;
        polarPositive[19][2] = 1.49; //cy
        polarPositive[20][1] = 0.282;
        polarPositive[20][2] = 1.5; //cy
        polarPositive[21][1] = 0.330;
        polarPositive[21][2] = 1.3; //cy
        polarPositive[22][1] = 0.350;
        polarPositive[22][2] = 1; //cy
        polarPositive[23][1] = 0.360;
        polarPositive[23][2] = 0.08; //cy

        for (int i = 0; i <= 23; i++) {
            polarNegative[i][0] = i; //angle of attack	
        }

        polarNegative[0][1] = 0.02;
        polarNegative[0][2] = 0; //cx, cy 
        polarNegative[1][1] = 0.021;
        polarNegative[1][2] = -0.027;
        polarNegative[2][1] = 0.023;
        polarNegative[2][2] = -0.057;
        polarNegative[3][1] = 0.025;
        polarNegative[3][2] = -0.083;
        polarNegative[4][1] = 0.029;
        polarNegative[4][2] = -0.11;
        polarNegative[5][1] = 0.033;
        polarNegative[5][2] = -0.136; //cy
        polarNegative[6][1] = 0.039;
        polarNegative[6][2] = -0.163; //cy
        polarNegative[7][1] = 0.046;
        polarNegative[7][2] = -0.19; //cy
        polarNegative[8][1] = 0.054;
        polarNegative[8][2] = -0.216; //cy
        polarNegative[9][1] = 0.063;
        polarNegative[9][2] = -0.243; //cy
        polarNegative[10][1] = 0.073;
        polarNegative[10][2] = -0.27; //cy
        polarNegative[11][1] = 0.084;
        polarNegative[11][2] = -0.3; //cy
        polarNegative[12][1] = 0.098;
        polarNegative[12][2] = -0.326; //cy
        polarNegative[13][1] = 0.112;
        polarNegative[13][2] = -0.353; //cy
        polarNegative[14][1] = 0.129;
        polarNegative[14][2] = -0.38; //cy
        polarNegative[15][1] = 0.146;
        polarNegative[15][2] = -0.41; //cy
        polarNegative[16][1] = 0.167;
        polarNegative[16][2] = -0.43; //cy
        polarNegative[17][1] = 0.190;
        polarNegative[17][2] = -0.456; //cy
        polarNegative[18][1] = 0.217;
        polarNegative[18][2] = -0.48; //cy
        polarNegative[19][1] = 0.247;
        polarNegative[19][2] = -0.496; //cy
        polarNegative[20][1] = 0.282;
        polarNegative[20][2] = -0.5; //cy
        polarNegative[21][1] = 0.330;
        polarNegative[21][2] = -0.4; //cy	
        polarNegative[22][1] = 0.350;
        polarNegative[22][2] = -0.2; //cy
        polarNegative[23][1] = 0.360;
        polarNegative[23][2] = -0.1; //cy

        for (int i = 0; i <= 15; i++) {
            polarRudder[i][0] = i; //angle of attack VOP	
        }
        polarRudder[0][1] = 0; //cx, cy 
        polarRudder[1][1] = 0.13;
        polarRudder[2][1] = 0.26;
        polarRudder[3][1] = 0.39;
        polarRudder[4][1] = 0.52;
        polarRudder[5][1] = 0.65; //cy
        polarRudder[6][1] = 0.78; //cy
        polarRudder[7][1] = 0.91; //cy
        polarRudder[8][1] = 1.04; //cy
        polarRudder[9][1] = 1.17; //cy
        polarRudder[10][1] = 1.3; //cy
        polarRudder[11][1] = 1.43; //cy
        polarRudder[12][1] = 1.56; //cy
        polarRudder[13][1] = 1.69; //cy
        polarRudder[14][1] = 1.82; //cy
        polarRudder[15][1] = 1.95; //cy

        for (int i = 0; i <= 21; i++) {
            polarAileron[i][0] = i; //angle of attack Ailerons
        }
        polarAileron[0][1] = 0; //cx, cy 
        polarAileron[1][1] = 0.04;
        polarAileron[2][1] = 0.085;
        polarAileron[3][1] = 0.125;
        polarAileron[4][1] = 0.165;
        polarAileron[5][1] = 0.205; //cy
        polarAileron[6][1] = 0.245; //cy
        polarAileron[7][1] = 0.285; //cy
        polarAileron[8][1] = 0.325; //cy
        polarAileron[9][1] = 0.365; //cy
        polarAileron[10][1] = 0.405; //cy
        polarAileron[11][1] = 0.45; //cy
        polarAileron[12][1] = 0.49; //cy
        polarAileron[13][1] = 0.53; //cy
        polarAileron[14][1] = 0.57; //cy
        polarAileron[15][1] = 0.615; //cy
        polarAileron[16][1] = 0.65; //cy
        polarAileron[17][1] = 0.685; //cy
        polarAileron[18][1] = 0.72; //cy
        polarAileron[19][1] = 0.745; //cy
        polarAileron[20][1] = 0.75; //cy
        polarAileron[21][1] = 0.755; //cy

        for (int i = 0; i <= 21; i++) {
            polarElevator[i][0] = i; //angle of attack VOP	
        }
        polarElevator[0][1] = 0; //cx, cy 
        polarElevator[1][1] = 0.08;
        polarElevator[2][1] = 0.170;
        polarElevator[3][1] = 0.25;
        polarElevator[4][1] = 0.33;
        polarElevator[5][1] = 0.41; //cy
        polarElevator[6][1] = 0.49; //cy
        polarElevator[7][1] = 0.57; //cy
        polarElevator[8][1] = 0.65; //cy
        polarElevator[9][1] = 0.73; //cy
        polarElevator[10][1] = 0.81; //cy
        polarElevator[11][1] = 0.9; //cy
        polarElevator[12][1] = 0.98; //cy
        polarElevator[13][1] = 1.06; //cy
        polarElevator[14][1] = 1.14; //cy
        polarElevator[15][1] = 1.23; //cy
        polarElevator[16][1] = 1.3; //cy
        polarElevator[17][1] = 1.37; //cy
        polarElevator[18][1] = 1.44; //cy
        polarElevator[19][1] = 1.49; //cy
        polarElevator[20][1] = 1.5; //cy
        polarElevator[21][1] = 1.51; //cy

    }
}
