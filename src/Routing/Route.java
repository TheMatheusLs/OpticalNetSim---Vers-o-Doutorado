package Routing;

import java.util.ArrayList;
import java.util.List;

import Config.ParametersSimulation;
import GeneralClasses.AuxiliaryFunctions;
import Network.TopologyManager;
import Network.Structure.OpticalLink;
import Types.ModulationLevelType;
import Types.GeneralTypes.CallRequestType;
import Types.GeneralTypes.PhysicalLayerOption;

public class Route {

    /**
     * Lista de inteiros com os ID dos nós das redes
     */
    private List<Integer> path;
    /**
     * ID do nó de origem da rota
     */
    private int originNone;
    /**
     * ID do nó de destino da rota
     */
    private int destinationNone;
    /**
     * Conjuntos de Links que ligam a origem ao destino;
     */
    private List<OpticalLink> upLink;
    /**
     * Conjuntos de Links que ligam o destino a origem;
     */
    private List<OpticalLink> downLink;
    /**
     * Lista com todas os posíveis tamanhos de requisição para cada valor de bitrate para a maior modulação possível nessa rota
     */
    private int[] allReqSizes;
    /**
     * Lista com todas os posíveis valores de bitrate
     */
    private int[] allBitrates;
    /**
     * Custo da Rota. A soma dos custos dos links conforme a métrica selecionada
     */
    private double cost;
    /**
     * Índice que a rota foi encontrada pelo algoritmo YEN
     */
    private int kFindIndex;
    /**
     * Lista que armazena a ocupação dos slots em todos os links da rota.
     */
	private short[] slotOcupationLink;
    /**
     * Representa o valor para a quantidade de slots ocupados nos links da rota.
     */
    private int numberOfSlotOcupation;
    /**
     * Lista que armazena o conjunto de rotas interferentes a rota principal
     */
    private List<Route> allConflictRoutes;

    /**
     * Construtor da rota
     * 
     * @param path Lista com ID dos nós da rede
     * @param topology Classe que armazena a topologia da rede
     */
    public Route(List<Integer> path, TopologyManager topology) {

        //Quantidade de nós no caminho da rede
        final int pathSize = path.size();

        this.path = path;
        this.originNone = path.get(0);
        this.destinationNone = path.get(pathSize - 1);

        // Cria o uplink e downlink
        this.upLink = new ArrayList<OpticalLink>();
        this.downLink = new ArrayList<OpticalLink>();
        
        this.allConflictRoutes = new ArrayList<Route>();
        this.slotOcupationLink = new short[ParametersSimulation.getNumberOfSlotsPerLink()];
        this.numberOfSlotOcupation = 0;

        for (int iPath = 1; iPath < pathSize; iPath++){
            upLink.add(topology.getLink(this.path.get(iPath - 1), this.path.get(iPath)));
        }
        
        final CallRequestType callRequestType = ParametersSimulation.getCallRequestType();

        if(callRequestType.equals(CallRequestType.Bidirectional)){
            for(int iPath = (pathSize - 1); iPath > 0; iPath--){
                downLink.add(topology.getLink(this.path.get(iPath), this.path.get(iPath - 1)));
            }
        }

        this.allBitrates = ParametersSimulation.getTrafficOption();
        this.allReqSizes = this.findSizeReqForModulationAndBitrate();

        this.setCost(topology);
        this.kFindIndex = -1;
    }

    /**
     * Configura o índice k encontrado pelo YEN
     * 
     * @param kFindIndex Índice da rota na ordem do YEN
     */
    public void setKFindIndex(int kFindIndex) {
        this.kFindIndex = kFindIndex;
    }

    /**
     * Método para encontrar o número de enlaces da rede
     * 
     * @return Número de enlaces da rede
     */
    private int getNumHops() {
        return this.path.size() - 1;
    }

    /**
     * Calcula e configura o custo da rede com base nos links que a compõem
     * 
     * @param topology Classe que armazena a configuração da rede
     */
    private void setCost(TopologyManager topology) {
        OpticalLink link;
        double cost = 0.0;
        
        for(int a = 0; a < this.getNumHops(); a++){
            link = topology.getLink(this.path.get(a), this.path.get(a + 1));
            cost += link.getCost();
        }
        
        this.setCost(cost);
    }

    private void setCost(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return this.cost;
    }

