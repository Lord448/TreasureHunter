package ca.crit.treasurehunter;

import static ca.crit.treasurehunter.GameHandler.RoundTrips;
import static ca.crit.treasurehunter.GameHandler.WORLD_HEIGHT;
import static ca.crit.treasurehunter.GameHandler.WORLD_WIDTH;
import static ca.crit.treasurehunter.GameHandler.angle_laptop;
import static ca.crit.treasurehunter.GameHandler.angle_sensor;
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
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


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

    /*TO DISPLAY INFORMATION*/
    private CSVwriter csvWriter;
    private Sampling anglesSampling, lapsSampling;

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

        /*STAGE*/
        stage = new Stage(new StretchViewport(720,480, new OrthographicCamera()));
        skin = new Skin(Gdx.files.internal("Menu/ShadeUISkin/uiskin.json"));
    }
    @Override
    public void show() {
        circleBarAngles = new CircleBar( speed_MainMenu, 70, 15,50, beginningAngle_MainMenu, endAngle_MainMenu);
        circleBarLaps = new CircleBar(speed_MainMenu, 70, 15,50, 0, rotationMode_MainMenu);
        stage_constructor();

        /*TO DISPLAY INFORMATION*/
        csvWriter = new CSVwriter();
        if(gameMode_MainMenu == "angles"){
            anglesSampling = new Sampling(beginningAngle_MainMenu, endAngle_MainMenu);
        }
        else if (gameMode_MainMenu == "laps") {
            lapsSampling = new Sampling();
        }
    }

    @Override
    public void render(float delta) {

        /**TO KNOW HOW MUCH TIME HAS PASSED SINCE THE GAME STARTED*/
        playedTime_sec += delta;
        if(playedTime_sec >= 60){
            playedTime_min ++;
            playedTime_sec = 0;
        }
        /**COUNT THE TREASURES COLLECTED*/
        if(Intersector.overlaps(treasures.rectangle, ship.rectangle) && flag){
            counter ++;
            flag = false;
        }
        /** TO KNOW IF SHIP-TREASURE COLLIDED*/
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

        if(GameHandler.environment == GameHandler.DESKTOP_ENV) {
            if(gameMode_MainMenu == "angles"){
                anglesSampling.angles_render(delta, angle_laptop);
            } else if (gameMode_MainMenu == "laps") {
                lapsSampling.laps_render(delta, angle_laptop);
            }

        } else if(GameHandler.environment == GameHandler.MOBILE_ENV) {
            if(gameMode_MainMenu == "angles"){
                anglesSampling.angles_render(delta, angle_sensor);
            } else if (gameMode_MainMenu == "laps") {
                lapsSampling.laps_render(delta, angle_sensor);
            }
        }

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

                /*Updating Data*/
                GameHandler.headerTextData = new String[][]{
                        {"Tesoros", "Vueltas", "Duración Sesión"},
                        {String.valueOf(counter), String.valueOf(RoundTrips), (int) playedTime_min + " min con " + (int) playedTime_sec + " sec"},
                        {"Ciclo", "Ángulo", "Alcanzado en"}
                };
                printMatrix(GameHandler.resumeData);

                /*Setting the date of how the csv file will be saved*/
                Calendar date = new GregorianCalendar();
                int day = date.get(Calendar.DAY_OF_MONTH);
                int month = date.get(Calendar.MONTH);
                int year = date.get(Calendar.YEAR);
                String currentDate = day + "-" + (month+1) + "-" + year;

                /*Loading Name and the updated Data to write the file*/
                csvWriter.writeCSV(GameHandler.card_MainMenu+"_"+currentDate+".csv");
            }
        });
        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /*RETURN TO THE MENU*/
                screen = "menu";
                /*RESET TEXT SCREEN DATA*/
                playedTime_min = 0;
                playedTime_sec = 0;
                RoundTrips = 0;
                counter = 0;
                /*RESET TREASURE CLASS VARIABLES*/
                treasures.setX(WORLD_WIDTH+70);
                treasures.setInitialX_Position(WORLD_WIDTH+70);
                /*RESET RESUME DATA*/
                GameHandler.resumeData.clear();
            }
        });
    }

    private void renderGraphics(float delta){
        Gdx.input.setInputProcessor(stage);
        stage.draw();
        stage.act(delta);
    }
    public static void printMatrix(ArrayList<ArrayList<String>> dynamicArray) {
        // Imprimir el contenido
        for (ArrayList<String> row : dynamicArray) {
            for (String value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}