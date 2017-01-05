
package org.usfirst.frc.team5827.robot;

import edu.wpi.first.wpilibj.DriverStation;
import java.io.IOException;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc.team5827.robot.commands.ExampleCommand;
import org.usfirst.frc.team5827.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	RobotDrive Drive;
	Victor motor;
	Timer timer;
	double timeLast = 0;
	Victor motor2;
	Joystick joy;
	double time;
	Encoder enc;
	double angle;
	Servo servo = new Servo(4);
	

	Command autonomousCommand;
	SendableChooser chooser;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		oi = new OI();
		joy = new Joystick(0);
		motor = new Victor(4);
		//motor2 = new Victor(3);
		angle = 0;
		// d = new SmartDashboard();
		// d = new DriverStation();
		Drive = new RobotDrive(0, 1, 2, 3);
		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", new ExampleCommand());
		//chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		enc = new Encoder(2,3);
		enc.setDistancePerPulse(360/2048);
		//enc.setDistancePerPulse(0.17578125);
		//enc.setMinRate(0);
		enc.reset();
		timer = new Timer();
		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	public void autonomousInit() {
		autonomousCommand = (Command) chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		time = 0;
		if (autonomousCommand != null)
			autonomousCommand.start();
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

		Scheduler.getInstance().run();
		if (time < 500) {
			Drive.tankDrive(.5, .5);
			

		} else if(time < 600)
		{
			//motor.set(1);
		} else if(time < 700)
		{
			motor.set(-1);
			
		}
		else 
			{Drive.tankDrive(-.5, -.5);
			//motor.set(0);
			
			}
		
			//wait(100);
			
			
		
		
	//System.currentTimeMillis();
//		Timer.delay(0.01);
//		time += 1;
		SmartDashboard.putString("DB/String 0", ((Double) time++).toString());
		
		// output("Test");
		// d.putInt("r", 5827);

	}

	public void teleopInit() {
		
		// TkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOooo
		// his makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		timer.start();
		enc.reset();
		timeLast = timer.get();
		//motor.set(1);
		//motor2.set(-1);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		motor.set(1);
		//motor.set(enc.getRate()/2048);
		DriverStation.reportError(String.valueOf(angle) + "\n",false);
		angle = enc.getDistance();
		if(angle > 360) angle = angle - (360 * ((int)enc.getDistance() / 360));
		if(angle < 0) angle = angle + (360 * Math.abs(((int)enc.getDistance() / 360)));
		Drive.tankDrive(-joy.getRawAxis(5), joy.getRawAxis(1));
		
		//motor.set(enc.getRate()*2000000);
		
		//if (joy.getRawButton(8) == true) {
		/*if(timer.get() - timeLast >  .1)
		{
			motor.set(-motor.get());
			//motor2.set(-motor2.get());
			timeLast = timer.get();

		} */
		//else
		//{
			//motor.set(0);
			//motor2.set(0);
			//motor.stopMotor();
		
			//motor2.stopMotor();
		//}
		  
		 
		//motor.set(enc.getRate()/2048);
		//System.out.println("LLLLLLLLLLLLLLLLLLLLLLAAAAAAAAAAAAAAAAAARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRYYYYYYYYYYYYYYYYYYYYYY!!!!!!!!!");
	
		//("DB/String 0", ((Double) enc.getRate()).toString());
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
		//artDashboard.putString("DB/String 0", enc.getRate()).toString());
		//SmartDashboard.putString("DB/String 0", "Our code is in poiple! and we be messin w/ sasquach!");
		//String dashData = SmartDashboard.getString("DB/String 0", "myDefaultData");
		SmartDashboard.putNumber("Encoder", enc.getRate());
		
	}
}
