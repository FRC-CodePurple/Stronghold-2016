
package org.usfirst.frc.team5827.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import java.io.IOException;
import edu.wpi.first.wpilibj.AnalogAccelerometer;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.usfirst.frc.team5827.robot.commands.ExampleCommand;
import org.usfirst.frc.team5827.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot
{

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

	RobotDrive drive;
	Victor flipper;
	Victor shooter;
	Victor highShooter;
	double flipperAng;

	Joystick joy;
	double leftx;
	double lefty;
	double rightx;
	double righty;
	AnalogPotentiometer pot;
	Joystick joy2;
	double leftx2;
	double lefty2;
	double rightx2;
	double righty2;

	double triggerDrive = 0;
	double driveTarget;
	double speed = 1; // Changed Speed
	double gyroAngCount;

	int auto = 0;
	int driveType = 2;

	Timer timer;
	DigitalInput ballIn = new DigitalInput(6);
	DigitalInput flipperLimit = new DigitalInput(7);

	ADXRS450_Gyro gyro = new ADXRS450_Gyro();

	PID PIDturn;
	PID PIDflipper;

	CameraServer server;
	Encoder encDriveleft;
	Encoder encDriveright;

	PID PIDDrive;
	// Encoder encFlipper;

	double lastTime;

	Command autonomousCommand;
	SendableChooser chooser;

	public void robotInit()
	{

		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam1");

		pot = new AnalogPotentiometer(0, 250, -21);
		flipper = new Victor(2);
		shooter = new Victor(3);
		highShooter = new Victor(4);
		oi = new OI();
		chooser = new SendableChooser();
		joy = new Joystick(0);
		joy2 = new Joystick(1);

		encDriveleft = new Encoder(2, 3);
		encDriveleft.setDistancePerPulse(25.1327412287 / 1440.0);
		encDriveright = new Encoder(4, 5);
		encDriveright.setDistancePerPulse(25.1327412287 / 1440.0);

		PIDDrive = new PID(0, 0, .02, .0075, .3);

		drive = new RobotDrive(0, 1);
		// encFlipper = new Encoder(0, 1);
		PIDturn = new PID(0, 0, .02, .0075, .3);
		PIDflipper = new PID(0, 0, .02, .005, 0);
		timer = new Timer();
		// gyro = new ADXRS450_Gyro();

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
		// autonomousCommand = (Command) chooser.getSelected();

		if (autonomousCommand != null)
			autonomousCommand.start();
		gyro.reset();
		gyro.calibrate();
		timer.start();
		lastTime = timer.get();
		driveTarget = 0;
	}

	public void autonomousPeriodic()
	{
		Scheduler.getInstance().run();
		PIDDrive.update(driveTarget, (encDriveleft.getDistance() + encDriveright.getDistance()) / 2);

		if (auto == 0)
		{
			// DriverStation.reportError(String.valueOf(gyro.getAngle()) + "\n",
			// false);
			if (timer.get() - lastTime < .75)
				drive.arcadeDrive(1, -GyroGet(0));
			else if(timer.get() - lastTime < 2.25)
				drive.arcadeDrive(.75, -GyroGet(0));
			else if (timer.get() - lastTime < 5)
				drive.arcadeDrive(0, -GyroGet(180));
			/*
			 * else if (timer.get() - lastTime < 7) drive.arcadeDrive(.75,
			 * -GyroGet(180)); else if (timer.get() - lastTime < 8)
			 * drive.arcadeDrive(0, -GyroGet(0)); else if (timer.get() -
			 * lastTime < 11) drive.arcadeDrive(.75, -GyroGet(0));
			 */
			else
				drive.arcadeDrive(0, 0);
																															
		} else if (auto == 1)
		{
			if (timer.get() - lastTime < 3)
				drive.arcadeDrive(.75, -GyroGet(0));
			else if (timer.get() - lastTime < 6)
				drive.arcadeDrive(-.75, -GyroGet(0));
			else if (timer.get() - lastTime < 9)
				drive.arcadeDrive(.75, -GyroGet(0));
			else
				drive.arcadeDrive(0, 0);
		}

	}

	public void teleopInit()
	{
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		gyro.reset();
		gyro.calibrate();
		flipperAng = 170;
		// encFlipper.reset();
	}

	public void teleopPeriodic()
	{
		Scheduler.getInstance().run();

		setJoy();

		drive();

		flipperStuff();

		buttons();
		//DriverStation.reportError(String.valueOf(flipperLimit.get()), false);
		// DriverStation.reportError(String.valueOf(tolerances(righty2)) + "\n",
		// false);
		//Driverstation.reportError("Death to your family, a curse upon your name");
		DriverStation.reportError(String.valueOf(170 - pot.get()) + " , " +
		 String.valueOf(flipperAng) + " , " + String.valueOf(gyro.getAngle()) +"\n", false);

	}

	public void testPeriodic()
	{
		LiveWindow.run();
	}

	public void drive()
	{
		if (driveType == 0)
			driveArcade();
		else if (driveType == 1)
			driveTank();
		else if (driveType == 2)
			driveTrigger();

	}

	public void buttons()
	{
		// if (joy.getRawButton(1))
		// shooter.set(1);
		// if (joy.getRawButton(2))
		// shooter.set(-1);
		if (joy.getRawButton(3))
			gyroAngCount = gyro.getAngle();

	}

	public void driveTank()
	{
		flipperAng += joy.getRawAxis(2) - joy.getRawAxis(3);
		drive.tankDrive(righty / speed, lefty / speed);

	}

	public void driveArcade()
	{
		flipperAng += joy.getRawAxis(2) - joy.getRawAxis(3);
		gyroAngCount += rightx * 3.5;
		drive.arcadeDrive(lefty / speed, -GyroGet(gyroAngCount));

	}

	public void driveTrigger()
	{

		flipperAng += tolerances(lefty2);
		highShooter.set(tolerances(joy2.getRawAxis(2)) - joy2.getRawAxis(3));
		if (righty2 > .6)
		{

			righty2 = .6;
		}

		if (joy2.getRawButton(4)) // mid
		{
			flipperAng = 75;

		}

		if (joy2.getRawButton(3))// robot
		{
			flipperAng = 170;

		}

		if (joy2.getRawButton(2))// ground
		{
			flipperAng = 0;

		}
		if (false)
		{
			shooter.set(-.3);
		} else
		{

			if (ballIn.get())// if no ball

			{

				shooter.set(tolerances(righty2));
			} else if (tolerances(righty2) < 0)
			{

				shooter.set(tolerances(righty2));
			} else
				shooter.set(0);
		}
		gyroAngCount += leftx * 3.5;
		triggerDrive = tolerances(joy.getRawAxis(2)) - joy.getRawAxis(3);
		drive.arcadeDrive(triggerDrive / speed, -GyroGet(gyroAngCount));

	}

	public void flipperStuff()
	{

		if (flipperAng < 0)
			flipperAng = 0;

		if (flipperAng > 170)
			flipperAng = 170;

		PIDflipper.update(flipperAng, 170 - pot.get());
//		if (!flipperLimit.get() && PIDflipper.getPower() > 0)
	//	{
		//	flipper.set(0);

		//} else
		//{
			flipper.set(PIDflipper.getPower() / 3);
		//}
			
			//DriverStation.reportError(String.valueOf(170 - pot.get()), false);
	}

	public void setJoy()
	{

		leftx = tolerances(joy.getRawAxis(0));
		lefty = tolerances(joy.getRawAxis(1));
		rightx = tolerances(joy.getRawAxis(4));
		righty = tolerances(joy.getRawAxis(5));

		leftx2 = tolerances(joy2.getRawAxis(0));
		lefty2 = tolerances(joy2.getRawAxis(1));
		rightx2 = tolerances(joy2.getRawAxis(4));
		righty2 = tolerances(joy2.getRawAxis(5));
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