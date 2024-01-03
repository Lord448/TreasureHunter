package ca.crit.treasurehunter;

import com.badlogic.gdx.Game;

import java.util.ArrayList;

public class Sampling {
    private final float hysteresis = 4;                 //will make the user arrive to the correct angle in +-1° of error
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
        anglesSampling(deltaTime, userAngle);
        //System.out.println("USER:"+userAngle+" ANGLE:"+angleSample[index]);
    }

    public void anglesSampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        boolean nextCycle = false;
        if(angleReached){
            System.out.println("USER:"+userAngle+" ANGLE:"+angleSample[index]);
            csvWriter.sampledData_CSVFile(GameHandler.resumeData, row, cycle, angleSample, index, elapsedTime);   //Method that saves the samples into a dynamic array
            row ++;
            elapsedTime = 0;
            if(goForward){
                index ++;
                if(index > 10){         //Prepare variables to sample in the opposite direction
                    index = index - 1;
                    goForward = false;
                    goBack = true;
                    nextCycle = true;
                }
            }
            if(goBack){
                index --;
                if(index < 0){          //Prepare variables to sample in the opposite direction
                    index = index + 2;
                    goForward = true;
                    goBack = false;
                    nextCycle = true;
                }
            }
            if(nextCycle){
                cycle ++;
                elapsedTime = 0;
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
        lapsSampling(deltaTime, userAngle);
    }

    public void lapsSampling(float deltaTime, float userAngle){
        boolean angleReached = userAngle > (angleSample[index]-hysteresis) && userAngle < (angleSample[index]+hysteresis);
        boolean nextCycle = false;
        if(angleReached){
            System.out.println("USER:"+userAngle+" ANGLE:"+angleSample[index]);
            //Method that saves the samples into a dynamic array
            csvWriter.sampledData_CSVFile(GameHandler.resumeData, row, cycle, angleSample, index, elapsedTime);
            row ++;
            elapsedTime = 0;
            if(GameHandler.rotationMode_MainMenu.equals("derecha")){
                index --;
                if(index < 0){
                    index = 9;                      //index reset to sample from angleSample[9] = 324° to angleSample[0] = 0°
                    nextCycle = true;
                }
            }else {
                index ++;
                if(index > 10) {
                    index = 1;                      //index reset to sample from angleSample[9] = 324° to angleSample[0] = 0°
                    nextCycle = true;
                }
            }
            if(nextCycle){
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
