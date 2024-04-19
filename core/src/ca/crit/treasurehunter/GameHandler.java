package ca.crit.treasurehunter;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

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
    public static float angle_sensor = 0;       //  Saves the angle captured by the sensor
    public static float angle_calibrated = 0;   //  Saves the angle from the calibration menu

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
    public static int environment = 2;
    public static final int MOBILE_ENV = 1;
    public static final int DESKTOP_ENV = 2;
    public static float timer;

    /**
     * --------------------------------------------------------------------------
     *                          COMMUNICATION WITH BLE
     * --------------------------------------------------------------------------
     */
    public static boolean sensorCalibrationRequest = false; //Send the instruction to the sensor to make calibration
    public static boolean sensorFinishedCalibration = false; //Indicates that the sensor has finished the calibration
    public static boolean gattServerDisconnected = false; //Indicates that the ESP32 has been disconnect from android the device

    /**
     * --------------------------------------------------------------------------
     *                                MAIN MENU
     * --------------------------------------------------------------------------
     */
    public static String gameMode_MainMenu;                 //"angles" mode or "laps" mode
    public static Integer beginningAngle_MainMenu = 0;      //Chosen angle to initialize the computer circle on Angles game mode
    public static Integer endAngle_MainMenu = 0;            //Chosen angle to finish the computer circle on Angles game mode
    public static Integer speed_MainMenu = 0;               //Speed of the computer circle to chase on both game modes
    public static String screen = "menu";                   //Runs the game on the screen: MainMenu/Game/Resume(Information)
    public static String rotationMode_MainMenu;             //The direction of the computer's circle rotation "izquierda" or "derecha"
    public static String card_MainMenu;                     //Saves the user's number card
    public static boolean isCalibrated;

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
    public static ArrayList<ArrayList<String>> relevantData = new ArrayList<>();  //dynamic array for display relevant captured Data
    public static ArrayList<ArrayList<String>> resumeData = new ArrayList<>();      //dynamic array for Sampling Data

    /**
     * --------------------------------------------------------------------------
     *                                CSV FILE
     * --------------------------------------------------------------------------
     */
    public static String savedFilePath = "path";

    /**
     * --------------------------------------------------------------------------
     *                                METHODS
     * --------------------------------------------------------------------------
     */
    public static void init(int env){
        GameHandler.environment = env;
    }

    public static void printMatrix(ArrayList<ArrayList<String>> dynamicArray) {
        // Imprimir el contenido
        for (ArrayList<String> row : dynamicArray) {
            for (String value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    public static void labelBlink(Label label, float visibleTime, float invisibleTime){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                label.setVisible(false);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        label.setVisible(true);
                    }
                }, visibleTime); // 1 segundo de espera
            }
        }, invisibleTime); // 1 segundo de espera
    }

    /*Just for BitmapFont objects*/
    public static void infiniteBlink(BitmapFont bitmapFont,SpriteBatch batch,String text, Integer X, Integer Y){
        timer += Gdx.graphics.getDeltaTime();
        batch.begin();
            if(timer < 1){
                bitmapFont.draw(batch, text, X, Y);     //The text Appears
            }
            if (timer > 1 && timer < 2) {
                bitmapFont.draw(batch, " ", X, Y);  //The text disappears
            }
            if(timer > 2){
                timer = 0;
            }
        batch.end();
    }
}
