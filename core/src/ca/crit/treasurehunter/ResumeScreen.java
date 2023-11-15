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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ResumeScreen implements Screen {
    //private final String TAG = "ResumeScreen";

    /*SCREEN*/
    private final Camera camera;
    private final Viewport viewport, uiViewport;

    /*GRAPHICS*/
    private final SpriteBatch batch;
    private final Texture backgroundTexture;

    /*GRAPHIC USAGES*/
    private Skin skin;
    private final Stage stage;

    public ResumeScreen(){
        /*SCREEN*/
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        uiViewport = new StretchViewport(viewportWidth, viewportHeight, new OrthographicCamera());

        /*GRAPHICS*/
        batch = new SpriteBatch();
        backgroundTexture = new Texture("Background/background1.png");

        /*STAGE*/
        stage = new Stage(uiViewport);
        skin = new Skin(Gdx.files.internal("Menu/ShadeUISkin/uiskin.json"));
    }

    @Override
    public void show() {
        stage_constructor();
    }

    @Override
    public void render(float delta) {
        batch.begin();
            batch.draw(backgroundTexture, WORLD_WIDTH*100, WORLD_HEIGHT);
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

    }

    private void renderGraphics(float delta){
        Gdx.input.setInputProcessor(stage);
        stage.draw();
        stage.act(delta);
    }

    private void stage_constructor() {
        Label label;
        label = new Label("titulo", skin);
        label.setPosition(50,50);
        stage.addActor(label);
    }
}
