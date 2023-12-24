package ca.crit.treasurehunter;
import static ca.crit.treasurehunter.GameHandler.RoundTrips;
import static ca.crit.treasurehunter.GameHandler.angle_sensor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CircleBar {

    /**COMMON ATTRIBUTES*/
    float WIDTH = 20, HEIGHT = 20;
    float x = 3,y = 4;
    Texture userTexture = new Texture("Objects/circle_user.png");
    Texture computerTexture = new Texture("Objects/circle_computer.png");
    Texture circleTexture = new Texture("Objects/circle.png");
    TextureRegion user = new TextureRegion(userTexture);
    TextureRegion computer = new TextureRegion(computerTexture);
    Sprite user_sprite = new Sprite(user);
    Sprite computer_sprite = new Sprite(computer);
    float speed_computer, speed_user;       // How fast the circles move forward
    float hunting_maxDistance;              // Maximum Distance between both circles that allows the ship to hunt treasures and make parallax
    float angle_computer, angle_user;       // Current angles of the user and computer
    float beginningAngle;                   // Angle where circles begin the movement
    int farAway_maxDistance;                // Maximum distance allow between both circles before computer circle stops to wait the user circle

    /**GAME MODE: ANGLES ATTRIBUTES*/
    float lastAngle;                        // Last angle saves the last position of circle_computer when it exceeds the "maxDistance"
    float endAngle;                         // Limit angle where computer circle goes back to the begging angle to complete a loop
    boolean goForward = false, goBack = false, stop = false;     // Flags to make go forward or go back the computer circle in a certain range
    private boolean normalCount = false;    //normalCount happens when beginningAngle<endAngle
    /**GAME MODE: LAPS ATTRIBUTES*/
    String direction;
    boolean flagLaps = true;

    /**FOR SAMPLING VARIABLES*/
    int[] angleSample = new int[10];       //At what angle a sample is taken
    float[] timeSample;                    //At what angle a sample is taken
    float time;                            //Time played
    int index = 0, numberOfSamples=0;

    /**CONSTRUCTOR AND RENDER OF ANGLES GAME MODE*/
    public CircleBar(float speed_computer, float speed_user, float hunting_maxDistance,int farAway_maxDistance, float beginningAngle, float endAngle){
        this.speed_computer = speed_computer;
        this.speed_user = speed_user;
        this.hunting_maxDistance = hunting_maxDistance;
        this.farAway_maxDistance = farAway_maxDistance;

        /*TO AVOID THE PROBLEM OF CIRCLE COMPUTER DON'T GO ON WHEN END_ANGLE < BEGINNING_ANGLE*/
        if(endAngle<beginningAngle){
            this.endAngle = beginningAngle;
            this.beginningAngle = endAngle;
            normalCount = false;
            goBack = true;
        }else {
            this.beginningAngle = beginningAngle;
            this.endAngle = endAngle;
            normalCount = true;
            goForward = true;
        }
        angle_computer = beginningAngle;
        angle_user = beginningAngle;

        /* MODIFY THE SIZE AND POSITION OF THE SPRITES*/
        user_sprite.setSize(WIDTH, HEIGHT);
        computer_sprite.setSize(WIDTH, HEIGHT);
        user_sprite.setX(x);
        user_sprite.setY(y);
        computer_sprite.setX(x);
        computer_sprite.setY(y);

        /* SET THE RADIUS SPIN CIRCLES*/
        user_sprite.setOrigin((user_sprite.getWidth()/2), (user_sprite.getHeight()/2));
        computer_sprite.setOrigin((computer_sprite.getWidth()/2), (computer_sprite.getHeight()/2));

        /*SAMPLING*/
        //timeSamples(this.beginningAngle, this.endAngle); //todo
    }

    public void render_AnglesGame(float deltaTime, final SpriteBatch batch){
        batch_sprite_rotations(batch);
        user_movement(deltaTime);
        //PARALLAX AND HUNTING HAPPENS
        GameHandler.reached = isInRange(angle_computer, angle_user);     // Both circles are near each other

        //RESET USER ANGLE VARIABLE ONLY
        if(angle_user > 360){
            angle_user = 0;
        } else if (angle_user < 0) {
            angle_user = 360;
        }
        //----------------------------------
        if(angle_computer < beginningAngle){        // Computer circle arrived to the beginning angle
            goForward = true;                       // Computer circle has to go forward
            goBack = false;
            if(normalCount == true){
                RoundTrips ++;
            }
        }else if (angle_computer > endAngle){       // Computer circle arrived to the end angle
            goForward = false;
            goBack = true;                          // Computer circle has to go back
            if(normalCount == false){
                RoundTrips ++;
            }
        }
        //----------------------------------
        stop = isFarAway(angle_computer, angle_user);// Computer circle has to go stop
        //----------------------------------
        lastAngle = angle_computer;
        if(goForward){
            angle_computer += deltaTime * speed_computer;
        }
        if(goBack){
            angle_computer -= deltaTime * speed_computer;
        }
        if(stop){
            angle_computer = lastAngle;
        }

        /*SAMPLING RENDER*/
        //render_Sampling(deltaTime); //todo
    }

    /**CONSTRUCTOR AND RENDER OF LAPS GAME MODE*/
    public CircleBar(float speed_computer, float speed_user, float hunting_maxDistance,int farAway_maxDistance, float beginningAngle, String direction){
        this.speed_computer = speed_computer;
        this.speed_user = speed_user;
        this.hunting_maxDistance = hunting_maxDistance;
        this.farAway_maxDistance = farAway_maxDistance;
        this.beginningAngle = beginningAngle;
        this.direction = direction;

        angle_user = beginningAngle;
        angle_computer = beginningAngle;

        if(beginningAngle == 0 & direction.equals("izquierda")){    //TO RESOLVE START PROBLEMS OF GO ON AT COMPUTER CIRCLE WHEN BEGINNING ANGLE=0
            angle_user = 360;                                       // IN IZQUIERDA DIRECTION MODE
        }

        /* MODIFY THE SIZE AND POSITION OF THE SPRITES*/
        user_sprite.setSize(WIDTH, HEIGHT);
        computer_sprite.setSize(WIDTH, HEIGHT);
        user_sprite.setX(x);
        user_sprite.setY(y);
        computer_sprite.setX(x);
        computer_sprite.setY(y);

        /* SET THE RADIUS SPIN CIRCLES*/
        user_sprite.setOrigin((user_sprite.getWidth()/2), (user_sprite.getHeight()/2));
        computer_sprite.setOrigin((computer_sprite.getWidth()/2), (computer_sprite.getHeight()/2));
    }

    public void render_LapsGame(float deltaTime, final SpriteBatch batch){
        batch_sprite_rotations(batch);
        user_movement(deltaTime);

        //PARALLAX AND HUNTING HAPPENS
        GameHandler.reached = isInRange(angle_computer, angle_user);     // Both circles are near each other

        /*DIRECTIONS MOVEMENTS*/
        lastAngle = angle_computer;

        if(direction.equals("derecha")){
            angle_computer -= deltaTime * speed_computer;
            if (angle_computer <= 0) {
                angle_computer = 360;
            }
            /*COUNTING LAPS*/
            if( angle_computer > beginningAngle & angle_computer < beginningAngle+5 & flagLaps){    // a 5° range to increment laps
                RoundTrips ++;
                flagLaps = false;
            }
            if(angle_computer <= beginningAngle-50){     // flagLaps becomes true after a lower beginning angle to avoid bounces
                flagLaps = true;
            }
            if(stop){
                angle_computer = lastAngle;
            }
        }
        else if (direction.equals("izquierda")) {
            angle_computer += deltaTime * speed_computer;
            if(angle_computer >= 360){
                angle_computer = 0;
            }
            /*COUNTING LAPS*/
            if( angle_computer < beginningAngle & angle_computer > beginningAngle-5 & flagLaps){
                RoundTrips ++;
                flagLaps = false;
            }
            if(angle_computer >= beginningAngle+50){    // flagLaps becomes true after exceeds beginning angle to avoid bounces
                flagLaps = true;
            }
            if(stop){   //(angle_computer < angle_user + 300) to not to stop when angle_computer=0° and angle_user = 360°
                angle_computer = lastAngle;
            }
        }

        /*STOP COMPUTER CIRCLE IF USER CIRCLE IS TOO FAR AWAY*/
        stop = isFarAway(angle_computer, angle_user);

        /*RESET USER ANGLES VARIABLES*/
        if(angle_user > 360){
            angle_user = 0;
        }
        if (angle_user < 0) {
            angle_user = 360;
        }
    }

    /**COMMON METHODS FOR ANGLES GAME MODE AND LAPS GAME MODE*/
    private void user_movement(float deltaTime){
        System.out.println(GameHandler.environment);
        if(GameHandler.environment == GameHandler.DESKTOP_ENV) {


            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                angle_user += deltaTime * speed_user;     // How user circle go forward
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                angle_user -= deltaTime * speed_user;     // How user circle go back
            }
        }
        else if(GameHandler.environment == GameHandler.MOBILE_ENV) {
            angle_user = angle_sensor;
        }
    }

    private void batch_sprite_rotations(SpriteBatch batch){
        batch.draw(circleTexture, x, y, WIDTH, HEIGHT);

        computer_sprite.draw(batch);
        user_sprite.draw(batch);

        computer_sprite.setRotation(angle_computer);
        user_sprite.setRotation(angle_user);
    }

    private boolean isInRange(float computer, float user){
        float rangeHigh = computer + hunting_maxDistance;    // Maximum angle near to make parallax and hunting
        float rangeLow = computer - hunting_maxDistance;     // Minimum angle near to make parallax and hunting
        return user < rangeHigh && user > rangeLow;
    }

    private boolean isFarAway(float computer, float user){

        // Circles are far away from each other under a problematic stop function to work correctly:
        boolean riskZone = (computer>(360-farAway_maxDistance) && user<farAway_maxDistance) || (user>(360-farAway_maxDistance) && computer<farAway_maxDistance);
        if(riskZone){
            if(computer > user){                                //computer is forward user
                return (360-(computer-user)) > farAway_maxDistance;          //exceeds the allowed distance between circles
            }else {                                             //user is forward computer
                return (360-(user-computer)) > farAway_maxDistance;          //exceeds the allowed distance between circles
            }
        }
        // Circles are far away from each other under a normal stop function to work correctly:
        else {
            boolean computer_isForward = (computer-user) > farAway_maxDistance;
            boolean user_isForward = (user-computer) > farAway_maxDistance;
            return  computer_isForward || user_isForward;   //exceeds the allowed distance between circles
        }
    }
    public void dispose(){
        userTexture.dispose();
        computerTexture.dispose();
        circleTexture.dispose();
    }

    /**SAMPLING TIME PER ANGLES*/
    public void timeSamples(float beginningAngle, float endAngle){
        int samplePerAngle = (int)(Math.abs(endAngle - beginningAngle)/10);     //Angle separation between samples

        if(beginningAngle < endAngle){
            for (int i=angleSample.length-1; i >= 0; i--){
                angleSample[i] = samplePerAngle * (i+1);
            }

        } else if (beginningAngle > endAngle) {
            for (int i=0; i<angleSample.length; i++){
                angleSample[i] = samplePerAngle * (i+1);
            }
        }

    }
    public void render_Sampling(float deltaTime){
        time += deltaTime;

        if(goForward)
            if(angle_user > angleSample[index]){
                timeSample[numberOfSamples] = time;
                System.out.println(timeSample[numberOfSamples]);
                time = 0;
                numberOfSamples ++;
                index ++;
                if(index > 10){
                    index = 10;
                }
            }

        if(goBack){
            if(angle_user < angleSample[index]){
                timeSample[numberOfSamples] = time;
                System.out.println(timeSample[numberOfSamples]);
                time = 0;
                numberOfSamples ++;
                index --;
                if(index < 0){
                    index = 0;
                }
            }
        }
    }

    /**CONSTRUCTOR AND RENDER FOR DRAWING CHOSEN ANGLES ON THE ANGLES GAME MODE MENU*/
    public CircleBar(float x, float y, int numberOfCircles){
        if(numberOfCircles == 1){
            computer_sprite.setSize(WIDTH, HEIGHT);
            computer_sprite.setX(x);
            computer_sprite.setY(y);
            /* SET THE RADIUS SPIN CIRCLE*/
            computer_sprite.setOrigin((computer_sprite.getWidth()/2), (computer_sprite.getHeight()/2));
        }else {
            user_sprite.setSize((float) (WIDTH*1.5), (float) (HEIGHT*1.5));
            user_sprite.setX(x);
            user_sprite.setY(y);
            computer_sprite.setSize((float) (WIDTH*1.5), (float) (HEIGHT*1.5));
            computer_sprite.setX(x);
            computer_sprite.setY(y);
            /* SET THE RADIUS SPIN CIRCLES*/
            user_sprite.setOrigin((user_sprite.getWidth()/2), (user_sprite.getHeight()/2));
            computer_sprite.setOrigin((computer_sprite.getWidth()/2), (computer_sprite.getHeight()/2));
        }
    }
    public void batch_sprite_rotation(float x, float y, final SpriteBatch batch, float initAngle, float endAngle){
        batch.draw(circleTexture, x, y, (float) (WIDTH*1.5), (float) (HEIGHT*1.5));
        user_sprite.setRotation(initAngle);
        user_sprite.draw(batch);

        computer_sprite.setRotation(endAngle);
        computer_sprite.draw(batch);
    }

    /**RENDER FOR EXEMPLIFY SPEED ON BOTH GAME MODE MENUS*/
    public void render_speedRotation(final SpriteBatch batch, float deltaTime, float x, float y, float speed){
        batch.draw(circleTexture, x, y, WIDTH, HEIGHT);
        computer_sprite.setRotation(angle_computer);
        computer_sprite.draw(batch);
        angle_computer += deltaTime * speed;
        if(angle_computer > 360){
            angle_computer = 0;
        }
    }
}
