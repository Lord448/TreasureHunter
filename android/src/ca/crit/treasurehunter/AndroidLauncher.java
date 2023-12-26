package ca.crit.treasurehunter;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Log;
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
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private String deviceMacAddress;
	private RojoBLE rojoTX, rojoRX;
	private String strValue;
	private boolean firstEvent = true, flagNormalZone = false, flagRiskZone = false;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Main_treasureHunter(), config);

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
		//GameHandler.init(GameHandler.MOBILE_ENV);
		//GameHandler.init(GameHandler.DESKTOP_ENV);
	}

	public void onCharacteristicNotificationListener(byte[] value) {
		final float MaxIncrement = 20; //TODO Fine adjust with kids
		//final float MaxIncrement_RiskZone = 360-MaxIncrement_NormalZone;
		float localAngle = 0;
		float diff;

		strValue = RojoBLE.getString(value);

		//Log.i(TAG, "Received: " + strValue);
		try {
			localAngle = Float.parseFloat(strValue);
			if(firstEvent) {
				GameHandler.angle_sensor = localAngle;
				firstEvent = false;
			}
		}
		catch (NumberFormatException ex) {
			Log.i(TAG, "Dato que llego no es un numero");
		}

		/** FIXES THE CIRCLE BE DROWN IN ANGLES WHERE ACTUALLY THE SENSOR IS NOT
		 * (Consider that if localAngle is 360° it will pass to be a 0° in order to respect a full circle from 0° to 360°)*/
		diff = Math.abs(localAngle - GameHandler.angle_sensor);
		//Draws the circle between a range of 0° to 360° degrees
		if(diff < MaxIncrement){
			GameHandler.angle_sensor = localAngle;
		}
		//Allows the circle to be drown from 10° to 350° passing through 0°
		if(GameHandler.angle_sensor < MaxIncrement && localAngle >= (360-MaxIncrement)){
			GameHandler.angle_sensor = localAngle;
		}
		//Allows the circle to be drown from 350° to 10° passing through 0°
		if(GameHandler.angle_sensor > (360-MaxIncrement) && (localAngle-360+GameHandler.angle_sensor) <= MaxIncrement){
			GameHandler.angle_sensor = localAngle;
		}
		/**-------------------------------------------------------------------------------------------------------------------*/
	}
}
