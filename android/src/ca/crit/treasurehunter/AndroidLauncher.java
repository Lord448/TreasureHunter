package ca.crit.treasurehunter;

import static java.lang.Thread.sleep;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.UUID;

import ca.crit.treasurehunter.rojoble.RojoBLE;

public class AndroidLauncher extends AndroidApplication {
	public final UUID txChUUID = UUID.fromString("131552ca-7c3a-11ee-b962-0242ac120002");
	public final UUID rxChUUID = UUID.fromString("0f16c6ae-7c3a-11ee-b962-0242ac120002");
	private static final String TAG = "AndroidLauncher";
	private static final String deviceName = "ShoulderWheel";
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private String deviceMacAddress;
	private RojoBLE rojoTX, rojoRX;
	private String strValue;
	private boolean isFirstEvent = true;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		}
		GameHandler.init(GameHandler.MOBILE_ENV);

		/**
		 * THREADS CALL
		 */

		/**
		 * @brief Thread that handles the detection of the global variables
		 *        and the bidirectional messages with the ESP32
		 */
		Thread BLECommThread = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						sleep(1000);
						//handler.post(this);

						//Code implementation
						if(GameHandler.sensorCalibrationRequest) {
							//Calibration request ordered from the game
							GameHandler.sensorCalibrationRequest = false;
							GameHandler.sensorFinishedCalibration = false;
							rojoTX.sendData("MPUStartCal");
						}
						//Code implementation

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

	/**
	 * @brief Listener that handles all the receiving messages
	 * @param value
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
	}

	/**
	 * @brief  Fixes the circle be drown in angles where actually the sensor is not
	 * @note   Consider that if localAngle is 360° it will pass to be a 0°
	 * 		   in order to respect a full circle from 0° to 360°
	 * @param  msg : Message received to be parsed and processed
	 * @return result ->
	 * 			0: If everything is ok
	 * 		    1: If the execution of the method had errors
	 */
	private int numberHandling(String msg) {
		int result = 0;
		float diff, angle = 0;
		final float MaxIncrement = 20; //TODO Fine adjust with kids
		//final float MaxIncrement_RiskZone = 360-MaxIncrement_NormalZone;

		try {
			angle = Float.parseFloat(msg);
			if(isFirstEvent) {
				GameHandler.angle_sensor = angle;
				isFirstEvent = false;
			}
		}
		catch (NumberFormatException exception) {
			Log.i(TAG, "The incoming data is not a number, wrong parse");
			result = 1;
		}

		diff = Math.abs(angle - GameHandler.angle_sensor);
		//Draws the circle between a range of 0° to 360° degrees
		if(diff < MaxIncrement){
			GameHandler.angle_sensor = angle;
		}
		//Allows the circle to be drown from 10° to 350° passing through 0°
		if(GameHandler.angle_sensor < MaxIncrement && angle >= (360-MaxIncrement)){
			GameHandler.angle_sensor = angle;
		}
		//Allows the circle to be drown from 350° to 10° passing through 0°
		if(GameHandler.angle_sensor > (360-MaxIncrement) && (angle-360+GameHandler.angle_sensor) <= MaxIncrement){
			GameHandler.angle_sensor = angle;
		}
		return result;
	}

	/**
	 * @brief  Parses the desired command into the system
	 * @note   Compare strings are being extracted from the ESP32 code
	 * @param  msg : Message received to be parsed and processed
	 * @return result ->
	 * 			0: If everything is ok
	 * 		    1: If the execution of the method had errors
	 */
	private int controlStringHandler(String msg) {
		final String sensorCalDone = "MPUReady";

		int result = 0;
		if(msg.contains(sensorCalDone)) {
			GameHandler.sensorFinishedCalibration = true;
			Log.i(TAG, msg);
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
