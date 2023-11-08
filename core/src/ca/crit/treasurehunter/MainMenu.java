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
    private Skin skin;
    private GameText tittleText, cardText, gameModeText, beginningAngleText, endAngleText;
    public MainMenu() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameHandler.WORLD_WIDTH, GameHandler.WORLD_HEIGHT, camera);
        uiViewport = new StretchViewport(viewportWidth, viewportHeight, new OrthographicCamera());
        batch = new SpriteBatch();
        background = new Background();
        skin = new Skin(Gdx.files.internal("Menu/UISkin/uiskin.json"));
        //skin2 = new Skin(Gdx.files.internal("Menu/GlassyUI/assets/glassy-ui.json"));

        /*GAMETEXT*/
        tittleText = new GameText("TREASURE HUNTER", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        tittleText.setXY(34,67);
        tittleText.setScaleXY(0.18f,0.18f);
        cardText = new GameText("No.Carnet", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        cardText.setXY(49,65);
        cardText.setScaleXY(0.18f,0.18f);
        gameModeText = new GameText("Modo de juego", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        gameModeText.setXY(45,50);
        gameModeText.setScaleXY(0.17f,0.17f);
        beginningAngleText = new GameText("Ángulo de Inicio", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        beginningAngleText.setXY(45,50);
        beginningAngleText.setScaleXY(0.17f,0.17f);
        endAngleText = new GameText("Ángulo de Fin", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        endAngleText.setXY(45,50);
        endAngleText.setScaleXY(0.17f,0.17f);//todo

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
        final Skin skin2 = new Skin(Gdx.files.internal("Menu/GlassyUI/assets/glassy-ui.json"));
        final ImageButton btnPlay = new ImageButton(skin2);
        btnPlay.setSize(170,150);
        btnPlay.getStyle().imageUp =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/play_up.png"))));
        btnPlay.getStyle().imageDown =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/play_down.png"))));
        btnPlay.setPosition(270, 140);

        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuState = MenuState.configurationState;
            }
        });
        initStage.addActor(btnPlay);
    }
    private void ConfigurationMenu_construct(){
        final Skin skin2 = new Skin(Gdx.files.internal("Menu/GlassyUI/assets/glassy-ui.json"));
        /*LABEL ERROR*/
        Label lbError = new Label("Selecciona un modo de juego", skin2);
        lbError.setPosition(viewportWidth/3 - 10, viewportHeight/5);

        /*TEXTFIELD - NO.CARNET*/
        TextField txtCard = new TextField("", skin);
        txtCard.setPosition(viewportWidth/3 +45,viewportHeight/2 + 120);
        configStage.addActor(txtCard);
        /*CHECKBOX 1 - ANGLES MODE*/
        CheckBox cbAnglesMode = new CheckBox(" Angulos", skin);
        cbAnglesMode.setPosition(viewportWidth/3 + 50, viewportHeight/2 + 20);
        configStage.addActor(cbAnglesMode);

        /*CHECKBOX 2 - LAPS MODE*/
        CheckBox cbLapsMode = new CheckBox("Vueltas Completas", skin);
        cbLapsMode.setPosition(viewportWidth/3 + 50, viewportHeight/2 - 10);
        configStage.addActor(cbLapsMode);

        /*IMAGEBUTTON - NEXT*/
        ImageButton btnNext = new ImageButton(skin2);
        btnNext.setSize(100,100);
        btnNext.setPosition((viewportWidth/7)*5, viewportHeight/9);
        btnNext.getStyle().imageUp =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/right_up.png"))));
        btnNext.getStyle().imageDown =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/right_down.png"))));
        configStage.addActor(btnNext);

        /*LISTENERS*/
        cbAnglesMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cbLapsMode.setChecked(false);
                GameHandler.gameMode_MainMenu = "angles";
                lbError.setText("");
            }
        });
        cbLapsMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cbAnglesMode.setChecked(false);
                GameHandler.gameMode_MainMenu = "laps";
                lbError.setText("");
            }
        });

        btnNext.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(GameHandler.gameMode_MainMenu == "angles"){
                    menuState = MenuState.anglesSate;
                }
                if(GameHandler.gameMode_MainMenu == "laps"){
                    menuState = MenuState.lapsState;
                }
                if(cbAnglesMode.isChecked() == false && cbLapsMode.isChecked() == false){
                    menuState = MenuState.configurationState;
                    lbError.setText("Selecciona un modo de juego");
                    configStage.addActor(lbError);
                }
            }
        });
    }
    private void AnglesMenu_construct(){
        /*TEXTFIELD - BEGINNING ANGLE*/
        TextField txtBeginningAngle = new TextField("", skin);
        txtBeginningAngle.setPosition((viewportWidth/4)*3,(viewportHeight/4)*3);
        anglesStage.addActor(txtBeginningAngle);

        /*TEXTFIELD - END ANGLE*/
        TextField txtEndAngle = new TextField("", skin);
        txtEndAngle.setPosition((viewportWidth/4)*3,(viewportHeight/5)*3);
        anglesStage.addActor(txtEndAngle);

       /*LIST - SPEED MODES*/
        List lstSpeed = new List<>(skin);
        lstSpeed.setItems("Facil", "Normal", "Dificil");
        lstSpeed.setPosition((viewportWidth/4)*3, (viewportHeight/6)*3);

        /*IMAGEBUTTON - START*/
        final Skin skin2 = new Skin(Gdx.files.internal("Menu/GlassyUI/assets/glassy-ui.json"));
        ImageButton btnStart = new ImageButton(skin2);
        btnStart.setSize(100,100);
        btnStart.setPosition((viewportWidth/7)*5, viewportHeight/9);
        btnStart.getStyle().imageUp =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/accept_up.png"))));
        btnStart.getStyle().imageDown =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/accept_down.png"))));
        anglesStage.addActor(btnStart);

        /*IMAGEBUTTON - GO BACK*/
        final Skin skin3 = new Skin(Gdx.files.internal("Menu/GlassyUI/assets/glassy-ui.json"));
        ImageButton btnReturn = new ImageButton(skin3);
        btnReturn.setSize(100,100);
        btnReturn.setPosition((viewportWidth/7)*1, viewportHeight/9);
        btnReturn.getStyle().imageUp =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/left_up.png"))));
        btnReturn.getStyle().imageDown =
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal("Menu/ImageButtons/left_down.png"))));
        anglesStage.addActor(btnReturn);

        /*LISTENERS*/
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
