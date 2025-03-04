// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Autos;

import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.*;

import static frc.robot.Constants.DriveConstants.MAX_DRIVE_SPEED;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public final class Autos {
    /** Example static factory for an autonomous command. */
    public static Command exampleAuto(ExampleSubsystem subsystem) {
      return Commands.sequence(subsystem.exampleMethodCommand(), new ExampleCommand(subsystem));
    }
  
    public static Command troughAuto(SwerveDrive swerve, Claw claw) {
      ChassisSpeeds chassisSpeeds = new ChassisSpeeds(1,0,0);
      return Commands.sequence(
        new TimedDrive(swerve, 1, chassisSpeeds, MAX_DRIVE_SPEED),
        Commands.run(() -> {claw.outtakeCoral();})
      );
    }
    public static Command forwardAuto(SwerveDrive swerve) {
      ChassisSpeeds chassisSpeeds = new ChassisSpeeds(1,0,0);
      return Commands.sequence(
        new TimedDrive(swerve, 1, chassisSpeeds, MAX_DRIVE_SPEED)
      );
    }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
