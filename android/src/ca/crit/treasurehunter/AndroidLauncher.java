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
	private float pastAngle = 0;
	private boolean firstEvent = true;
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
		GameHandler.init(GameHandler.MOBILE_ENV);
	}

	public void onCharacteristicNotificationListener(byte[] value) {
		final float MaxIncrement = 50; //TODO Fine adjust with kids
		float localAngle = 0;
		float diff;

		strValue = RojoBLE.getString(value);

		Log.i(TAG, "Received: " + strValue);
		try {
			localAngle = Float.parseFloat(strValue);
			if(firstEvent) {
				pastAngle = localAngle;
				firstEvent = false;
			}
		}
		catch (NumberFormatException ex) {
			Log.i(TAG, "Dato que llego no es un numero");
		}
		GameHandler.angle_sensor = localAngle;

		diff = localAngle - pastAngle;
		if(localAngle >= 309.99)
			diff = 360 - diff;
		if(diff < MaxIncrement) {
			GameHandler.angle_sensor = localAngle;
			pastAngle = localAngle;
		}
	}
}
