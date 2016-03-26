package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc.team4536.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	/*----------------Initializations------------------*/
	
	//Joysticks
	public static Joystick mainStick = new Joystick(RobotMap.MAIN_STICK);
	public static Joystick secondaryStick = new Joystick(RobotMap.SECONDARY_STICK);
	public static Joystick tertiaryStick = new Joystick(RobotMap.TERTIARY_STICK);
	
	//Buttons
	public static Button intake; 
	public static Button eject;
	public static Button hold;
	public static Button plop;
	public static Button autoIntake;
	public static Button turnTest;
	public static Button deployScissors;
	public static Button TurnNDriveCurrentAngle;
	public static Button TurnNDrive0;
	public static Button TurnNDrive180;
	public static Button TurnNDriveRightGoal;
	public static Button TurnNDriveLeftGoal;
	public static Button TurnNDrive90;
	
	/**
	 * @author Liam
	 * This instantiates Button handling.
	 */
	public static void buttonHandling () {
		
		intake = new JoystickButton(secondaryStick, 5);
		eject = new JoystickButton(secondaryStick, 6);
		hold = new JoystickButton(secondaryStick, 7);
		autoIntake = new JoystickButton(secondaryStick, 4);
		plop = new JoystickButton(secondaryStick, 12);
		
		deployScissors = new JoystickButton(tertiaryStick, 1);
		
		TurnNDriveCurrentAngle = new JoystickButton(mainStick, RobotMap.HOLD_AT_CURRENT_ANGLE);
		TurnNDrive0 = new JoystickButton(mainStick, RobotMap.HOLD_AT_0_DEGREES);
		TurnNDrive180 = new JoystickButton(mainStick, RobotMap.HOLD_AT_180_DEGREES);
		TurnNDriveRightGoal = new JoystickButton(mainStick, RobotMap.HOLD_AT_ANGLE_FOR_RIGHT_GOAL);
		TurnNDriveLeftGoal = new JoystickButton(mainStick, RobotMap.HOLD_AT_ANGLE_FOR_LEFT_GOAL);
		TurnNDrive90 = new JoystickButton(mainStick, 6);
		
		/*-------------------Actions------------------------*/

		intake.whenPressed(new IntakeBoulderAccelLimited());
		eject.whenPressed(new EjectBoulderAccelLimited());
		hold.whenPressed(new HoldBoulderAccelLimited());
		autoIntake.whenPressed(new AutoIntake());
		TurnNDriveCurrentAngle.whenPressed(new TurnNDrive());
		TurnNDrive0.whenPressed(new TurnNDrive(0));
		TurnNDrive180.whenPressed(new TurnNDrive(180));
		TurnNDriveRightGoal.whenPressed(new TurnNDrive(-60));
		TurnNDriveLeftGoal.whenPressed(new TurnNDrive(60));
		TurnNDrive90.whenPressed(new TurnNDrive(90));

		/*------------------Canceling actions--------------*/
		
		intake.whenReleased(new DriveIntakeAccelLimited());
		eject.whenReleased(new DriveIntakeAccelLimited());
		hold.whenReleased(new DriveIntakeAccelLimited());
		autoIntake.whenReleased(new DriveIntakeAccelLimited());
		
		TurnNDriveCurrentAngle.whenReleased(new DriveAccelLimited());
		TurnNDrive0.whenReleased(new DriveAccelLimited());
		TurnNDrive180.whenReleased(new DriveAccelLimited());
		TurnNDriveRightGoal.whenReleased(new DriveAccelLimited());
		TurnNDriveLeftGoal.whenReleased(new DriveAccelLimited());
		TurnNDrive90.whenReleased(new DriveAccelLimited());
	}
}
