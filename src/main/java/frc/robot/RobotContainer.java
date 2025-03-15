// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.DriveWithJoystick;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.SpinClaw;
import frc.robot.commands.Climber.Climb;
import frc.robot.commands.Auto.Autos;
import frc.robot.commands.Auto.RotateToAngle;
import frc.robot.commands.Claw.Intake;
import frc.robot.commands.Claw.Outtake;
import frc.robot.commands.Elevator.MoveElevatorToPosition;
import frc.robot.commands.Elevator.MoveElevatorWithJoystick;
import frc.robot.commands.Wrist.WristToPosition;
import frc.robot.commands.Wrist.WristWithJoystick;
import frc.robot.Constants.ElevatorConstants.ElevatorHeights;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.*;

import static frc.robot.Constants.ElevatorConstants.*;
import static frc.robot.Constants.WristConstants.*;

import java.io.Writer;
import java.security.AlgorithmConstraints;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Commands;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.SendableCameraWrapper;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {


  // Because the angles are the same for both L2 & L3, there will only be an L2
  // command that will be used for both

  private final MoveElevatorWithJoystick moveElevatorWithJoystick;

  private Command onStop;
  private SpinClaw intakeCommand;
  private Command outtakeCommand;

  private final JoystickButton toggleClimbButton;
  private final JoystickButton climbButton;


  // The robot's subsystems and commands are defined here...
  
  // Controllers 
  private XboxController operator;
  private XboxController driver;

  // Subsytems 
  private Wrist wrist;
  private Elevator elevator;
  private SwerveDrive swerve;
  private Claw claw;
  private Climber climber;


  // Commands
  private Intake intake;
  private Outtake outtake;
  private WristWithJoystick wristWithJoy;
  private DriveWithJoystick driveWithJoystick;
  private Command fieldOrienatedCommand;
  private Command slowModeToggle;
  private RotateToAngle rotateToAngle;
  private Climb climb;

  private Command toggleCoralMode;
  private Command toggleAlgaeMode;

  private Command reverseCoralIntake;
  private Command slowOuttake;

  private Command resetEncoder;

  private SendableChooser<Command> autoChooser;

  private SequentialCommandGroup goToIntake;
  private SequentialCommandGroup goToL2;
  private SequentialCommandGroup goToL3;
  private SequentialCommandGroup goToL4;
  private SequentialCommandGroup goToProcessor;
  private ParallelCommandGroup climbSequence;

  // Buttons
  private JoystickButton intakeButton;
  private JoystickButton outtakeButton;
  private JoystickButton level1Button;
  private JoystickButton level2Button;
  private JoystickButton level3Button;
  private JoystickButton level4Button;

  private JoystickButton fieldOrienatedButton;
  private JoystickButton slowModeButton;

  private JoystickButton elevatorOverrideButton;
  private JoystickButton wristOverrideButton;

  private POVButton toggleCoralModeButton;
  private POVButton toggleAlgaeModeButton;

  private POVButton resetEncoderButton;

  private JoystickButton rotate90Button;

  private JoystickButton reverseCoralButton;
  private JoystickButton slowOuttakeButton;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // The robot's subsystems and commands are defined here...

    // Subsytems 
    wrist = new Wrist();
    elevator = new Elevator();
    claw = new Claw();
    swerve = new SwerveDrive();
    climber = new Climber();

    

    SmartDashboard.putData(wrist);


    // Controllers
    driver = new XboxController(0);
    operator = new XboxController(1);

    

    // Commands 
    wristWithJoy = new WristWithJoystick(operator, wrist);
    moveElevatorWithJoystick = new MoveElevatorWithJoystick(elevator,wrist, operator);
    rotateToAngle = new RotateToAngle(180, swerve);

    reverseCoralIntake = Commands.startEnd(() -> {claw.spin(.1);}, () -> {claw.spin(0);}, claw);
    
    slowOuttake = Commands.runOnce(() -> { claw.slowOutakeCoral(); });

    resetEncoder = Commands.runOnce(() -> { wrist.resetEncoder(); });

    intake = new Intake(claw, wrist);
    outtake = new Outtake(wrist, claw);

    toggleAlgaeMode = new SequentialCommandGroup(Commands.runOnce(() -> {wrist.setAlgaeMode();}, wrist), new WristToPosition(wrist, WristPositions.TOGGLE_POSITION));
    toggleCoralMode = new SequentialCommandGroup(Commands.runOnce(() -> {wrist.setCoralMode();}, wrist), new WristToPosition(wrist, WristPositions.TOGGLE_POSITION));

    fieldOrienatedCommand = Commands.runOnce(() -> {
        swerve.toggleFieldOriented();
      }, swerve);

    slowModeToggle = Commands.runOnce(() -> {swerve.toggleSlowMode();}, swerve);
    onStop = Commands.runOnce(() -> { claw.stopButton(); }, claw);
    outtakeCommand = Commands.runOnce(() -> { claw.outTakeCoral(); }, claw);
    intakeCommand = new SpinClaw(claw);

    // Creating sequential command groups that use wrist and elevator
    goToIntake = new SequentialCommandGroup(
      new WristToPosition(wrist, WristPositions.TOGGLE_POSITION),
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_POSITION_L1),
      new ParallelCommandGroup(
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_POSITION_L1, true), 
      new WristToPosition(wrist, WristPositions.LOW_WRIST_POSITION)
      )
    );

    goToL2 = new SequentialCommandGroup(
      new WristToPosition(wrist, WristPositions.TOGGLE_POSITION),
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_MIDDLE_POSITION_L2), 
      new ParallelCommandGroup(
        new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_MIDDLE_POSITION_L2, true), 
        new WristToPosition(wrist, WristPositions.MIDDLE_WRIST_POSITION)
      )
    );

    goToL3 = new SequentialCommandGroup(
      new WristToPosition(wrist, WristPositions.TOGGLE_POSITION),
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.HIGH_MIDDLE_POSITION_L3), 
      new ParallelCommandGroup(
        new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.HIGH_MIDDLE_POSITION_L3, true), 
        new WristToPosition(wrist, WristPositions.MIDDLE_WRIST_POSITION)
      )
    );

    goToL4 = new SequentialCommandGroup(
      new WristToPosition(wrist, WristPositions.TOGGLE_POSITION),
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.HIGH_POSITION_L4),
      new ParallelCommandGroup(
        new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.HIGH_POSITION_L4, true), 
        new WristToPosition(wrist, WristPositions.HIGH_WRIST_POSITION)
      )
    );

    goToProcessor = new SequentialCommandGroup(
      new WristToPosition(wrist, WristPositions.TOGGLE_POSITION),
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.PROCESSOR_POSITION),
      new ParallelCommandGroup(
        new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.PROCESSOR_POSITION, true), 
        new WristToPosition(wrist, WristPositions.ALGAE_WRIST_POSITION)
      )
    );

    climbSequence = new ParallelCommandGroup(
      new Climb(climber),
      Commands.run(() -> {
        if(climber.getClimbMode()){
          if(Climb.getIter() % 2 == 1)
            climber.funnelUp();
          else
            climber.funnelDown();
        }
      }, climber)
    );
    // Button Assigments 
    level1Button = new JoystickButton(operator, XboxController.Button.kA.value );
    level2Button = new JoystickButton(operator, XboxController.Button.kB.value);
    level3Button = new JoystickButton(operator, XboxController.Button.kX.value);
    level4Button = new JoystickButton(operator, XboxController.Button.kY.value);

    intakeButton = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);
    outtakeButton = new JoystickButton(operator,XboxController.Button.kRightBumper.value);

    toggleAlgaeModeButton = new POVButton(operator, 0);
    toggleCoralModeButton = new POVButton(operator, 180);

    // Configure the trigger bindings
    
    toggleClimbButton = new JoystickButton(driver, XboxController.Button.kX.value);
    climbButton = new JoystickButton(driver, XboxController.Button.kB.value);
    
    //wrist.setDefaultCommand(wristToAlgae);
    elevator.setDefaultCommand(moveElevatorWithJoystick);

    // Configure the trigger bindings


    resetEncoderButton = new POVButton(operator, 270);

    fieldOrienatedButton = new JoystickButton(driver, XboxController.Button.kY.value);
    slowModeButton = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    driveWithJoystick = new DriveWithJoystick(swerve, driver);

    wristOverrideButton = new JoystickButton(operator, XboxController.Button.kStart.value);
    elevatorOverrideButton = new JoystickButton(operator, XboxController.Button.kBack.value);

    rotate90Button = new JoystickButton(driver, XboxController.Button.kA.value);

    slowOuttakeButton = new JoystickButton(operator, XboxController.Button.kRightStick.value);
    reverseCoralButton = new JoystickButton(operator, XboxController.Button.kLeftStick.value);

    // Autos
    autoChooser = new SendableChooser<>();
    //autoChooser.addOption("PathPlannerTest", new PathPlannerAuto("New Auto"));
    autoChooser.addOption("Drive Foward", Autos.driveFoward(swerve));
    autoChooser.addOption("Score Middle", Autos.scoreMiddle(swerve,wrist,claw));
    autoChooser.addOption("Do Nothing", Autos.doNothing(swerve));

    // Smartdashboard Data 
    SmartDashboard.putData(wrist);
    SmartDashboard.putData(swerve);
    SmartDashboard.putData(claw);
    SmartDashboard.putData(autoChooser);
    SmartDashboard.putData(climber);
    
    wrist.setDefaultCommand(wristWithJoy);
    elevator.setDefaultCommand(moveElevatorWithJoystick);
    swerve.setDefaultCommand(driveWithJoystick);

    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    toggleAlgaeModeButton.onTrue(toggleAlgaeMode);
    toggleCoralModeButton.onTrue(toggleCoralMode);

    resetEncoderButton.onTrue(resetEncoder);

    // Moves the wrist to a certain position based on what button is pressed
    level1Button.onTrue(goToIntake); //Right button on d-pad
    level2Button.onTrue(goToL2); //Left button on d-pad
    level3Button.onTrue(goToL3); //B button
    level4Button.onTrue(goToL4); //Top button on d-pad
    
    //Toggles climb mode
    toggleClimbButton.onTrue(Commands.runOnce(() -> {climber.toggleClimbMode();}, climber)); // X button
    //runs climber
    climbButton.onTrue(climbSequence); // B button
    //climbButton.whileTrue(climb); // B button

    // claw
    intakeButton.whileTrue(intake);
    outtakeButton.whileTrue(outtake);
    slowOuttakeButton.whileTrue(slowOuttake);
    reverseCoralButton.whileTrue(reverseCoralIntake);

    rotate90Button.onTrue(rotateToAngle);
    

    elevatorOverrideButton.onTrue(moveElevatorWithJoystick);
    wristOverrideButton.onTrue(wristWithJoy);

    //outtakeButton.whileTrue();
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    fieldOrienatedButton.whileTrue(fieldOrienatedCommand);
    slowModeButton.whileTrue(slowModeToggle);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return autoChooser.getSelected();
  }
}
