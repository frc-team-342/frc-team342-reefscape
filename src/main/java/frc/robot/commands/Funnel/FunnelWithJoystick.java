// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Funnel;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Funnel;
import static frc.robot.Constants.FunnelConstants.*;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class FunnelWithJoystick extends Command {
  /** Creates a new FunnelWithJoystick. */
  private final XboxController joy;
  private final Funnel funnel;

  private double currentPosition;
  private double speed;

  private boolean goingDown;
  
  public FunnelWithJoystick(XboxController joy, Funnel funnel) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.joy = joy;
    this.funnel = funnel;

    addRequirements(funnel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentPosition = funnel.getThroughBore().get();
    speed = MathUtil.applyDeadband(0.15, joy.getRightY());

    goingDown = (speed < 0);

    if((goingDown && currentPosition <= LOW_FUNNEL_POSITION) || (!goingDown && currentPosition >= HIGH_FUNNEL_POSITION))
      funnel.move(0);
    else 
      //Divided by four to reduce speed
      funnel.move(speed/4);
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
