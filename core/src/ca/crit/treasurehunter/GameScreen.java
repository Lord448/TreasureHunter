package ca.crit.treasurehunter;

import static ca.crit.treasurehunter.GameHandler.RoundTrips;
import static ca.crit.treasurehunter.GameHandler.WORLD_HEIGHT;
import static ca.crit.treasurehunter.GameHandler.WORLD_WIDTH;
import static ca.crit.treasurehunter.GameHandler.beginningAngle_MainMenu;
import static ca.crit.treasurehunter.GameHandler.collided;
import static ca.crit.treasurehunter.GameHandler.counter;
import static ca.crit.treasurehunter.GameHandler.endAngle_MainMenu;
import static ca.crit.treasurehunter.GameHandler.gameMode_MainMenu;
import static ca.crit.treasurehunter.GameHandler.onomatopoeiaAppear;
import static ca.crit.treasurehunter.GameHandler.playedTime_sec;
import static ca.crit.treasurehunter.GameHandler.playedTime_min;
import static ca.crit.treasurehunter.GameHandler.rotationMode_MainMenu;
import static ca.crit.treasurehunter.GameHandler.screen;
import static ca.crit.treasurehunter.GameHandler.speed_MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private CircleBar circleBarAngles;
    private CircleBar circleBarLaps;

    /*TEXT*/
    private final TextScreen textScreen;

    /*GRAPHIC USAGES*/
    private TextButton btnEndGame, btnReturn;
    private Skin skin;
    private Stage stage;

    /*OTHERS*/
    public static boolean flag;

    /*CSV FILES*/
    private CSVwriter csvWriter;

    GameScreen(){
        /*SCREEN*/
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        /*GRAPHICS*/
        batch = new SpriteBatch();
        background = new Background();

        /*CHARACTERS*/
        ship = new Ship(29, 7, 10, 5);

        /*OBJECTS*/
        treasures = new Treasures(WORLD_WIDTH+70, 7, 6);

        /*TEXT*/
        textScreen = new TextScreen();

        /*OTHERS*/
        flag = true;

        /*CSV*/
        csvWriter = new CSVwriter();

        /*STAGE*/
        stage = new Stage(new StretchViewport(720,480, new OrthographicCamera()));
        skin = new Skin(Gdx.files.internal("Menu/ShadeUISkin/uiskin.json"));
    }
    @Override
    public void show() {
        circleBarAngles = new CircleBar( speed_MainMenu, 70, 15,50, beginningAngle_MainMenu, endAngle_MainMenu);
        circleBarLaps = new CircleBar(speed_MainMenu, 70, 15,50, 200, rotationMode_MainMenu);
        stage_constructor();
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
            if(gameMode_MainMenu.equals("angles")){
                circleBarAngles.render_AnglesGame(delta, batch);
            } else if (gameMode_MainMenu.equals("laps")) {
                circleBarLaps.render_LapsGame(delta, batch);
            }
        batch.end();

        renderGraphics(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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
        batch.dispose();
        treasures.dispose();
        ship.dispose();
        textScreen.dispose();
        circleBarAngles.dispose();
        circleBarLaps.dispose();
        background.dispose();
    }

    private void stage_constructor(){
        btnEndGame = new TextButton("Finalizar Sesion", skin);
        btnEndGame.setPosition(600, 440);
        stage.addActor(btnEndGame);

        btnReturn = new TextButton("Regresar", skin);
        btnReturn.setPosition(10, 440);
        stage.addActor(btnReturn);
        /*LISTENERS*/
        btnEndGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                csvWriter.writeCSV("CRIT.csv");
                openCSV();
            }
        });
        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /*RETURN TO THE MENU*/
                screen = "menu";
                /*RESET GLOBAL VARIABLES*/
                playedTime_min = 0;
                playedTime_sec = 0;
                RoundTrips = 0;
                counter = 0;
                /*RESET TREASURE CLASS VARIABLES*/
                treasures.setX(WORLD_WIDTH+70);
                treasures.setInitialX_Position(WORLD_WIDTH+70);
            }
        });
    }

    private void renderGraphics(float delta){
        Gdx.input.setInputProcessor(stage);
        stage.draw();
        stage.act(delta);
    }

    private void openCSV() {
        // Ruta relativa o absoluta al archivo CSV creado
        String filePath = csvWriter.getFilePath();
        System.out.println("Se intenta abrir: "+ csvWriter.getFilePath());

        // Crea un objeto FileHandle con la ruta del archivo
        FileHandle file = Gdx.files.internal(filePath);

        // Intenta abrir el archivo
        if (file.exists()) {
            Gdx.app.log("Apertura de CSV", "Archivo existe");
        } else {
            Gdx.app.log("Apertura de CSV", "El archivo no existe.");
        }
    }

}