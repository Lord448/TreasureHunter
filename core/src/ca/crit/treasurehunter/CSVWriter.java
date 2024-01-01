package ca.crit.treasurehunter;
import static ca.crit.treasurehunter.GameHandler.RoundTrips;
import static ca.crit.treasurehunter.GameHandler.beginningAngle_MainMenu;
import static ca.crit.treasurehunter.GameHandler.counter;
import static ca.crit.treasurehunter.GameHandler.endAngle_MainMenu;
import static ca.crit.treasurehunter.GameHandler.gameMode_MainMenu;
import static ca.crit.treasurehunter.GameHandler.playedTime_min;
import static ca.crit.treasurehunter.GameHandler.playedTime_sec;
import static ca.crit.treasurehunter.GameHandler.rotationMode_MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class CSVWriter {

    /**-------------------------------------------------------
     *                         METHODS
     * --------------------------------------------------------*/
    public void writeCSV(String fileName) {
        // Getting the specific external saving file path from the app
        String externalStoragePath = Gdx.files.getExternalStoragePath();

        // Combining the extern saving file path with the wished file name
        String filePath = externalStoragePath + fileName;
        GameHandler.savedFilePath = filePath;
        System.out.println("Archivo CSV descargado en: "+ GameHandler.savedFilePath);

        // Creating a fileHandle with the complete saving file path
        FileHandle file = Gdx.files.absolute(filePath);

        try {
            /**----------------------------------------------
             *      SAVING RELEVANT GAME DATA TEXT
             *----------------------------------------------*/
            StringBuilder csvRelevantData = new StringBuilder();
            fillStringBuilder(csvRelevantData, GameHandler.relevantData);
            file.writeString(csvRelevantData.toString(), false);

            /**-------------------------------------------------
             * SAVING THE TAKEN ANGLES SAMPLES DURING THE GAME
             * -----------------------------------------------*/
            StringBuilder csvResumeData = new StringBuilder();
            fillStringBuilder(csvResumeData, GameHandler.resumeData);
            file.writeString(csvResumeData.toString(), true);
            /***-------------------------------------------------------*/
            Gdx.app.log("Escritura de CSV", "Archivo escrito exitosamente");

        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.error("Escritura de CSV", "Error al escribir en el archivo");
        }
    }
    private void fillStringBuilder(StringBuilder stringBuilder, ArrayList<ArrayList<String>> arrayList){
        for(ArrayList<String> row : arrayList){
            for (String value : row){
                stringBuilder.append(value).append(",");
            }
            stringBuilder.setLength(stringBuilder.length()-1);  // Remove the trailing comma
            stringBuilder.append("\n");
        }
    }
    public void addingEmptyRow(ArrayList<ArrayList<String>> arrayList, int row){
        arrayList.get(row).add(" ");
        arrayList.get(row).add(" ");
        arrayList.get(row).add(" ");
        arrayList.add(new ArrayList<>());  //Creating a new row to fill in
    }
    public void noneData_CSVFile(ArrayList<ArrayList<String>> arrayList){
        arrayList.add(new ArrayList<>());  //Creating the first row to fill in
        /*If the user selects "Finalizar sesión" button before any sample, CVS file will be written with the text bellow*/
        arrayList.get(0).add("No hay valores capturados aún"); //Text written at the Cycle column
        arrayList.get(0).add("No hay valores capturados aún"); //Text written at the Angle column
        arrayList.get(0).add("No hay valores capturados aún"); //Text written at the Reached time column
    }
    /**-------------------------------------------------------
     *          TEXT TO BE WRITTEN INTO THE CSV FILE
     * --------------------------------------------------------*/
    public void relevantData_CSVFile(){
        /**-----------------
         * TEXT SCREEN DATA
         * ---------------*/
        int row = 0;
        GameHandler.relevantData.add(new ArrayList<>());  //Creating a new row to fill in
        GameHandler.relevantData.get(row).add(0, "Tesoros");
        GameHandler.relevantData.get(row).add(1, "Vueltas");
        GameHandler.relevantData.get(row).add(2, "Duración Sesión");
        row ++;
        GameHandler.relevantData.add(new ArrayList<>());  //Creating a new row to fill in
        GameHandler.relevantData.get(row).add(0, String.valueOf(counter));
        GameHandler.relevantData.get(row).add(1, String.valueOf(RoundTrips));
        GameHandler.relevantData.get(row).add(2, (int) playedTime_min + " min con " + (int) playedTime_sec + " sec");
        row ++;
        GameHandler.relevantData.add(new ArrayList<>());  //Creating a new row to fill in
        GameHandler.relevantData.get(row).add(0, "----------------");
        GameHandler.relevantData.get(row).add(1, "----------");
        GameHandler.relevantData.get(row).add(2, "-------------------");
        row++;
        /**------------------------
         * MENU CONFIGURATION DATA
         * ----------------------*/
        GameHandler.relevantData.add(new ArrayList<>());  //Creating a new row to fill in
        GameHandler.relevantData.get(row).add(0, "Modo de Juego");
        if(gameMode_MainMenu.equals("angles")){
            GameHandler.relevantData.get(row).add(1, "Inicio");
            GameHandler.relevantData.get(row).add(2, "Fin");
        }else {
            GameHandler.relevantData.get(row).add(1, "Sentido");
            GameHandler.relevantData.get(row).add(2, " ");
        }
        row ++;
        GameHandler.relevantData.add(new ArrayList<>());  //Creating a new row to fill in
        if(gameMode_MainMenu.equals("angles")){
            GameHandler.relevantData.get(row).add(0, "Rango Ángulos");
            GameHandler.relevantData.get(row).add(1, String.valueOf(beginningAngle_MainMenu));
            GameHandler.relevantData.get(row).add(2, String.valueOf(endAngle_MainMenu));
        }else {
            GameHandler.relevantData.get(row).add(0, "Giros");
            GameHandler.relevantData.get(row).add(1, rotationMode_MainMenu);
            GameHandler.relevantData.get(row).add(2, " ");
        }
        row++;
        GameHandler.relevantData.add(new ArrayList<>());  //Creating a new row to fill in
        GameHandler.relevantData.get(row).add(0, "----------------");
        GameHandler.relevantData.get(row).add(1, "----------");
        GameHandler.relevantData.get(row).add(2, "-------------------");
        row++;
        /**----------------------
         * HEADERS SAMPLING DATA
         * --------------------*/
        GameHandler.relevantData.add(new ArrayList<>());  //Creating a new row to fill in
        GameHandler.relevantData.get(row).add(0, "Ciclo");
        GameHandler.relevantData.get(row).add(1, "Ángulo");
        GameHandler.relevantData.get(row).add(2, "Alcanzado en");
    }
    @SuppressWarnings("DefaultLocale")
    public void sampledData_CSVFile(ArrayList<ArrayList<String>> arrayList, int row,
                                    int cycle, float[] angleSample, int index, float elapsedTime){
        //To set with important variables the first row (in case that the user tried to write into the CSV file with none samples)
        if(row == 0){
            arrayList.get(row).set(0, String.valueOf(cycle));
            arrayList.get(row).set(1, (int) angleSample[index] + "°");
            arrayList.get(row).set(2, String.format("%.2f",elapsedTime) + " sec");
        }
        //filling the information variable with No.Cycle, No.Sampled angle and the Time to reach the sampled angle
        else {
            arrayList.get(row).add(String.valueOf(cycle));
            arrayList.get(row).add((int) angleSample[index] + "°");
            arrayList.get(row).add(String.format("%.2f",elapsedTime) + " sec");
        }
        arrayList.add(new ArrayList<>());  //Creating a new row to fill in
    }
}

