// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import com.studica.frc.AHRS;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.*;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.SwerveDrive;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class DriveWithJoystick extends Command {
  /** Creates a new DriveWithJoystick. */

  private SwerveDrive swerve;
  private XboxController joyStick;
  private ChassisSpeeds chassisSpeeds;
  private AHRS gyro;

  private SlewRateLimiter xLimiter;
  private SlewRateLimiter yLimiter;
  private SlewRateLimiter rotateLimiter;



  public DriveWithJoystick(SwerveDrive swerve, XboxController joyStick) {

    this.swerve = swerve;
    this.joyStick = joyStick;

    xLimiter = new SlewRateLimiter(3);
    yLimiter = new SlewRateLimiter(3);
    rotateLimiter = new SlewRateLimiter(3);

    addRequirements(swerve);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

   double xSpeed = joyStick.getLeftX();
   double ySpeed = joyStick.getLeftY();
   double rotateSpeed = joyStick.getRawAxis(4);

   xSpeed = MathUtil.applyDeadband(xSpeed, 0.15);
   ySpeed = MathUtil.applyDeadband(ySpeed, 0.15);
   rotateSpeed = MathUtil.applyDeadband(rotateSpeed, 0.15);

   xSpeed = xLimiter.calculate(xSpeed) * DriveConstants.MAX_DRIVE_SPEED;
   ySpeed = yLimiter.calculate(ySpeed) * DriveConstants.MAX_DRIVE_SPEED;
   rotateSpeed = rotateLimiter.calculate(rotateSpeed) * DriveConstants.MAX_ROTATE_SPEED;

   chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, rotateSpeed);
   System.out.println(rotateSpeed);
   swerve.drive(chassisSpeeds);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    swerve.stopModules();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
