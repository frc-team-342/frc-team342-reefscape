// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.studica.frc.AHRS;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.SwerveModule;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.Odometry;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class SwerveDrive extends SubsystemBase {

  private SwerveDriveKinematics kinematics;
  private SwerveDriveOdometry odometry;
  private AHRS NavX;
  
  private SwerveModule frontLeftModule;
  private SwerveModule frontRightModule;
  private SwerveModule backLeftModule;
  private SwerveModule backRightModule; 


  /** Creates a new SwerveDrive. */
  public SwerveDrive() {


      frontLeftModule = new SwerveModule(
        DriveConstants.FRONT_LEFT_DRIVE_ID, 
        DriveConstants.FRONT_LEFT_ROTATE_ID, 
        0, 
        false, false, 
        0, 
        "FL"
        );

        frontRightModule = new SwerveModule(
        DriveConstants.FRONT_RIGHT_DRIVE_ID, 
        DriveConstants.FRONT_RIGHT_ROTATE_ID, 
        0, 
        false, false, 
        0, 
        "FR"
        );

        backLeftModule = new SwerveModule(
        DriveConstants.BACK_LEFT_DRIVE_ID, 
        DriveConstants.BACK_LEFT_ROTATE_ID, 
        0, 
        false, false, 
        0, 
        "BL"
        );

        backRightModule = new SwerveModule(
        DriveConstants.BACK_RIGHT_DRIVE_ID, 
        DriveConstants.BACK_RIGHT_ROTATE_ID, 
        0, 
        false, false, 
        0, 
        "BR"
        );


      kinematics = new SwerveDriveKinematics(
          new Translation2d(Units.inchesToMeters(-12.5), Units.inchesToMeters(12.5)),
          new Translation2d(Units.inchesToMeters(12.5), Units.inchesToMeters(12.5)),
          new Translation2d(Units.inchesToMeters(12.5), Units.inchesToMeters(-12.5)),
          new Translation2d(Units.inchesToMeters(-12.5), Units.inchesToMeters(-12.5))
      );

      /* Initalize NavX (Gyro) */
      NavX = new AHRS(AHRS.NavXComType.kMXP_SPI);

      odometry = new SwerveDriveOdometry(
        
      kinematics, // Swerve Drive Kinematics  
      
      NavX.getRotation2d(), // Returns Gyro reading as a Rotation 2d

      new SwerveModulePosition[]{ new SwerveModulePosition(), new SwerveModulePosition(), new SwerveModulePosition(), new SwerveModulePosition()}, 
      // Module Order : Front-Left, Front-Right, Back-Left, Back-Right
      
      new Pose2d(0,0, new Rotation2d()));   

  }

      public void drive(){

        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(Units.inchesToMeters(14), Units.inchesToMeters(4), Units.degreesToRadians(30));

        SwerveModuleState[] swerveModuleStates = kinematics.toSwerveModuleStates(chassisSpeeds);

        frontLeftModule.setState(swerveModuleStates[0]);
        frontRightModule.setState(swerveModuleStates[1]);
        backLeftModule.setState(swerveModuleStates[2]);
        backRightModule.setState(swerveModuleStates[3]);

      }

      public SwerveModulePosition[] getCurrentSwerveModulePositions(){

        return new SwerveModulePosition[]{

            new SwerveModulePosition(frontLeftModule.getDistance(), frontLeftModule.getAngle()), // Front left
            new SwerveModulePosition(frontRightModule.getDistance(), frontRightModule.getAngle()), // Front Right
            new SwerveModulePosition(backLeftModule.getDistance(), backLeftModule.getAngle()), // Back Left
            new SwerveModulePosition(backRightModule.getDistance(), backRightModule.getAngle()) // Back Right

        };
      } 


      
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    odometry.update(NavX.getRotation2d(), getCurrentSwerveModulePositions());
  }
}
