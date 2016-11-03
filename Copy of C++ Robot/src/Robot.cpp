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

class Robot: public SampleRobot {

private:
	MotorGroup* leftSideDrive;
	MotorGroup* rightSideDrive;

	Spark vertAd;

	Victor shoot1;
	Talon shoot2;

	Joystick joyLeft;
	Joystick joyRight;
	Joystick joyOP;

	Relay loader;

	CameraServer* camera;

	const double DEADZONE = .08, LOADZONE = .8;

	bool arcadeDriveVar;

public:
	Robot():
//		leftSide(Spark(0), Spark(1)),
//		rightSide(Spark(2), Spark(3)),

		shoot1(5),
		shoot2(6),

		loader(2),
		vertAd(4),

		joyOP(0),
		joyLeft(1),
		joyRight(2),

		camera(CameraServer::GetInstance())
	{
		camera->SetQuality(50);
		camera->StartAutomaticCapture("cam0");
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

		leftSideDrive->Set(leftSpeed);
		rightSideDrive->Set(rightSpeed);
	}

	void Autonomous() {
//		int seconds = 3;
//		long startTime = System.currentTimeMillis();
//
//		while(System.currentTimeMillis() < startTime + seconds * 1000){
//			leftSideDrive->Set(-.75);
//			rightSideDrive->Set(.75);
//		}
//
//		leftSideDrive->Set(0);
//		rightSideDrive->Set(0);
	}

	void arcadeDrive(Joystick* arcadeJoy){
		double leftSpeed = 0;
		double rightSpeed = 0;

		if(abs(arcadeJoy->GetRawAxis(1)) >= DEADZONE || abs(arcadeJoy->GetRawAxis(0)) >= DEADZONE){
			leftSpeed = arcadeJoy->GetRawAxis(1) - arcadeJoy->GetRawAxis(0);
			rightSpeed = -arcadeJoy->GetRawAxis(1) - arcadeJoy->GetRawAxis(0);
		}

		leftSideDrive->Set(leftSpeed);
		rightSideDrive->Set(rightSpeed);
	}

	void updateShooter(){
		if(joyOP.GetRawAxis(3) <= -LOADZONE){
			loader.Set(Relay::Value::kReverse);
		}

		else if(joyOP.GetRawAxis(3) >= LOADZONE){
			loader.Set(Relay::Value::kForward);
		}

		else{
			loader.Set(Relay::Value::kOff);
		}

		if(joyOP.GetRawButton(1) && abs(joyOP.GetRawAxis(1)) >= DEADZONE){
			shoot1.Set(joyOP.GetRawAxis(1));
			shoot2.Set(-joyOP.GetRawAxis(1));
			loader.Set(Relay::Value::kForward);
		}

		else{
			shoot1.Set(0);
			shoot2.Set(0);
		}

		if(joyOP.GetRawButton(4) && abs(joyOP.GetRawAxis(2)) >= DEADZONE){
			vertAd.Set(joyOP.GetRawAxis(2));
		}

		else
			vertAd.Set(0);
	}

	void checkButtons(){
		if(joyRight.GetRawButton(6) || joyLeft.GetRawButton(6)){
			arcadeDriveVar = false;
		}

		else if(joyRight.GetRawButton(7) || joyLeft.GetRawButton(6)){
			arcadeDriveVar = true;
		}
	}

	void updateDrive(){
		checkButtons();

		if(arcadeDriveVar){
			SmartDashboard::PutString("You are currently using: ", "Arcade Drive");
			arcadeDrive(&joyRight);
		}

		else{
			SmartDashboard::PutString("You are currently using: ", "Tank Drive");
			tankDrive();
		}
	}

	void printData(){
//		SmartDashboard::PutDouble("Vertical Potentiometer: ", verticalPoten->Get());
		SmartDashboard::PutNumber("Vertical 3: ", vertAd.Get());
		SmartDashboard::PutNumber("Left Side: ", leftSideDrive->Get());
		SmartDashboard::PutNumber("Right Side: ", rightSideDrive->Get());
	}

	void OperatorControl() {
		while (IsOperatorControl() && IsEnabled()) {
			updateDrive();
			updateShooter();
			printData();

			Wait(0.005);
		}
	}

	void Test() {

	}
};

START_ROBOT_CLASS(Robot)
