package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;

/*
 * creates the robot class and extends the SampleRobot class
 * the Sample robot class gives the operater control, test method, etc..
 */

public class Robot extends SampleRobot {
	
	/*
	 * Creates a new Camera for the smartdashboard
	 * Smartdashboard it the java application that the driver station uses to put whatever the programmer tells it to put on it
	 */
	CameraServer camera = CameraServer.getInstance();
	
	/*
	 * Each side has 2 motors that turns 1 shaft.
	 * Used to give that shaft a greater power.
	 */
	
	private Spark driveSparkL1; //left side 
	private Spark driveSparkL2; //left side
	
	private Spark driveSparkR1; //right side
	private Spark driveSparkR2; //right side
	
	
	/*
	 * Creates 2 new joy stick objects
	 */
	private Joystick joyLeft;
	private Joystick joyRight;
	
	/*
	 * When someone lets go of the joy stick, the joy stick doesn't end up at perfectly 0
	 * If the joy stick doesn't end up at 0, then the motors get instructions to set speed at, for example, .05.
	 * Over time, sending it this instruction breaks the motor, and draws power.
	 * The dead zone is the area of the joy stick that the joy stick won't send the speed controller instructions.
	 */
	private final static double DEADZONE = .08;
	
	
	/*
	 * For proper encapsulation, the slave spark follows the other spark, but to do this the slave must override the spark set method
	 * this method requires the parameter in order for it to be overwritten
	 * in the slave set method, it sets the speed to the master spark, so the speed of the method parameter that is passed in
	 * is irrelevent, but at the same time significantly required
	 */
	private int dummySpeed = 0;
	
	/*
	 * Class constructor, this is called when the program starts.
	 */
    public Robot() {
    	
    	/*
    	 * The Sparks take in a port number from the PWM pins on the RoboRIO
    	 * The Joysticks each take in port numbers, this number can be found in the driver station when joystick is plugged in
    	 */
    	driveSparkL1 = new Spark(0);
    	driveSparkL2 = new SlaveSpark(1, driveSparkL1);
    	
    	driveSparkR1 = new Spark(2);
    	driveSparkR2 = new SlaveSpark(3, driveSparkR1);
    	
    	joyLeft = new Joystick(0);
    	joyRight = new Joystick(3);
    	
    	camera.setQuality(50);
    	camera.startAutomaticCapture("cam0");
    }
    
    /*
     * If this method is called, then the rio will process the joystick values, if it is outside the deadzone
     * then it will tell the motors to get at the speed the joystick say.
     * Tank drive is controlled with 2 different joysticks and each joystick controls a side 
     */ 
    public void tankDrive(){
    	double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(joyLeft.getRawAxis(1)) >= DEADZONE){
        	leftSpeed = joyLeft.getRawAxis(1);
    	}
    	
    	if(Math.abs(joyRight.getRawAxis(1)) >= DEADZONE){
    		rightSpeed = -joyRight.getRawAxis(1);
    	}
    	
    	driveSparkL1.set(leftSpeed);
    	driveSparkL2.set(dummySpeed);
    	
    	driveSparkR1.set(rightSpeed);
		driveSparkR2.set(dummySpeed);
		
		Timer.delay(0.005);
    }
    
    
    /*
     * arcade drive is controlled by only on Joysitck
     * in this method one joystick is passed in, any joystick can be passed in, as long it was made earlier in the code as a field
     */
	public void arcadeDrive(Joystick arcadeJoy){
    	double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(arcadeJoy.getRawAxis(1)) >= DEADZONE || Math.abs(arcadeJoy.getRawAxis(0)) >= DEADZONE){ 
    		leftSpeed = arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0);
    		rightSpeed = -arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0);
    	}
    	
    	driveSparkL1.set(leftSpeed);
		driveSparkL2.set(dummySpeed);
		
		driveSparkR1.set(rightSpeed);
		driveSparkR2.set(dummySpeed);
		
//		SmartDashboard.putDouble("Left", driveSparkL1.get());
//		SmartDashboard.putDouble("Left slave", driveSparkL2.get());
//		
//		SmartDashboard.putDouble("Right", driveSparkR1.get());
//		SmartDashboard.putDouble("Right slave", driveSparkR2.get());
    }
    
    /*
     * This while loop runs when the operator control is on and the robot is enabled
     * the robot is updated in the loop by taking in inputs and such...
     */
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
        	arcadeDrive(joyRight);
//        	tankDrive();
        }
    }
}
