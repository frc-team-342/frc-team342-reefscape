// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Autos;
import frc.robot.commands.DriveWithJoystick;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.SpinClaw;
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
  private final XboxController operator;

  private final Wrist wrist;
  private final Elevator elevator;

  // Because the angles are the same for both L2 & L3, there will only be an L2
  // command that will be used for both
  //private final WristToPosition wristToIntake;
  //private final WristToPosition wristToL2;
  //private final WristToPosition wristToL4;
  //private final WristToPosition wristToAlgae;
  //private final WristToPosition wristToBarge;

  //private final MoveElevatorToPosition moveElevatorL1;
  //private final MoveElevatorToPosition moveElevatorL2;
  //private final MoveElevatorToPosition moveElevatorL3;
  //private final MoveElevatorToPosition moveElevatorL4;
  private final MoveElevatorWithJoystick moveElevatorWithJoystick;

  /* 
  private final JoystickButton elevatorToL1;
  private final JoystickButton elevatorToL2;
  private final JoystickButton elevatorToL3;
  private final JoystickButton elevatorToL4;
*/

  private final JoystickButton level1Button;
  private final JoystickButton level2Button;
  //private final JoystickButton level3Button;
  private final JoystickButton level4Button;

  private final SequentialCommandGroup goToIntake;
  //private final SequentialCommandGroup goToL1;
  private final SequentialCommandGroup goToL2;
  private final SequentialCommandGroup goToL3;
  private final SequentialCommandGroup goToL4;
  private final SequentialCommandGroup goToProcessor;

  private final WristWithJoystick wristWithJoy;

  //private final JoystickButton l1Button;
  //private final JoystickButton l2Button;
  //private final JoystickButton l3Button;
  //private final JoystickButton l4Button;
  //private final JoystickButton algaeButton;

  private final Claw claw;
  private Intake intake;
  private Outtake outtake;
  private Command onStop;
  private SpinClaw intakeCommand;

 // private final JoystickButton lowFunnelButton;
  //private final JoystickButton highFunnelButton;

  private final CommandXboxController m_driverController;

  // The robot's subsystems and commands are defined here...
  
  private SwerveDrive swerve;
  private XboxController driver;

  private JoystickButton fieldOrienatedButton;

  private DriveWithJoystick driveWithJoystick;
  private Command fieldOrienatedCommand;

  private SendableChooser<Command> autoChooser;

  // down below for claw?
  private JoystickButton intakeButton;
  private JoystickButton outtakeButton;

  private Command toggleCoralMode;
  private Command toggleAlgaeMode;

  private POVButton toggleCoralModeButton;
  private POVButton toggleAlgaeModeButton;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // The robot's subsystems and commands are defined here...

    // Replace with CommandPS4Controller or CommandJoystick if needed
    m_driverController = new CommandXboxController(OperatorConstants.kDriverControllerPort);
    wrist = new Wrist();
    elevator = new Elevator();
    claw = new Claw();

   /*  onStop = Commands.runOnce(() -> {
      claw.stopButton();
    }, claw);
    outtakeButton = Commands.runOnce(() -> {
      claw.outTakeCoral();
    }, claw);
    intakeCommand = new SpinClaw(claw);
    
    intakeCommand = Commands.run(() -> {
      claw.intakeCoral();
    }, claw); */

    SmartDashboard.putData(wrist);

    operator = new XboxController(1);

    level1Button = new JoystickButton(operator, XboxController.Button.kY.value );
    level2Button = new JoystickButton(operator, XboxController.Button.kB.value);
    //level3Button = new JoystickButton(operator, XboxController.Button.kA.value);
    level4Button = new JoystickButton(operator, XboxController.Button.kX.value);
    /* 

    // Creates commands telling the wrist to go to different coral branches
    wristToIntake = new WristToPosition(wrist, INTAKE_POSITION);
    wristToL2 = new WristToPosition(wrist, L2_POSITION);
    wristToL4 = new WristToPosition(wrist, L4_POSITION);
    wristToAlgae = new WristToPosition(wrist, ALGAE_POSITION);
    wristToBarge = new WristToPosition(wrist, BARGE_POSITION);

    // Creates commands telling the elevator to go to different coral branches
    moveElevatorL1 = new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_POSITION);
    moveElevatorL2 = new MoveElevatorToPosition(elevator,wrist, ElevatorHeights.LOW_MIDDLE_POSITION);
    moveElevatorL3 = new MoveElevatorToPosition(elevator,wrist, ElevatorHeights.HIGH_MIDDLE_POSITION);
    moveElevatorL4 = new MoveElevatorToPosition(elevator,wrist, ElevatorHeights.HIGH_POSITION);
    //moveElevatorProcessor = new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.PROCESSOR_POSITION);

    */

    wristWithJoy = new WristWithJoystick(operator, wrist);
    moveElevatorWithJoystick = new MoveElevatorWithJoystick(elevator, operator);

    // Creating sequential command groups that use wrist and elevator
    goToIntake = new SequentialCommandGroup(
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_POSITION),
      new ParallelCommandGroup(
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_POSITION, true), 
      new WristToPosition(wrist, INTAKE_POSITION)
      )
    );

    goToL2 = new SequentialCommandGroup(
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_MIDDLE_POSITION), 
      new ParallelCommandGroup(
        new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_MIDDLE_POSITION, true), 
        new WristToPosition(wrist, L2_POSITION)
      )
    );

    goToL3 = new SequentialCommandGroup(
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.HIGH_MIDDLE_POSITION), 
      new ParallelCommandGroup(
        new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.HIGH_MIDDLE_POSITION, true), 
        new WristToPosition(wrist, L2_POSITION)
      )
    );

    goToL4 = new SequentialCommandGroup(
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.HIGH_POSITION),
      new ParallelCommandGroup(
        new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.HIGH_POSITION, true), 
        new WristToPosition(wrist, L4_POSITION)
      )
    );

    goToProcessor = new SequentialCommandGroup(
      new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_POSITION),
      new ParallelCommandGroup(
        new MoveElevatorToPosition(elevator, wrist, ElevatorHeights.LOW_POSITION, true), 
        new WristToPosition(wrist, ALGAE_POSITION)
      )
    );

    intake = new Intake(claw, wrist);
    outtake = new Outtake(wrist, claw);

    // stuff for claw?? down
    intakeButton = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);
    outtakeButton = new JoystickButton(operator,XboxController.Button.kRightBumper.value);

    //Toggle modes
    toggleAlgaeMode = new SequentialCommandGroup(Commands.runOnce(() -> {wrist.setAlgaeMode();}, wrist), new WristToPosition(wrist, ALGAE_POSITION));
    toggleCoralMode = new SequentialCommandGroup(Commands.runOnce(() -> {wrist.setCoralMode();}, wrist), new WristToPosition(wrist, L2_POSITION));

    toggleAlgaeModeButton = new POVButton(operator, 0);
    toggleCoralModeButton = new POVButton(operator, 180);

    // Configure the trigger bindings

    //lowFunnelButton = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);
    //highFunnelButton = new JoystickButton(operator, XboxController.Button.kRightBumper.value);

    //wrist.setDefaultCommand(wristToAlgae);
    elevator.setDefaultCommand(moveElevatorWithJoystick);

    // Configure the trigger bindings

    swerve = new SwerveDrive();

    driver = new XboxController(0);

    fieldOrienatedButton = new JoystickButton(driver, XboxController.Button.kY.value);
    driveWithJoystick = new DriveWithJoystick(swerve, driver);


    fieldOrienatedCommand = Commands.runOnce(() -> {
      swerve.toggleFieldOriented();
    }, swerve);

    swerve.setDefaultCommand(driveWithJoystick);
    autoChooser = new SendableChooser<>();

    autoChooser.addOption("PathPlannerTest", new PathPlannerAuto("New Auto"));

    SmartDashboard.putData(swerve);
    SmartDashboard.putData(claw);
    SmartDashboard.putData(autoChooser);

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

    /*
    elevatorToL1.onTrue(goToIntake); // down button on d-pad
    elevatorToL2.onTrue(goToL2); // left button on d-pad
    elevatorToL3.onTrue(goToL3); // right button on d-pad
    elevatorToL4.onTrue(goToL4); // top button on d-pad
 */

    //elevatorToProcessor.onTrue(moveElevatorProcessor); // the A button

    // Moves the wrist to a certain position based on what button is pressed
    level1Button.onTrue(goToIntake); 
    level2Button.onTrue(goToL2);
    level4Button.onTrue(goToL4);
    //algaeButton.onTrue(wristToAlgae);

    // claw
    intakeButton.whileTrue(intake);
    outtakeButton.whileTrue(outtake);
    //outtakeButton.whileTrue();
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    fieldOrienatedButton.whileTrue(fieldOrienatedCommand);

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
