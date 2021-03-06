package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.RobotMap;
import org.usfirst.frc.team4536.robot.subsystems.Intake;
import org.usfirst.frc.team4536.robot.subsystems.*;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CommandBase extends Command {
	
	public static DriveTrain driveTrain = new DriveTrain(RobotMap.LEFT_MOTOR_BACK, 			  RobotMap.LEFT_MOTOR_FRONT,
															RobotMap.RIGHT_MOTOR_BACK, 		  RobotMap.RIGHT_MOTOR_FRONT,
															RobotMap.LEFT_ENCODER_CHANNEL_A,  RobotMap.LEFT_ENCODER_CHANNEL_B,
															RobotMap.RIGHT_ENCODER_CHANNEL_A, RobotMap.RIGHT_ENCODER_CHANNEL_B,
															RobotMap.FRONT_ULTRA_CHANNEL, 	  RobotMap.BACK_ULTRA_CHANNEL,
															RobotMap.LEFT_IR_CHANNEL);
	public static Intake intake = new Intake(RobotMap.INTAKE_MOTOR_CHANNEL, RobotMap.INTAKE_RELAY, RobotMap.INTAKE_IR_CHANNEL);
	public static ScissorLift scissorLift = new ScissorLift(RobotMap.SCISSOR_MOTOR);
	
    public CommandBase() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}