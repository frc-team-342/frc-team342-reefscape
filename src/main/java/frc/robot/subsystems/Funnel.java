// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.SendableBuilder;

import static frc.robot.Constants.FunnelConstants.*;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.RelativeEncoder;
public class Funnel extends SubsystemBase {
  private final SparkMax funnel;
  private final SparkMaxConfig funnelConfig;
  private final RelativeEncoder relativeEncoder;
  private final SparkClosedLoopController funnelPID;

  private final Climber climber;
  /** Creates a new Funnel. */
  public Funnel(Climber climber) {
    this.climber = climber;
    funnel = new SparkMax(FUNNEL_ID, MotorType.kBrushless);
    funnelConfig = new SparkMaxConfig();
    funnelPID = funnel.getClosedLoopController();
    relativeEncoder = funnel.getEncoder(); 

    
    funnelConfig
      .idleMode(IdleMode.kBrake)
      .smartCurrentLimit(30);
    funnelConfig.encoder
      .positionConversionFactor(FUNNEL_POSITION_CONVERSION);
    funnelConfig.closedLoop
      .p(FUNNEL_PID_VALUES[0])
      .i(FUNNEL_PID_VALUES[1])
      .d(FUNNEL_PID_VALUES[2])
      .outputRange(-.2, .2);
    
    funnel.configure(funnelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
  }
  public void move(double speed){
    funnel.set(speed);
  }
  public void funnelToPosition(double position){
    System.out.println("(Pretending to) move funnel");
    //funnelPID.setReference(position, ControlType.kPosition);
  }
  public double getPosition(){
    return relativeEncoder.getPosition();
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(climber.getClimbMode()) 
      funnelToPosition(FUNNEL_UP);
    else 
      funnelToPosition(FUNNEL_DOWN);
  }
  @Override
  public void initSendable(SendableBuilder sendableBuilder) {
    super.initSendable(sendableBuilder);
    sendableBuilder.setSmartDashboardType("Funnel");
    sendableBuilder.addDoubleProperty("Funnel Encoder", () -> getPosition(), null);
  }
}
