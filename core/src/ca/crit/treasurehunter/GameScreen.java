package ca.crit.treasurehunter;

import static ca.crit.treasurehunter.GameHandler.WORLD_HEIGHT;
import static ca.crit.treasurehunter.GameHandler.WORLD_WIDTH;
import static ca.crit.treasurehunter.GameHandler.collided;
import static ca.crit.treasurehunter.GameHandler.counter;
import static ca.crit.treasurehunter.GameHandler.onomatopoeiaAppear;
import static ca.crit.treasurehunter.GameHandler.playedTime_sec;
import static ca.crit.treasurehunter.GameHandler.playedTime_min;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    /*SCREEN*/
    private final Camera camera;
    private final Viewport viewport;

    /*GRAPHICS*/
    private final SpriteBatch batch;
    private final Background background;

    /*CHARACTER*/
    private final Ship ship;

    /*OBJECTS*/
    private final Treasures treasures;
    private final CircleBar circleBarAngles;
    private final CircleBar circleBarLaps;

    /*TEXT*/
    private TextScreen textScreen;

    public static boolean flag;
    public static String gameMode;

    GameScreen(){
        /*SCREEN*/
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        /*GRAPHICS*/
        batch = new SpriteBatch();
        background = new Background();
        /*CHARACTERS*/
        ship = new Ship(130, 70, 100, 40);
        /*OBJECTS*/
        treasures = new Treasures(580, 35, 35);
        circleBarAngles = new CircleBar( 40, 55, 15,50, 270, 90);
        circleBarLaps = new CircleBar(40, 55, 15,50, 200, "izquierda");
        /*TEXT*/
        textScreen = new TextScreen();
        /*OTHERS*/
        flag = true;
        gameMode = "angles";  // To choose the game mode: "angles" or "laps"

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        playedTime_sec += delta;     // How many time has passed since the game started
        if(playedTime_sec >= 60){
            playedTime_min ++;
            playedTime_sec = 0;
        }

        /*COUNT THE TREASURES COLLECTED*/
        if(Intersector.overlaps(treasures.rectangle, ship.rectangle) && flag){
            counter ++;
            flag = false;
        }
        /* TO KNOW IF SHIP-TREASURE COLLIDED*/
        if(Intersector.overlaps(treasures.rectangle, ship.rectangle)){
            collided = true;
            onomatopoeiaAppear = true;
        }else {
            collided = false;
        }

        batch.begin();
        background.render(delta, batch);
        treasures.render(delta, batch);
        ship.render(batch);
        textScreen.render(batch);
        if(gameMode.equals("angles")){
            circleBarAngles.render_AnglesGame(delta, batch);
        } else if (gameMode.equals("laps")) {
            circleBarLaps.render_LapsGame(delta, batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
       /*SABER POSICIÓN DE CURSOR CON CLICK EN EL MAPA
        if(Gdx.input.isTouched()){
            System.out.println("x: " + Gdx.input.getX());
            System.out.println("y: " + (-1)*(Gdx.input.getY() - 479));
        }*/