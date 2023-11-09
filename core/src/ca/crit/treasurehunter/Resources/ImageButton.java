package ca.crit.treasurehunter.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ImageButton extends com.badlogic.gdx.scenes.scene2d.ui.ImageButton {

    public ImageButton(String skinPath, String imageUpPath, String imageDownPath){
        super(new Skin(Gdx.files.internal(skinPath)));

        super.getStyle().imageUp =
                new TextureRegionDrawable(
                        new TextureRegion(
                            new Texture(Gdx.files.internal(imageUpPath))));
        super.getStyle().imageDown =
                new TextureRegionDrawable(
                        new TextureRegion(
                            new Texture(Gdx.files.internal(imageDownPath))));
    }

    public ImageButton(Skin skin, String imageUpPath, String imageDownPath){
        super(skin);

        super.getStyle().imageUp =
                new TextureRegionDrawable(
                        new TextureRegion(
                            new Texture(Gdx.files.internal(imageUpPath))));
        super.getStyle().imageDown =
                new TextureRegionDrawable(
                        new TextureRegion(
                            new Texture(Gdx.files.internal(imageDownPath))));
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
    }

    @Override
    public Skin getSkin() {
        return super.getSkin();
    }
}
