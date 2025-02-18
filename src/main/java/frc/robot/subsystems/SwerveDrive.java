// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.fasterxml.jackson.core.filter.FilteringGeneratorDelegate;
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
import edu.wpi.first.util.sendable.SendableBuilder;

public class SwerveDrive extends SubsystemBase {

  private SwerveDriveKinematics kinematics;
  private SwerveDriveOdometry odometry;
  private AHRS NavX;

  private ChassisSpeeds ChassisSpeeds;
  
  private SwerveModule frontLeftModule;
  private SwerveModule frontRightModule;
  private SwerveModule backLeftModule;
  private SwerveModule backRightModule; 


  /** Creates a new SwerveDrive. */
  public SwerveDrive() {


      frontLeftModule = new SwerveModule(
        DriveConstants.FRONT_LEFT_DRIVE_ID, 
        DriveConstants.FRONT_LEFT_ROTATE_ID, 
        DriveConstants.FL_ENCODER_PORT, 
        false, false, 
        DriveConstants.FRONT_LEFT_OFFSET, 
        "FL"
        );

        frontRightModule = new SwerveModule(
        DriveConstants.FRONT_RIGHT_DRIVE_ID, 
        DriveConstants.FRONT_RIGHT_ROTATE_ID, 
        DriveConstants.FR_ENCODER_PORT, 
        false, false, 
        DriveConstants.FRONT_RIGHT_OFFSET, 
        "FR"
        );

        backLeftModule = new SwerveModule(
        DriveConstants.BACK_LEFT_DRIVE_ID, 
        DriveConstants.BACK_LEFT_ROTATE_ID, 
        DriveConstants.BL_ENCODER_PORT, 
        false, false, 
        DriveConstants.BACK_LEFT_OFFSET, 
        "BL"
        );

        backRightModule = new SwerveModule(
        DriveConstants.BACK_RIGHT_DRIVE_ID, 
        DriveConstants.BACK_RIGHT_ROTATE_ID, 
        DriveConstants.BR_ENCODER_PORT, 
        false, false, 
        DriveConstants.BACK_RIGHT_OFFSET, 
        "BR"
        );


      /* Initalizes Kinematics */
      kinematics = new SwerveDriveKinematics(

        /*Front Left */ new Translation2d(Units.inchesToMeters(14.5), Units.inchesToMeters(14.5)),
        /*Front Right */ new Translation2d(Units.inchesToMeters(14.5), Units.inchesToMeters(-14.5)),
        /*Back Left */ new Translation2d(Units.inchesToMeters(-14.5), Units.inchesToMeters(14.5)),
        /*Back Right */ new Translation2d(Units.inchesToMeters(-14.5), Units.inchesToMeters(-14.5))

      );

      /* Initalize NavX (Gyro) */
      NavX = new AHRS(AHRS.NavXComType.kMXP_SPI);

      /* Initalizes Odometry */
      odometry = new SwerveDriveOdometry(

      kinematics, // Swerve Drive Kinematics  
      NavX.getRotation2d(), // Returns Gyro reading as a Rotation 2d
      new SwerveModulePosition[]{ new SwerveModulePosition(), new SwerveModulePosition(), new SwerveModulePosition(), new SwerveModulePosition()}, 
      // Module Order : Front-Left, Front-Right, Back-Left, Back-Right
      new Pose2d(0,0, new Rotation2d()));   

  }

  public ChassisSpeeds getChassisSpeeds() {

    return new ChassisSpeeds(ChassisSpeeds.vxMetersPerSecond, ChassisSpeeds.vyMetersPerSecond, ChassisSpeeds.omegaRadiansPerSecond);

  }

      /* This drive method takes the values from the chassisspeeds and 
      applys in to each indivual Module using the "SetState" Method created in SwereMoudle */

