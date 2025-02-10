// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import static frc.robot.Constants.WristConstants.HIGH_WRIST_POS;
import static frc.robot.Constants.WristConstants.LOW_WRIST_POS;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import com.revrobotics.jni.CANSparkJNI;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.WristConstants;
import frc.robot.subsystems.Wrist;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class WristToPosition extends Command {
  private Wrist wrist;
  private XboxController joystick;
  private PIDController pidController;
  private boolean goingDown;

  private double position;
  /** Creates a new WristToPosition. */
  public WristToPosition(Wrist wrist, double position) {
    this.wrist = wrist;
    goingDown = false;
    this.position = position;

    pidController = new PIDController(WristConstants.WRIST_P, WristConstants.WRIST_I, WristConstants.WRIST_D);
    addRequirements(wrist );
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
