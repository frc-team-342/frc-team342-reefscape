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
  /*
   * Constants are tbd
   */
  public static class WristConstants {
    public static final int INTAKE_SENSOR = 1;
  
    public static final int WRIST_ID = 9;
  
    public static final int MAG_ENCODER = 7;
  
    public static final double WRIST_SPEED = 0.7;

    //Wrist PID values; they're a list for sake of simplicity
    public static final double[] WRIST_PID_VALUES = {1, 0, 0.01};
  
    //Wrist position Values (absolute enocder values I think)
    public static final double LOW_WRIST_POS = 0.9;
    public static final double HIGH_WRIST_POS = 0.30;

    //Zeroes below are placeholders for the time being
    //Because L2 and L3 have the same angles, only L2 will be used
    public static final double L1_POSITION = 0;
    public static final double L2_POSITION = 0;
    public static final double L4_POSITION = 0;
    public static final double ALGAE_POSITION = 0;
  
    public static final double MAX_DISTANCE = 83;
    
    public static final double DEFAULT_CURRENT = 30;
  }
  
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
}