      public void drive(ChassisSpeeds chassisSpeeds) {

        SwerveModuleState[] swerveModuleStates = kinematics.toSwerveModuleStates(chassisSpeeds);

        frontLeftModule.setState(swerveModuleStates[0]);
        frontRightModule.setState(swerveModuleStates[1]);
        backLeftModule.setState(swerveModuleStates[2]);
        backRightModule.setState(swerveModuleStates[3]);

      }

      public void testDrive(){

        ChassisSpeeds testSpeeds = new ChassisSpeeds(Units.inchesToMeters(14), Units.inchesToMeters(0), Units.degreesToRadians(0));

        SwerveModuleState[] swerveModuleStates = kinematics.toSwerveModuleStates(testSpeeds);

        frontLeftModule.setState(swerveModuleStates[0]);
        frontRightModule.setState(swerveModuleStates[1]);
        backLeftModule.setState(swerveModuleStates[2]);
        backRightModule.setState(swerveModuleStates[3]);
      }

      /* Method that returns the Moudle positions */
      public SwerveModulePosition[] getCurrentSwerveModulePositions(){
        return new SwerveModulePosition[]{

            new SwerveModulePosition(frontLeftModule.getDistance(), frontLeftModule.getAngle()), // Front left
            new SwerveModulePosition(frontRightModule.getDistance(), frontRightModule.getAngle()), // Front Right
            new SwerveModulePosition(backLeftModule.getDistance(), backLeftModule.getAngle()), // Back Left
            new SwerveModulePosition(backRightModule.getDistance(), backRightModule.getAngle()) // Back Right

        };
      } 

      /* Method that stops all modules */

      public void stopModules() {
        frontLeftModule.stop();
        frontRightModule.stop();
        backLeftModule.stop();
        backRightModule.stop();
    }

    public void resetEncoder() {

      frontLeftModule.resetEncoder();
      frontRightModule.resetEncoder();
      backLeftModule.resetEncoder();
      backRightModule.resetEncoder();

    }
    

    public void putFrontLeftValues(SendableBuilder sendableBuilder){
      sendableBuilder.addDoubleProperty(frontLeftModule.printLabel() + " Offset", ()-> frontLeftModule.getOffset(), null);
      sendableBuilder.addDoubleProperty(frontLeftModule.printLabel() + " Rotate Encoder(Radians): " , ()-> frontLeftModule.getRotatePosition(), null);
    }

    public void putFrontRightValues(SendableBuilder sendableBuilder){
      sendableBuilder.addDoubleProperty(frontRightModule.printLabel() + " Offset", ()-> frontRightModule.getOffset(), null);
      sendableBuilder.addDoubleProperty(frontRightModule.printLabel() + " Rotate Encoder(Radians): " , ()-> frontRightModule.getRotatePosition(), null);
    }

    public void putBackLeftModule(SendableBuilder sendableBuilder){
      sendableBuilder.addDoubleProperty(backLeftModule.printLabel() + " Offset", ()-> backLeftModule.getOffset(), null);
      sendableBuilder.addDoubleProperty(backLeftModule.printLabel() + " Rotate Encoder(Radians): " , ()-> backLeftModule.getRotatePosition(), null);

    }

    public void putBackRightModule(SendableBuilder sendableBuilder){
      sendableBuilder.addDoubleProperty(backRightModule.printLabel() + " Offset", ()-> backRightModule.getOffset(), null);
      sendableBuilder.addDoubleProperty(backLeftModule.printLabel() + " Rotate Encoder(Radians): " , ()-> backRightModule.getRotatePosition(), null);
    }




  @Override 
  public void initSendable(SendableBuilder sendableBuilder){
    putFrontLeftValues(sendableBuilder);
    putFrontRightValues(sendableBuilder);
    putBackLeftModule(sendableBuilder);
    putBackRightModule(sendableBuilder);

  }


      
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    //Updates the odometry every run
    odometry.update(NavX.getRotation2d(), getCurrentSwerveModulePositions());

  }
}
