package ca.crit.treasurehunter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class CSVWriter {

    /**METHODS*/
    public void writeCSV(String fileName) {
        String externalStoragePath = Gdx.files.getExternalStoragePath(); // Getting the specific external saving file path from the app

        String filePath = externalStoragePath + fileName;  // Combining the extern saving file path with the wished file name
        GameHandler.savedFilePath = filePath;
        System.out.println("Archivo CSV descargado en: "+ GameHandler.savedFilePath);

        FileHandle file = Gdx.files.absolute(filePath); // Creating a fileHandle with the complete saving file path

        try {
            /**----------------------------------------------
             *      SAVING THE SCREEN'S DATA TEXT
             *----------------------------------------------*/
            StringBuilder csvScreenData = new StringBuilder();
            for (String[] row : GameHandler.headerTextData) {
                for (String value : row) {
                    csvScreenData.append(value).append(",");
                }
                csvScreenData.setLength(csvScreenData.length() - 1); // Remove the trailing comma
                csvScreenData.append("\n");
            }

            /**-------------------------------------------------
             * SAVING THE TAKEN ANGLES SAMPLES DURING THE GAME
             * -----------------------------------------------*/
            StringBuilder csvResumeData = new StringBuilder();
            for(ArrayList<String> row : GameHandler.resumeData){
                for (String value : row){
                    csvResumeData.append(value).append(",");
                }
                csvResumeData.setLength(csvResumeData.length()-1);  // Remove the trailing comma
                csvResumeData.append("\n");
            }

            /**WRITING THE CSV FILE*/
            file.writeString(csvScreenData.toString(), false);
            file.writeString(csvResumeData.toString(), true);
            Gdx.app.log("Escritura de CSV", "Archivo escrito exitosamente");

        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.error("Escritura de CSV", "Error al escribir en el archivo");
        }
    }
}

