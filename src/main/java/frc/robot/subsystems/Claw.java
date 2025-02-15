// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ResourceBundle.Control;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ControlModeValue;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;


public class Claw extends SubsystemBase {
  /** Creates a new Claw. */
  
    private TalonFX claw;
    private DigitalInput clawSensor;
    private double intakeSpeed = .09;
    private double outtakeSpeed = -.09;

   // private Joystick joy;
    //private JoystickButton jButton;



  public Claw() {

    TalonFX claw = new TalonFX(1);
    DigitalInput clawSensor = new DigitalInput(2);
    //Joystick joy = new Joystick(1);
   // JoystickButton jButton = new JoystickButton(joy, 1);

    claw.set(.05);
  }

  public void Intake(){
      if(clawSensor.get()){
        claw.set(0);
      }
      else{
        claw.set(intakeSpeed);
      }
      //if the sensor detects something it will stop but if not it will default to spinning intake
  } 

  public void outTake(){
    if(){
      claw.set(outtakeSpeed);
    }
  }

  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
