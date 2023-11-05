package ca.crit.treasurehunter;

import static ca.crit.treasurehunter.GameHandler.WORLD_HEIGHT;
import static ca.crit.treasurehunter.GameHandler.WORLD_WIDTH;
import static ca.crit.treasurehunter.GameHandler.collided;
import static ca.crit.treasurehunter.GameHandler.counter;
import static ca.crit.treasurehunter.GameHandler.onomatopoeiaAppear;
import static ca.crit.treasurehunter.GameHandler.playedTime_min;
import static ca.crit.treasurehunter.GameHandler.playedTime_sec;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class TextScreen {
    private final BitmapFont treasureCounter;   //Creado con fuente
    private final BitmapFont elapsedTime;   //Creado con fuente
    private final BitmapFont lapsCounter;   //Creado con fuente
    private final Texture collisionTexture;         //Creado con Imagen
    private final Texture treasureTexture;         //Creado con Imagen
    private float timer;

    TextScreen(){
        /*POINTS COUNTER*/
        treasureTexture = new Texture("Objects/treasure.png");
        treasureCounter = new BitmapFont(Gdx.files.internal("Fonts/counter.fnt"), Gdx.files.internal("Fonts/counter.png"), false);
        treasureCounter.getData().setScale(0.2f, 0.2f);

        /*ELAPSED PLAYED TIME*/
        elapsedTime = new BitmapFont(Gdx.files.internal("Fonts/counter.fnt"), Gdx.files.internal("Fonts/counter.png"), false);
        elapsedTime.getData().setScale(0.2f, 0.2f);

        /* LAPS COUNTER*/
        lapsCounter = new BitmapFont(Gdx.files.internal("Fonts/counter.fnt"), Gdx.files.internal("Fonts/counter.png"), false);
        lapsCounter.getData().setScale(0.2f, 0.2f);

        /*ONOMATOPOEIA COLLISION*/
        collisionTexture = new Texture("Fonts/onomat_bien.png");

        timer = 0;
        onomatopoeiaAppear = false;
    }

    public void render(final SpriteBatch batch){
        batch.draw(treasureTexture, 5, WORLD_WIDTH/2 - 5, WORLD_HEIGHT/8, WORLD_HEIGHT/9);
        treasureCounter.draw(batch, "x" + GameHandler.counter, 15, WORLD_WIDTH/2 + 2);
        lapsCounter.draw(batch, "Vueltas: "+ GameHandler.RoundTrips, WORLD_WIDTH/3 + 7, WORLD_HEIGHT - 6);

        //To see the time in seconds
        if(playedTime_sec < 60 & playedTime_min == 0){
            elapsedTime.draw(batch, (int)GameHandler.playedTime_sec + " s", WORLD_WIDTH/2 + 50, WORLD_HEIGHT - 6);
        }
        //To see the time in minutes
        else if(playedTime_sec < 10){
            elapsedTime.draw(batch, (int)GameHandler.playedTime_min +":0"+ (int)GameHandler.playedTime_sec + " min", WORLD_WIDTH/2 + 35, WORLD_HEIGHT - 6);
        }else {
            elapsedTime.draw(batch, (int)GameHandler.playedTime_min +":"+ (int)GameHandler.playedTime_sec + " min", WORLD_WIDTH/2 + 35, WORLD_HEIGHT - 6);
        }


        if(collided){
            counter ++;
        }
        if(onomatopoeiaAppear){
            batch.draw(collisionTexture, 34, GameHandler.treasurePosition-4, WORLD_HEIGHT/4, WORLD_HEIGHT/4);
            timer += Gdx.graphics.getDeltaTime();
            if(timer >=2){
                onomatopoeiaAppear = false;
                timer = 0;
                GameScreen.flag = true;
            }
        }
    }
}
