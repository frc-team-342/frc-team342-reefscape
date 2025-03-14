// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.IOException;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.print.attribute.standard.MediaSize.NA;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.filter.FilteringGeneratorDelegate;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
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
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class SwerveDrive extends SubsystemBase {

  private SwerveDriveKinematics kinematics;
  private SwerveDriveOdometry odometry;
  private AHRS NavX;

  private ChassisSpeeds chassisSpeeds;
  
  private boolean fieldOriented; 
  private boolean slowMode;
  
  private SwerveModule frontLeftModule;
  private SwerveModule frontRightModule;
  private SwerveModule backLeftModule;
  private SwerveModule backRightModule; 

  private Supplier<Pose2d> poseSupplier;
  private Consumer<Pose2d> resetPoseConsumer;
  private Consumer<ChassisSpeeds> robotRelativeOutput;
  private Supplier<ChassisSpeeds> chasisSpeedSupplier;
  private BooleanSupplier shouldFlipSupplier;
  private RobotConfig config;
  private Field2d feild;

  //Mr. Neal Cooking, delete later
  // private double heading = NavX.getAngle();
  private PIDController rotateCompensator;

  SwerveModuleState[] swerveModuleStates;
  SwerveModulePosition[] swerveModulePositions;

  /** Creates a new SwerveDrive. */
  public SwerveDrive() {

      //rotateCompensator = new PIDController(0.1,0,0);

      chassisSpeeds = new ChassisSpeeds(0, 0, 0);

      frontLeftModule = new SwerveModule(
        DriveConstants.FRONT_LEFT_DRIVE_ID, 
        DriveConstants.FRONT_LEFT_ROTATE_ID, 
        DriveConstants.FL_ENCODER_PORT, 
        false, true, 
        DriveConstants.FRONT_LEFT_OFFSET, 
        "FL"
        );

        frontRightModule = new SwerveModule(
        DriveConstants.FRONT_RIGHT_DRIVE_ID, 
        DriveConstants.FRONT_RIGHT_ROTATE_ID, 
        DriveConstants.FR_ENCODER_PORT, 
        false, true, 
        DriveConstants.FRONT_RIGHT_OFFSET, 
        "FR"
        );

        backLeftModule = new SwerveModule(
        DriveConstants.BACK_LEFT_DRIVE_ID, 
        DriveConstants.BACK_LEFT_ROTATE_ID, 
        DriveConstants.BL_ENCODER_PORT, 
        false, true, 
        DriveConstants.BACK_LEFT_OFFSET, 
        "BL"
        );

        backRightModule = new SwerveModule(
        DriveConstants.BACK_RIGHT_DRIVE_ID, 
        DriveConstants.BACK_RIGHT_ROTATE_ID, 
        DriveConstants.BR_ENCODER_PORT, 
        false, true, 
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
      NavX = new AHRS(AHRS.NavXComType.kUSB1);

      /* Initalizes Odometry */
      odometry = new SwerveDriveOdometry( 

        kinematics, 
        Rotation2d.fromDegrees(NavX.getAngle() % 360),
        getCurrentSwerveModulePositions()

        );

        fieldOriented = false;
        slowMode = false;

      poseSupplier = () -> getPose2d();
      resetPoseConsumer = pose -> resetOdometry(pose);
      robotRelativeOutput = chassisSpeeds -> drive(chassisSpeeds);
      chasisSpeedSupplier = () -> getChassisSpeeds();
      shouldFlipSupplier = () -> { var alliance = DriverStation.getAlliance(); System.out.println(alliance.get() == DriverStation.Alliance.Red);  
                                   return alliance.get() == DriverStation.Alliance.Red;};

                               

        try {
          config = RobotConfig.fromGUISettings();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (ParseException e) {
          e.printStackTrace();
        }    

        feild = new Field2d();

        new Thread(() -> {
          try {
            Thread.sleep(1000);
            NavX.reset();
          } catch (Exception e) {}
        }).start();

        configureAutoBuilder();
    }

    public void toggleFieldOriented (){
      
      fieldOriented = !fieldOriented;

    }

    public void toggleSlowMode() {
      slowMode = !slowMode;
    }

    public boolean getSlowMode() {
      return slowMode;
    }

    public ChassisSpeeds getChassisSpeeds(){

      return chassisSpeeds;

    }

      /* This drive method takes the values from the chassisspeeds and 
      applys in to each indivual Module using the "SetState" Method created in SwereMoudule */
  
      public void drive(ChassisSpeeds chassisSpeeds) {

        /* When Field Orientated is True, passes the chassis speed and the Gryo's current angle through "fromFieldRelativeSpeeds",
         before passing it through the rest of the drive Method */

         /*
         //More Garbage Mr. Neal Cooked Up
         if(Math.abs(chassisSpeeds.omegaRadiansPerSecond) > 0)
            heading = NavX.getAngle();

         double rotateCompensation = rotateCompensator.calculate(NavX.getAngle(), heading);
         chassisSpeeds = new ChassisSpeeds(chassisSpeeds.vxMetersPerSecond, chassisSpeeds.vyMetersPerSecond, chassisSpeeds.omegaRadiansPerSecond + rotateCompensation);
        //Stop Mr. Neal Garbage
        */
        
        if (fieldOriented) {
          chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(chassisSpeeds, new Rotation2d(NavX.getRotation2d().getRadians()));

        }
    
        SwerveModuleState swerveModuleStates[] = kinematics.toWheelSpeeds(chassisSpeeds);

        frontLeftModule.setState(swerveModuleStates[0]);
        frontRightModule.setState(swerveModuleStates[1]);
        backLeftModule.setState(swerveModuleStates[2]);
        backRightModule.setState(swerveModuleStates[3]);
        
        this.chassisSpeeds = chassisSpeeds;

      }

      /* This drive method simply spins wheels  */

      public void testDrive(){

        ChassisSpeeds testSpeeds = new ChassisSpeeds(Units.inchesToMeters(1), Units.inchesToMeters(0), Units.degreesToRadians(0));

        SwerveModuleState[] swerveModuleStates = kinematics.toWheelSpeeds(testSpeeds);

        frontLeftModule.setState(swerveModuleStates[0]);
        frontRightModule.setState(swerveModuleStates[1]);
        backLeftModule.setState(swerveModuleStates[2]);
        backRightModule.setState(swerveModuleStates[3]);
      }



      /* Method that returns the Moudle positions */
      public SwerveModulePosition[] getCurrentSwerveModulePositions(){
        return new SwerveModulePosition[]{

            new SwerveModulePosition(frontLeftModule.getDistance(), new Rotation2d(frontLeftModule.getRotateEncoderPosition())), // Front left
            new SwerveModulePosition(frontRightModule.getDistance(), new Rotation2d(frontRightModule.getRotateEncoderPosition())), // Front Right
            new SwerveModulePosition(backLeftModule.getDistance(), new Rotation2d(backLeftModule.getRotateEncoderPosition())), // Back Left
            new SwerveModulePosition(backRightModule.getDistance(), new Rotation2d(backRightModule.getRotateEncoderPosition())) // Back Right

        };
      } 

      /* Method that stops all modules */
      public void stopModules() {
        frontLeftModule.stop();
        frontRightModule.stop();
        backLeftModule.stop();
        backRightModule.stop();
    }

    public Pose2d getPose2d(){
      return odometry.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose){
       odometry.resetPosition(NavX.getRotation2d(), getCurrentSwerveModulePositions(), pose);
    }
    
    public AHRS getGyro(){
      return NavX;
    }

    public void configureAutoBuilder() {
      AutoBuilder.configure(
        poseSupplier, 
        resetPoseConsumer, 
        chasisSpeedSupplier, 
        robotRelativeOutput, 
        DriveConstants.PATH_CONFIG_CONTROLLER, 
        config, 
        shouldFlipSupplier,
        this
        );
    }
    

    public void putFrontLeftValues(SendableBuilder sendableBuilder){
      sendableBuilder.addDoubleProperty(frontLeftModule.printLabel() + " Offset", ()-> frontLeftModule.getRawOffsets(), null);
      sendableBuilder.addDoubleProperty(frontLeftModule.printLabel() + " Rotate Encoder(Radians): " , ()-> frontLeftModule.getRotateEncoderPosition(), null);
      sendableBuilder.addDoubleProperty(frontLeftModule.printLabel() + " Absoulete Position " , ()-> frontLeftModule.getAnalogEnoderValue(), null);
      sendableBuilder.addDoubleProperty(frontLeftModule.printLabel() + "Velocity", () -> frontLeftModule.getDriveVelocity(), null);
      if(swerveModuleStates != null)
        sendableBuilder.addDoubleProperty(frontLeftModule.printLabel() + " Analog Offest " , ()-> swerveModuleStates[0].angle.getRadians(), null);

    }

    public void putFrontRightValues(SendableBuilder sendableBuilder){
      sendableBuilder.addDoubleProperty(frontRightModule.printLabel() + " Offset", ()-> frontRightModule.getRawOffsets(), null);
      sendableBuilder.addDoubleProperty(frontRightModule.printLabel() + " Rotate Encoder(Radians): " , ()-> frontRightModule.getRotateEncoderPosition(), null);
      sendableBuilder.addDoubleProperty(frontRightModule.printLabel() + " Absoulete Position " , ()-> frontRightModule.getAnalogEnoderValue(), null);
      sendableBuilder.addDoubleProperty(frontRightModule.printLabel() + "Velocity", () -> frontRightModule.getDriveVelocity(), null);
      if(swerveModuleStates != null)
        sendableBuilder.addDoubleProperty(frontRightModule.printLabel() + " Analog Offest " , ()-> swerveModuleStates[1].angle.getRadians(), null);
    }

    public void putBackLeftModule(SendableBuilder sendableBuilder){
      sendableBuilder.addDoubleProperty(backLeftModule.printLabel() + " Offset", ()-> backLeftModule.getRawOffsets(), null);
      sendableBuilder.addDoubleProperty(backLeftModule.printLabel() + " Rotate Encoder(Radians): " , ()-> backLeftModule.getRotateEncoderPosition(), null);
      sendableBuilder.addDoubleProperty(backLeftModule.printLabel() + " Absoulete Position " , ()-> backLeftModule.getAnalogEnoderValue(), null);
      sendableBuilder.addDoubleProperty(backLeftModule.printLabel() + "Velocity", () -> backLeftModule.getDriveVelocity(), null);
      if(swerveModuleStates != null)
        sendableBuilder.addDoubleProperty(backLeftModule.printLabel() + " Analog Offest " , ()-> swerveModuleStates[2].angle.getRadians(), null);

    }

    public void putBackRightModule(SendableBuilder sendableBuilder){
      sendableBuilder.addDoubleProperty(backRightModule.printLabel() + " Offset", ()-> backRightModule.getRawOffsets(), null);
      sendableBuilder.addDoubleProperty(backRightModule.printLabel() + " Rotate Encoder(Radians): " , ()-> backRightModule.getRotateEncoderPosition(), null);
      sendableBuilder.addDoubleProperty(backRightModule.printLabel() + " Absoulete Position " , ()-> backRightModule.getAnalogEnoderValue(), null);
      sendableBuilder.addDoubleProperty(backRightModule.printLabel() + "Velocity", () -> backRightModule.getDriveVelocity(), null);
      if(swerveModuleStates != null)
        sendableBuilder.addDoubleProperty(backRightModule.printLabel() + " Analog Offest " , ()-> swerveModuleStates[3].angle.getRadians(), null);
    }


  @Override 
  public void initSendable(SendableBuilder sendableBuilder){
    putFrontLeftValues(sendableBuilder);
    putFrontRightValues(sendableBuilder);
    putBackLeftModule(sendableBuilder);
    putBackRightModule(sendableBuilder);

    sendableBuilder.addBooleanProperty("Field Orienated", ()-> fieldOriented, null);
    sendableBuilder.addBooleanProperty("Slow Mode", ()-> slowMode, null);
    sendableBuilder.addDoubleProperty("Gyro Reading", ()-> NavX.getAngle() % 360, null);

    sendableBuilder.addDoubleProperty("FL Distance Travlled", ()-> frontLeftModule.getDistance(), null);
    sendableBuilder.addDoubleProperty("FL Velocity", ()-> frontLeftModule.getDriveVelocity(), null);

    sendableBuilder.addDoubleProperty("Pose2d  X", () ->  odometry.getPoseMeters().getX(), null);
    sendableBuilder.addDoubleProperty("Pose2d  Y", () ->  odometry.getPoseMeters().getY(), null);

    sendableBuilder.addDoubleProperty("Rotations", () ->  odometry.getPoseMeters().getRotation().getRadians(), null);

    sendableBuilder.addDoubleProperty("Chasis speeds, X", () -> getChassisSpeeds().vxMetersPerSecond, null);
    sendableBuilder.addDoubleProperty("Chasis speeds, Y", () -> getChassisSpeeds().vyMetersPerSecond, null);
    sendableBuilder.addDoubleProperty("Chasis speeds, rotation", () -> getChassisSpeeds().omegaRadiansPerSecond, null);


  }
      
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    //Updates the odometry every run
    odometry.update(Rotation2d.fromDegrees(NavX.getAngle() % 360), getCurrentSwerveModulePositions());

  }
}
