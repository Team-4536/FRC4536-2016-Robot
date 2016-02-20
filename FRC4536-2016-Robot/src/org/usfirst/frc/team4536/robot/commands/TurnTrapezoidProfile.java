package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;
import org.usfirst.frc.team4536.robot.TurningTrapezoidProfile;
import org.usfirst.frc.team4536.robot.Utilities;
import edu.wpi.first.wpilibj.Timer;

/**
 * @author Liam
 */
public class TurnTrapezoidProfile extends CommandBase {
	
	Timer timer;
	TurningTrapezoidProfile turnProfile;
	private double startingAngle;
	private double proportionalityConstant = 0.0; //TODO assign after testing profile

	/**
	 * @author Liam
	 * @param angle The desired angle the robot should travel to. May be negative or positive to indicate direction.
	 * @param maxAngularSpeed The maximum possible angular speed the robot could be traveling at. Always positive.
	 * @param maxAngularAcceleration The maximum possible angular acceleration the speed can change by. Always positive.
	 */
    public TurnTrapezoidProfile(double angle, double angularSpeed, double angularAcceleration) {
        
    	requires(driveTrain);
    	timer = new Timer();
    	turnProfile = new TurningTrapezoidProfile(angle, angularSpeed, angularAcceleration);
    }
    
	/**
	 * @author Liam
	 * @param angle The desired angle the robot should travel to. May be negative or positive to indicate direction.
	 * @param maxAngularSpeed The maximum possible angular speed the robot could be traveling at. Always positive.
	 * @param maxAngularAcceleration The maximum possible angular acceleration the speed can change by. Always positive.
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
     * @return time needed from the trapezoid profile method
     */
    public double getNeededTime(){
    	
    	return turnProfile.timeNeeded();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	timer.reset();
    	timer.start();
    	startingAngle = driveTrain.getNavXYaw();
    	//setTimeout(turnProfile.timeNeeded() + Constants.TRAPEZOID_PROFILE_TIMEOUT_OFFSET);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	driveTrain.arcadeDrive(0.0, turnProfile.throttle(timer.get()));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	return false;
        //return isTimedOut();
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
