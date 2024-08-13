package ca.crit.treasurehunter;

import static java.lang.Thread.sleep;
import static ca.crit.treasurehunter.GameHandler.WORLD_HEIGHT;
import static ca.crit.treasurehunter.GameHandler.WORLD_WIDTH;
import static ca.crit.treasurehunter.GameHandler.viewportHeight;
import static ca.crit.treasurehunter.GameHandler.viewportWidth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

import ca.crit.treasurehunter.Resources.ImageButton;

public class MainMenu implements Screen {
    private enum MenuState{
        initialState,
        configurationState,
        anglesSate,
        lapsState,
        calibrationState
    }

    private final String TAG = "MainMenu";
    public static float x = 95, y = 35;     //Coordinates for example of circle angles in Angles Game Mode
    public static float maxSpeed = 100;
    public MenuState menuState;
    private final Camera camera;
    private final Viewport viewport, uiViewport;
    private final SpriteBatch batch;
    private final Background background;
    private final Stage initStage, configStage, anglesStage, lapsStage, calibrationStage;
    private final Skin skin, skinGlassy;
    private final String skinPath = "Menu/UISkin/uiskin.json";
    private final String skinGlassyPath = "Menu/GlassyUI/assets/glassy-ui.json";
    private final GameText tittleText, cardText, gameModeText, beginningAngleText, endAngleText, speedText, rotationText, calibrationText;
    private final CircleBar circleBarAnglesMode, circleBarSpeedAnglesMode, circleBarSpeedLapsMode, circleBarCalibrationMode;
    private final Texture circleArrowsLeftTexture, circleArrowsRightTexture, circleGreen, circleYellow;
    private TextButton btnCalibrate;

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
        calibrationText = new GameText("Calibracion", Gdx.files.internal("Fonts/treasurehunter.fnt"), Gdx.files.internal("Fonts/treasurehunter.png"), false);
        calibrationText.setXY(47,65);
        calibrationText.setScaleXY(0.17f,0.17f);

        /*STAGES*/
        initStage = new Stage(uiViewport);
        configStage = new Stage(uiViewport);
        lapsStage = new Stage(uiViewport);
        anglesStage = new Stage(uiViewport);
        calibrationStage = new Stage(uiViewport);

        /*THE FIRST STAGE TO SEE*/
        menuState = MenuState.initialState;

        /*ANGLES STAGE MENU*/
        circleBarAnglesMode = new CircleBar(x, y, 2);
        circleBarSpeedAnglesMode = new CircleBar(5, 40, 1);
        circleBarCalibrationMode = new CircleBar(x-63, 0);
        circleArrowsLeftTexture = new Texture("Objects/circle_arrows-Left.png");
        circleArrowsRightTexture = new Texture("Objects/circle_arrows-Right.png");
        circleGreen = new Texture("Objects/circle_user.png");
        circleYellow = new Texture("Objects/circle_computer.png");

