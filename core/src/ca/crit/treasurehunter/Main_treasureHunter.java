package ca.crit.treasurehunter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Objects;

public class Main_treasureHunter extends Game {
	GameScreen gameScreen;
	MainMenu menuScreen;
	BitmapFont bFDisconnected;
	SpriteBatch spriteBatch;
	@Override
	public void create () {
		gameScreen = new GameScreen();
		menuScreen = new MainMenu();
		bFDisconnected = new BitmapFont(Gdx.files.internal("Fonts/normalTextWhite.fnt"),
				Gdx.files.internal("Fonts/normalTextWhite.png"), false);
		spriteBatch = new SpriteBatch();
	}

	@Override
	public void render () {
		super.render();
		if(Objects.equals(GameHandler.screen, "game")){
			setScreen(gameScreen);
			GameHandler.screen = "";
		}
		if(Objects.equals(GameHandler.screen, "menu")){
			setScreen(menuScreen);
			GameHandler.screen = "";
		}
		if(Objects.equals(GameHandler.screen, "resume")){
			GameHandler.screen = "";
		}
		//Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)
		if(GameHandler.gattServerDisconnected){
			String string = "Error: Verifica tu conexion Bluetooth y\n" +
					"          reconecta el modulo por favor";
			bFDisconnected.getData().setScale(0.8F, 0.8F);
			bFDisconnected.setColor(Color.RED);
			GameHandler.infiniteBlink(bFDisconnected, spriteBatch, string, 170, 150);
		}
	}
	
	@Override
	public void dispose () {
		menuScreen.dispose();
		gameScreen.dispose();
		spriteBatch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}
}
