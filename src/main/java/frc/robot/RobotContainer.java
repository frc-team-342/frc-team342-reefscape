// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.*;

import static frc.robot.Constants.WristConstants.ALGAE_POSITION;
import static frc.robot.Constants.WristConstants.L1_POSITION;
import static frc.robot.Constants.WristConstants.L2_POSITION;
import static frc.robot.Constants.WristConstants.L4_POSITION;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.WristConstants.*;
import frc.robot.commands.Wrist_Commands.WristToPosition;
import frc.robot.commands.Wrist_Commands.WristWithJoystick;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
    private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
    private final Wrist wrist;

    // Replace with CommandPS4Controller or CommandJoystick if needed
    private final CommandXboxController m_driverController =
        new CommandXboxController(OperatorConstants.kDriverControllerPort);
    private final XboxController operator;

    //Because the angles are the same for both L2 & L3, there will only be an L2 command that will be used for both
    private final WristToPosition wristToL1;
    private final WristToPosition wristToL2;
    private final WristToPosition wristToL4;
    private final WristToPosition wristToAlgae;

    private final WristWithJoystick wristWithJoy;

    private final POVButton l1Button;
    private final POVButton l2Button;
    private final POVButton l4Button;
    private final POVButton algaeButton;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    wrist = new Wrist();

    operator = new XboxController(1);

    //Creates commands telling the wrist to go to different coral branches
    wristToL1 = new WristToPosition(wrist, L1_POSITION);
    wristToL2 = new WristToPosition(wrist, L2_POSITION);
    wristToL4 = new WristToPosition(wrist, L4_POSITION);
    wristToAlgae = new WristToPosition(wrist, ALGAE_POSITION);

    wristWithJoy = new WristWithJoystick(operator, wrist);

    //creating new buttons for L1, L2/L3, L4, and algae
    l1Button = new POVButton(operator, 90);
    l2Button = new POVButton(operator, 270);
    l4Button = new POVButton(operator, 0);
    algaeButton = new POVButton(operator, 180);

    wrist.setDefaultCommand(wristWithJoy);

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
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());

    //Moves the wrist to a certain position based on what button is pressed
    l1Button.onTrue(wristToL1);
    l2Button.onTrue(wristToL2);
    l4Button.onTrue(wristToL4);
    algaeButton.onTrue(wristToAlgae);
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
