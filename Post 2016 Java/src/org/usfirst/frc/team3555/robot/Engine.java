package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Engine {
	/*
	 * Each side has 2 motors that turns 1 shaft.
	 * Used to give that shaft a greater power.
	 * 
	 * Motor Group is a team made class that controllers motors in a group format
	 * Motor Group allows the two motors to be controlled at the same speed, and not allow them to be set to different speeds
	 */
	private MotorGroup leftSideDrive;
	private MotorGroup rightSideDrive;
	
	/*
	 * These speed controllers control the shooter motors
	 */
	private Victor shoot1;  
	private Talon shoot2; 

	/*
	 * The relay is a motor that can go either forward or backward
	 * Since the loader only needs to go these two directions, the relay is used
	 */
	private Relay loader;
	
	/*
	 * Speed controller that controls the motor on the vertical alignment
	 */
	private Spark vertAd;
	
	/*
	 * Creates 3 new joy stick objects
	 * The side joysticks control each side of the robot (Depending on the drive method)
	 */
	private Joystick joyLeft;
	private Joystick joyRight;
	private Joystick joyOP;
	
	private CameraServer camera = CameraServer.getInstance();
	
	/*
	 * the potentiometer measures angle
	 * This potentiometer measures the angle of the vertical alignment of the shooter
	 * 
	 * Currently not being used because of time constraints
	 */
	private AnalogPotentiometer verticalPoten;
	
	/*
	 * These constraints keep the robot from hitting either end
	 */
	private final double verticalDownMax = .2;
	private final double verticalUpMax = .8;
	
	/*
	 * This variable tells whether or not arcade drive is being used
	 */
	private boolean arcadeDrive;
	
	/*
	 * When someone lets go of the joy stick, the joy stick doesn't end up at perfectly 0
	 * If the joy stick doesn't end up at 0, then the motors get instructions to set speed at, for example, .05.
	 * Over time, sending it this instruction breaks the motor, and draws power.
	 * The dead zone is the area of the joy stick that the joy stick won't send the speed controller instructions.
	 * 
	 * The loadzone is the zone were the loader won't be activated, 
	 * this is used to make it so the loader can be activated on each end of the slider
	 */
	private final static double DEADZONE = .08, LOADZONE = .8;// ,VERT_HIGH = .6, VERT_MEDIUM = .4, VERT_LOW = .2;
	
	public Engine(){
		/*
    	 * The Sparks take in a port number from the PWM pins on the RoboRIO
    	 * The joysticks each take in port numbers, this number can be found in the driver station when joystick is plugged in
    	 */
    	leftSideDrive = new MotorGroup(new Spark(0), new Spark(1));
    	rightSideDrive = new MotorGroup(new Spark(2), new Spark(3));
    	
    	shoot1 = new Victor(5);
    	shoot2 = new Talon(6);
    	
    	loader = new Relay(2);
		vertAd = new Spark(4);
    	
    	joyOP = new Joystick(0);
    	joyLeft = new Joystick(1);
    	joyRight = new Joystick(2);
    	
    	verticalPoten = new AnalogPotentiometer(0);
    	
    	camera.setQuality(50);
    	camera.startAutomaticCapture("cam0");
	}
	
	  /*
     * If this method is called, then the RIO will process the joystick values, if it is outside the deadzone
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
    	
    	leftSideDrive.set(leftSpeed);
    	rightSideDrive.set(rightSpeed);
    }
	
	public void driveFoward(int seconds, double speedLeft, double speedRight){
//    	long startTime = System.currentTimeMillis();
//    	
//    	while(System.currentTimeMillis() < startTime + seconds * 1000){
//    		leftSideDrive.set(-.75);
//    		rightSideDrive.set(.75);
//    	}
//    	
//    	leftSideDrive.set(0);
//    	rightSideDrive.set(0);
		
		leftSideDrive.set(speedLeft);
		rightSideDrive.set(speedRight);
		
		Timer.delay(seconds);
		
		leftSideDrive.set(0);
		rightSideDrive.set(0);
	}
	
	 /*
     * arcade drive is controlled by only one joysitck
     * in this method one joystick is passed in, any joystick can be passed in, as long it was made earlier in the code as a field
     */
	public void arcadeDrive(Joystick arcadeJoy){
    	double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(arcadeJoy.getRawAxis(1)) >= DEADZONE || Math.abs(arcadeJoy.getRawAxis(0)) >= DEADZONE){ 
    		leftSpeed = arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0);
    		rightSpeed = -arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0);
    	}
    	
    	leftSideDrive.set(leftSpeed);
    	rightSideDrive.set(rightSpeed);
    }
    
	/*
	 * This method checks if the buttons are being pressed
	 * if so, adjust the drive method accordingly
	 */
	public void checkButtons(){
		if(joyOP.getRawButton(12)){
			setShooterAngle(.25);
		}
		
		if(joyRight.getRawButton(6) || joyLeft.getRawButton(6)){
    		arcadeDrive = false;
    	}
    	
		else if(joyRight.getRawButton(7) || joyLeft.getRawButton(6)){
    		arcadeDrive = true;
    	}
	}
	
	/*
	 * This will update the speed of the drive motors
	 */
	public void updateDrive(){
		if(arcadeDrive){
    		SmartDashboard.putString("You are currently using: ", "Arcade Drive");
    		arcadeDrive(joyRight);
    	}
    		
    	else{
    		SmartDashboard.putString("You are currently using: ", "Tank Drive");
    		tankDrive();
    	}
	}
	//negative motor spped makes it move up, positive moves it down 
	public void setShooterAngle(double angle){
		double difference = verticalPoten.get()  - angle;
		
		while(Math.abs(difference) > .06){
			difference = verticalPoten.get()  - angle;
			//needs to move down
			if(difference < 0){
				while(Math.abs(difference) > .6){
					vertAd.set(.5);
				}
				
				while(Math.abs(difference) > .4){
					vertAd.set(.25);
				}
				while(Math.abs(difference) > .1){
					vertAd.set(.1);
				}
			}
			
			//needs to move up
			if(difference > 0){
				while(Math.abs(difference) > .6){
					vertAd.set(-.5);
				}
				
				while(Math.abs(difference) > .3){
					vertAd.set(-.25);
				}
				while(Math.abs(difference) > .1){
					vertAd.set(-.1);
				}
			}
		}
		
	}
	
	/*
	 * This method updates the state of the shooter
	 */
	public void updateShooter(){
		if(joyOP.getRawAxis(3) <= -LOADZONE){
    		loader.set(Relay.Value.kReverse);
    	}
    	
    	else if(joyOP.getRawAxis(3) >= LOADZONE){
    		loader.set(Relay.Value.kForward);
    	}
    	
    	else{
    		loader.set(Relay.Value.kOff);
    	}
    	
    	if(joyOP.getRawButton(1) && Math.abs(joyOP.getRawAxis(1)) >= DEADZONE){
    		shoot1.set(joyOP.getRawAxis(1));
    		shoot2.set(-joyOP.getRawAxis(1));
    		loader.set(Relay.Value.kForward);
    	}

    	else{
    		shoot1.set(0);
    		shoot2.set(0);
    	}
    	
    	if(joyOP.getRawButton(4) && Math.abs(joyOP.getRawAxis(2)) >= DEADZONE){
    		if(joyOP.getRawAxis(2) < -DEADZONE && verticalPoten.get() > verticalDownMax){
    			vertAd.set(joyOP.getRawAxis(2));
    		}
    		
    		if(joyOP.getRawAxis(2) > DEADZONE && verticalPoten.get() < verticalUpMax){
    			vertAd.set(joyOP.getRawAxis(2));
    		}
    	}
    	
    	else
    		vertAd.set(0);
    	
    	if(verticalPoten.get() <= verticalDownMax){
    		SmartDashboard.putString("Vertical", "too low");
		}
		
		if(verticalPoten.get() >= verticalUpMax){
			SmartDashboard.putString("Vertical", "too high");
		}
	}
	
	/*
	 * Prints any data that is needed to the Smart Dashboard
	 */
	@SuppressWarnings("deprecation")
	public void printData(){
    	SmartDashboard.putDouble("Vertical Potentiometer: ", verticalPoten.get());
    	SmartDashboard.putDouble("Vertical 3: ", vertAd.get());
		SmartDashboard.putDouble("Left Side: ", leftSideDrive.get());
    	SmartDashboard.putDouble("Right Side: ", rightSideDrive.get());
	}
}
