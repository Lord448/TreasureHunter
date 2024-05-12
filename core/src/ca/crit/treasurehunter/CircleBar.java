package ca.crit.treasurehunter;
import static ca.crit.treasurehunter.GameHandler.RoundTrips;
import static ca.crit.treasurehunter.GameHandler.angle_calibrated;
import static ca.crit.treasurehunter.GameHandler.angle_sensor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CircleBar {

    /**COMMON ATTRIBUTES BETWEEN GAME MODES*/
    float WIDTH = 20, HEIGHT = 20;
    final float x = 3,y = 4;
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

    /**GAME MODE ATTRIBUTES: ANGLES*/
    float lastAngle;                        // Last angle saves the last position of circle_computer when it exceeds the "maxDistance"
    float endAngle;                         // Limit angle where computer circle goes back to the begging angle to complete a loop
    boolean goForward = false, goBack = false, stop = false;     // Flags to make go forward or go back the computer circle in a certain range
    private boolean normalCount = false;    //normalCount happens when beginningAngle<endAngle
    /**GAME MODE ATTRIBUTES: LAPS*/
    String direction;
    boolean flagLaps = true;

    /**CONSTRUCTOR AND RENDER FOR GAME MODE: ANGLES*/
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
        angle_user = angle_calibrated;

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
        System.out.println("angles, "+angle_user);

        //----------------------------------
        if(angle_computer < beginningAngle){        // Computer circle arrived to the beginning angle
            goForward = true;                       // Computer circle has to go forward
            goBack = false;
            if(normalCount){
                RoundTrips ++;
            }
        }else if (angle_computer > endAngle){       // Computer circle arrived to the end angle
            goForward = false;
            goBack = true;                          // Computer circle has to go back
            if(!normalCount){
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
    }

    /**CONSTRUCTOR AND RENDER FOR GAME MODE: LAPS*/
    public CircleBar(float speed_computer, float speed_user, float hunting_maxDistance,int farAway_maxDistance, float beginningAngle, String direction){
        this.speed_computer = speed_computer;
        this.speed_user = speed_user;
        this.hunting_maxDistance = hunting_maxDistance;
        this.farAway_maxDistance = farAway_maxDistance;
        this.beginningAngle = beginningAngle;
        this.direction = direction;

        angle_user = angle_calibrated;
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

        System.out.println("laps, "+angle_user);
    }

    /**COMMON METHODS FOR GAME MODE: LAPS & ANGLES*/
    private void user_movement(float deltaTime){
        if(GameHandler.environment == GameHandler.DESKTOP_ENV) {
            if(angle_user < 0) angle_user = 360;
            if(angle_user > 360) angle_user = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                angle_user += deltaTime * speed_user;     // How user circle go forward
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                angle_user -= deltaTime * speed_user;     // How user circle go back
            }
            //GameHandler.angle_laptop = angle_user;
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
        user_sprite.setRotation(angle_user+15);
    }
    private boolean isInRange(float computer, float user){
        float rangeHigh = computer + hunting_maxDistance;    // Maximum angle near to make parallax and hunting
        float rangeLow = computer - hunting_maxDistance-30;     // Minimum angle near to make parallax and hunting
        return user < rangeHigh && user > rangeLow;
    }
    private boolean isFarAway(float computer, float user){
        computer -= 15;
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

    /**CONSTRUCTOR AND RENDER FOR GAME MODE MENU: ANGLES*/
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
        user_sprite.setRotation(initAngle+15);
        user_sprite.draw(batch);

        computer_sprite.setRotation(endAngle);
        computer_sprite.draw(batch);
    }

    /**RENDER FOR EXEMPLIFY SPEED GAME MODE MENU: ANGLES & LAPS*/
    public void render_speedRotation(final SpriteBatch batch, float deltaTime, float x, float y, float speed){
        batch.draw(circleTexture, x, y, WIDTH, HEIGHT);
        computer_sprite.setRotation(angle_computer);
        computer_sprite.draw(batch);
        angle_computer += deltaTime * speed;
        if(angle_computer > 360){
            angle_computer = 0;
        }
    }

    /**CONSTRUCTOR FOR CALIBRATION MENU*/
    public CircleBar (float x, float y){
        speed_user = 60;
        user_sprite.setSize((float) (WIDTH*1.5), (float) (HEIGHT*1.5));
        user_sprite.setX(x+35);
        user_sprite.setY(y);

        computer_sprite.setSize((float) (WIDTH*1.5), (float) (HEIGHT*1.5));
        computer_sprite.setX(x);
        computer_sprite.setY(y);
        /* SET THE RADIUS SPIN CIRCLES*/
        user_sprite.setOrigin((user_sprite.getWidth()/2), (user_sprite.getHeight()/2));
        computer_sprite.setOrigin((computer_sprite.getWidth()/2), (computer_sprite.getHeight()/2));

        /*If clause for simulation proposes with a desktop environment*/
        if(GameHandler.environment == GameHandler.DESKTOP_ENV){
            angle_user = 90;
        }
    }

    public void render_calibrationMenu(float x, float y, final SpriteBatch batch, float delta){
        /**-----------------------------------------
         *          CIRCLES AND ARROWS DRAWING
         *-----------------------------------------*/
        batch.draw(circleTexture, x+35, y, (float) (WIDTH*1.5), (float) (HEIGHT*1.5));  //Drawing the black arrow
        user_movement(delta);                                                              //Green circle movement on a desktop/mobile environment
        user_sprite.draw(batch);                                                           //Drawing the green circle
        user_sprite.setRotation(angle_user+15);                                               //Updating the angle position of green circle

        batch.draw(circleTexture, x, y, (float) (WIDTH*1.5), (float) (HEIGHT*1.5));     //Drawing the black arrow
        computer_sprite.setRotation(15);                                                 // Setting circle to a 0° angle
        computer_sprite.draw(batch);                                                    //Green yellow movement on a desktop/mobile environment

        /**----------------------------------------
         *         CALIBRATION DETECTION
         *---------------------------------------*/
        calibrationDetection();

    }
    public boolean calibrationDetection(){
        if(angle_user > (360-1) || angle_user < 1){
            angle_calibrated = angle_user;
            return true;
        }else {
            return false;
        }
    }

    public void dispose(){
        userTexture.dispose();
        computerTexture.dispose();
        circleTexture.dispose();
    }
}
