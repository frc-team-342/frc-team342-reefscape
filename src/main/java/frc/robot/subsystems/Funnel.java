// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.MathUtil;
import static frc.robot.Constants.FunnelConstants.*;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;



public class Funnel extends SubsystemBase {
  private final SparkMax funnel;
  private final SparkMaxConfig funnelConfig;
  private final PIDController pidController;
  private final DutyCycleEncoder throughBore;

  private boolean goingDown;
  private double speed;
  private double currentPosition;

  /** Creates a new Funnel. */
  public Funnel() {
    funnel = new SparkMax(FUNNEL_ID, MotorType.kBrushless);
    funnelConfig = new SparkMaxConfig();
    pidController = new PIDController(FUNNEL_PID_VALUES[0], FUNNEL_PID_VALUES[1], FUNNEL_PID_VALUES[2]);
    throughBore = new DutyCycleEncoder(2);

    goingDown = false;

    funnelConfig
      .idleMode(IdleMode.kBrake)
      .smartCurrentLimit(30);

    funnel.configure(funnelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
  }

  public void move(double speed){
    funnel.set(speed);
  }

  public void funnelToPosition(double position){
    pidController.setTolerance(0.1);
    currentPosition = throughBore.get();
    speed = pidController.calculate(currentPosition, position);

    goingDown = speed < 0;

    //These are being used as soft stops so when we're tuning the PID values the wrist won't slam into the mechanical stops
    if((goingDown && currentPosition <= LOW_FUNNEL_POSITION) || (!goingDown && currentPosition >= HIGH_FUNNEL_POSITION))
      move(0);
    else
      move(speed);
  }

  public DutyCycleEncoder getThroughBore(){
    return throughBore;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Funnel Speed: ", speed);
  }
}
