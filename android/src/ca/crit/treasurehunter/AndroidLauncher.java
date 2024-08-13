package ca.crit.treasurehunter;

import static java.lang.Thread.sleep;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.UUID;

import ca.crit.treasurehunter.rojoble.RojoBLE;

public class AndroidLauncher extends AndroidApplication {
	public final UUID txChUUID = UUID.fromString("131552ca-7c3a-11ee-b962-0242ac120002");
	public final UUID rxChUUID = UUID.fromString("0f16c6ae-7c3a-11ee-b962-0242ac120002");
	private static final String TAG = "AndroidLauncher";
	private static final String deviceName = "ShoulderWheel";
	private final int MAX_NO_CONDITIONS_MET = 50;
	private final float DEFAULT_MAX_INCREMENT = 20;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private String deviceMacAddress;
	private RojoBLE rojoTX, rojoRX;
	private String strValue;
	private boolean isFirstEvent = true;
	private boolean moveCircle = false;
	private int noSafeCondCounter = 0;
	private float sensorAngle = 0;
	private float MaxIncrement = DEFAULT_MAX_INCREMENT;
	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		GameHandler.init(GameHandler.MOBILE_ENV);
		if(!RojoBLE.checkBLESupport(this, bluetoothAdapter)) {
			Toast.makeText(getApplicationContext(), "Your device doesn't support bluetooth", Toast.LENGTH_LONG).show();
			finish();
		}
		deviceMacAddress = RojoBLE.searchForMacAddress(this, bluetoothAdapter, deviceName);
		if(deviceMacAddress == null) {
			Log.e(TAG, "ESP32 not found");
			Toast.makeText(getApplicationContext(), "ESP32 not found", Toast.LENGTH_LONG).show();
		}
		else {
			rojoTX = new RojoBLE(this, rxChUUID, RojoBLE.ROJO_TYPE_WRITE, deviceMacAddress);
			rojoRX = new RojoBLE(this, txChUUID, RojoBLE.ROJO_TYPE_NOTIFY, deviceMacAddress);
			rojoRX.setOnCharacteristicNotificationListener(this::onCharacteristicNotificationListener);
			rojoRX.setOnGattServerDisconnectedListener(this::onGattServerDisconnectedListener);
			rojoRX.setOnGattServerConnectedListener(this::onGattServerConnectedListener);
		}
		/**
		 * THREADS CALL
		 */

		/**
		 * @brief Thread that handles the detection of the global variables
		 *        and the bidirectional messages with the ESP32
		 * @note  The periodic call of the thread it's modified depending if the
		 * 		  ESP32 it's connected or not, in order to react more quickly at some events
		 */
		//Variables declared as final pointer in order to use a static RAM
		//location so it can be accessible from the Thread
		//This variables are not local to the thread so they cannot be destroyed if the thread gets killed
		final int[] MAX_NUM_TRIES = {5}; //Number of tries for the error reporting
		final int[] timeCounter = {0}; //Counts the time that has passed to make an intent of reconnection
		final int[] numberOfTries = {0}; //Counts the number of tries to report the disconnection as error

