package ca.crit.treasurehunter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameText {
    private float x, y;
    private float scaleX, scaleY;
    private String text;
    private BitmapFont font;

    public GameText(String text, BitmapFont font) {
        this.text = text;
        this.font = font;
        scaleX = 0.2f;
        scaleY = 0.2f;
        x = 0;
        y = 0;
        font.getData().setScale(scaleX, scaleY);
    }

    public GameText(String text, FileHandle fontFile, FileHandle imageFile, boolean flip) {
        this.text = text;
        scaleX = 0.2f;
        scaleY = 0.2f;
        x = 0;
        y = 0;
        font = new BitmapFont(fontFile, imageFile, flip);
        font.getData().setScale(scaleX, scaleY);
    }

    public GameText(String text, FileHandle fontFile, FileHandle imageFile, boolean flip, float scaleX, float scaleY) {
        this.text = text;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        x = 0;
        y = 0;
        font = new BitmapFont(fontFile, imageFile, flip);
        font.getData().setScale(scaleX, scaleY);
    }

    public GameText(String text, float x, float y) {
        this.text = text;
        this.x =  x;
        this.y = y;
        scaleX = 0.16f;
        scaleY = 0.38f;
        font = new BitmapFont(Gdx.files.internal("Fonts/logros.fnt"), Gdx.files.internal("Fonts/logros.png"), false);
        font.getData().setScale(scaleX, scaleY);
    }

    public void draw(Batch batch) {
        font.draw(batch, text, x, y);
    }

    //Getters and setters
    public void setText(String text) {
        this.text = text;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        font.getData().setScale(scaleX, scaleY);
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        font.getData().setScale(scaleX, scaleY);
    }

    public void setScaleXY(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        font.getData().setScale(scaleX, scaleY);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setXY(float x, float y){
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void dispose() {
        font.dispose();
    }

}
