#include <Joystick.h>
#include <RobotBase.h>
#include <SampleRobot.h>
#include <Spark.h>
#include <Timer.h>
#include <cmath>

	using namespace std;

class Robot: public SampleRobot {

private:
	Spark driveSparkL1; // controls left side
	Spark driveSparkL2;

	Spark driveSparkR1; // right side
	Spark driveSparkR2;

	Joystick joyLeft;
	Joystick joyRight;

	const double DEADZONE = .08;

public:
	Robot():
		driveSparkL1(0),
		driveSparkL2(1),
		driveSparkR1(2),
		driveSparkR2(3),

		joyLeft(0),
		joyRight(3)

	{

	}

	void RobotInit() {

	}

	void Autonomous() {

	}

	void tankDrive(){
		double leftSpeed = 0;
		double rightSpeed = 0;

		if(abs(joyLeft.GetRawAxis(1)) >= DEADZONE){
			leftSpeed = joyLeft.GetRawAxis(1);
		}

		if(abs(joyRight.GetRawAxis(1)) >= DEADZONE){
			rightSpeed = -joyRight.GetRawAxis(1);
		}

		driveSparkL1.Set(leftSpeed);
		driveSparkL2.Set(leftSpeed);

		driveSparkR1.Set(rightSpeed);
		driveSparkR2.Set(rightSpeed);
	}

	void arcadeDrive(Joystick* arcadeJoy){
		double leftSpeed = 0;
		double rightSpeed = 0;

		if(abs(arcadeJoy->GetRawAxis(1)) >= DEADZONE || abs(arcadeJoy->GetRawAxis(0)) >= DEADZONE){
			leftSpeed = arcadeJoy->GetRawAxis(1) - arcadeJoy->GetRawAxis(0);
			rightSpeed = -arcadeJoy->GetRawAxis(1) - arcadeJoy->GetRawAxis(0);
		}

		driveSparkL1.Set(leftSpeed);
		driveSparkL2.Set(leftSpeed);

		driveSparkR1.Set(rightSpeed);
		driveSparkR2.Set(rightSpeed);
	}

	void OperatorControl() {
		while (IsOperatorControl() && IsEnabled()) {
			arcadeDrive(&joyRight);

			Wait(0.005);
		}
	}

	void Test() {

	}
};

START_ROBOT_CLASS(Robot)
