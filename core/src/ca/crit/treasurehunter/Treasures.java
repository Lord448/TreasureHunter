package ca.crit.treasurehunter;

import static ca.crit.treasurehunter.GameHandler.WORLD_HEIGHT;
import static ca.crit.treasurehunter.GameHandler.WORLD_WIDTH;
import static ca.crit.treasurehunter.GameHandler.collided;
import static ca.crit.treasurehunter.GameHandler.treasurePosition;
import static ca.crit.treasurehunter.GameHandler.treasureAppeared;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;


public class Treasures {
    private float WIDTH, HEIGHT;                    // Treasure dimensions
    private float x, initialX_Position;               // Treasure position in axis X
    private float speed;                            // Treasure scrolling speed
    private float time;                             // Timer for the next treasure appearance
    private float appearTime, randomY;              // Random time for the next treasure appearance at a random Y position
    private Texture treasureTexture;
    public Rectangle rectangle;                     // Rectangle shape for the treasure hit box

    public Treasures(float x, float WIDTH, float HEIGHT){
        this.x = x;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        initialX_Position = x;
        speed = 20;
        appearTime = MathUtils.random(3, 5);
        randomY = MathUtils.random(2, WORLD_HEIGHT/4);
        treasureTexture= new Texture("Objects/treasure.png");

        treasureAppeared = true;
        treasurePosition = randomY;
        rectangle = new Rectangle();
    }

    public void render(float deltaTime, final SpriteBatch batch){

        if(x<-42 || collided){                      // Treasure disappeared from the map or collided with the ship
            treasureAppeared = false;               // Theres not a treasure in the map
            collided = false;                       // Reset the collision
            x = initialX_Position;                    // Next treasure has to appear at the same X position than the first one
            randomY = MathUtils.random(2, WORLD_HEIGHT/4);    // Choose the next Y treasure position
            treasurePosition = randomY;             // Where did the treasure appear? So the ship can capture it
        }
        if(x<-42 || collided || x == initialX_Position){      // Timer starts if treasure disappeared, collided or the next treasure is ready to appear
            time += deltaTime;
            if(time >= appearTime){                         // Timer arrived to the random lapse of time to make another treasure appear
                treasureAppeared = true;
                appearTime = MathUtils.random(3, 5);        // Choose another random time to appear the next treasure
            }
        }
        if(treasureAppeared){
            time = 0;               // Reset timer
            batch.draw(treasureTexture, x, randomY, WIDTH, HEIGHT);
            rectangle.set(x, randomY, WIDTH, HEIGHT);
            x -= deltaTime*speed;
        }
    }
    public void dispose(){
        treasureTexture.dispose();
        x = WORLD_WIDTH+70;
        initialX_Position = x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setInitialX_Position(float initialX_Position) {
        this.initialX_Position = initialX_Position;
    }

    /*
    private ShapeRenderer shapeRenderer;
    shapeRenderer = new ShapeRenderer();
    private void shapeRenderer(float randomY){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect((x+WIDTH/32), (randomY+HEIGHT/32), (float) WIDTH*2, (int) HEIGHT*2);
        shapeRenderer.end();
    }
     */

}