        /*LAPS STAGE MENU*/
        circleBarSpeedLapsMode = new CircleBar(53, 1, 1);
    }
    @Override
    public void show() {
        InitialMenu_construct();
        ConfigurationMenu_construct();
        AnglesMenu_construct();
        LapsMenu_construct();
        CalibrationMenu_construct();
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
                batch.draw(circleGreen, 35, 45, 30, 30);    //static image
                beginningAngleText.draw(batch);
                batch.draw(circleYellow, 35, 30, 30, 30);   //static image
                endAngleText.draw(batch);
                speedText.setXY(5, 65);
                speedText.draw(batch);
                circleBarAnglesMode.batch_sprite_rotation(x, y, batch, GameHandler.beginningAngle_MainMenu, GameHandler.endAngle_MainMenu); //Position in map of both circles
                circleBarSpeedAnglesMode.render_speedRotation(batch, delta, 5, 40, GameHandler.speed_MainMenu); //Animation to exemplify the computer circle speed
                if(GameHandler.beginningAngle_MainMenu > GameHandler.endAngle_MainMenu){
                    batch.draw(circleArrowsRightTexture, x+8, y+7, 16, 16);
                }else {
                    batch.draw(circleArrowsLeftTexture, x+8, y+7, 16, 16);
                }

                break;
            case lapsState:
                speedText.setXY(48,50);
                speedText.draw(batch);
                rotationText.draw(batch);
                circleBarSpeedLapsMode.render_speedRotation(batch, delta, 53, 1, GameHandler.speed_MainMenu);
                break;
            case calibrationState:
                calibrationText.draw(batch);
                circleBarCalibrationMode.render_calibrationMenu(x-63, 0, batch, delta);
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
            case calibrationState:
                Gdx.input.setInputProcessor(calibrationStage);
                calibrationStage.draw();
                calibrationStage.act(delta);
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
        batch.dispose();
        background.dispose();
        initStage.dispose();
        configStage.dispose();
        anglesStage.dispose();
        lapsStage.dispose();
        calibrationStage.dispose();
        skin.dispose();
        skinGlassy.dispose();
        tittleText.dispose();
        cardText.dispose();
        gameModeText.dispose();
        beginningAngleText.dispose();
        endAngleText.dispose();
        speedText.dispose();
        rotationText.dispose();
        calibrationText.dispose();
    }

    /**------------------------------------------------------------
     *                     MENU CONSTRUCTORS
     *----------------------------------------------------------- */

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

        /*TEXTFIELD - NO.CARNET*/
        TextField txtCard = new TextField(" ", skin);
        txtCard.setPosition(viewportWidth/3 +45,viewportHeight/2 + 120);
        configStage.addActor(txtCard);

        /*CHECKBOX 1 - ANGLES MODE*/
        CheckBox cbAnglesMode = new CheckBox(" Angulos", skin);
        cbAnglesMode.setPosition(viewportWidth/3 + 50, viewportHeight/2 + 20);
        configStage.addActor(cbAnglesMode);

        /*CHECKBOX 2 - LAPS MODE*/
        CheckBox cbLapsMode = new CheckBox(" Giros", skin);
        cbLapsMode.setPosition(viewportWidth/3 + 50, viewportHeight/2 - 10);
        configStage.addActor(cbLapsMode);

        /*IMAGEBUTTON - NEXT*/
        ImageButton btnNext = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/right_up.png", "Menu/ImageButtons/right_down.png");
        btnNext.setPosition((viewportWidth/7)*5, viewportHeight/9);
        btnNext.setSize(100,100);
        configStage.addActor(btnNext);

        /* CREATE HIDE KEYBOARD BUTTON */
        createHideKeyboardBtn(configStage);

        /*LISTENERS*/
        txtCard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameHandler.card_MainMenu = txtCard.getText().trim();
            }
        });
        cbAnglesMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                if(cbAnglesMode.isChecked()){
                    cbLapsMode.setChecked(false);
                }
                GameHandler.gameMode_MainMenu = "angles";
                lbError.setText("");
            }
        });
        cbLapsMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                if(cbLapsMode.isChecked()){
                    cbAnglesMode.setChecked(false);
                }
                GameHandler.gameMode_MainMenu = "laps";
                lbError.setText("");
            }
        });
        btnNext.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(Objects.equals(GameHandler.gameMode_MainMenu, "angles") && GameHandler.card_MainMenu != null){
                    menuState = MenuState.anglesSate;
                }
                if(Objects.equals(GameHandler.gameMode_MainMenu, "laps") && GameHandler.card_MainMenu != null){
                    menuState = MenuState.lapsState;
                }
                /*ERRORS DETECTION*/
                if((!cbAnglesMode.isChecked() && !cbLapsMode.isChecked()) || GameHandler.card_MainMenu == null ){
                    menuState = MenuState.configurationState;
                    if(GameHandler.card_MainMenu == null){
                        lbError.setText("Por favor ingrese un numero de carnet");
                        lbError.setPosition(viewportWidth/5, viewportHeight/5);
                    }else {
                        lbError.setText("Selecciona un modo de juego");
                        lbError.setPosition(viewportWidth/3 - 10, viewportHeight/5);
                    }
                    configStage.addActor(lbError);
                }
            }
        });
    }
    private void AnglesMenu_construct(){
        /*TEXTFIELD - BEGINNING ANGLE*/
        TextField txtBeginningAngle = new TextField("20", skin);
        txtBeginningAngle.setPosition((viewportWidth/5)*2,((viewportHeight/4)*3)-5);
        anglesStage.addActor(txtBeginningAngle);
        GameHandler.beginningAngle_MainMenu = Integer.valueOf(txtBeginningAngle.getText().trim());

        /*TEXTFIELD - END ANGLE*/
        TextField txtEndAngle = new TextField("180", skin);
        txtEndAngle.setPosition((viewportWidth/5)*2,(viewportHeight/4)*2);
        anglesStage.addActor(txtEndAngle);
        GameHandler.endAngle_MainMenu = Integer.valueOf(txtEndAngle.getText().trim());

        /*TEXTFIELD - SPEED*/
        TextField txtSpeed = new TextField("25", skin);
        txtSpeed.setPosition(50, viewportHeight/2);
        txtSpeed.setSize(85, 30);
        anglesStage.addActor(txtSpeed);
        GameHandler.speed_MainMenu = Integer.valueOf(txtSpeed.getText().trim());

        /*IMAGEBUTTON - ACCEPT*/
        ImageButton btnAccept = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/accept_up.png", "Menu/ImageButtons/accept_down.png");
        btnAccept.setPosition((viewportWidth/7)*5, viewportHeight/15);
        btnAccept.setSize(100,100);
        anglesStage.addActor(btnAccept);

        /*IMAGEBUTTON - GO BACK*/
        ImageButton btnReturn = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/left_up.png", "Menu/ImageButtons/left_down.png");
        btnReturn.setPosition((viewportWidth/7)*1, viewportHeight/15);
        btnReturn.setSize(100,100);
        anglesStage.addActor(btnReturn);

        /*LABEL - ERRORS*/
        Label lbError = new Label("", skin);

        /* CREATE HIDE KEYBOARD BUTTON */
        createHideKeyboardBtn(anglesStage);

        /*LISTENERS*/
        //Listener for the numeric pad
        txtEndAngle.addListener(new FocusListener() {
            @Override
            public boolean handle(Event event) {
                Gdx.input.setOnscreenKeyboardVisible(true, Input.OnscreenKeyboardType.NumberPad);
                return super.handle(event);
            }
        });
        txtEndAngle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String strInitAngle = txtBeginningAngle.getText().trim();
                String strEndAngle = txtEndAngle.getText().trim();
                String strSpeed = txtSpeed.getText().trim();
                int Error = ErrorNumberDetection(strInitAngle, strEndAngle, strSpeed);
                if(strEndAngle.isEmpty() || Error == 1){    // To not crash the game if the text field is empty or theres something different from a number
                    GameHandler.endAngle_MainMenu = 0;
                }else {
                    GameHandler.endAngle_MainMenu = Integer.valueOf(txtEndAngle.getText().trim());
                }
            }
        });
        //Listener for the numeric pad
        txtBeginningAngle.addListener(new FocusListener() {
            @Override
            public boolean handle(Event event) {
                Gdx.input.setOnscreenKeyboardVisible(true, Input.OnscreenKeyboardType.NumberPad);
                return super.handle(event);
            }
        });
        txtBeginningAngle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String strInitAngle = txtBeginningAngle.getText().trim();
                String strEndAngle = txtEndAngle.getText().trim();
                String strSpeed = txtSpeed.getText().trim();
                int Error = ErrorNumberDetection(strInitAngle, strEndAngle, strSpeed);
                if(strInitAngle.isEmpty() || Error == 1){   // To not crash the game if the text field is empty or theres something different from a number
                    GameHandler.beginningAngle_MainMenu = 0;
                }else {
                    GameHandler.beginningAngle_MainMenu = Integer.valueOf(txtBeginningAngle.getText().trim());
                }
            }
        });
        txtSpeed.addListener(new FocusListener() {
            @Override
            public boolean handle(Event event) {
                Gdx.input.setOnscreenKeyboardVisible(true, Input.OnscreenKeyboardType.NumberPad);
                return super.handle(event);
            }
        });
        txtSpeed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String strInitAngle = txtBeginningAngle.getText().trim();
                String strEndAngle = txtEndAngle.getText().trim();
                String strSpeed = txtSpeed.getText().trim();
                int Error = ErrorNumberDetection(strInitAngle, strEndAngle, strSpeed);
                if(strSpeed.isEmpty() || Error == 1){   // To not crash the game if the text field is empty or theres something different from a number
                    GameHandler.speed_MainMenu = 0;
                } else if (Error == 4) {
                    GameHandler.speed_MainMenu = 0;
                } else {
                    GameHandler.speed_MainMenu = Integer.valueOf(txtSpeed.getText().trim());
                    System.out.println("speed: "+GameHandler.speed_MainMenu);
                }
            }
        });
        btnAccept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                String strInitAngle = txtBeginningAngle.getText().trim();
                String strEndAngle = txtEndAngle.getText().trim();
                String strSpeed = txtSpeed.getText().trim();
                /*ERRORS DETECTION*/
                boolean isEmpty = strInitAngle.isEmpty() || strEndAngle.isEmpty() || strSpeed.isEmpty();
                int Error = ErrorNumberDetection(strInitAngle, strEndAngle, strSpeed);
                /*THERE ARE NO ERRORS - EVERYTHING WAS OK*/
                if(!isEmpty && Error == 0) {
                    //GameHandler.screen = "game";
                    menuState = MenuState.calibrationState;
                }
                /*POSSIBLE COMMITTED ERRORS*/
                else {
                    if(isEmpty){
                        lbError.setText("Completa todos los espacios disponibles");
                        lbError.setPosition(viewportWidth/3 - 25, viewportHeight/3 - 10);
                    }
                    else if(Error == 1){
                        lbError.setText("Favor de ingresar unicamente numeros");
                        lbError.setPosition(viewportWidth/3 - 25, viewportHeight/3 - 10);
                    }
                    else if (Error == 2) {
                        lbError.setText("Ingresa numeros entre 0 y 360 para los angulos");
                        lbError.setPosition(viewportWidth/3-30, viewportHeight/3 - 10);
                    }
                    else if (Error == 3) {
                        lbError.setText("La distancia a recorrer es muy poca, aumenta el valor de los angulos");
                        lbError.setPosition(viewportWidth/6, viewportHeight/3 - 10);
                    }
                    else if (Error == 4) {
                        lbError.setText("Disminuye el valor de la velocidad");
                        lbError.setPosition(viewportWidth/3 -25, viewportHeight/3 - 10);
                    }
                    anglesStage.addActor(lbError);
                    menuState = MenuState.anglesSate;
                }
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
        cbLeft.setChecked(true);
        GameHandler.rotationMode_MainMenu = "izquierda";

        Table tableCheckboxes = new Table();
        tableCheckboxes.setFillParent(true);
        tableCheckboxes.setPosition(0,(viewportHeight/4)+20);
        tableCheckboxes.add(cbLeft);
        tableCheckboxes.add(cbRight);
        lapsStage.addActor(tableCheckboxes);

        /*TEXTFIELD - SPEED*/
        TextField txtSpeed = new TextField("25", skin);
        txtSpeed.setPosition(viewportWidth/3 + 65, viewportHeight/2);
        txtSpeed.setSize(85, 30);
        lapsStage.addActor(txtSpeed);
        GameHandler.speed_MainMenu = Integer.valueOf(txtSpeed.getText().trim());

        /*IMAGEBUTTON - ACCEPT*/
        ImageButton btnAccept = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/accept_up.png", "Menu/ImageButtons/accept_down.png");
        btnAccept.setPosition((viewportWidth/7)*5, viewportHeight/15);
        btnAccept.setSize(100,100);
        lapsStage.addActor(btnAccept);

        /*IMAGEBUTTON - GO BACK*/
        ImageButton btnReturn = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/left_up.png", "Menu/ImageButtons/left_down.png");
        btnReturn.setPosition((viewportWidth/7)*1, viewportHeight/15);
        btnReturn.setSize(100,100);
        lapsStage.addActor(btnReturn);

        /*LABEL - ERRORS DETECTION*/
        Label lbError = new Label("Selecciona un sentido de giro por favor", skin);
        lbError.setPosition(viewportWidth/3 - 20, viewportHeight/4 + 15);

        /* CREATE HIDE KEYBOARD BUTTON */
        createHideKeyboardBtn(lapsStage);

        /*LISTENERS*/
        btnAccept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                boolean notSelected = !cbLeft.isChecked() && !cbRight.isChecked();
                String strSpeed = txtSpeed.getText().trim();
                int Error = ErrorNumberDetection(strSpeed);
                boolean noErrors = (Error == 0) && (!notSelected);

                /*THERE ARE NO ERRORS - EVERYTHING IS OK*/
                if(noErrors){
                    //GameHandler.screen = "game";
                    menuState = MenuState.calibrationState;
                }
                /*POSSIBLE COMMITED ERRORS*/
                else {
                    if (strSpeed.isEmpty() || Error == 1 || Error == 4) {
                        GameHandler.speed_MainMenu = 0;
                        lbError.setText("Ingresa una velocidad menor a " + (int)maxSpeed);
                    } else{
                        lbError.setText("Selecciona un sentido de giro por favor");
                    }
                    menuState = MenuState.lapsState;
                    lapsStage.addActor(lbError);
                }
            }
        });
        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                menuState = MenuState.configurationState;
                lbError.setText("");
                lapsStage.addActor(lbError);
            }
        });

        cbLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                if(cbLeft.isChecked()){
                    cbRight.setChecked(false);
                }
                GameHandler.rotationMode_MainMenu = "izquierda";
            }
        });
        cbRight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                if(cbRight.isChecked()){
                    cbLeft.setChecked(false);
                }
                GameHandler.rotationMode_MainMenu = "derecha";
            }
        });
        txtSpeed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String strSpeed = txtSpeed.getText().trim();
                int Error = ErrorNumberDetection(strSpeed);
                if(strSpeed.isEmpty() || Error == 1){   // To not crash the game if the text field is empty or theres something different from a number
                    GameHandler.speed_MainMenu = 0;
                } else if (Error == 4) {
                    GameHandler.speed_MainMenu = 0;
                } else {
                    GameHandler.speed_MainMenu = Integer.valueOf(txtSpeed.getText().trim());
                    System.out.println("speed: "+GameHandler.speed_MainMenu);
                }
            }
        });
    }
    private void CalibrationMenu_construct(){
        /*IMAGEBUTTON - GO BACK*/
        ImageButton btnReturn = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/left_up.png", "Menu/ImageButtons/left_down.png");
        btnReturn.setPosition(viewportWidth/7 - 48, viewportHeight/15);
        btnReturn.setSize(100,100);
        calibrationStage.addActor(btnReturn);

        /*IMAGEBUTTON - ACCEPT*/
        ImageButton btnAccept = new ImageButton(skinGlassyPath,
                "Menu/ImageButtons/accept_up.png", "Menu/ImageButtons/accept_down.png");
        btnAccept.setPosition(viewportWidth/2 + 190, viewportHeight/15);
        btnAccept.setSize(100,100);
        btnAccept.setVisible(true);
        calibrationStage.addActor(btnAccept);

        /*BUTTON - CALIBRATE*/
        btnCalibrate = new TextButton("Calibrar", skin);
        btnCalibrate.setPosition(viewportWidth/3 + 80, viewportWidth/5 + 20);
        btnCalibrate.setWidth(80);
        btnCalibrate.setHeight(45);
        //btnCalibrate.setColor(Color.RED);
        calibrationStage.addActor(btnCalibrate);

        /*LABEL - READY*/
        Label lbReady = new Label(" ", skin);
        lbReady.setPosition(viewportWidth - 210, viewportWidth/2 - 200);
        calibrationStage.addActor(lbReady);

        /*LABEL - INSTRUCTIONS*/
        Label lbInstructions = new Label(
                "Paso 1. Manten el modulo a la altura del\n" +
                    "circulo amarillo que tienes en pantalla.\n"+
                    "Puedes apoyarte de la marca visible en la pared." + "\n\n"
                +"Paso 2. Presiona el botón de Calibrar y \n" +
                        "espera el mensaje de confirmacion." + "\n\n"
                +"Paso 3. Presiona el boton de palomita.", skin);
        lbInstructions.setPosition(20, viewportWidth/2 - 150);
        calibrationStage.addActor(lbInstructions);

        /*LABEL - INSTRUCTIONS 2*/
        Label lbInstructions2 = new Label(
                "NOTA:\n" +
                        "Para comprobar una calibracion\n"+
                        "exitosa, verifica que el circulo\n" +
                        "verde se mueva correctamente" ,skin);
        lbInstructions2.setPosition(viewportWidth/2 + 90, viewportWidth/2 - 70);
        calibrationStage.addActor(lbInstructions2);

        /*LISTENERS*/
        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(GameHandler.gameMode_MainMenu.equals("laps")){
                    menuState = MenuState.lapsState;
                } else if (GameHandler.gameMode_MainMenu.equals("angles")){
                    menuState = MenuState.anglesSate;
                }
                lbReady.setText(" ");
                GameHandler.isCalibrated = false;
            }
        });

        btnCalibrate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final int POLL_TIMEOUT = 5000;

                if(GameHandler.environment == GameHandler.DESKTOP_ENV) {
                    GameHandler.isCalibrated = true;
                    lbReady.setText("LISTO, SENSOR CALIBRADO");
                    System.out.println("BLE Calibration command simulated");
                }
                else if (GameHandler.environment == GameHandler.MOBILE_ENV) {
                    //Requesting the calibration
                    GameHandler.sensorCalibrationRequest = true;
                    lbReady.setText("SENSOR CALIBRANDO...\n NO MUEVAS EL TIMON");
                    //Waiting for the calibration to be finished
                    long time = System.currentTimeMillis();
                    while(!GameHandler.sensorFinishedCalibration) {
                        //If the characteristic failed to write or timeout
                        if(GameHandler.failedToWriteCharacteristic ||
                           System.currentTimeMillis() - time > POLL_TIMEOUT) {
                            lbReady.setText("ERROR AL CALIBRAR");
                            return;
                        }
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    //Sensor has finished the calibration
                    lbReady.setText("LISTO, SENSOR CALIBRADO");
                }
            }
        });

        btnAccept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GameHandler.screen = "game";
                lbReady.setText(" ");
            }
        });


    }
    /*CLASS METHODS*/
    /**
     * @brief This method creates a text button that will appear on the left corner of the screen
     *        it's intended for those UI menus that needs to hide the keyboard to continue
     * @param stage Stage that will have the button
     */
    private void createHideKeyboardBtn(Stage stage) {
        TextButton btnHideKeyboard = new TextButton("Oculta Teclado", skin);
        btnHideKeyboard.setPosition(WORLD_WIDTH+468, WORLD_HEIGHT+370);
        btnHideKeyboard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Gdx.input.setOnscreenKeyboardVisible(false);
                Gdx.input.setOnscreenKeyboardVisible(false, Input.OnscreenKeyboardType.Default);
                //Hiding the numeric pad keyboards
                Gdx.input.setOnscreenKeyboardVisible(false, Input.OnscreenKeyboardType.NumberPad);
            }
        });
        stage.addActor(btnHideKeyboard);
    }
    /*TYPE OF ERRORS*/
    private int ErrorNumberDetection(String strInitAngle, String strEndAngle, String strspeed){
        int initAngle = 0, endAngle = 0, speed = 0;
        try{
            initAngle = Integer.parseInt(strInitAngle);
            endAngle = Integer.parseInt(strEndAngle);
            speed = Integer.parseInt(strspeed);
            if((initAngle < 0 || endAngle < 0) || (initAngle > 360 || endAngle > 360)){
                return 2;
            }
            if((Math.abs(initAngle - endAngle) < 90)|| Math.abs(endAngle - initAngle) < 90){
                return 3;
            }
            if(speed > maxSpeed){
                return 4;
            }
        } catch (NumberFormatException exception){
            return 1;
        }
        return 0; // 0 means that there are no errors
    }
    private int ErrorNumberDetection(String strspeed){
        int speed = 0;
        try {
            speed = Integer.parseInt(strspeed);
            if(speed > maxSpeed){
                return 4;
            }
        }catch (NumberFormatException exception){
            return 1;
        }
        return 0;
    }

    /*GETTERS AND SETTERS*/

}
