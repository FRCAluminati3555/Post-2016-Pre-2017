package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.Spark;

public class SlaveSpark extends Spark {

	/*
	 * This Spark is the master of this spark class
	 * In this program, this means that this SlaveSpark will always follow the speed of the master
	 */
	
	private Spark Master;
	
	/*
	 * For proper encapsulation, the slave spark follows the other spark, but to do this the slave must override the spark set method
	 * this method requires the parameter in order for it to be overwritten
	 * in the slave set method, it sets the speed to the master spark, so the speed of the method parameter that is passed in
	 * is irrelevent, but at the same time significantly required
	 */
	private int dummySpeed = 0;
	
	
	/*
	 * Constructor that takes in the channel of the PWM slot in the RIO
	 * The channel number is necassary to create the Spark for motor control
	 * This constructor gets a Spark passed in, this is the master Spark
	 * The Spark that is passed in will always be followed by this SlaveSpark 
	 * 
	 * Super will call the constructor of the parent of this class, which is the regular Spark class
	 */
	public SlaveSpark(int channel, Spark Master) {
		super(channel);
		
		this.Master = Master;
	}
	
	/*
	 * In order to make the method call look nicer, this method is called to call the set method 
	 * This makes it so the dummySpeed is handles here, rather than in the Robot class
	 * Also, calling this method by a different name makes it easier to distinguish between slave and master in the Robot class
	 */
	
	public void update(){
		set(dummySpeed);
	}
	
	/*
	 * This method creation actually overrides the method that is in the regular Spark class
	 * This means that if someone tries to call the set method on a SlaveSpark, all it will do is set itself to the master's speed
	 * This ensures that the two speed controllers will always send out the same instruction
	 */
	public void set(double speed){
		speed = Master.get();
		super.set(speed);
	    Feed();
	}
}
