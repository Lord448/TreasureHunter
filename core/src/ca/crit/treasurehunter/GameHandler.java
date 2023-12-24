package ca.crit.treasurehunter;


public class GameHandler {
    /**
     * --------------------------------------------------------------------------
     *                                WORLD CONSTANTS
     * --------------------------------------------------------------------------
     */
    public static int WORLD_WIDTH = 128;
    public static int WORLD_HEIGHT = 72;

    /**
     * --------------------------------------------------------------------------
     *                                 TEXT SCREEN
     * --------------------------------------------------------------------------
     */
    public static float playedTime_sec = 0;    // Elapsed game time in seconds
    public static float playedTime_min = 0;    // Elapsed game time in minutes
    public static int RoundTrips = 0;          // Number of times the computer circle went forward-back

    /*SHIP*/
    public static boolean reached = false;      // Flag that makes parallax and treasure hunt occur
    /**
     * --------------------------------------------------------------------------
     *                                CIRCLE BAR
     * --------------------------------------------------------------------------
     */
    public static float angle_sensor;

    /**
     * --------------------------------------------------------------------------
     *                                 COLLISION
     * --------------------------------------------------------------------------
     */
    public static boolean collided;             // Collision between treasure-ship
    public static boolean treasureAppeared;     // A new treasure appeared

    /**
     * --------------------------------------------------------------------------
     *                                 TREASURE
     * --------------------------------------------------------------------------
     */
    public static float treasurePosition;
    public static int counter;

    public static boolean onomatopoeiaAppear;

    /**
     * --------------------------------------------------------------------------
     *                                  OTHERS
     * --------------------------------------------------------------------------
     */
    public static int environment=1;
    public static final int MOBILE_ENV = 1;
    public static final int DESKTOP_ENV = 2;

    /**
     * --------------------------------------------------------------------------
     *                                MAIN MENU
     * --------------------------------------------------------------------------
     */
    public static String gameMode_MainMenu;                 //Angles mode or laps mode
    public static Integer beginningAngle_MainMenu = 0;      //Chosen angle to initialize on Angles game mode
    public static Integer endAngle_MainMenu = 0;            //Chosen angle to finish on Angles game mode
    public static Integer speed_MainMenu = 0;               //Speed of the computer circle to chase on both game modes
    public static String screen = "menu";                   //Runs the game on the screen: MainMenu/Game/Resume(Information)
    public static String rotationMode_MainMenu;             //The direction of the computer's circle rotation
    public static String card_MainMenu;                     //Saves the user's number card

    //TODO: FIZ THAT GAME DOESNT WORK WITH THE ARROWS FROM THE LAPTOP
    /**
     * --------------------------------------------------------------------------
     *                                SCREEN
     * --------------------------------------------------------------------------
     */
    public static final float viewportWidth = 720;
    public static final float viewportHeight = 480;

    /**
     * --------------------------------------------------------------------------
     *                                RESUME MENU
     * --------------------------------------------------------------------------
     */
    public static String[][] data = {{"Tesoros", "Vueltas", "Tiempo"},
            {String.valueOf(0), String.valueOf(0),  0+" min con "+0 + " sec"}};
    public static void setData(String[][] data) {
        GameHandler.data = data;
        System.out.println("data: " + data[1][2]);
    }

    /**
     * --------------------------------------------------------------------------
     *                                METHODS
     * --------------------------------------------------------------------------
     */
    public static void init(int env){
        GameHandler.environment = env;
    }
}
