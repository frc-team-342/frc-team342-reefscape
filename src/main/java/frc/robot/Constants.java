// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  
public static class DriveConstants {

  // Drive Motor IDs
  public static int FRONT_LEFT_DRIVE_ID;
  public static int FRONT_RIGHT_DRIVE_ID;
  public static int BACK_LEFT_DRIVE_ID;
  public static int BACK_RIGHT_DRIVE_ID;

  // Rotate Motor IDs
  public static int FRONT_LEFT_ROTATE_ID;
  public static int FRONT_RIGHT_ROTATE_ID;
  public static int BACK_LEFT_ROTATE_ID;
  public static int BACK_RIGHT_ROTATE_ID;

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