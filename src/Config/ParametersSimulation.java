package Config;

import Types.ModulationLevelType;
import Types.GeneralTypes.CallRequestType;
import Types.GeneralTypes.KSortedRoutesByType;
import Types.GeneralTypes.LinkCostType;
import Types.GeneralTypes.PhysicalLayerOption;
import Types.GeneralTypes.RandomGenerationType;
import Types.GeneralTypes.RoutingAlgorithmType;
import Types.GeneralTypes.SpectralAllocationAlgorithmType;
import Types.GeneralTypes.StopCriteriaType;
import Types.GeneralTypes.TopologyType;

/*
 * Classe para armazenar os parametros da simulação. Cada nova simulação segue os parametros informados aqui.
 */
public class ParametersSimulation{

    // Semente aleátoria principal
    final static int mainSeed = 42; 

    // Configura o número e as cargas para simulação
    final static double minLoadNetwork = 260;
    final static double maxLoadNetwork = 260;
    final static int numberOfPointsLoadNetwork = 1;
    final static int numberOfSimulationsPerLoadNetwork = 1;
    
    // Seleciona a topologia da rede
    final static TopologyType topologyType = TopologyType.NSFNet;
    final static int numberOfSlotsPerLink = 128;
    final static long maxNumberOfRequisitions = (long) 1e6;
    final static int maxNumberOfBlockedRequests = 1000;
    
    // Algoritmo de Roteamento
    final static RoutingAlgorithmType routingAlgorithmType = RoutingAlgorithmType.Dijstra;
    final static int kShortestRoutes = 1;
    final static KSortedRoutesByType kSortedRoutesByType = KSortedRoutesByType.None;

    
    // Algoritmo de alocação do espectro
    final static SpectralAllocationAlgorithmType spectralAllocationAlgorithmType = SpectralAllocationAlgorithmType.FirstFit;
    /* Restrições:
     * O First Fit só pode ser usado quanto o algoritmo de roteamento for: YEN ou Djisktra
     * O MSCL só pode ser usado quanto o algoritmo de roteamento for: MSCL Sequencial ou MSCL Combinado
     */
    
    // Configurações Gerais
    final static PhysicalLayerOption physicalLayerOption = PhysicalLayerOption.Disabled;
    final static int[] trafficOption = new int[]{100, 200, 400};
    final static LinkCostType linkCostType = LinkCostType.Hops;
    final static CallRequestType callRequestType = CallRequestType.Unidirectional;
    final static int numberOfPolarizations = 2; // Número de polarização
    final static int guardBandSize = 0; 
    
    final static RandomGenerationType randomGeneration = RandomGenerationType.PseudoRandomGeneration;
    final static StopCriteriaType stopCriteria = StopCriteriaType.TotalCallRequest;


    final static ModulationLevelType[] mudulationLevelType = { //Formato de modulação, DEVE SER ORDENADO DO DE MAIOR CONSTELAÇÃO
		// ModulationLevelType.SIXTYFOUR_QAM,
		// ModulationLevelType.THIRTYTWO_QAM,
		// ModulationLevelType.SIXTEEN_QAM,
        ModulationLevelType.EIGHT_QAM,
		// ModulationLevelType.FOUR_QAM,
    };

    // Pasta onde serão armazenadas os relatórios da simulação. Em caso da utilização no sistema linux é necessário aterar a classe CreateFolder.java
    final static String pathToSaveResults = "D:\\ProgrammingFiles\\ReportsOpticalNetSim\\"; 

    public static double getMinLoadNetwork() {
        return minLoadNetwork;
    }

    public static double getMaxLoadNetwork() {
        return maxLoadNetwork;
    }

    public static int getMainSeed() {
        return mainSeed;
    }

    public static int getNumberOfPointSloadNetwork() {
        return numberOfPointsLoadNetwork;
    }

    public static int getNumberOfSimulationsPerLoadNetwork() {
        return numberOfSimulationsPerLoadNetwork;
    }

    public static int getNumberOfSlotsPerLink() {
        return numberOfSlotsPerLink;
    }

    public static long getMaxNumberOfRequisitions() {
        return maxNumberOfRequisitions;
    }

    public static int getMaxNumberOfBlockedRequests() {
        return maxNumberOfBlockedRequests;
    }

    public static int getKShortestRoutes() {
        return kShortestRoutes;
    }

    public static int getGuardBandSize() {
        return guardBandSize;
    }

    public static TopologyType getTopologyType() {
        return topologyType;
    }

    public static RoutingAlgorithmType getRoutingAlgorithmType() {
        return routingAlgorithmType;
    }

    public static SpectralAllocationAlgorithmType getSpectralAllocationAlgorithmType() {
        return spectralAllocationAlgorithmType;
    }

    public static LinkCostType getLinkCostType() {
        return linkCostType;
    }

    public static int[] getTrafficOption() {
        return trafficOption;
    }

    public static int getNumberofPolarizations() {
        return numberOfPolarizations;
    }

    public static RandomGenerationType getRandomGeneration() {
        return randomGeneration;
    }

    public static String getPathToSaveResults() {
        return pathToSaveResults;
    }

    public static CallRequestType getCallRequestType() {
        return callRequestType;
    }

    public static KSortedRoutesByType getKSortedRoutesByType() {
        return kSortedRoutesByType; //TODO: Colocar no to string
    }

    public static ModulationLevelType[] getMudulationLevelType() {
        return mudulationLevelType; //TODO: Colocar no to string
    }

    public static PhysicalLayerOption getPhysicalLayerOption() {
        return physicalLayerOption; //TODO: Colocar no to string
    }

    public static StopCriteriaType getStopCriteriaType() {
        return stopCriteria; //TODO: Colocar no to string
    }

    public static String save() {

        String txt = "\t*** Parameters *** \n";

        txt += String.format("minLoadNetwork = %f\n", minLoadNetwork);
        txt += String.format("maxLoadNetwork = %f\n", maxLoadNetwork);
        txt += String.format("numberOfPointsLoadNetwork = %d\n", numberOfPointsLoadNetwork);
        txt += String.format("numberOfSimulationsPerLoadNetwork = %d\n", numberOfSimulationsPerLoadNetwork);

        txt += String.format("numberOfSlotsPerLink = %d\n", numberOfSlotsPerLink);

        txt += String.format("maxNumberOfRequisitions = %d\n", maxNumberOfRequisitions);
        txt += String.format("maxNumberOfBlockedRequests = %d\n", maxNumberOfBlockedRequests);

        txt += String.format("kShortestRoutes = %d\n", kShortestRoutes);

        txt += String.format("numberOfPolarizations = %d\n", numberOfPolarizations);
        txt += String.format("guardBandSize = %d\n", guardBandSize);
        
        txt += String.format("mainSeed = %d\n", mainSeed);

        txt += String.format("topologyType = %s\n", topologyType.name());
        txt += String.format("routingAlgorithmType = %s\n", routingAlgorithmType.name());
        txt += String.format("spectralAllocationAlgorithmType = %s\n", spectralAllocationAlgorithmType.name());
        txt += String.format("linkCostType = %s\n", linkCostType.name());

        txt += String.format("stopCriteria = %s\n", stopCriteria.name());
        txt += String.format("randomGeneration = %s\n", randomGeneration.name());

        txt += String.format("callRequestType = %s\n", callRequestType.name());

        txt += "trafficOption = ";
        for (int t = 0; t < trafficOption.length - 1; t++){
            txt += String.format("%d, ", trafficOption[t]);
        }
        txt += String.format("%d", trafficOption[trafficOption.length-1]);

        return txt;
    }
}