package Main;

import java.util.Random;

import Config.ParametersSimulation;
import Manager.FolderManager;
import Network.TopologyManager;
import Routing.RoutesManager;
import Types.GeneralTypes.RandomGenerationType;

/**
 * Classe que representa a simulação de uma rede óptica
 * 
 * @author Matheus
 */
public class Simulation {
    
    private FolderManager folderManager;
    private RoutesManager routesManager;
    private int[] seedsForLoad;
    private long currentRandomSeed;

    private Random randomGeneration;

    private TopologyManager topology;

    public Simulation(FolderManager folderManager) {
        this.folderManager = folderManager;
        this.seedsForLoad = this.generateRandomSeeds();

        this.inicialize();
    }

    public void inicialize() {

        // Cria uma nova instância da topologia
        this.topology = new TopologyManager();
        this.topology.save(this.folderManager);

        // Cria uma nova instância de routing
        this.routesManager = new RoutesManager(this.topology);
        this.routesManager.save(folderManager);

    }

    private int[] generateRandomSeeds() {
        
        int[] auxSeeds = new int[ParametersSimulation.getNumberOfSimulationsPerLoadNetwork()];

        if (ParametersSimulation.getRandomGeneration().equals(RandomGenerationType.PseudoRandomGeneration)){
            Random randomAux = new Random(ParametersSimulation.getMainSeed());

            for (int nSim = 0; nSim < ParametersSimulation.getNumberOfSimulationsPerLoadNetwork(); nSim++){
                auxSeeds[nSim] = randomAux.nextInt(Integer.MAX_VALUE);
            }

            this.randomGeneration = new Random(ParametersSimulation.getMainSeed());
        } else {
            if (ParametersSimulation.getRandomGeneration().equals(RandomGenerationType.SameRequestForAllPoints)){

                Random randomAux = new Random(ParametersSimulation.getMainSeed());
    
                int seedFix = randomAux.nextInt(Integer.MAX_VALUE);

                for (int nSim = 0; nSim < ParametersSimulation.getNumberOfSimulationsPerLoadNetwork(); nSim++){
                    auxSeeds[nSim] = seedFix;
                }

                this.randomGeneration = new Random(ParametersSimulation.getMainSeed());
            } else {
                if (ParametersSimulation.getRandomGeneration().equals(RandomGenerationType.RandomGeneration)){
                    Random randomAux = new Random();

                    this.randomGeneration = new Random(randomAux.nextInt(Integer.MAX_VALUE));

                    for (int nSim = 0; nSim < ParametersSimulation.getNumberOfSimulationsPerLoadNetwork(); nSim++){
                        auxSeeds[nSim] = randomAux.nextInt(Integer.MAX_VALUE);
                    }
                }
            }
        }

        return auxSeeds;
    }

    
}
