// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Wrist;

import static frc.robot.Constants.WristConstants.WRIST_ERROR;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Wrist;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class WristToPosition extends Command {
  private final Wrist wrist;
  private final double position;

  /** Creates a new WristToPosition. */
  public WristToPosition(Wrist wrist, double position) {
    this.wrist = wrist;
    this.position = position;

    addRequirements(wrist);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    wrist.wristToPosition(position);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    wrist.holdWristPosition();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (wrist.getPosition() > position - WRIST_ERROR) && (wrist.getPosition() < position + WRIST_ERROR);
  }
}
