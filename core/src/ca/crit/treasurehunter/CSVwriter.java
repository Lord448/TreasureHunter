package ca.crit.treasurehunter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class CSVwriter {
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void writeCSV(String fileName) {
        // Obtén el directorio externo de almacenamiento específico de la aplicación
        String externalStoragePath = Gdx.files.getExternalStoragePath();

        // Combina el directorio externo con el nombre del archivo
        filePath = externalStoragePath + fileName;
        System.out.println("se guardo en: "+filePath);

        // Crea un FileHandle con la ruta completa
        FileHandle file = Gdx.files.absolute(filePath);

        try {
            file.writeString("Hola, Mundo", false);
            Gdx.app.log("Escritura de CSV", "Archivo escrito exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.error("Escritura de CSV", "Error al escribir en el archivo");
        }

    }

    /*
    StringBuilder csvBuilder = new StringBuilder();

        for (String[] row : data) {
            for (String value : row) {
                csvBuilder.append(value).append(",");
            }
            csvBuilder.setLength(csvBuilder.length() - 1); // Remove the trailing comma
            csvBuilder.append("\n");
        }
    */
}

