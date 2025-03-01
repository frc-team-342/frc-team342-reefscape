// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Elevator;
import frc.robot.commands.Elevator.MoveElevatorToPosition;
import frc.robot.commands.Elevator.MoveElevatorWithJoystick;
import static frc.robot.Constants.ElevatorConstants.*;

import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final XboxController operator;

  private final Elevator elevator;

  private final MoveElevatorToPosition moveElevatorL1;
  private final MoveElevatorToPosition moveElevatorL2;
  private final MoveElevatorToPosition moveElevatorL3;
  private final MoveElevatorToPosition moveElevatorL4;
  private final MoveElevatorWithJoystick moveElevatorWithJoystick;

  private final POVButton elevatorToL1;
  private final POVButton elevatorToL2;
  private final POVButton elevatorToL3;
  private final POVButton elevatorToL4;

  private final CommandXboxController m_driverController;
  private final ExampleSubsystem m_exampleSubsystem;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // The robot's subsystems and commands are defined here...
    m_exampleSubsystem = new ExampleSubsystem();
  

    // Replace with CommandPS4Controller or CommandJoystick if needed
    m_driverController = new CommandXboxController(OperatorConstants.kDriverControllerPort);

    elevator = new Elevator();

    operator = new XboxController(1);

    //Creates commands telling the elevator to go to different coral branches
    moveElevatorL1 = new MoveElevatorToPosition(elevator, L1_HEIGHT);
    moveElevatorL2 = new MoveElevatorToPosition(elevator, L2_HEIGHT);
    moveElevatorL3 = new MoveElevatorToPosition(elevator, L3_HEIGHT);
    moveElevatorL4 = new MoveElevatorToPosition(elevator, L4_HEIGHT);

    moveElevatorWithJoystick = new MoveElevatorWithJoystick(elevator, operator);

    //Operator buttons for elevator
    elevatorToL1 = new POVButton(operator, 180);
    elevatorToL2 = new POVButton(operator, 90);
    elevatorToL3 = new POVButton(operator, 270);
    elevatorToL4 = new POVButton(operator, 0);

    elevator.setDefaultCommand(moveElevatorWithJoystick);

    // Configure the trigger bindings
    configureBindings();

    SmartDashboard.putData(elevator);
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
  elevatorToL1.onTrue(moveElevatorL1); // down button on d-pad
  elevatorToL2.onTrue(moveElevatorL2); // left button on d-pad
  elevatorToL3.onTrue(moveElevatorL3); // right button on d-pad
  elevatorToL4.onTrue(moveElevatorL4); // top button on d-pad

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

