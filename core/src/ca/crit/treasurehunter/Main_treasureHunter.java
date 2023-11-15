package ca.crit.treasurehunter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Objects;

public class Main_treasureHunter extends Game {
	GameScreen gameScreen;
	MainMenu menuScreen;
	ResumeScreen resumeScreen;
	@Override
	public void create () {
		gameScreen = new GameScreen();
		menuScreen = new MainMenu();
		resumeScreen = new ResumeScreen();
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
			setScreen(resumeScreen);
			GameHandler.screen = "";
		}
	}
	
	@Override
	public void dispose () {
		menuScreen.dispose();
		gameScreen.dispose();
		resumeScreen.dispose();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}
}
