package org.usfirst.frc.team5827.robot;

public class RockWall extends Robot
{
	public void go()
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
}
