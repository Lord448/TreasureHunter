package ca.crit.treasurehunter;
import static ca.crit.treasurehunter.GameHandler.WORLD_HEIGHT;
import static ca.crit.treasurehunter.GameHandler.WORLD_WIDTH;
import static ca.crit.treasurehunter.GameHandler.viewportHeight;
import static ca.crit.treasurehunter.GameHandler.viewportWidth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    //TODO : Fix that list doesnt erease itself when return button from gamescreen is selected
    private final String TAG = "MainMenu";
    public static float x = 95, y = 35;     //Coordenates for example of circle angles in Angles Game Mode
    private enum MenuState{
        initialState,
        configurationState,
        anglesSate,
        lapsState
    }
    public MenuState menuState;
    private final Camera camera;
    private final Viewport viewport, uiViewport;
    private final SpriteBatch batch;
    private final Background background;
    private final Stage initStage, configStage, anglesStage, lapsStage;
    private final Skin skin, skinGlassy;
    private final String skinPath = "Menu/UISkin/uiskin.json";
    private final String skinGlassyPath = "Menu/GlassyUI/assets/glassy-ui.json";
    private final GameText tittleText, cardText, gameModeText, beginningAngleText, endAngleText, speedText, rotationText;
    private final CircleBar circleBarAngles,circleBarSpeed;
    private final Texture circleArrowTexture, circleGreen, circleYellow;
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

        /*ANGLES STAGE MENU*/
        circleBarAngles = new CircleBar(x, y, 2);
        circleBarSpeed = new CircleBar(70, 0, 1);
        circleArrowTexture = new Texture("Objects/circle_arrow.png");
        circleGreen = new Texture("Objects/circle_user.png");
        circleYellow = new Texture("Objects/circle_computer.png");

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
                batch.draw(circleGreen, 35, 45, 30, 30);
                beginningAngleText.draw(batch);
                batch.draw(circleYellow, 35, 30, 30, 30);
                endAngleText.draw(batch);
                speedText.setXY(40,18);
                speedText.draw(batch);
                circleBarAngles.batch_sprite_rotation(x, y, batch, GameHandler.beginningAngle_MainMenu, GameHandler.endAngle_MainMenu);
                circleBarSpeed.render_speedRotation(batch, delta, 70, 0, GameHandler.speed_MainMenu);
                batch.draw(circleArrowTexture, x+8, y+7, 16, 16);
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
        batch.dispose();
        background.dispose();
        initStage.dispose();
        configStage.dispose();
        anglesStage.dispose();
        lapsStage.dispose();
        skin.dispose();
        skinGlassy.dispose();
        tittleText.dispose();
        cardText.dispose();
        gameModeText.dispose();
        beginningAngleText.dispose();
        endAngleText.dispose();
        speedText.dispose();
        rotationText.dispose();
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
                if(GameHandler.gameMode_MainMenu == "angles" && GameHandler.card_MainMenu != null){
                    menuState = MenuState.anglesSate;
                }
                if(GameHandler.gameMode_MainMenu == "laps" && GameHandler.card_MainMenu != null){
                    menuState = MenuState.lapsState;
                }
                /*ERRORS DETECTION*/
                if((cbAnglesMode.isChecked() == false && cbLapsMode.isChecked() == false) || GameHandler.card_MainMenu == null ){
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
        TextField txtBeginningAngle = new TextField("0", skin);
        txtBeginningAngle.setPosition((viewportWidth/5)*2,((viewportHeight/4)*3)-5);
        anglesStage.addActor(txtBeginningAngle);
        GameHandler.beginningAngle_MainMenu = Integer.valueOf(txtBeginningAngle.getText().trim());

        /*TEXTFIELD - END ANGLE*/
        TextField txtEndAngle = new TextField("90", skin);
        txtEndAngle.setPosition((viewportWidth/5)*2,(viewportHeight/4)*2);
        anglesStage.addActor(txtEndAngle);
        GameHandler.endAngle_MainMenu = Integer.valueOf(txtEndAngle.getText().trim());

        /*TEXTFIELD - SPEED*/
        TextField txtSpeed = new TextField("25", skin);
        txtSpeed.setPosition(viewportWidth/3 +20, viewportHeight/10);
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

        /*LISTENERS*/
        txtEndAngle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String strInitAngle = txtBeginningAngle.getText().trim();
                String strEndAngle = txtEndAngle.getText().trim();
                String strSpeed = txtSpeed.getText().trim();
                int Error = ErrorNumberDetection(strInitAngle, strEndAngle, strSpeed);
                if(strEndAngle.equals("") || Error == 1){    // To not crash the game if the text field is empty or theres something different from a number
                    GameHandler.endAngle_MainMenu = 0;
                }else {
                    GameHandler.endAngle_MainMenu = Integer.valueOf(txtEndAngle.getText().trim());
                }
            }
        });
        txtBeginningAngle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String strInitAngle = txtBeginningAngle.getText().trim();
                String strEndAngle = txtEndAngle.getText().trim();
                String strSpeed = txtSpeed.getText().trim();
                int Error = ErrorNumberDetection(strInitAngle, strEndAngle, strSpeed);
                if(strInitAngle.equals("") || Error == 1){   // To not crash the game if the text field is empty or theres something different from a number
                    GameHandler.beginningAngle_MainMenu = 0;
                }else {
                    GameHandler.beginningAngle_MainMenu = Integer.valueOf(txtBeginningAngle.getText().trim());
                }
            }
        });
        txtSpeed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String strInitAngle = txtBeginningAngle.getText().trim();
                String strEndAngle = txtEndAngle.getText().trim();
                String strSpeed = txtSpeed.getText().trim();
                int Error = ErrorNumberDetection(strInitAngle, strEndAngle, strSpeed);
                if(strSpeed.equals("") || Error == 1){   // To not crash the game if the text field is empty or theres something different from a number
                    GameHandler.speed_MainMenu = 0;
                }else {
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
                boolean isEmpty = strInitAngle.equals("") || strEndAngle.equals("") || strSpeed.equals("");
                int Error = ErrorNumberDetection(strInitAngle, strEndAngle, strSpeed);
                /*THERE ARE NO ERRORS*/
                if(isEmpty == false && Error == 0) {
                    GameHandler.screen = "game";
                }
                /*POSSIBLE ERRORS COMMITTED*/
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

        /*LABEL - ERRORS DETECTION*/
        Label lbError = new Label("Selecciona un sentido de giro por favor", skin);
        lbError.setPosition(viewportWidth/3 - 20, viewportHeight/4);

        /*LISTENERS*/
        btnAccept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(cbLeft.isChecked() == false && cbRight.isChecked() == false){
                    lapsStage.addActor(lbError);
                    menuState = MenuState.lapsState;
                }else {
                    GameHandler.screen = "game";
                    if(lstSpeed.getSelectedIndex() == 0){
                        GameHandler.speed_MainMenu = 10;
                    }
                    if(lstSpeed.getSelectedIndex() == 1){
                        GameHandler.speed_MainMenu = 20;
                    }
                    if(lstSpeed.getSelectedIndex() == 2){
                        GameHandler.speed_MainMenu = 30;
                    }
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
                if(cbLeft.isChecked()){
                    cbRight.setChecked(false);
                }
                GameHandler.rotationMode_MainMenu = "izquierda";
            }
        });
        cbRight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(cbRight.isChecked()){
                    cbLeft.setChecked(false);
                }
                GameHandler.rotationMode_MainMenu = "derecha";
            }
        });
    }

    private int ErrorNumberDetection(String txtInitAngle, String txtEndAngle, String txtspeed){
        int initAngle = 0, endAngle = 0, speed = 0;
        try{
            initAngle = Integer.parseInt(txtInitAngle);
            endAngle = Integer.parseInt(txtEndAngle);
            speed = Integer.parseInt(txtspeed);
            if((initAngle < 0 || endAngle < 0) || (initAngle > 360 || endAngle > 360)){
                return 2;
            }
            if((Math.abs(initAngle - endAngle) < 90)|| Math.abs(endAngle - initAngle) < 90){
                return 3;
            }
            if(speed > 100){
                return 4;
            }
        } catch (NumberFormatException exception){
            return 1;
        }
        return 0; // 0 means that there are no errors
    }
}
