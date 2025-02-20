// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.commands.Elevator.MoveElevatorToPosition;
import frc.robot.commands.Elevator.MoveElevatorWithJoystick;
import frc.robot.commands.Funnel.FunnelToPosition;
import frc.robot.commands.Wrist.WristToPosition;
import frc.robot.commands.Wrist.WristWithJoystick;
import frc.robot.subsystems.Elevator;

import frc.robot.subsystems.*;

import static frc.robot.Constants.FunnelConstants.HIGH_FUNNEL_POSITION;
import static frc.robot.Constants.FunnelConstants.LOW_FUNNEL_POSITION;
import static frc.robot.Constants.WristConstants.*;
import static frc.robot.Constants.ElevatorConstants.*;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final XboxController operator;

  private final Elevator elevator;
  private final Wrist wrist;
  private final Funnel funnel;

  private final MoveElevatorToPosition moveElevatorIntake;
  private final MoveElevatorToPosition moveElevatorProcessor;
  private final MoveElevatorToPosition moveElevatorL1;
  private final MoveElevatorToPosition moveElevatorL2;
  private final MoveElevatorToPosition moveElevatorL3;
  private final MoveElevatorToPosition moveElevatorL4;
  private final MoveElevatorWithJoystick moveElevatorWithJoystick;
  
  //Because the angles are the same for both L2 & L3, there will only be an L2 command that will be used for both
  private final WristToPosition wristToIntake;
  private final WristToPosition wristToL1;
  private final WristToPosition wristToL2;
  private final WristToPosition wristToL4;
  private final WristToPosition wristToAlgae;

  private final FunnelToPosition funnelToLow;
  private final FunnelToPosition funnelToHigh;

  private final SequentialCommandGroup goToIntake;
  private final SequentialCommandGroup goToL1;
  private final SequentialCommandGroup goToL2;
  private final SequentialCommandGroup goToL3;
  private final SequentialCommandGroup goToL4;
  private final SequentialCommandGroup goToProcessor;

  private final WristWithJoystick wristWithJoy;

  private final POVButton l1Button;
  private final POVButton l2Button;
  private final POVButton l3Button;
  private final POVButton l4Button;
  private final JoystickButton processorButton;
  private final JoystickButton intakePositionButton;

  private final JoystickButton lowFunnelButton;
  private final JoystickButton highFunnelButton;

  private final CommandXboxController m_driverController;
  private final ExampleSubsystem m_exampleSubsystem;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // The robot's subsystems and commands are defined here...
    m_exampleSubsystem = new ExampleSubsystem();
  
    // Replace with CommandPS4Controller or CommandJoystick if needed
    m_driverController = new CommandXboxController(OperatorConstants.kDriverControllerPort);
    wrist = new Wrist();
    elevator = new Elevator();
    funnel = new Funnel();

    SmartDashboard.putData(wrist);

    operator = new XboxController(1);

    //Creates commands telling the wrist to go to different coral branches
    wristToIntake = new WristToPosition(wrist, INTAKE_POSITION);
    wristToL1 = new WristToPosition(wrist, L1_POSITION);
    wristToL2 = new WristToPosition(wrist, L2_POSITION);
    wristToL4 = new WristToPosition(wrist, L4_POSITION);
    wristToAlgae = new WristToPosition(wrist, ALGAE_POSITION);

    //Creates commands telling the elevator to go to different coral branches
    moveElevatorIntake = new MoveElevatorToPosition(elevator, wrist, BOTTOM_POSITION);
    moveElevatorL1 = new MoveElevatorToPosition(elevator, wrist, L1_HEIGHT);
    moveElevatorL2 = new MoveElevatorToPosition(elevator, wrist, L2_HEIGHT);
    moveElevatorL3 = new MoveElevatorToPosition(elevator, wrist, L3_HEIGHT);
    moveElevatorL4 = new MoveElevatorToPosition(elevator, wrist, L4_HEIGHT);
    moveElevatorProcessor = new MoveElevatorToPosition(elevator, wrist, PROCESSOR_HEIGHT);

    funnelToLow = new FunnelToPosition(funnel, LOW_FUNNEL_POSITION);
    funnelToHigh = new FunnelToPosition(funnel, HIGH_FUNNEL_POSITION);

    //Runs the elevator and wrist commands at the same time for simplicity
    goToIntake = new SequentialCommandGroup(moveElevatorIntake, wristToIntake);
    goToL1 = new SequentialCommandGroup(moveElevatorL1, wristToL1);
    goToL2 = new SequentialCommandGroup(moveElevatorL2, wristToL2);
    goToL3 = new SequentialCommandGroup(moveElevatorL3, wristToL2);
    goToL4 = new SequentialCommandGroup(moveElevatorL4, wristToL4);
    goToProcessor = new SequentialCommandGroup(moveElevatorProcessor, wristToAlgae);

    wristWithJoy = new WristWithJoystick(operator, wrist);
    moveElevatorWithJoystick = new MoveElevatorWithJoystick(elevator, wrist, operator);

    //Creating new buttons for L1, L2/L3, L4, and algae
    l1Button = new POVButton(operator, 180);
    l2Button = new POVButton(operator, 90);
    l3Button = new POVButton(operator, 270);
    l4Button = new POVButton(operator, 0);
    processorButton = new JoystickButton(operator, XboxController.Button.kA.value);
    intakePositionButton = new JoystickButton(operator, XboxController.Button.kB.value);

    lowFunnelButton = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);
    highFunnelButton = new JoystickButton(operator, XboxController.Button.kRightBumper.value);

    wrist.setDefaultCommand(wristToL2);
    elevator.setDefaultCommand(moveElevatorWithJoystick);

    // Configure the trigger bindings
    configureBindings();
  }

/**
 * Use this method to define your trigger->command mappings. Triggers can be created via the
 * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
 * predicate, or via the named factories in {@link
 * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
 * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
 * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
 * joysticks}.
 */
private void configureBindings() {
  //Moves the wrist to a certain position based on what button is pressed
  l1Button.onTrue(goToL1); // down button on d-pad
  l2Button.onTrue(goToL2); // right button on d-pad
  l3Button.onTrue(goToL3); // left button on d-pad
  l4Button.onTrue(goToL4); // top button on d-pad
  processorButton.onTrue(goToProcessor); // the A button
  intakePositionButton.onTrue(goToIntake); // the B button

  lowFunnelButton.onTrue(funnelToLow); // the left bumper
  highFunnelButton.onTrue(funnelToHigh); // the right bumper

  // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
  new Trigger(m_exampleSubsystem::exampleCondition)
      .onTrue(new ExampleCommand(m_exampleSubsystem));

  // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
  // cancelling on release.
  m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
  m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  }

/**
 * Use this to pass the autonomous command to the main {@link Robot} class.
 *
 * @return the command to run in autonomous
 */
public Command getAutonomousCommand() {
  // An example command will be run in autonomous
  return Autos.exampleAuto(m_exampleSubsystem);
}
}

