package ca.crit.treasurehunter;

import com.badlogic.gdx.Game;

import java.util.ArrayList;

public class Sampling {
    private final float hysteresis = 1;                 //will make the user arrive to the correct angle in +-1° of error
    private final float[] angleSample = new float[12];  //saves the angle to be sampled
    private int index;                                  //the number of the "angleSample" array
    private int cycle;                                  //the sample lot number
    private float elapsedTime;                          //the time that has passed before user arrived to the correct angle
    private boolean goForward=false, goBack=false;      //goForward to samples in a right direction and goBack in a left direction
    private int row;                              //the number of rows to be written at the CSV file
    private CSVWriter csvWriter = new CSVWriter();

    /**-------------------------------------------------------
     *                  ANGLES GAME MODE
     * --------------------------------------------------------*/
    /*Constructor and Render for Game Mode: ANGLES*/
    public Sampling(int initAngle, int endAngle){
        int angleJump = Math.abs(endAngle - initAngle)/10;
        int smallestNumber = Math.min(endAngle, initAngle);
        for (int i=0 ; i<=10 ; i++){
            angleSample[i] = (angleJump * i)+ smallestNumber;
        }
        if(GameHandler.beginningAngle_MainMenu > GameHandler.endAngle_MainMenu){
            goBack = true;
            goForward = false;
            index = 9;
        }else {
            goForward = true;
            goBack = false;
            index = 1;
        }
        row = 0;
        cycle = 1;

        /*If the user selects "Finalizar sesión" button before any sample, CVS file will be written with the text bellow*/
        csvWriter.noneData_CSVFile(GameHandler.resumeData);
    }
    public void angles_render(float deltaTime, float userAngle){
        if(goForward){
            goForward_Sampling(deltaTime, userAngle);

        } else if (goBack) {
            goBack_Sampling(deltaTime, userAngle);
        }
        System.out.println("USER:"+userAngle+" ANGLE:"+angleSample[index]);
    }

    /*Methods for Sampling in Game Mode: ANGLES*/
    public void goForward_Sampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        if(angleReached){
            //Method that saves the samples into a dynamic array
            csvWriter.sampledData_CSVFile(GameHandler.resumeData, row, cycle, angleSample, index, elapsedTime);
            row ++;
            elapsedTime = 0;
            index ++;
            if(index > 10){                      //Prepare variables to sample in the opposite direction
                index = index-2;
                cycle ++;
                elapsedTime = 0;
                goBack = true;
                goForward = false;
                csvWriter.addingEmptyRow(GameHandler.resumeData, row);   //Method to write and empty row inside the CSV file to separate cycles
                row++;
            }
        }else {
            elapsedTime += deltaTime;
        }
    }
    public void goBack_Sampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        if(angleReached){
            csvWriter.sampledData_CSVFile(GameHandler.resumeData, row, cycle, angleSample, index, elapsedTime);   //Method that saves the samples into a dynamic array
            row ++;
            elapsedTime = 0;
            index --;
            if(index < 0){                       //Prepare variables to sample in the opposite direction
                index = index + 2;
                cycle ++;
                elapsedTime = 0;
                goForward = true;
                goBack = false;
                csvWriter.addingEmptyRow(GameHandler.resumeData, row);   //Method to write and empty row inside the CSV file to separate cycles
                row++;
            }
        }else {
            elapsedTime += deltaTime;
        }
    }

    /**-------------------------------------------------------
     *                  LAPS GAME MODE
     * --------------------------------------------------------*/
    /*Constructor and Render for Game Mode: LAPS*/
    public Sampling(){
        for (int i=0 ; i<=10 ; i++){
            angleSample[i] = 36 * i; //angleSample[0] = 0 and angleSample[9] = 360
        }
        if(GameHandler.rotationMode_MainMenu.equals("derecha")){
            index = 9;  //to make user reach angleSample[9] = 324 degrees
        }else if(GameHandler.rotationMode_MainMenu.equals("izquierda")){
            index = 1;  //to make user reach angleSample[1] = 36 degrees
        }
        row = 0;
        cycle = 1;

        /*If the user selects "Finalizar sesión" button before any sample, CVS file will be written with the text bellow*/
        csvWriter.noneData_CSVFile(GameHandler.resumeData);
    }
    public void laps_render(float deltaTime, float userAngle){
        if(GameHandler.rotationMode_MainMenu.equals("izquierda")){
            leftRotation_Sampling(deltaTime, userAngle);
        }
        else if (GameHandler.rotationMode_MainMenu.equals("derecha")) {
            rightRotation_Sampling(deltaTime, userAngle);
        }
    }

    /*Methods for Sampling in Game Mode: LAPS*/
    public void leftRotation_Sampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        if(angleReached){
            //Method that saves the samples into a dynamic array
            csvWriter.sampledData_CSVFile(GameHandler.resumeData, row, cycle, angleSample, index, elapsedTime);
            row ++;
            elapsedTime = 0;
            index ++;
            if(index > 10){
                index = 1;                      //index reset to sample from angleSample[1] = 36° to angleSample[10] = 360°
                cycle ++;
                elapsedTime = 0;
               csvWriter.addingEmptyRow(GameHandler.resumeData, row);   //Method to write and empty row inside the CSV file to separate cycles
                row++;
            }
        }else {
            elapsedTime += deltaTime;
        }
    }
    public void rightRotation_Sampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        if(angleReached){
            //Method that saves the samples into a dynamic array
            csvWriter.sampledData_CSVFile(GameHandler.resumeData, row, cycle, angleSample, index, elapsedTime);
            row ++;
            elapsedTime = 0;
            index --;
            if(index < 0){
                index = 9;                      //index reset to sample from angleSample[9] = 324° to angleSample[0] = 0°
                cycle ++;
                elapsedTime = 0;
                csvWriter.addingEmptyRow(GameHandler.resumeData, row);  //Method to write and empty row inside the CSV file to separate cycles
                row++;
            }
        }else {
            elapsedTime += deltaTime;
        }
    }
}
