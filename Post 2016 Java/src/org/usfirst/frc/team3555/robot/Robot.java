package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/*
 * creates the robot class and extends the SampleRobot class
 * the Sample robot class gives the operator control, test method, etc..
 */
public class Robot extends SampleRobot {
	Engine engine;
	
	/*
	 * Class constructor, this is called when the program starts.
	 */
    public Robot() {
    	engine = new Engine();
    }
    
    /*
     * This method is called by the field at the competition
     * This will just drive the robot forward at 75% Speed for the set amount of seconds, the local variable seconds
     */
    public void autonomous() {
    	engine.driveFoward(1, -.5, .5);
	}
	
    /*
     * This while loop runs when the operator control is on and the robot is enabled
     * the robot is updated in the loop by taking in inputs and such...
     */

	public void operatorControl() {
        while(isOperatorControl() && isEnabled()) {
        	engine.checkButtons();
        	engine.updateDrive();
//        	engine.updateShooter();
        	engine.printData();
        	
        	Timer.delay(0.005);
        }
    }
}
