package ca.crit.treasurehunter;


import com.badlogic.gdx.Screen;

public class GameHandler {
    public static int WORLD_WIDTH = 128;
    public static int WORLD_HEIGHT = 72;

    public static float playedTime_sec = 0;         // Elapsed game time
    public static float playedTime_min = 0;         // Elapsed game time
    public static int RoundTrips = 0;          // Number of times the computer circle went forward-back
    /*TIMON*/
    public static boolean reached = false;      // Bandera para que ocurra el parallax y la recolección de tesoros

    /*COLLISION*/
    public static boolean collided;             // Colisionan barco-cofre
    public static boolean treasureAppeared;     // Apareció un cofre

    /*TREASURE*/
    public static float treasurePosition;
    public static int counter;

    public static boolean onomatopoeiaAppear;

    /*OTHERS*/
    public static int environment;
    public static final int MOBILE_ENV = 1;

    /*MENU*/
    public static String gameMode_MainMenu;
    public static Integer beginningAngle_MainMenu = 0;
    public static Integer endAngle_MainMenu = 0;
    public static Integer speed_MainMenu = 0;
    public static String screen_MainMenu = "menu";
    public static String rotationMode_MainMenu;

    public static void init(int env){
        GameHandler.environment = env;
    }
}
