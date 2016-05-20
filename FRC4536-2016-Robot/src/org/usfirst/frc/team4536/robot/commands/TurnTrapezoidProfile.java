package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;
import org.usfirst.frc.team4536.robot.TurningTrapezoidProfile;
import org.usfirst.frc.team4536.robot.Utilities;
import edu.wpi.first.wpilibj.Timer;

/**
 * @author Liam
 * Only works with values between [-180, 180]
 */
public class TurnTrapezoidProfile extends CommandBase {
	
	Timer timer;
	TurningTrapezoidProfile turnProfile;
	private double proportionalityConstant;
	private double angleDiff;
	private double accumulatedError = 0.0; // The accumulated error over time
	
	/**
	 * @author Liam
	 * @param angle the angle desired to be traveled to in degrees
	 * Sets the max angular speed in degrees per second and acceleration in degrees per second squared to the defaults defined in Constants
	 */
	public TurnTrapezoidProfile(double angle) {
		
		this(angle, Constants.TURNING_TRAPEZOID_DEFAULT_ANGULAR_SPEED, Constants.TURNING_TRAPEZOID_DEFAULT_ANGULAR_ACCELERATION);
	}

	/**
	 * @author Liam
	 * @param angle The desired angle the robot should travel to in degrees. May be negative or positive to indicate direction on the range [-180, 180]. Negative is left and positive is right.
	 * @param maxAngularSpeed The maximum possible angular speed the robot could be traveling at in degrees per second. Scalar value so always positive.
	 * @param maxAngularAcceleration The maximum possible angular acceleration in degrees per second squared the speed can change by. Always positive.
	 */
    public TurnTrapezoidProfile(double angle, double angularSpeed, double angularAcceleration) {
    	
    	proportionalityConstant = Constants.TURNING_TRAPEOID_GYRO_PROPORTIONALITY;
    	requires(driveTrain);
    	timer = new Timer();
    	turnProfile = new TurningTrapezoidProfile(angle, angularSpeed, angularAcceleration);
    }
    
	/**
	 * @author Liam
	 * @param angle The desired angle the robot should travel to in degrees. May be negative or positive to indicate direction.
	 * @param maxAngularSpeed The maximum possible angular speed the robot could be traveling at in degrees per second. Scalar so always positive.
	 * @param maxAngularAcceleration The maximum possible angular acceleration in degrees per second squared the speed can change by. Always positive.
	 * @param custom gyro proportionality constant to override the default. Useful for command groups that may require more correction due to terrain.
	 */
    public TurnTrapezoidProfile(double angle, double angularSpeed, double angularAcceleration, double gyroProportionality) {
    	
    	this(angle, angularSpeed, angularAcceleration);
    	proportionalityConstant = gyroProportionality;
    }
    
    /**
     * @author Liam
     * @return time in seconds since the command was started
     */
    public double getTime() {
    	
    	return timer.get();
    }
    
    /**
     * @author Liam
     * @return time needed from the trapezoid profile method in seconds
     */
    public double getNeededTime(){
    	
    	return turnProfile.timeNeeded();
    }
    
	/**
	 * @author Liam
	 * @return the accumulatedError in degree seconds
	 */
	public double getAccumulatedError() {
		
		accumulatedError += getError() * Utilities.getCycleTime();
		
		return accumulatedError;
	}
	
	/**
	 * @author Liam
	 * @return the error from the most recent cycle of code in degree seconds
	 */
	public double getError() {
		
		double diff = -Utilities.angleDifference(driveTrain.getNavXYaw(), turnProfile.idealDistance(timer.get()));
		
		return diff;
	}

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	driveTrain.resetNavX(driveTrain.getAngle());
    	accumulatedError = 0.0;
    	timer.reset();
    	timer.start();
    	setTimeout(turnProfile.timeNeeded() + Constants.TURNING_TRAPEZOID_TIMEOUT_OFFSET);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	    	
    	double throttle = turnProfile.throttle(timer.get()) - proportionalityConstant * getError() - Constants.TURNING_TRAPEZOID_INTEGRAL * getAccumulatedError(); 
    	
    	driveTrain.arcadeDrive(0.0, throttle);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	if ((driveTrain.getNavXYaw() >= turnProfile.getAngle()-Constants.TURNING_TRAPEZOID_ANGLE_THRESHOLD &&
    			driveTrain.getNavXYaw() <= turnProfile.getAngle() + Constants.TURNING_TRAPEZOID_ANGLE_THRESHOLD) &&
    		(driveTrain.getYawRate() >= -Constants.TURNING_TRAPEZOID_ANGULAR_SPEED_THRESHOLD &&
    			driveTrain.getYawRate() <= Constants.TURNING_TRAPEZOID_ANGULAR_SPEED_THRESHOLD)){ // Conditions may end
    		
    		System.out.println("TurnTrapezoidProfile finished from ending criteria.");
    		
    		return true;
    	}
    	else { // time out may end
    		
    		System.out.println("TurnTrapezoidProfile finished from timing out.");
    		
    		return isTimedOut();
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    	driveTrain.arcadeDrive(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	
    	end();
    }
}
