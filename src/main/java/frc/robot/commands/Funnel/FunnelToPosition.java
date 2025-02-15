// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Funnel;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Funnel;
import edu.wpi.first.math.controller.PIDController;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class FunnelToPosition extends Command {
  /** Creates a new FunnelToPosition. */
  private final Funnel funnel;
  private double position;
  public FunnelToPosition(Funnel funnel, double position) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.funnel = funnel;
    this.position = position;

    addRequirements(funnel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    funnel.funnelToPosition(position);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
