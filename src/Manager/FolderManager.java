package Manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        
        File file = new File(this.folderPath + "/" + fileName);
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Método para salvar o estado final da simulação, junto com o tempo de execução completo.
     * 
     * @param totalTime Tempo de execução
     */
    public void writeDone(double totalTime) {

        String content = "Simulação finalizada com sucesso!\n" +
        "Tempo total de execução: " + totalTime + " milissegundos\n";

        this.writeFile("done.txt", content);
    }

    /**
     *  Salva os paramétros dessa simulação
     */
    private void writeParameters() {
        this.writeFile("Parameters.txt", ParametersSimulation.save());
    }

    /**
     * Salva a topologia dessa simulação
     * 
     * @param content
     */
    public void writeTopology(String content) {
        this.writeFile("Topology.txt", content);
    }

    /**
     * Salva as rotas dessa simulação
     * 
     * @param content
     */
    public void writeRoutes(String content) {
        this.writeFile("Routes.txt", content); 
    }

    /**
     * Salva os resultados dessa simulação
     * 
     * @param content
     */
    public void writeResults(String content) {
        this.writeFile("Results.txt", content);
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
