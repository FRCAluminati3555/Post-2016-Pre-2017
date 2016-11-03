/*
 * MotorGroup.h
 *
 *  Created on: Nov 1, 2016
 *      Author: frcAluminati
 */

#ifndef SRC_MOTORGROUP_H_
#define SRC_MOTORGROUP_H_

#include <SpeedController.h>

class MotorGroup {

private :
	SpeedController* speedController1;
	SpeedController* speedController2;

public :
	MotorGroup(SpeedController* sC1, SpeedController* sC2) :
		speedController1(sC1),
		speedController2(sC2)
	{

	}

	void Set(double speed){
		speedController1->Set(speed);
		speedController2->Set(speed);
	}

	double Get(){
		return speedController1->Get();
	}
};


#endif /* SRC_MOTORGROUP_H_ */
