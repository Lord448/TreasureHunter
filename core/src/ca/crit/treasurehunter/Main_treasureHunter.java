package ca.crit.treasurehunter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Objects;

public class Main_treasureHunter extends Game {
	GameScreen gameScreen;
	MainMenu menu;
	@Override
	public void create () {
		gameScreen = new GameScreen();
		menu = new MainMenu();
	}

	@Override
	public void render () {
		super.render();
		if(Objects.equals(GameHandler.screen_MainMenu, "gameScreen")){
			setScreen(gameScreen);
			GameHandler.screen_MainMenu = "";
		}
		if(Objects.equals(GameHandler.screen_MainMenu, "menu")){
			setScreen(menu);
			GameHandler.screen_MainMenu = "";
		}
	}
	
	@Override
	public void dispose () {
		gameScreen.dispose();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}
}
