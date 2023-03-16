package Manager;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import Config.ParametersSimulation;

public class FolderManager {
    
    private String folderName;
    private String folderPath;
    private boolean status;


    /**
     * Classe para gerenciar o acesso a pasta de relatórios
     * 
     * @param tagName
     * @throws Exception
     */
    public FolderManager(String tagName) throws Exception {

        // Data e hora do momento do início da simulação
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();
        String dateTime = sdf.format(date);

        this.folderName = dateTime + "_" + ParametersSimulation.getTopologyType() + "_" + "AlgRoute" + "_" + "AlgSpectrum" + "_" + tagName;

        this.folderPath = ParametersSimulation.getPathToSaveResults() + this.folderName;

        this.status = new java.io.File(this.folderPath).mkdirs();

        if (this.status){
            System.out.println("Pasta criada com o nome: " + this.folderName);  
        }
        else{
            throw new Exception("ERRO: Pasta não foi criada");
        }

        this.writeParameters();
    }


    /**
     * Método para salvar o conteúdo do arquivo.
     * 
     * @param fileName
     * @param content
     */
    private void writeFile(String fileName, String content) {
        try {
            File file = new File(this.folderPath + "/" + fileName);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para salvar o estado final da simulação, junto com o tempo de execução completo.
     * 
     * @param totalTime Tempo de execução
     */
    public void writeDone(double totalTime) {
        try {

            String content = "Simulação finalizada com sucesso!\n" +
            "Tempo total de execução: " + totalTime + " milissegundos\n";

            this.writeFile("done.txt", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Salva os paramétros dessa simulação
     */
    private void writeParameters() {
        try {
            this.writeFile("Parameters.txt", ParametersSimulation.save());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva a topologia dessa simulação
     * 
     * @param content
     */
    public void writeTopology(String content) {
        try {
            this.writeFile("Topology.txt", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva as rotas dessa simulação
     * 
     * @param content
     */
    public void writeRoutes(String content) {
        try {
            this.writeFile("Routes.txt", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva os resultados dessa simulação
     * 
     * @param content
     */
    public void writeResults(String content) {
        try {

            //TODO


            this.writeFile("Results.txt", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isStatus() {
        return status;
    }


    public String getFolderName() {
        return folderName;
    }


    public String getFolderPath() {
        return folderPath;
    }
}
