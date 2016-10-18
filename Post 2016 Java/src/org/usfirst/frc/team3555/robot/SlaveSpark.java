package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.Spark;

public class SlaveSpark extends Spark {

	private Spark Master;
	
	public SlaveSpark(int channel, Spark Master) {
		super(channel);
		
		this.Master = Master;
	}
	
	public void set(double speed){
		speed = Master.get();
		super.set(speed);
	    Feed();
	}
}
