
package org.usfirst.frc.team5827.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogAccelerometer;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
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
	RobotDrive drive;
	Victor flipper;
	double flipperAng;

	ADXRS450_Gyro gyro2 = new ADXRS450_Gyro();
	
	
	
	Joystick joy;
	double leftx;
	double lefty;
	double rightx;
	double righty;

	double leftTarget;
	double rightTarget;

	Joystick joy2;
	double leftx2;
	double lefty2;
	double rightx2;
	double righty2;

	double triggerDrive = 0;

	double speed = 1; // Changed Speed
	double gyroAngCount;

	int auto = 0;
	int driveType = 0;

	Timer timer;

	AnalogAccelerometer acc;
	AnalogGyro gyro;

	PID PIDturn;
	PID PIDflipper;

	Encoder encDrive;
	Encoder encFlipper;

	double lastTime;

	Command autonomousCommand;
	SendableChooser autoChooser;

	public void robotInit()
	{

		autoChooser = new SendableChooser();
		autoChooser.addDefault("Low Bar", new LowBar());
		autoChooser.addObject("Rock Wall etc.", new RockWall());
		
		flipper = new Victor(2);
		oi = new OI();
		autoChooser = new SendableChooser();
		joy = new Joystick(0);
		gyroAngCount = 0;
		drive = new RobotDrive(0, 1);

		PIDturn = new PID(0, 0, .02, .0075, .3);
		PIDflipper = new PID(0, 0, .02, .005, -.5);
		timer = new Timer();

		gyro = new AnalogGyro(1);
		acc = new AnalogAccelerometer(0);
		acc.setZero(1.528);
		
		

	}

	public void disabledInit()
	{

	}

	public void disabledPeriodic()
	{
		Scheduler.getInstance().run();
	}

	public void autonomousInit()
	{
		autonomousCommand = (Command) autoChooser.getSelected();

		if (autonomousCommand != null)
			autonomousCommand.start();
		
		timer.start();
		lastTime = timer.get();
	}

	public void autonomousPeriodic()
	{
		Scheduler.getInstance().run();

		 
		if (auto == 0)
		{
			if (timer.get() - lastTime < 2)
				drive.arcadeDrive(1, GyroGet(0));
			else if (timer.get() - lastTime < 4)
				drive.arcadeDrive(0, GyroGet(gyro.getAngle() + 180));
			else if (timer.get() - lastTime < 6)
				drive.arcadeDrive(1, GyroGet(gyro.getAngle()));
			else if (timer.get() - lastTime < 8)
				drive.arcadeDrive(0, GyroGet(gyro.getAngle() + 180));
			else if (timer.get() - lastTime < 10)
				drive.arcadeDrive(1, GyroGet(gyro.getAngle()));
			else
			{
				drive.arcadeDrive(0, 0);
				drive.stopMotor();
			}
		}
		else if(auto == 1) //low bar
		{
			if(timer.get() - lastTime < 2)
			{
				drive.arcadeDrive(.5, GyroGet(0));
			}
			else if(timer.get() - lastTime < 4)
			{
				drive.arcadeDrive(-.5, GyroGet(0));				
			}
			else if(timer.get() - lastTime < 6)
			{
				drive.arcadeDrive(.5, GyroGet(0));
			}
			else
			{
				drive.arcadeDrive(0,0);
				drive.stopMotor();
			}
			
		}

	}

	public void teleopInit()
	{
		if (autonomousCommand != null)
			autonomousCommand.cancel();

		gyro.reset();
		gyro.calibrate();
	}

	public void teleopPeriodic()
	{
		
		

		Scheduler.getInstance().run();

		leftx = tolerances(joy.getRawAxis(0));
		lefty = tolerances(joy.getRawAxis(1));
		rightx = tolerances(joy.getRawAxis(4));
		righty = tolerances(joy.getRawAxis(5));

		leftx2 = tolerances(joy2.getRawAxis(0));
		lefty2 = tolerances(joy2.getRawAxis(1));
		rightx2 = tolerances(joy2.getRawAxis(4));
		righty2 = tolerances(joy2.getRawAxis(5));

		flipperAng += lefty2;
		PIDflipper.update(flipperAng, encFlipper.getRaw() * (360 / 1440));
		//flipper.set(PIDflipper.getPower());
		triggerDrive = joy.getRawAxis(2) - joy.getRawAxis(3);
		gyroAngCount += rightx;
		
		DriverStation.reportError(String.valueOf(gyro2) + "\n", false);

		if (joy.getRawButton(1))
			speed = 1.75;
		if (joy.getRawButton(2))
			speed = 1;
		if (joy.getRawButton(3))
			gyroAngCount = gyro.getAngle();

		if (driveType == 0)
			drive.arcadeDrive(lefty / speed, -GyroGet(gyroAngCount));
		else if (driveType == 1)
			drive.tankDrive(lefty / speed, righty / speed);
		else if (driveType == 2)
			drive.arcadeDrive(triggerDrive / speed, -GyroGet(gyroAngCount));

		/*
		 * //left ramp up leftTarget = joy.getRawAxis(1); if (leftTarget > lefty
		 * && leftTarget > 0) { lefty += .02; }
		 * 
		 * if (leftTarget < lefty && leftTarget < 0) { lefty -= .02; }
		 * 
		 * //right ramp up rightTarget = joy.getRawAxis(5); if (rightTarget >
		 * righty && rightTarget > 0) { righty += .02; }
		 * 
		 * if (rightTarget < righty && rightTarget < 0) { righty -= .02; }
		 */
	}

	public void testPeriodic()
	{
		LiveWindow.run();
	}

	public double GyroGet(double angle)
	{
		PIDturn.update(angle, gyro.getAngle());
		return PIDturn.getPower();

	}

	public double tolerances(double joyPos)
	{

		double out = joyPos * Math.abs(joyPos);
		if (out < .065 && out > -.065)
			out = 0;
		return out;

	}

}

