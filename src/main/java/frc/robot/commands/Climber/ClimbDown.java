// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;


import static frc.robot.Constants.ClimbConstants.CLIMB_UP;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;
/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ClimbDown extends Command {
  private Climber climber;
  private boolean isUp;
  private double speed;
  /** Creates a new Climb. */
  public ClimbDown(Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.climber=climber;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isUp = ClimbUp.getIter() > 0;
    speed = (CLIMB_UP > 0)? 0.9 : -0.9;

  }
  
  // Called every time 
  //the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(isUp)
      climber.moveClimber(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
