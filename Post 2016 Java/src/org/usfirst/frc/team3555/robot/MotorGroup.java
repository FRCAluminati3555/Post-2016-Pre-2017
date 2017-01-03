
package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class MotorGroup {
	/*
	 * these are the fields for the passed in speed controllers
	 */
	private SpeedController speedController1;
	private SpeedController speedController2;
	
	/*
	 * constructor of the MotorGroup class
	 * this takes in 2 speed controllers, only two because that's all that is used on the robot
	 */
	public MotorGroup(SpeedController speedController1, SpeedController speedController2){
		this.speedController1 = speedController1;
		this.speedController2 = speedController2;
	}
	
	/*
	 * This method sets the speed of the speed controllers using their set method, given by the speed controller class
	 */
	public void set(double speed){
		speedController1.set(speed);
		speedController2.set(speed);
	}
	
	/*
	 * This method returns the speed of the speed controllers using their get method, given by the speed controller class
	 * only one speed controller is used here because both of the controllers are at the same speed, 
	 * and only one value can be returned
	 */
	public double get(){
		return speedController1.get();
	}
}
