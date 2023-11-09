package ca.crit.treasurehunter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ca.crit.treasurehunter.Resources.ImageButton;
import ca.crit.treasurehunter.Resources.PrintTag;

public class MainMenu implements Screen {
    private final String TAG = "MainMenu";
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
    private final Skin skin, skinGlassy;
    private final String skinPath = "Menu/UISkin/uiskin.json";
    private final String skinGlassyPath = "Menu/GlassyUI/assets/glassy-ui.json";
    private GameText tittleText, cardText, gameModeText, beginningAngleText, endAngleText, speedText, rotationText;
    public MainMenu() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameHandler.WORLD_WIDTH, GameHandler.WORLD_HEIGHT, camera);
        uiViewport = new StretchViewport(viewportWidth, viewportHeight, new OrthographicCamera());
        batch = new SpriteBatch();
        background = new Background();
        skin = new Skin(Gdx.files.internal("Menu/UISkin/uiskin.json"));
        skinGlassy = new Skin(Gdx.files.internal("Menu/GlassyUI/assets/glassy-ui.json"));

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
        beginningAngleText = new GameText("Angulo de Inicio", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        beginningAngleText.setXY(44,65);
        beginningAngleText.setScaleXY(0.17f,0.17f);
        endAngleText = new GameText("Angulo de Fin", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        endAngleText.setXY(45,50);
        endAngleText.setScaleXY(0.17f,0.17f);
        speedText = new GameText("Velocidad", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        speedText.setScaleXY(0.17f,0.17f);
        rotationText = new GameText("Sentido de Giro", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        rotationText.setXY(44,65);
        rotationText.setScaleXY(0.17f,0.17f);

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
                beginningAngleText.draw(batch);
                endAngleText.draw(batch);
                speedText.setXY(48,25);
                speedText.draw(batch);
                break;
            case lapsState:
                speedText.setXY(48,50);
                speedText.draw(batch);
                rotationText.draw(batch);
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
        ImageButton btnPlay = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/play_up.png", "Menu/ImageButtons/play_down.png");
        btnPlay.setPosition(270, 140);
        btnPlay.setSize(170,150);
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuState = MainMenu.MenuState.configurationState;
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
        ImageButton btnNext = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/right_up.png", "Menu/ImageButtons/right_down.png");
        btnNext.setPosition((viewportWidth/7)*5, viewportHeight/9);
        btnNext.setSize(100,100);
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
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
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
        txtBeginningAngle.setPosition((viewportWidth/5)*2,((viewportHeight/4)*3)-5);
        anglesStage.addActor(txtBeginningAngle);

        /*TEXTFIELD - END ANGLE*/
        TextField txtEndAngle = new TextField("", skin);
        txtEndAngle.setPosition((viewportWidth/5)*2,(viewportHeight/4)*2);
        anglesStage.addActor(txtEndAngle);

       /*LIST - SPEED MODES*/
        List lstSpeed = new List<>(skin);
        lstSpeed.setItems("Facil", "Normal", "Dificil");
        Table table = new Table();
        table.setFillParent(true);
        table.setPosition(-10,-viewportHeight/3);
        table.add(lstSpeed);
        anglesStage.addActor(table);

        /*IMAGEBUTTON - ACCEPT*/
        ImageButton btnAccept = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/accept_up.png", "Menu/ImageButtons/accept_down.png");
        btnAccept.setPosition((viewportWidth/7)*5, viewportHeight/9);
        btnAccept.setSize(100,100);
        anglesStage.addActor(btnAccept);

        /*IMAGEBUTTON - GO BACK*/
        ImageButton btnReturn = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/left_up.png", "Menu/ImageButtons/left_down.png");
        btnReturn.setPosition((viewportWidth/7)*1, viewportHeight/9);
        btnReturn.setSize(100,100);
        anglesStage.addActor(btnReturn);

        /*LISTENERS*/
        btnAccept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
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
                PrintTag.Print(TAG, String.valueOf(GameHandler.speed_MainMenu));
            }
        });
        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                menuState = MenuState.configurationState;
            }
        });
    }
    private void LapsMenu_construct(){
        /*CHECKBOX - ROTATION MODE*/
        CheckBox cbLeft = new CheckBox("Izquierda", skin);
        CheckBox cbRight = new CheckBox("Derecha", skin);
        Table tableCheckboxes = new Table();
        tableCheckboxes.setFillParent(true);
        tableCheckboxes.setPosition(0,(viewportHeight/4)+20);
        tableCheckboxes.add(cbLeft);
        tableCheckboxes.add(cbRight);
        lapsStage.addActor(tableCheckboxes);

        /*LIST - SPEED MODES*/
        List lstSpeed = new List<>(skin);
        lstSpeed.setItems("Facil", "Normal", "Dificil");
        Table tableSpeed = new Table();
        tableSpeed.setFillParent(true);
        tableSpeed.setPosition(-5,viewportHeight/60);
        tableSpeed.add(lstSpeed);
        lapsStage.addActor(tableSpeed);

        /*IMAGEBUTTON - ACCEPT*/
        ImageButton btnAccept = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/accept_up.png", "Menu/ImageButtons/accept_down.png");
        btnAccept.setPosition((viewportWidth/7)*5, viewportHeight/9);
        btnAccept.setSize(100,100);
        lapsStage.addActor(btnAccept);

        /*IMAGEBUTTON - GO BACK*/
        ImageButton btnReturn = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/left_up.png", "Menu/ImageButtons/left_down.png");
        btnReturn.setPosition((viewportWidth/7)*1, viewportHeight/9);
        btnReturn.setSize(100,100);
        lapsStage.addActor(btnReturn);

        /*LISTENERS*/
        btnAccept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
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
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                menuState = MenuState.configurationState;
            }
        });

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
    }
}
