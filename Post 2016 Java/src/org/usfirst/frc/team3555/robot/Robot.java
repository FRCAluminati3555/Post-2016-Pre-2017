package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Spark;

public class Robot extends SampleRobot {
	
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
	 * Constructor not used because fields established in the start of the class
	 */
    public Robot() {
    	driveSparkL1 = new Spark(0);
    	driveSparkL2 = new Spark(1);
    	
    	driveSparkR1 = new Spark(2);
    	driveSparkR2 = new Spark(3);
    	
    	joyLeft = new Joystick(0);
    	joyRight = new Joystick(3);
    }
    
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
    	driveSparkL2.set(leftSpeed);
    	
    	driveSparkR1.set(rightSpeed);
		driveSparkR2.set(rightSpeed);
    }
    
    public void arcadeDrive(Joystick arcadeJoy){
    	double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(arcadeJoy.getRawAxis(1)) >= DEADZONE || Math.abs(arcadeJoy.getRawAxis(0)) >= DEADZONE){ 
    		leftSpeed = arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0);
    		rightSpeed = -arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0);
    	}
    	
    	driveSparkL1.set(leftSpeed);
		driveSparkL2.set(leftSpeed);
		
		driveSparkR1.set(rightSpeed);
		driveSparkR2.set(rightSpeed);
    }
    
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
        	arcadeDrive(joyRight);
//        	tankDrive();
        	
//        	driveSparkL1.set(-.3);
//        	driveSparkR2.set(.3);
        }
    }

}
