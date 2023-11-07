package ca.crit.treasurehunter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicRadioButtonUI;

public class MainMenu implements Screen {
    private enum MenuState{
        initialState,
        configurationState,
        anglesSate,
        lapsState
    }
    private MenuState menuState;
    private static final float viewportWidth = 720;
    private static final float viewportHeight = 480;
    private Camera camera;
    private Viewport viewport, uiViewport;
    private SpriteBatch batch;
    private Background background;
    private Stage initStage, configStage, anglesStage, lapsStage;
    private Skin skin, skin2;
    private GameText tittleText, cardText, gameModeText;
    public MainMenu() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameHandler.WORLD_WIDTH, GameHandler.WORLD_HEIGHT, camera);
        uiViewport = new StretchViewport(viewportWidth, viewportHeight, new OrthographicCamera());
        batch = new SpriteBatch();
        background = new Background();
        skin = new Skin(Gdx.files.internal("Menu/UISkin/uiskin.json"));
        skin2 = new Skin(Gdx.files.internal("Menu/GlassyUI/assets/glassy-ui.json"));
        /*GAMETEXT*/
        tittleText = new GameText("TREASURE HUNTER", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        tittleText.setXY(34,67);
        tittleText.setScaleXY(0.18f,0.18f);
        cardText = new GameText("No.Carnet", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        cardText.setXY(37,65);
        cardText.setScaleXY(0.18f,0.18f);
        gameModeText = new GameText("Modo de juego", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        gameModeText.setXY(37,57);
        gameModeText.setScaleXY(0.18f,0.18f);

        /*STAGES*/
        initStage = new Stage(uiViewport);
        configStage = new Stage(uiViewport);
        lapsStage = new Stage(uiViewport);
        anglesStage = new Stage(uiViewport);

        /*THE FIRST STAGE TO SEE*/
        menuState = MenuState.initialState;

    }
    @Override
    public void show() {
        InitialMenu_construct();
        ConfigurationMenu_construct();
        AnglesMenu_construct();
        LapsMenu_construct();
    }

    @Override
    public void render(float delta) {
        batch.begin();
            background.render(delta, batch);
        switch (menuState){
            case initialState:
                tittleText.draw(batch);
                break;
            case configurationState:
                cardText.draw(batch);
                gameModeText.draw(batch);
                break;
            case anglesSate:

                break;
            case lapsState:

                break;
        }
        batch.end();

        switch (menuState){
            case initialState:
                Gdx.input.setInputProcessor(initStage);
                initStage.draw();
                initStage.act(delta);
                break;
            case configurationState:
                Gdx.input.setInputProcessor(configStage);
                configStage.draw();
                configStage.act(delta);
                break;
            case anglesSate:
                Gdx.input.setInputProcessor(anglesStage);
                anglesStage.draw();
                anglesStage.act(delta);
                break;
            case lapsState:
                Gdx.input.setInputProcessor(lapsStage);
                lapsStage.draw();
                lapsStage.act(delta);
                break;
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiViewport.update(width, height, true);
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

    }

    private void InitialMenu_construct(){
        ImageButton imgPlay = new ImageButton(skin2);
        imgPlay.setSize(170,150);
        imgPlay.getStyle().imageUp =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/play_up.png"))));
        imgPlay.getStyle().imageDown =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/play_down.png"))));
        imgPlay.setPosition(270, 140);

        imgPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuState = MenuState.configurationState;
            }
        });
        initStage.addActor(imgPlay);
    }
    private void ConfigurationMenu_construct(){
        TextField txtCard = new TextField("", skin);
        txtCard.setPosition(viewportWidth/2 + 20,viewportHeight/2 + 160);

        CheckBox cbAnglesMode = new CheckBox(" Angulos", skin);
        cbAnglesMode.setPosition(viewportWidth/3 + 50, viewportHeight/2 + 70);
        configStage.addActor(cbAnglesMode);
        CheckBox cbLapsMode = new CheckBox("Vueltas Completas", skin);
        configStage.addActor(cbLapsMode);



        TextButton btnNext = new TextButton("Siguiente", skin);

        configStage.addActor(txtCard);

        cbAnglesMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cbLapsMode.setChecked(false);
                GameHandler.gameMode_MainMenu = "angles";
            }
        });
        cbLapsMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cbAnglesMode.setChecked(false);
                GameHandler.gameMode_MainMenu = "laps";
            }
        });
        btnNext.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (GameHandler.gameMode_MainMenu == "angles"){
                    menuState = MenuState.anglesSate;
                }
                if(GameHandler.gameMode_MainMenu == "laps"){
                    menuState = MenuState.lapsState;
                }
            }
        });

    }
    private void AnglesMenu_construct(){
        TextButton btnStart = new TextButton("Comenzar", skin);
        TextButton btnReturn = new TextButton("Regresar", skin);
        Label lbFrom = new Label("Angulo de inicio: ", skin);
        Label lbUntil= new Label("Angulo Final: ", skin);
        Label lbSpeed = new Label("Velocidad: ", skin);
        TextField txtBeginningAngle = new TextField("", skin);
        TextField txtEndAngle = new TextField("", skin);
        List lstSpeed = new List<>(skin);
        lstSpeed.setItems("Facil", "Normal", "Dificil");

        Table table = new Table();
        table.setFillParent(true);
        table.setPosition(0,0);

        table.add(lbFrom);
        table.add(txtBeginningAngle);
        table.row();
        table.add(lbUntil);
        table.add(txtEndAngle);
        table.row();
        table.add(lbSpeed);
        table.add(lstSpeed);
        table.row();
        table.add(btnReturn);
        table.add(btnStart);
        anglesStage.addActor(table);

        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameHandler.beginningAngle_MainMenu = Integer.valueOf(txtBeginningAngle.getText());
                GameHandler.endAngle_MainMenu = Integer.valueOf(txtEndAngle.getText());
                if(lstSpeed.getSelectedIndex() == 0){
                    GameHandler.speed_MainMenu = 30;
                }
                if(lstSpeed.getSelectedIndex() == 1){
                    GameHandler.speed_MainMenu = 45;
                }
                if(lstSpeed.getSelectedIndex() == 2){
                    GameHandler.speed_MainMenu = 60;
                }
                GameHandler.screen_MainMenu = "gameScreen";
            }
        });

        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuState = MenuState.configurationState;
            }
        });
    }
    private void LapsMenu_construct(){
        TextButton btnStart = new TextButton("comenzar", skin);
        TextButton btnReturn = new TextButton("Regresar", skin);
        Label lbRotation = new Label("Sentido del giro: ", skin);
        Label lbSpeed = new Label("Velocidad: ", skin);
        CheckBox cbLeft = new CheckBox("Izquierda", skin);
        CheckBox cbRight = new CheckBox("Derecha", skin);
        List lstSpeed = new List<>(skin);
        lstSpeed.setItems("Facil", "Normal", "Dificil");

        Table table = new Table();
        table.setFillParent(true);
        table.setPosition(0,0);

        table.add(lbRotation);
        table.row();
        table.add(cbLeft);
        table.add(cbRight);
        table.row();
        table.add(lbSpeed);
        table.add(lstSpeed);
        table.row();
        table.add(btnReturn);
        table.add(btnStart);
        lapsStage.addActor(table);

        cbLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cbRight.setChecked(false);
                GameHandler.rotationMode_MainMenu = "izquierda";
            }
        });
        cbRight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cbLeft.setChecked(false);
                GameHandler.rotationMode_MainMenu = "derecha";
            }
        });

        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameHandler.screen_MainMenu = "gameScreen";
                if(lstSpeed.getSelectedIndex() == 0){
                    GameHandler.speed_MainMenu = 30;
                }
                if(lstSpeed.getSelectedIndex() == 1){
                    GameHandler.speed_MainMenu = 45;
                }
                if(lstSpeed.getSelectedIndex() == 2){
                    GameHandler.speed_MainMenu = 60;
                }
            }
        });
        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuState = MenuState.configurationState;
            }
        });
    }
}
