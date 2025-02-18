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
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.SparkAnalogSensor;

import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants.DriveConstants;



/** Add your docs here. */

public class SwerveModule {

    private SparkMax driveMotor;
    private SparkMax rotateMotor;

    private SparkMaxConfig driveConfig;
    private SparkMaxConfig rotateConfig;

    private RelativeEncoder driveEnconder;
    private RelativeEncoder rotateEncoder;

    private AnalogInput analogInput;
    private AnalogEncoder analogEncoder;


    //private PIDController rotatePID;

    private SparkClosedLoopController driveController;
    private SparkClosedLoopController rotateController;

    private double encoderOffset;

    private String label;


    public SwerveModule (int driveID, int rotateID, int magEncoderPort, boolean invertRotate, boolean invertDrive, double encoderOffset, String label){

        driveMotor = new SparkMax(driveID, MotorType.kBrushless);
        rotateMotor = new SparkMax(rotateID, MotorType.kBrushless);

        driveConfig = new SparkMaxConfig();
        rotateConfig = new SparkMaxConfig();

            /** not sure weather its supposed to be brake or coast lol so come back to this lol 
             * And make sure to update the numver withing the stalllimit lol*/  

        driveConfig.smartCurrentLimit(60).idleMode(IdleMode.kCoast).inverted(invertDrive);
        rotateConfig.smartCurrentLimit(60).idleMode(IdleMode.kCoast).inverted(invertRotate);


        /** Get the encoders from the respective motors */
        driveEnconder = driveMotor.getEncoder();
        rotateEncoder = rotateMotor.getEncoder();

        /* Sets the Drive converstion (Posistion and Velocity)  factors  */
        driveConfig.encoder.positionConversionFactor(DriveConstants.DRIVE_POSITION_CONVERSION); //POSITION
        driveConfig.encoder.velocityConversionFactor(DriveConstants.DRIVE_VELOCITY_CONVERSION); //VELOCITY

        /* Set the Rotate conversion (Posistion and Velocity) factors */
        driveConfig.encoder.positionConversionFactor(DriveConstants.DRIVE_POSITION_CONVERSION); //POSITION
        driveConfig.encoder.velocityConversionFactor(DriveConstants.DRIVE_VELOCITY_CONVERSION); //VELOCITY

        /** Get the PIDController from the respective motors */
        driveController = driveMotor.getClosedLoopController();
        rotateController = rotateMotor.getClosedLoopController();

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
        rotateConfig.closedLoop.p(DriveConstants.ROTATE_P_VALUE);
        rotateConfig.closedLoop.i(DriveConstants.ROTATE_I_VALUE);
        rotateConfig.closedLoop.d(DriveConstants.ROTATE_D_VALUE);
        rotateConfig.closedLoop.velocityFF(DriveConstants.ROTATE_FF_VALUE);


     /* Configures drive and rotate motors with there SparkMaxConfig NOT FINISHED*/

        driveMotor.configure(driveConfig, null, PersistMode.kPersistParameters);
        rotateMotor.configure(driveConfig, null, PersistMode.kPersistParameters);

        analogInput = new AnalogInput(magEncoderPort);
        analogEncoder = new AnalogEncoder(analogInput);

        this.encoderOffset = encoderOffset;
        this.label = label;

        resetEncoder();

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

    /* Absoulete encoder angle in radians with offset removed SAME MIGHT TRY AND REWROKK  */
    public double getAbsouleteEncoderPosition() {
        double angle = analogInput.getVoltage() / RobotController.getVoltage5V();
        encoderOffset = analogEncoder.get();

        angle *= 2 * Math.PI;
        angle += encoderOffset;
        angle %= 2 * Math.PI;

        return angle;
    }

    public double getOffset() {
        return analogEncoder.get();
    }

    public double getRawOffsets(){
        double angle = analogInput.getVoltage() / RobotController.getVoltage5V();
        angle *= 2 * Math.PI;
        return angle;
    }

    public void resetEncoder() {
        driveEnconder.setPosition(0);
        rotateEncoder.setPosition(getOffset());
    }

    public void stop() {
       driveMotor.set(0);
       rotateMotor.set(0);
    }

    public String printLabel() {
        return label;
    }

    public void setState(SwerveModuleState state){

        driveController.setReference(state.speedMetersPerSecond, ControlType.kVelocity);
        rotateController.setReference(state.angle.getRadians(), ControlType.kPosition);

        System.out.println("Drive PID refrence : " + state.speedMetersPerSecond);
        System.out.println("Rotate PID refrence : " + state.angle.getRadians());

    }

    





























}
