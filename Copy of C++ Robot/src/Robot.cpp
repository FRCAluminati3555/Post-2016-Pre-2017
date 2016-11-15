#include <CameraServer.h>
#include <Joystick.h>
#include <MotorGroup.h>
#include <RobotBase.h>
#include <SampleRobot.h>
#include <Spark.h>
#include <Timer.h>
#include <cstdbool>
#include <cstdlib>
#include <Victor.h>
#include <Talon.h>
#include <Relay.h>
#include <WPILib.h>
#include "Engine.h"

class Robot: public SampleRobot {

private:
	Engine* engine;

public:
	Robot():
		engine()
	{
	}

	void Autonomous() {
		engine->OutoDrive(3, .5, .5);
	}

	void OperatorControl() {
		while (IsOperatorControl() && IsEnabled()) {
			engine->UpdateDrive();
			engine->UpdateShooter();
			engine->PrintData();

			Wait(0.005);
		}
	}

	void Test() {

	}
};

START_ROBOT_CLASS(Robot)
