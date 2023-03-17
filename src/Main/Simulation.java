package Main;

import java.util.Random;

import CallRequests.CallRequest;
import CallRequests.CallRequestManager;
import Config.ConfigSimulator;
import Config.ParametersSimulation;
import GeneralClasses.ProbabilityFunctions;
import Manager.FolderManager;
import Manager.SimulationResults;
import Network.TopologyManager;
import Routing.RoutesManager;
import Types.GeneralTypes.CallRequestType;
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

    public void runMultiLoad() {

        final int numberOfLoadNetworkPoints = ParametersSimulation.getNumberOfPointSloadNetwork();
        final double maxLoadNetwork = ParametersSimulation.getMaxLoadNetwork();
        double step = 0; 
        
        if (numberOfLoadNetworkPoints >= 2){
            step = (maxLoadNetwork - ParametersSimulation.getMinLoadNetwork()) / (numberOfLoadNetworkPoints - 1);
        }

        // Loop para cada ponto de simulação da rede
        for (int loadPoint = 0; loadPoint < numberOfLoadNetworkPoints; loadPoint++) {

            // Calcula o carga atual para a simulação da rede
            double networkLoad = (maxLoadNetwork - (step * loadPoint)) / ConfigSimulator.getMeanRateOfCallsDuration();

            System.out.println("Simulando carga de " + networkLoad + " Erlangs");

            // Loop para múltiplos pontos em uma mesma carga da rede. Retira a média entre diferentes seeds
            for (int nSim = 1; nSim <= ParametersSimulation.getNumberOfSimulationsPerLoadNetwork(); nSim++){
            
                this.currentRandomSeed = seedsForLoad[nSim-1];

                this.randomGeneration = new Random(this.currentRandomSeed);

                System.out.println("Simulação nº: " + nSim + " com seed = " + this.currentRandomSeed);

                SimulationResults simulationResults = new SimulationResults(networkLoad, nSim, this.currentRandomSeed);

                // Executa uma simulação e retorna a classe de resultados
                simulationResults = this.runSingleLoad(networkLoad, simulationResults);

                // Escreve na tela os resultados
                System.out.println(simulationResults);

                // Salva os resultados
                this.folderManager.writeResults(simulationResults.toString());
            }
        }
    }

    private SimulationResults runSingleLoad(double networkLoad, SimulationResults simulationResults) {
        final long geralInitTime = System.currentTimeMillis();

        final double meanRateCallDur = ConfigSimulator.getMeanRateOfCallsDuration();
        final long numberMaxOfRequisitions = ParametersSimulation.getMaxNumberOfRequisitions();
        final CallRequestType callRequestType = ParametersSimulation.getCallRequestType();
        final int[] possibleBitRates = ParametersSimulation.getTrafficOption();

        int source, destination;
        double timeSim = 0.0;
        boolean hasSlots, hasQoT;

        final CallRequestManager listOfCalls = new CallRequestManager();

        // Loop para cada requisição simulada
        LOOP_REQ : for(int iReq = 1; iReq <= numberMaxOfRequisitions; iReq++){

            // Apresenta o progresso para a simulação
            if ((iReq % 10000) == 0){
                System.out.print(".");
            }

            // Informa que não houve bloqueio por slots ou QoT
            hasSlots = true; 
            hasQoT = true;

            do{ 
                source = (int) Math.floor(randomGeneration.nextDouble() * this.topology.getNumberOfNodes());		//TODO: Após terminar se testa o simulador, colocar essa linha fora do loop do		
                destination = (int) Math.floor(randomGeneration.nextDouble() * this.topology.getNumberOfNodes());				
            } while(source == destination);

            // Remove as requisição espiradas
            listOfCalls.removeCallRequest(timeSim);

            timeSim = ProbabilityFunctions.exponentialDistribution(networkLoad, this.randomGeneration);

            final CallRequest callRequest = new CallRequest(iReq, source, destination, callRequestType, possibleBitRates, timeSim, meanRateCallDur, this.randomGeneration);

        } // End LOOP_REQ
        
        System.out.println("\n\n");

        // Calcula o tempo final de uma simulação
        final long geralEndTime = System.currentTimeMillis();

        final long geralTotalTime = geralEndTime - geralInitTime;
        simulationResults.setExecutionTime(geralTotalTime);

        return simulationResults;
    }

    
}
