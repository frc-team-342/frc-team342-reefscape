// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;


import frc.robot.subsystems.*;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.subsystems.Claw;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final XboxController operator;

 

  
  //Because the angles are the same for both L2 & L3, there will only be an L2 command that will be used for both
  

  private final Claw claw;
  private Command onStop;
  private Command outtakeButton;
  private Command intakeCommand;


  private final ExampleSubsystem m_exampleSubsystem;

  //down below for claw?
  private final JoystickButton xButton;
  private final JoystickButton aButton;
  private final JoystickButton yButton;


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // The robot's subsystems and commands are defined here...
    m_exampleSubsystem = new ExampleSubsystem();

   
    claw = new Claw();
    onStop = Commands.runOnce(() -> {claw.stopButton();}, claw);
    outtakeButton = Commands.runOnce(() -> {claw.outTakeCoral();}, claw);
    intakeCommand = Commands.run(() -> {claw.intakeCoral();}, claw);

    operator = new XboxController(1);

    //stuff for claw?? down
    xButton = new JoystickButton(operator, XboxController.Button.kX.value); 
    aButton = new JoystickButton(operator, XboxController.Button.kA.value);
    yButton = new JoystickButton(operator, XboxController.Button.kY.value);

    // Configure the trigger bindings

    SmartDashboard.putData(claw);

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
  

  //claw
  xButton.whileTrue(onStop);
  aButton.whileTrue(outtakeButton);
  yButton.whileTrue(intakeCommand);

  // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
  new Trigger(m_exampleSubsystem::exampleCondition)
      .onTrue(new ExampleCommand(m_exampleSubsystem));

  // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
  // cancelling on release.

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
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

