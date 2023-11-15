package ca.crit.treasurehunter;


import com.badlogic.gdx.Screen;

public class GameHandler {
    public static int WORLD_WIDTH = 128;
    public static int WORLD_HEIGHT = 72;

    /*TEXT SCREEN*/
    public static float playedTime_sec = 0;    // Elapsed game time
    public static float playedTime_min = 0;    // Elapsed game time
    public static int RoundTrips = 0;          // Number of times the computer circle went forward-back

    /*SHIP*/
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

    /*MAIN MENU*/
    public static String gameMode_MainMenu;                 //Angles mode or laps mode
    public static Integer beginningAngle_MainMenu = 0;      //Chosen angle to initialize on Angles game mode
    public static Integer endAngle_MainMenu = 0;            //Chosen angle to finish on Angles game mode
    public static Integer speed_MainMenu = 0;               //Speed of the computer circle to chase on both game modes
    public static String screen = "menu";                   //Runs the game on the screen: MainMenu/Game/Resume(Information)
    public static String rotationMode_MainMenu;             //The direction of the computer's circle rotation
    public static String card_MainMenu;                     //Saves the user's number card

    /*SCREEN*/
    public static final float viewportWidth = 720;
    public static final float viewportHeight = 480;

    /*RESUME MENU*/


    public static void init(int env){
        GameHandler.environment = env;
    }
}
