// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.opencv.core.Mat;

import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
 
  /* FINAL NEEDS TO BE ADDED TO ALL OF THESE WHEN ACTAULLY VALUES ARE FOUND */
  
public static class DriveConstants {

  public static final double DRIVE_GEAR_RATIO = 6.75;
  public static final double ROTATE_GEAR_RATIO = 12.8;

  public static final double WHEEL_DIAMETER = Units.inchesToMeters(4);
  public static final double WHEEL_CIRCUMFERENCE = 2 * Math.PI;

  // Drive Motor IDs
  public static final int FRONT_LEFT_DRIVE_ID = 1 ;
  public static final int FRONT_RIGHT_DRIVE_ID = 2;
  public static int BACK_LEFT_DRIVE_ID = 3;
  public static int BACK_RIGHT_DRIVE_ID = 4;

  // Rotate Motor IDs
  public static final int FRONT_LEFT_ROTATE_ID = 5;
  public static final int FRONT_RIGHT_ROTATE_ID = 6;
  public static final int BACK_LEFT_ROTATE_ID = 7;
  public static final int BACK_RIGHT_ROTATE_ID = 8;

  //Encoder Ports

  public static final int FL_ENCODER_PORT = 1;
  public static final int FR_ENCODER_PORT = 0;
  public static final int BL_ENCODER_PORT = 3;
  public static final int BR_ENCODER_PORT = 2;


  // Drive PID Values 
  public static final double DRIVE_P_VALUE = 0.1 ;
  public static final double DRIVE_I_VALUE = 0.0;
  public static final double DRIVE_D_VALUE = 0.7;
  public static final double DRIVE_FF_VALUE = 0.9;
  
  // Rotate PID Values
  public static final double ROTATE_P_VALUE = 0.25;
  public static final double ROTATE_I_VALUE = 0.0;
  public static final double ROTATE_D_VALUE = 0.3; 
  public static double ROTATE_FF_VALUE;

  // Offsets
  public static final double FRONT_LEFT_OFFSET = 3.23;
  public static final double FRONT_RIGHT_OFFSET= 3.47;
  public static final double BACK_LEFT_OFFSET = 5.78;
  public static final double BACK_RIGHT_OFFSET = 3.94;

  // Factors
  public static final double DRIVE_POSITION_CONVERSION = ((Math.PI * 2 * WHEEL_DIAMETER) / DRIVE_GEAR_RATIO);
  public static final double DRIVE_VELOCITY_CONVERSION = DRIVE_POSITION_CONVERSION * 60;

  public static final double ROTATE_POSITION_CONVERSION = (Math.PI * 2) / ROTATE_GEAR_RATIO;
  public static final double ROTATE_VELOCITY_CONVERSION = ROTATE_POSITION_CONVERSION * 60;

  // Speeds
  public static double MAX_DRIVE_SPEED = Units.feetToMeters(15.1);
  public static double SLOW_DRIVE_SPEED = Units.feetToMeters(5);

  public static double MAX_ROTATE_SPEED = 4 * Math.PI;

}

public static class WristConstants {
  public static int WRIST_MOTOR_ID;
}

public static class ClawConstansts {
  public static int CLAW_MOTOR_ID;
}

public static class ClimbConstants {
  public static int CLIMB_MOTOR_ID;
}

public static class ElevatorConstants{
  
  public static int ELEVATOR_MOTOR_ID;
}

public static class OperatorConstants {
  public static final int kDriverControllerPort = 0;
}

}