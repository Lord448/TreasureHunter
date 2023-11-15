package ca.crit.treasurehunter;

import static ca.crit.treasurehunter.GameHandler.WORLD_HEIGHT;
import static ca.crit.treasurehunter.GameHandler.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
    /*ANIMACION OCEANO*/
    private float time;
    private final Texture oceanPack; //Imagen que contiene todos los frames del oceano
    private Animation oceanAnimation;
    private TextureRegion[] oceanTextures = new TextureRegion[21];
    /*BACKGROUNDS*/
    private final Texture[] dynamicBackgrounds;
    private final Texture[] staticBackgrounds;
    /*TIMING*/
    private final float[] backgroundOffsets = {0,0,0,0};
    private final float scrollingSpeed;

    public Background() {

        /*PROCESANDO ANIMACIÓN OCÉANO*/
        oceanPack = new Texture("Background/sea.png");
        TextureRegion[][] tmp = TextureRegion.split(oceanPack,oceanPack.getWidth()/5,
                        oceanPack.getHeight()/5); //Cortando cada frame de img. original                                                                                      // de img. original
        oceanMovementAnimation(tmp);
        time = 0f; //tiempo de renderización

        /*BACKGROUNDS*/
        dynamicBackgrounds = new Texture[3];
        dynamicBackgrounds [0] = new Texture("Background/clouds.png");
        dynamicBackgrounds [1] = new Texture("Background/rock1.png");
        dynamicBackgrounds [2] = new Texture("Background/rock2.png");

        staticBackgrounds = new Texture[2];
        staticBackgrounds[0] = new Texture("Background/sky.png");
        staticBackgrounds[1] = new Texture("Background/franja.png");

        scrollingSpeed =(float)(WORLD_HEIGHT/7);
    }

    public void render(float deltaTime, final SpriteBatch batch){
        backgroundOffsets [0]+= deltaTime * scrollingSpeed / 4;     //NUBES
        if(GameHandler.reached){
            backgroundOffsets [1] += deltaTime * scrollingSpeed / 2;    //ROCA 1
            backgroundOffsets [2] += deltaTime * scrollingSpeed / 3;    //ROCA 2
        }
        time += Gdx.graphics.getDeltaTime();    //Océano
        for(int layer=0; layer<backgroundOffsets.length; layer++){
            if(backgroundOffsets[layer] >= WORLD_WIDTH){
                backgroundOffsets[layer] = 0;
            }
            /*SKY*/
            batch.draw(staticBackgrounds[0],0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            /*NUBES*/
            batch.draw(dynamicBackgrounds[0], -backgroundOffsets[0], 0, WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(dynamicBackgrounds[0], -backgroundOffsets[0] + WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT);
            /*ANIMACIÓN OCEANO*/
            TextureRegion currentOceanFrame = (TextureRegion) oceanAnimation.getKeyFrame(time, true);
            batch.draw(currentOceanFrame, -4, 0, WORLD_WIDTH + 4, WORLD_HEIGHT/2);
            /*FRANJA - EVITA DEFECTO DE FRAMES EN OCEANO*/
            batch.draw(staticBackgrounds[1],0, 29, WORLD_WIDTH, WORLD_HEIGHT/10);
            /*ROCA 2 - DERECHA*/
            batch.draw(dynamicBackgrounds[2], -backgroundOffsets[2], 5, WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(dynamicBackgrounds[2], -backgroundOffsets[2] + WORLD_WIDTH, 5, WORLD_WIDTH, WORLD_HEIGHT);
            /*ROCA 1 - IZQUIERDA*/
            batch.draw(dynamicBackgrounds[1], -backgroundOffsets[1], 26, (float) (WORLD_WIDTH/1.5), (float) (WORLD_HEIGHT/1.5));
            batch.draw(dynamicBackgrounds[1], -backgroundOffsets[1] + WORLD_WIDTH, 26, (float) (WORLD_WIDTH/1.5), (float) (WORLD_HEIGHT/1.5));
        }

    }

    /*PARA ANIMAR LOS FRAMES RECORTADOS DE OCEANO*/
    private void oceanMovementAnimation(TextureRegion [][] temporal){
        int w=0;
        for(int i=0; i<5; i++){
            for(int j = 0; j<5; j++){
                oceanTextures[w] = temporal[i][j];
                w++;
                if(i==4 && j==0){
                    break;
                }
            }
        }
        oceanAnimation = new Animation<>(1/9f, oceanTextures);
    }

    public void dispose(){
        oceanPack.dispose();
        for (int i = 0; i< dynamicBackgrounds.length ; i++){
            dynamicBackgrounds[i].dispose();
        }
        for(int i = 0; i< staticBackgrounds.length; i++){
            staticBackgrounds[i].dispose();
        }
    }
}
