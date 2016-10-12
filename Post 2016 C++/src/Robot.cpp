#include <Joystick.h>
#include <RobotBase.h>
#include <SampleRobot.h>
#include <Spark.h>
#include <Timer.h>

class Robot: public SampleRobot {

	Spark *driveSparkL; // controls left side
	Spark *driveSparkR; // right side

	Joystick *joyLeft;
	Joystick *joyRight;


public:
	const double DEADZONE = .08;

	Robot() {

	}

	void RobotInit() {
		driveSparkL = new Spark(0);
		driveSparkR = new Spark(3);

		joyLeft = new Joystick(0);
		joyRight = new Joystick(3);
	}

	void Autonomous() {

	}

	void tankDrive(){
		if(joyRight->GetRawAxis(1) >= DEADZONE || joyRight->GetRawAxis(1) <= -DEADZONE) {
			driveSparkL->Set(joyRight->GetRawAxis(1));
		}

		if(joyLeft->GetRawAxis(1) >= DEADZONE || joyLeft->GetRawAxis(1) <= -DEADZONE) {
			driveSparkR->Set(-joyLeft->GetRawAxis(1));
		}
	}

	void arcadeDrive(){
		if(joyRight->GetRawAxis(1) >= DEADZONE || joyRight->GetRawAxis(1) <= -DEADZONE || //1=y
				joyRight->GetRawAxis(0) >= DEADZONE || joyRight->GetRawAxis(0) <= -DEADZONE){ //0=x

			driveSparkL->Set(joyRight->GetRawAxis(1) - joyRight->GetRawAxis(0));
			driveSparkR->Set(-joyRight->GetRawAxis(1) - joyRight->GetRawAxis(0));
		}

		else{
			driveSparkL->Set(0);
			driveSparkR->Set(0);
		}

	}

	void OperatorControl() {
		while (IsOperatorControl() && IsEnabled()) {

			driveSparkL->Set(.3);
			driveSparkR->Set(.3);

			Wait(0.005);
		}
	}

	void Test() {

	}
};

START_ROBOT_CLASS(Robot)
