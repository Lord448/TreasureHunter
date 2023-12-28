package ca.crit.treasurehunter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class CSVwriter {
    private String filePath;
    private Sampling sampling;

    /**METHODS*/
    public void writeCSV(String fileName) {
        String externalStoragePath = Gdx.files.getExternalStoragePath(); // Obtén el directorio externo de almacenamiento específico de la aplicación

        filePath = externalStoragePath + fileName;  // Combina el directorio externo con el nombre del archivo
        System.out.println("se guardo en: "+filePath);

        FileHandle file = Gdx.files.absolute(filePath); // Crea un FileHandle con la ruta completa

        try {
            StringBuilder csvScreenData = new StringBuilder();
            for (String[] row : GameHandler.headerTextData) {
                for (String value : row) {
                    csvScreenData.append(value).append(",");
                }
                csvScreenData.setLength(csvScreenData.length() - 1); // Remove the trailing comma
                csvScreenData.append("\n");
            }
            file.writeString(csvScreenData.toString(), false);

            StringBuilder csvResumeData = new StringBuilder();
            for(ArrayList<String> row : GameHandler.resumeData){
                for (String value : row){
                    csvResumeData.append(value).append(",");
                }
                csvResumeData.setLength(csvResumeData.length()-1);
                csvResumeData.append("\n");
            }
            file.writeString(csvResumeData.toString(), true);
            Gdx.app.log("Escritura de CSV", "Archivo escrito exitosamente");

        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.error("Escritura de CSV", "Error al escribir en el archivo");
        }
    }
    /**Getters*/
    public String getFilePath() {
        return filePath;
    }
}

