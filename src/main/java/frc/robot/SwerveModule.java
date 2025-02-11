// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.*;
import com.revrobotics.spark.*;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.config.*;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.servohub.ServoHub.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants.DriveConstants;



/** Add your docs here. */

public class SwerveModule {

    private SparkMax driveMotor;
    private SparkMax rotateMotor;

    private SparkMaxConfig driveConfig;
    private SparkMaxConfig rotateConfig;

    private RelativeEncoder driveEnconder;
    private RelativeEncoder rotateEncoder;


    private AbsoluteEncoder absoluteEncoder;

    //private PIDController rotatePID;

    //private SparkClosedLoopController driveController;
    //private SparkClosedLoopController rotateController;

    private double encoderOffset;

    private String label;


    public SwerveModule (int driveID, int rotateID, boolean invertRotate, boolean invertDrive, double encoderOffset, String label){

        driveMotor = new SparkMax(driveID, MotorType.kBrushless);
        rotateMotor = new SparkMax(rotateID, MotorType.kBrushless);

        driveConfig = new SparkMaxConfig();
        rotateConfig = new SparkMaxConfig();

            /** not sure weather its supposed to be brake or coast lol so come back to this lol 
             * And make sure to update the numver withing the stalllimit lol*/  
        driveConfig.smartCurrentLimit(60).idleMode(IdleMode.kCoast).inverted(invertDrive);
        rotateConfig.smartCurrentLimit(60).idleMode(IdleMode.kCoast).inverted(invertDrive);


        /** Get the encoders from the respective motors */
        driveEnconder = driveMotor.getEncoder();
        rotateEncoder = rotateMotor.getEncoder();

        /* Sets the Drive converstion (Posistion and Velocity)  factors  */
        driveConfig.encoder.positionConversionFactor(DriveConstants.DRIVE_POSITION_CONVERSION);
        driveConfig.encoder.velocityConversionFactor(DriveConstants.DRIVE_VELOCITY_CONVERSION);

        /* Set the Rotate conversion (Posistion and Velocity) factors */
        driveConfig.encoder.positionConversionFactor(DriveConstants.DRIVE_POSITION_CONVERSION);
        driveConfig.encoder.velocityConversionFactor(DriveConstants.DRIVE_VELOCITY_CONVERSION);



        /** Get the PIDController from the respective motors */
            //driveController = driveMotor.getClosedLoopController();
            //rotateController = rotateMotor.getClosedLoopController();

        /* Sets the feedback sensor for each motor */
        driveConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);
        rotateConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        /* Drive PID values */
        driveConfig.closedLoop.p(DriveConstants.DRIVE_P_VALUE);
        driveConfig.closedLoop.i(DriveConstants.DRIVE_I_VALUE);
        driveConfig.closedLoop.d(DriveConstants.DRIVE_D_VALUE);
        driveConfig.closedLoop.velocityFF(DriveConstants.DRIVE_FF_VALUE);

        /* Rotate PID wrapping */
        rotateConfig.closedLoop.positionWrappingEnabled(true);
        rotateConfig.closedLoop.positionWrappingMinInput(0);
        rotateConfig.closedLoop.positionWrappingMinInput(90);

         /* Rotate PID values */
        rotateConfig.closedLoop.p(0);
        rotateConfig.closedLoop.i(0);
        rotateConfig.closedLoop.d(0);
        rotateConfig.closedLoop.velocityFF(0);
        

     /* Configures drive and rotate motors with there SparkMaxConfig NOT FINISHED*/

        driveMotor.configure(driveConfig, null,PersistMode.kPersistParameters);
        rotateMotor.configure(driveConfig, null, null);

        this.encoderOffset = encoderOffset;
        this.label = label;

    }

    /* Returns the distance robot has travlled in meters */
    public double getDistance() {
            return driveEnconder.getPosition();
    }

    /* Returns the Angle of the wheels in Radians */
    public double getRotatePosition() {
        return rotateEncoder.getPosition();
    }

    /* Returns the Angle of the wheels in Degrees*/
    public Rotation2d getAngle() {
        return Rotation2d.fromDegrees(rotateEncoder.getPosition());
    }

    /* Returns the Drive Encoder velocity meters/second */
    public double getDriveVelocity() {
        return driveEnconder.getVelocity();
    }

    





























}
