package org.usfirst.frc.team5827.robot;

public class LowBar extends Robot
{
	public void go()
	{
		if (timer.get() - lastTime < 2)
		{
			drive.arcadeDrive(.5, GyroGet(0));
		} else if (timer.get() - lastTime < 4)
		{
			drive.arcadeDrive(-.5, GyroGet(0));
		} else if (timer.get() - lastTime < 6)
		{
			drive.arcadeDrive(.5, GyroGet(0));
		} else
		{
			drive.arcadeDrive(0, 0);
			drive.stopMotor();
		}
	}	
}
