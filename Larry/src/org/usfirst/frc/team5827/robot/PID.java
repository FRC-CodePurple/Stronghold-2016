package org.usfirst.frc.team5827.robot;

public class PID {

	double error;
	double errorS;
	double lasterror;
	double P;
	double I;
	double D;
	double kp;
	double ki;
	double kd;
	double target;
	
	public PID(double Target, double sVal, double kp, double ki, double kd) {
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		target = Target;
		error = sVal - target;
		errorS = error;
		
	}

	public void update(double Target, double sVal) {
		target = Target;
		lasterror = error;
		errorS = (sVal - target) + error;
		error = sVal - target;

		P();
		I();
		D();
	}

	private void P() {

		P = -kp * error;

	}

	private void I() {
		I = -ki * (errorS);
		if (error < .05 && error > -.05) {
			errorS = error;

		}

	}

	private void D() {
		D = -kd * (error - lasterror);

	}

	public double getPower() {
		/*
		 * double pow = P + I + D;
		 * 
		 * if(P + I + D <= .15 && P + I + D >= -.15) { return 0; } else if(P + I
		 * + D > .15 && P + I + D < .35) { return .35; } else if(P + I + D <
		 * -.15 && P + I + D > -.35) { return -.35; } else
		 */return (P + I + D);

	}

}
