package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Spark;

public class Robot extends SampleRobot {
	
	/*
	 * Each side has 2 motors that turns 1 shaft.
	 * Used to give that shaft a greater power.
	 * 
	 */
	
	Spark driveSparkL1 = new Spark(0); //left side 
	Spark driveSparkL2 = new Spark(1); //left side
	
	Spark driveSparkR1 = new Spark(2); //right side
	Spark driveSparkR = new Spark(3); //right side
	
	
	/*
	 * Creates 2 new Joysticks at ports 0 & 3
	 */
	Joystick joyLeft = new Joystick(0);
	Joystick joyRight = new Joystick(3);
	
	/*
	 * When someone lets go of the joystick, the joystick doesn't end up at perfectly 0
	 * If the joystick doesn't end up at 0, then the motors get intructions to set speed at, for example, .05.
	 * Over time, sending it this instruction breaks the motor, and draws power.
	 * The dead zone is the area of the joystick that the joystick won't send the speed controller insturctions.
	 */
	final static double DEADZONE = .08;
	
	/*
	 * Constructor not used because fields established in the start of the class
	 */
    public Robot() {
    	
    }
    
    public void robotInit() {

    }

    public void autonomous() {

    }

    public void tankDrive(){
    	if(joyRight.getRawAxis(1) >= DEADZONE || joyRight.getRawAxis(1) <= -DEADZONE){
        	driveSparkL.set(joyRight.getRawAxis(1));
    	}
    	
    	if(joyLeft.getRawAxis(1) >= DEADZONE || joyLeft.getRawAxis(1) <= -DEADZONE){
        	driveSparkR.set(-joyLeft.getRawAxis(1));
    	}
    	
    }
    
    
//    left = y_axis + x_axis
//    right = y_axis - x_axis
    
    public void arcadeDrive(Joystick arcadeJoy){
    	if(arcadeJoy.getRawAxis(1) >= DEADZONE || arcadeJoy.getRawAxis(1) <= -DEADZONE || //1=y
    		arcadeJoy.getRawAxis(0) >= DEADZONE || arcadeJoy.getRawAxis(0) <= -DEADZONE){ //0=x
    		
    		driveSparkL.set(arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0));
    		driveSparkR.set(-arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0));
    	}
    	
    	else{
    		driveSparkL.set(0);
    		driveSparkR.set(0);
    	}
    	
    }
    
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
//        	arcadeDrive(joyRight);
//        	tankDrive();
        	
        	driveSparkL.set(-.3);
        	driveSparkR.set(.3);
        }
    }

    public void test() {
    }
}
