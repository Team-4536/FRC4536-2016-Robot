package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;
import org.usfirst.frc.team4536.robot.TurningTrapezoidProfile;
import org.usfirst.frc.team4536.robot.Utilities;
import edu.wpi.first.wpilibj.Timer;

/**
 * @author Liam
 * Only works with values between [-180, 180]
 */
public class TeleopTurn extends CommandBase {
	
	private Timer timer = new Timer();
	private TurningTrapezoidProfile turnProfile;
	private double proportionalityConstant;
	private double angleDiff;
	private double accumulatedError = 0.0;
	
	private double desiredAngle;
	private double angularSpeed;
	private double angularAccel;
	private double startingAngle; 
	private double navXCorrection;
	private double time;
	
	/**
	 * @author Liam
	 * @param angle the angle desired to be traveled to
	 * Sets the max angular speed and acceleration to the defaults defined in Constants
	 */
	public TeleopTurn(double angle) {
		
		this(angle, Constants.TURNING_TRAPEZOID_DEFAULT_ANGULAR_SPEED, Constants.TURNING_TRAPEZOID_DEFAULT_ANGULAR_ACCELERATION);
	}

	/**
	 * @author Liam
	 * @param angle The desired angle the robot should travel to. May be negative or positive to indicate direction on the range [-180, 180]. Negative is left and positive is right.
	 * @param maxAngularSpeed The maximum possible angular speed the robot could be traveling at. Always positive.
	 * @param maxAngularAcceleration The maximum possible angular acceleration the speed can change by. Always positive.
	 */
    public TeleopTurn(double angle, double angularSpeed, double angularAcceleration) {
    	
    	requires(driveTrain);
    	
    	proportionalityConstant = Constants.TURNING_TRAPEOID_GYRO_PROPORTIONALITY;	
    	
    	desiredAngle = angle;
    	this.angularSpeed = angularSpeed;
    	this.angularAccel  = angularAcceleration;
    	
    }
    
	/**
	 * @author Liam
	 * @param angle The desired angle the robot should travel to. May be negative or positive to indicate direction.
	 * @param maxAngularSpeed The maximum possible angular speed the robot could be traveling at. Always positive.
	 * @param maxAngularAcceleration The maximum possible angular acceleration the speed can change by. Always positive.
	 * @param custom gyro proportionality constant to override the default. Useful for command groups that may require more correction due to terrain.
	 */
    public TeleopTurn(double angle, double angularSpeed, double angularAcceleration, double gyroProportionality) {
    	
    	this(angle, angularSpeed, angularAcceleration);
    	proportionalityConstant = gyroProportionality;
    }
    
    /**
     * @author Liam
     * @return time in seconds since the command was started
     */
    public double getTime() {
    	
    	return time;
    }
    
    /**
     * @author Liam
     * @return time needed from the trapezoid profile method
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
		
		double diff = Utilities.angleDifference(driveTrain.getAngle(), (startingAngle + turnProfile.idealDistance(time)));
		
		return diff;
	}

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	timer.reset();
    	timer.start();
    	
    	startingAngle = driveTrain.getAngle();
    	angleDiff = Utilities.angleDifference(startingAngle, desiredAngle);
    	turnProfile = new TurningTrapezoidProfile(angleDiff, angularSpeed, angularAccel);
    	setTimeout(turnProfile.timeNeeded() + Constants.TURNING_TRAPEZOID_TIMEOUT_OFFSET);
    }
    
    //TODO add integral correction that Turn Trapezoid Profile possesses and see if it improve accuracy
    //TODO replace driveTrain.getNavXYaw() with driveTrain.getAngle()
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	/*time = timer.get();
    	
    	navXCorrection = Utilities.angleDifference(driveTrain.getAngle(), (startingAngle + turnProfile.idealDistance(time)));
    	
    	driveTrain.arcadeDriveAccelLimit(0, turnProfile.throttle(this.getTime())
    			+ proportionalityConstant*navXCorrection);*/
    	
    	time = timer.get();
    	
    	navXCorrection = Utilities.angleDifference(driveTrain.getAngle(), (startingAngle + turnProfile.idealDistance(time)));
    	
    	driveTrain.arcadeDriveAccelLimit(0, turnProfile.throttle(this.getTime())
    			+ proportionalityConstant*getError() + Constants.TURNING_TRAPEZOID_INTEGRAL * getAccumulatedError());
    	
    	//double throttle = turnProfile.throttle(timer.get()) + proportionalityConstant * getError(); 
    	
    	//double throttle = turnProfile.throttle(timer.get()) - proportionalityConstant * getError() - Constants.TURNING_TRAPEZOID_INTEGRAL * getAccumulatedError(); 

    	//driveTrain.arcadeDrive(0.0, throttle);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	if ((Math.abs(driveTrain.getAngle() - desiredAngle) <= Constants.TURNING_TRAPEZOID_ANGLE_THRESHOLD  &&
    		(driveTrain.getYawRate()) <= Constants.TURNING_TRAPEZOID_ANGULAR_SPEED_THRESHOLD)){ // Conditions may end
    		
    		return true;
    	}
    	else { // time out may end
    		
    		return isTimedOut();	}
    	
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
