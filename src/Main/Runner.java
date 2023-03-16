package Main;

import Manager.FolderManager;

public class Runner {

    public static void main(String[] args) throws Exception {
        
        // Calcula o tempo inicial da simulação
        final long geralInitTime = System.currentTimeMillis(); 

        //Importa as configurações do simulador: TODO implement

        //Importa as configurações da simulação: TODO implement
        // TODO: Verificar se não hå nenhum conflito de configuração

        // Cria a pasta para armazenar os resultados e as configurações da simulação
        FolderManager folderManager = new FolderManager("CREATE");

        // Cria a simulação
        Simulation simulation = new Simulation(folderManager);


        // Calcula o tempo final da simulação
        final long geralEndTime = System.currentTimeMillis();

        // Calcula o tempo total da simulação
        final long geralTotalTime = geralEndTime - geralInitTime;

        System.out.println("Simulação finalizada com o tempo de " + geralTotalTime + " ms!");
    }
}
