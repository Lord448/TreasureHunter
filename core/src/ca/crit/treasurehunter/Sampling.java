package ca.crit.treasurehunter;

import java.util.ArrayList;

public class Sampling {
    private float hysteresis = 1;
    private float[] angleSample = new float[11];
    private int index, cycle;
    private float elapsedTime;
    private boolean goForward, goBack;
    private int row=0, column=0;

    /**Constructor and Render for Game Mode: ANGLES*/
    public Sampling(int initAngle, int endAngle){
        float angleJump = Math.abs(endAngle - initAngle)/10;
        for (int i=0 ; i<=9 ; i++){
            angleSample[i] = angleJump * i; //angleSample[0] = 0 and angleSample[9] = 90
        }
        if(GameHandler.beginningAngle_MainMenu > GameHandler.endAngle_MainMenu){
            goBack = true;
            goForward = false;
            index = 9;
        }else {
            goForward = true;
            goBack = false;
            index = 0;
        }
        cycle = 1;
        GameHandler.resumeData.add(new ArrayList<>());
    }
    public void angles_render(float deltaTime, float userAngle){
        if(goForward){
            goForward_Sampling(deltaTime, userAngle);

        } else if (goBack) {
            goBack_Sampling(deltaTime, userAngle);
        }
    }

    /**Constructor and Render for Game Mode: LAPS*/
    public Sampling(){
        for (int i=0 ; i<=9 ; i++){
            angleSample[i] = 36 * i; //angleSample[0] = 0 and angleSample[9] = 360
        }
        if(GameHandler.rotationMode_MainMenu == "derecha"){
            index = 9;
        }else if(GameHandler.rotationMode_MainMenu == "izquierda"){
            index = 0;
        }
        cycle = 1;
        GameHandler.resumeData.add(new ArrayList<>());
    }
    public void laps_render(float deltaTime, float userAngle){
        if(GameHandler.rotationMode_MainMenu == "izquierda"){
            leftRotation_Sampling(deltaTime, userAngle);
        }
        else if (GameHandler.rotationMode_MainMenu == "derecha") {
            rightRotation_Sampling(deltaTime, userAngle);
        }
    }

    /**Methods for Game Mode: ANGLES*/
    public void goForward_Sampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index+1]-hysteresis) && userAngle < (angleSample[index+1]+hysteresis);
        if(angleReached){
            angles_gettingData();
            elapsedTime = 0;
            index ++;
            if(index >= 9){
                index = index-1;
                cycle ++;
                elapsedTime = 0;
                goBack = true;
                goForward = false;
            }
        }else {
            elapsedTime += deltaTime;
        }
    }
    public void goBack_Sampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        if(angleReached){
            angles_gettingData();
            elapsedTime = 0;
            index --;
            if(index < 0){
                index = index + 1;
                cycle ++;
                elapsedTime = 0;
                goForward = true;
                goBack = false;
            }
        }else {
            elapsedTime += deltaTime;
        }
    }
    public void angles_gettingData(){
        if(goForward){
            GameHandler.resumeData.get(row).add(String.valueOf(cycle));
            column++;
            GameHandler.resumeData.get(row).add((int) angleSample[index + 1] + "°");
            column++;
            GameHandler.resumeData.get(row).add(String.format("%.2f",elapsedTime) + " sec");
            column = 0;
            row ++;
            GameHandler.resumeData.add(new ArrayList<>());
        } else if (goBack) {
            GameHandler.resumeData.get(row).add(String.valueOf(cycle));
            column++;
            GameHandler.resumeData.get(row).add((int)angleSample[index] + "°");
            column++;
            GameHandler.resumeData.get(row).add(String.format("%.2f",elapsedTime) + " sec");
            column = 0;
            row ++;
            GameHandler.resumeData.add(new ArrayList<>());
        }
    }
    //TODO: si el angulo de inicio es mayor que el de fin, no se llena GameHandler.resumedData de regreso, nomas de ida

    /**Methods for Game Mode: LAPS*/
    public void leftRotation_Sampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        if(angleReached){
            laps_gettingData();
            elapsedTime = 0;
            index ++;
            if(index < 0){
                index = 0;
                cycle ++;
                elapsedTime = 0;
            }
        }else {
            elapsedTime += deltaTime;
        }
    }
    public void rightRotation_Sampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        if(angleReached){
            laps_gettingData();
            elapsedTime = 0;
            index --;
            if(index < 0){
                index = 9;
                cycle ++;
                elapsedTime = 0;
            }
        }else {
            elapsedTime += deltaTime;
        }
    }
    public void laps_gettingData(){
        GameHandler.resumeData.get(row).add(String.valueOf(cycle));
        column++;
        GameHandler.resumeData.get(row).add((int) angleSample[index + 1] + "°");
        column++;
        GameHandler.resumeData.get(row).add(String.format("%.2f",elapsedTime) + " sec");
        column = 0;
        row ++;
        GameHandler.resumeData.add(new ArrayList<>());
    }
    //TODO: sale error al escribir archivos CSV en el modo de juego de vueltas completas
}