		Thread BLECommThread = new Thread() {
			@Override
			public void run() {
				final String ThTAG = "BLECommTh";
				//Initialization routine of the task
				timeCounter[0] = 0;
				try {
					//Infinite loop of the task
					while (true) {
						if(GameHandler.sensorCalibrationRequest) {
							//Calibration request ordered from the game
							GameHandler.sensorCalibrationRequest = false;
							GameHandler.sensorFinishedCalibration = false;
							if(!rojoTX.sendData("MPUStartCal")) {
								GameHandler.failedToWriteCharacteristic = true;
							}
							sleep(1000); //Setting resolution to 1 sec
						}
						else if(GameHandler.gattServerDisconnected){
							//The ESP32 has disconnected from the android device
							final int SECONDS_TO_RECONNECT = 10;

							if(timeCounter[0] == SECONDS_TO_RECONNECT){
								//Try to reconnect each 5 seconds
								Log.i(ThTAG, "Trying to reconnect to the gatt server");
								rojoRX.connectToGattServer();
								numberOfTries[0]++;
								timeCounter[0] = 0;

								if(numberOfTries[0] == MAX_NUM_TRIES[0]){
									//The number of tries has reached it's maximum
									rojoRX.bleAutoConnect = true;
									numberOfTries[0] = 0;
									Log.e(ThTAG, "Could not connect to the gatt server waiting 5 seconds");
									sleep(5000);
								}
							}
							timeCounter[0]++;
							sleep(100); //Changing resolution to 100ms
						}
						else{
							sleep(1000); //Default resolution to 1 sec
						}
						if(moveCircle) {
							//Program is requesting a circle position fix
							final int ANGLE_BOUNDS = 2;

							moveCircle = false;
							//Blocking the thread
							do {
								//Moving the circle
								if(GameHandler.angle_sensor > sensorAngle)
									GameHandler.angle_sensor++;
								else if(GameHandler.angle_sensor < sensorAngle)
									GameHandler.angle_sensor--;
								if(GameHandler.angle_sensor >= 360 || GameHandler.angle_sensor <= 0)
									GameHandler.angle_sensor = 0;
								sleep(1);
							} while(!(GameHandler.angle_sensor > sensorAngle-ANGLE_BOUNDS
									&& GameHandler.angle_sensor < sensorAngle+ANGLE_BOUNDS));
							MaxIncrement = DEFAULT_MAX_INCREMENT;
						}
					}
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		//Starting the thread
		BLECommThread.start();
		//Initializing the game

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Main_treasureHunter(), config);
	}

	public void onGattServerConnectedListener() {
		//Notifying to the game
		GameHandler.gattServerDisconnected = false;
	}

	public void onGattServerDisconnectedListener() {
		//Notifying to the game
		GameHandler.gattServerDisconnected = true;
	}

	/**
	 * @brief Listener that handles all the receiving messages
	 * @param value Data that arrives from the BLE device
	 */
	public void onCharacteristicNotificationListener(byte[] value) {
		strValue = RojoBLE.getString(value);

		if(strIsFloatNumber(strValue)) { //!Normal flow of the code!
			//The command is a number
			if (numberHandling(strValue) != 0) {
				Log.e(TAG, "The parse of the code has been executed with errors");
			}
		}
		else {
			//Is a command string
			if(controlStringHandler(strValue) != 0) {
				Log.e(TAG, "Unknown BLE command [" + strValue.replace("\n", "") + "]");
			}
		}
		Log.i(TAG, strValue);
	}

	/**
	 * @brief  Handles the data if it's a valid position for the circle and makes process
	 * @note   Consider that if localAngle is 360° it will pass to be a 0°
	 * 		   in order to respect a full circle from 0° to 360°
	 * @param  msg Message received to be parsed and processed
	 * @return result ->
	 * 			0: If everything is ok
	 * 		    1: If the execution of the method had errors
	 */
	private int numberHandling(String msg) {
		int result = 0;
		//Parsing the signal
		try {
			sensorAngle = Float.parseFloat(msg);
			//Log.i(TAG, "Data raw: " + sensorAngle);
			if (isFirstEvent) {
				GameHandler.angle_sensor = sensorAngle;
				isFirstEvent = false;
			}
		} catch (NumberFormatException exception) {
			Log.i(TAG, "The incoming data is not a number, wrong parse");
			result = 1;
		}

		//Processing the signal
		try {
			signalProcessing(sensorAngle);
		} catch (Exception irqEx) {
			irqEx.printStackTrace();
			result = 1;
		}
		return result;
	}

	/**
	 * @brief Uses bounds and the difference to avoid undesired jumps of the circle
	 * 		  Also reports to the Circle Thread if the circle got stuck
	 * @param angle Angle that te sensor reports from the Gatt server
	 */
	private void signalProcessing(float angle) {
		float diff = Math.abs(angle - GameHandler.angle_sensor);

		//Draws the circle between a range of 0° to 360° degrees
		if(diff < MaxIncrement){
			GameHandler.angle_sensor = angle;
		}
		//Allows the circle to stay from 10° to 350° passing through 0°
		else if(GameHandler.angle_sensor < MaxIncrement && angle >= (360-MaxIncrement)){
			GameHandler.angle_sensor = angle;
		}
		//Allows the circle to stay from 350° to 10° passing through 0°
		else if(GameHandler.angle_sensor > (360-MaxIncrement) && (angle-360+GameHandler.angle_sensor) <= MaxIncrement){
			GameHandler.angle_sensor = angle;
		}
		else {
			//None of the safe conditions were met
			noSafeCondCounter++;
			if(noSafeCondCounter >= MAX_NO_CONDITIONS_MET) {
				MaxIncrement = 0;
				moveCircle = true;
				noSafeCondCounter = 0;
			}

		}
		//Log.i(TAG, "Data of gamehandler: " + GameHandler.angle_sensor);
	}

	/**
	 * @brief  Parses the desired command into the system
	 * @note   Compare strings are being extracted from the ESP32 code
	 * @param  msg Message received to be parsed and processed
	 * @return result ->
	 * 			0: If everything is ok
	 * 		    1: If the execution of the method had errors
	 */
	private int controlStringHandler(String msg) {
		final String sensorCalDone = "MPUReady";
		final String sensorCalInit = "MPUCal";

		int result = 0;
		if(msg.contains(sensorCalDone)) {
			GameHandler.sensorFinishedCalibration = true;
			Log.i(TAG, "Finished calibration");
		}
		else if(msg.contains(sensorCalInit)) {
			Log.i(TAG, "Starting calibration");
		}
		else {
			result = 1;
		}
		return result;
	}

	/**
	 * @brief  It checks if a hole string is a number
	 * @param  input String that will be evaluated
	 * @return True if the input is a number
	 */
	private boolean strIsFloatNumber(String input) {
		assert input != null;
		try{
			Float.parseFloat(input);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}
}
