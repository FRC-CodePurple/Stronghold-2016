package org.usfirst.frc.team5827.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * This is a demo program showing the use of the CameraServer class. With start
 * automatic capture, there is no opportunity to process the image. Look at the
 * IntermediateVision sample for how to process the image before sending it to
 * the FRC PC Dashboard.
 */
public class Robot extends SampleRobot {

	CameraServer server;
	AnalogInput sound = new AnalogInput(0);
	PWM r = new PWM(0);
	PWM g = new PWM(1);
	PWM b = new PWM(2);
	DigitalOutput R = new DigitalOutput(0);
	DigitalOutput B = new DigitalOutput(1);
	
	
	Joystick joy;
	//RobotDrive Drive;
	//Victor motor;
	public Robot() {

		joy = new Joystick(0);
		//Drive = new RobotDrive(0, 1);
		//motor = new Victor(1);
		server = CameraServer.getInstance();

		server.setQuality(50);
		server.startAutomaticCapture("cam1");
		// the camera name (ex "cam0") can be found through the roborio web
		// interface

	}
	

	/**
	 * 
	 * start up automatic capture you should see the video stream from the
	 * webcam in your FRC PC Dashboard.
	 */
	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {
			DriverStation.reportError(String.valueOf(sound.getValue()), false);
			//r.setRaw((int)(joy.getRawAxis(1)*255));
			//b.setRaw((int)(joy.getRawAxis(5)*255));
			
			r.setRaw(255);
			
			R.set(joy.getRawAxis(1) > 0);
			B.set(joy.getRawAxis(5) > 0);
		
			//Drive.tankDrive(-joy.getRawAxis(5), joy.getRawAxis(1));

			//if (joy.getRawButton(1)) {
				//motor.set(1);

			//} else
				//motor.set(0);
			Timer.delay(0.005);
		}
		/** robot code here! **/
		//Timer.delay(0.005); // wait for a motor update time
	}
}
