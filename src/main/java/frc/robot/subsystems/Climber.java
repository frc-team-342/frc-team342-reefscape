// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import com.revrobotics.spark.SparkClosedLoopController;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimbConstants;

public class Climber extends SubsystemBase {
  private SparkMax climbMotor;
  private SparkMaxConfig climbConfig;

  private RelativeEncoder relativeEncoder;

  private SparkClosedLoopController climbPID;

  private boolean climbMode = false;

  /** Creates a new Climb. */
  public Climber() {
    climbMotor = new SparkMax(ClimbConstants.CLIMB_ID, MotorType.kBrushless);
    climbConfig = new SparkMaxConfig();
    climbPID = climbMotor.getClosedLoopController();

    climbConfig
      .idleMode(IdleMode.kBrake)
      .smartCurrentLimit(30);
    
      
      climbMotor.configure(climbConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
      relativeEncoder = climbMotor.getEncoder();
    }

  public void moveClimber(double speed){
    climbMotor.set(speed);
  }
  public void stop() {
    climbMotor.set(0);
  }

  public double getPosition(){
    return relativeEncoder.getPosition();
  } 

  public void holdPosition() {
    climbPID.setReference(getPosition(), ControlType.kPosition);
  }

  public boolean getClimbMode() {
    return climbMode;
  }

  public void toggleClimbMode(){
    climbMode = !climbMode;
    //if(climbMode)
    //  open funnel
    //  loosen
  }
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Climber Position", getPosition());

  }
  @Override
  public void initSendable(SendableBuilder sendableBuilder) {
    sendableBuilder.addBooleanProperty("Climb Mode", () -> climbMode, null);
  }
}
