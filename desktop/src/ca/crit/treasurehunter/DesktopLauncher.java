package ca.crit.treasurehunter;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ca.crit.treasurehunter.Main_treasureHunter;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(800, 480);
		config.setForegroundFPS(60);
		config.setTitle("TreasureHunter");
		GameHandler.init(GameHandler.DESKTOP_ENV);
		new Lwjgl3Application(new Main_treasureHunter(), config);
	}
}
