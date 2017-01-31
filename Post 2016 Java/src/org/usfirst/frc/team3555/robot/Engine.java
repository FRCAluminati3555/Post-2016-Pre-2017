package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
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
	 * This Encoder constructor takes in the 2 D I/O ports, and the encoding type that you'd like to use
	 * Encoder WPI article:
	 * https://wpilib.screenstepslive.com/s/4485/m/13809/l/599717-encoders-measuring-rotation-of-a-wheel-or-other-shaft
	 *
	 * For this robot:
	 * 1 revolution is 720 units
	 * 1 degree is 2 units
	 */
	private Encoder right = new Encoder(0, 1, true, Encoder.EncodingType.k1X);
	
	/*
	 * These speed controllers control the shooter motors
	 *
	 * Article on speed controllers: 
	 * https://wpilib.screenstepslive.com/s/4485/m/13809/l/599702-driving-motors-with-speed-controller-objects-victors-talons-and-jaguars
	 */
	private Victor shoot1;  
	private Talon shoot2; 

	/*
	 * The relay is a motor that can go either forward or backward
	 * Since the loader only needs to go two directions, the relay is used
	 * 
	 * WPI article:
	 * https://wpilib.screenstepslive.com/s/4485/m/13809/l/599706-on-off-control-of-motors-and-other-mechanisms-relays
	 */
	private Relay loader;
	
	/*
	 * Speed controller that controls the motor on the vertical alignment
	 */
	private Spark vertAd;
	
	/*
	 * Creates 3 new joy stick objects
	 * The side joysticks control each side of the robot (Depending on the drive method)
	 * Joy left and right are the two joysticks that control the drian train 
	 * JoyOP is the fancier joystick on the side, this is the Joystick for the operator, hence OP at the end of the name
	 *
	 * Article on Joysticks: 
	 * https://wpilib.screenstepslive.com/s/4485/m/13809/l/599723-joysticks
	 */
	private Joystick joyLeft;
	private Joystick joyRight;
	private Joystick joyOP;
	
	/*
	 * Creates the camera object to get feed from the camera
	 * sends feed to the dashbaord
	 */
	private CameraServer camera = CameraServer.getInstance();
	
	/*
	 * the potentiometer measures angle
	 * This potentiometer measures the angle of the vertical alignment of the shooter
	 *
	 * Article on potentiometer:
	 * https://wpilib.screenstepslive.com/s/4485/m/13809/l/599719-potentiometers-measuring-joint-angle-or-linear-motion
	 */
	private AnalogPotentiometer verticalPoten;
	
	/*
	 * These constraints keep the robot shooter from hitting either end of the front of the robot
	 */
	private final double verticalDownMax = 200;
	private final double verticalUpMax = 120;
	
	/*
	 * This variable tells whether or not arcade drive is being used
	 * if it false then tank drive is used
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
	
	/*
	 * CIRC is the circumference of the wheels on the robot
	 * Units Per Rev is the amount of units in each revolution of a wheel
	 * the other variables represent some calculations done later in the prigram
	 * revs being the amount of revolutions, revsps is the amount of revs per second, ftps is the robot speed int ft/s
	 * mips is the robot speed in miles per second
	 */
	private final static double CIRC = 38.5;
	
	private double unitsPerRev = 695, revs,revsps,revsPFT, ftps, mips;
	
	
	public Engine(){
		/*
    	 * The Sparks take in a port number from the PWM pins on the RoboRIO
    	 * The joysticks each take in port numbers, this number can be found in the driver station when joystick is plugged in
    	 * MotorGroup takes in two speed controllers, spark is a speed controller, this is a class made by the team, not WPI
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
    	
	/*
	* This makes a new potentiometer object, taking in a channel in the Analog ports on the RIO
	* this also takes in a number for scale, which is basically the amount of degrees the potentiometer can turn
	*/
    	verticalPoten = new AnalogPotentiometer(0, 360);
    	
	/*
	* right is the encoder on the right side of the robot, this method will make each puls sent from the encoder eqaul to 1
	* this is done to keep the numbers whole, and at a low number
	*/
    	right.setDistancePerPulse(1);
    	
	/*
	* These two methods set the capture of the camera to the cam0, which is usually the name of the camera on the robot
	*/
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

	/*
	 * This method will take in 3 parameters that specify the speed, and the amount of time that it will run for
	 * This will just move foward or backward, maybe spin
	 */
	public void outoDrive(int seconds, double speedLeft, double speedRight){
		leftSideDrive.set(-speedLeft);
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
	 * This method updates the shooter system
	 * it checks the operator joystick's slider and checks the axises on the joystick
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
		
	/*
	* This method will check if the shooter is moving, if so, it will check to see if it is too low or too high
	* if it in the extremes, then it will stop the shooter, however if you are trying to get out of the extremes then it will let you
	*/
    	
    	if(joyOP.getRawButton(4) && Math.abs(joyOP.getRawAxis(2)) >= DEADZONE){
    		if(joyOP.getRawAxis(2) < -DEADZONE && verticalPoten.get() < verticalUpMax + 15){//too high
    			vertAd.set(joyOP.getRawAxis(2));
    		}
    		
    		else if(verticalPoten.get() > verticalUpMax && verticalPoten.get() < verticalDownMax){//in mid
    			vertAd.set(joyOP.getRawAxis(2));
    		}
    		
    		else if(joyOP.getRawAxis(2) > DEADZONE && verticalPoten.get() > verticalDownMax - 12){//too low
    			vertAd.set(joyOP.getRawAxis(2));
    		}  
    		
    		if(joyOP.getRawAxis(2) > -DEADZONE && verticalPoten.get() < verticalUpMax + 15){//too high
    			vertAd.set(0);
    		}
    		
    		else if(joyOP.getRawAxis(2) < DEADZONE && verticalPoten.get() > verticalDownMax - 12){//too low
    			vertAd.set(0);
    		} 
    	}
    	
    	else
    		vertAd.set(0);

		/*
		* This will print out wether or not the potentiometer is too high or low, or is in the middle(all good)
		*/
    	if(verticalPoten.get() < verticalUpMax && joyOP.getRawButton(4)){
			SmartDashboard.putString("Vertical", "too High");
		}
    	
    	else if(verticalPoten.get() > verticalUpMax && verticalPoten.get() < verticalDownMax){
    		SmartDashboard.putString("Vertical", "All Good");
    	}
    	 
    	else if(verticalPoten.get() > verticalDownMax && joyOP.getRawButton(4)){
    		SmartDashboard.putString("Vertical", "too Low");
		}
	}
	
	/*
	 * This method checks if the buttons are being pressed
	 * if so, do what the button corresponds to
	 */
	double low = 200;
	double med = 174;
	double high = 148;
	
	public void checkButtons(){
//		if(joyOP.getRawButton(12)){
//			setShooterAngle(.25);
//		}
		/*
		* This block of if statements will move the shooter to three different positions using a potentiometer
		* 
		*/
		
		if(joyOP.getRawButton(8)){//low
			while(verticalPoten.get() >= low){
				vertAd.set(.5);
			}
			
			while(verticalPoten.get() <= low){
				vertAd.set(-.4);
			}
			
			vertAd.set(0);
		}
		
		if(joyOP.getRawButton(10)){//med
			while(verticalPoten.get() >= med){
				vertAd.set(.5);
			}
			
			while(verticalPoten.get() <= med){
				vertAd.set(-.4);
			}
			
			vertAd.set(0);
		}
		
		if(joyOP.getRawButton(12)){//high
			while(verticalPoten.get() >= high){
				vertAd.set(.5);
			}
			
			while(verticalPoten.get() <= high){
				vertAd.set(-.4);
			}
			
			vertAd.set(0);
		}
		
		/*
		* This block will check the buttons, and adjust the boolean value of the arcade drive to switch drive modes
		*/
		if(joyRight.getRawButton(6) || joyLeft.getRawButton(6)){
    		arcadeDrive = false;
    	}
    	
		else if(joyRight.getRawButton(7) || joyLeft.getRawButton(6)){
    		arcadeDrive = true;
    	}
	}
	
	/*
	 * This will update the speed of the drive motors using the corresponding method
	 * then say which method is being used
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
	
	/*
	* this method will reset all the encoders, this is done because if the robot is enabled and disabled (not turned off)
	* then the values will carry over to the next loop, and that wouldn't be great for measuring distance
	* however if the robot is turned off, then the values will go back to 0
	*/
	public void resetEncoders(){
		right.reset();
	}
	
	/*
	 * Prints any data that is needed to the Smart Dashboard
	 */
	@SuppressWarnings("deprecation")
	public void printData(){
		revs = right.get() / unitsPerRev;
		revsps = right.getRate() / unitsPerRev;
		ftps = (right.getRate()/unitsPerRev) * CIRC;
		mips = (((right.getRate()/unitsPerRev) * CIRC) * 5280) / 3600;
		
    	SmartDashboard.putDouble("Vertical Loader Potentiometer: ", verticalPoten.get());
    	SmartDashboard.putDouble("Vertical Loader Speed Controller: ", vertAd.get());
		SmartDashboard.putDouble("Left Side Motor: ", leftSideDrive.get());
    	SmartDashboard.putDouble("Right Side Motor: ", rightSideDrive.get());
    	
    	SmartDashboard.putNumber("Encoder Right: ", right.get());
    	SmartDashboard.putNumber("Encoder Right revs: ", revs);
    	SmartDashboard.putNumber("Encoder Right revs/sec: ", revsps);
    	
		SmartDashboard.putNumber("Encoder Right ft/s ", ftps);
		SmartDashboard.putNumber("Encoder Right m/s: ", mips);
	}
}