    /**
     * Calcula o tamanho da requisição que será usada para cada modulação e cada bitrate
     * 
     * @return Retorna uma lista com os tamanhos necessários para a requisição
     */
    private int[] findSizeReqForModulationAndBitrate() {
        
        ModulationLevelType[] allModulationLevels = ParametersSimulation.getMudulationLevelType();
        PhysicalLayerOption physicalLayerOption = ParametersSimulation.getPhysicalLayerOption();

        int numberOfBitrates = this.allBitrates.length;   

        int[] allReqSizesAux = new int[numberOfBitrates];

        //Percorre todos os bitrates
        FOR_BITRATE:for (int indexBitrate = 0; indexBitrate < numberOfBitrates; indexBitrate++){
            //Percorre todas modulações
            for (ModulationLevelType mLevelType: allModulationLevels){

                int bitRate = this.allBitrates[indexBitrate];

                if (physicalLayerOption.equals(PhysicalLayerOption.Disabled)){
                    allReqSizesAux[indexBitrate] = AuxiliaryFunctions.getNumberSlots(mLevelType, bitRate);
                    continue FOR_BITRATE;
                } else {

                }
            }
        }

        return allReqSizesAux;
    }


    /**
     * Método para recuperar o tamanho da requisição com base no bitrare selecionado
     * 
     * @param bitrate Valor do bitrate
     * @return Tamanho da requisição necessária
     */
    public int getReqSize(int bitrate) {

        int index = 0;
        for (;index < this.allBitrates.length; index++) {
            if (this.allBitrates[index] == bitrate) {
                break;
            }
        }

        return this.allReqSizes[index];
    }

    public List<OpticalLink> getUpLink() {
        return upLink;
    }

    public void setUpLink(List<OpticalLink> upLink) {
        this.upLink = upLink;
    }

    public List<OpticalLink> getDownLink() {
        return downLink;
    }

    public void setDownLink(List<OpticalLink> downLink) {
        this.downLink = downLink;
    }

    @Override
    public String toString() {
        return "Route [Source = " + originNone + " Destination = " + destinationNone +   " path = " + path + "]\n";
    }

    public boolean isQoT() {
        return true; //TODO: Equação do QoT
    }

    public List<Integer> getPath(){
        return this.path;
    }

    public int getNodeID(int index) {
        return this.path.get(index);
    }

    public int getNumNodes() {
        return this.path.size();
    }

    public short getSlotValue(int index){
		return this.slotOcupationLink[index];
	}

    public boolean isSlotAvailable(int indexSlot) {

        for (OpticalLink opticalLink: this.upLink){
            if (!opticalLink.isAvailableSlotAt(indexSlot)){
                return false;
            }
        }

        return true;
    }

    /**
     * Incrementa a ocupação dos slots requisição. Somente funciona se o conjunto de rotas interferentes for gerado 
     * 
     * @param fSlotsIndex Conjunto de slots a ser alocado
     */
    public void incrementSlotsOcupy(List<Integer> fSlotsIndex) {

        for (int s: fSlotsIndex){
			this.incrementSlots(s);

			for (Route route : this.allConflictRoutes){
				route.incrementSlots(s);
			}
		}
    }

    /**
     * Incrementa a ocupação do slot. Somente funciona se o conjunto de rotas interferentes for gerado
     * 
     * @param slot Slot a ser alocadado
     */
    private void incrementSlots(int slot) {
        if (this.slotOcupationLink[slot] == 0){
            this.numberOfSlotOcupation++;
        }
		this.slotOcupationLink[slot]++;
    }

    /**
     * Decrementa a ocupação dos slots requisição. Somente funciona se o conjunto de rotas interferentes for gerado 
     * 
     * @param fSlotsIndex Conjunto de slots a ser alocado
     */
    public void decreasesSlotsOcupy(List<Integer> fSlotsIndex){

		for (int s: fSlotsIndex){
			this.decreasesSlots(s);

			for (Route route : this.allConflictRoutes){
				route.decreasesSlots(s);
			}
		}
	}

    /**
     * Decrementa a ocupação do slot. Somente funciona se o conjunto de rotas interferentes for gerado
     * 
     * @param slot Slot a ser alocadado
     */
    private void decreasesSlots(int slot){
        this.slotOcupationLink[slot]--;
        if (this.slotOcupationLink[slot] == 0){
            this.numberOfSlotOcupation--;
        }
	}

}
