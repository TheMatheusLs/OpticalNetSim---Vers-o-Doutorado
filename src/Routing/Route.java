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
    private List<Integer> path;
    private int originNone;
    private int destinationNone;

    /**
     * Conjuntos de Links que ligam a origem ao destino;
     */
    private List<OpticalLink> upLink;
    /**
     * Conjuntos de Links que ligam o destino a origem;
     */
    private List<OpticalLink> downLink;

    private int[] allReqSizes;
    private int[] allBitrates;

    private double cost;

    private int kFindIndex;

    /**
     * Lista que armazena a ocupação dos slots em todos os links da rota.
     */
	private short[] slotOcupationLink;
    private int numberOfSlotOcupation;
    private List<Route> allConflictRoutes;


    public Route(List<Integer> path, TopologyManager topology) {

        final int pathSize = path.size();

        this.path = path;
        this.originNone = path.get(0);;
        this.destinationNone = path.get(pathSize - 1);

        // Cria o up e downlink
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

    public void setKFindIndex(int kFindIndex) {
        this.kFindIndex = kFindIndex;
    }

    private int getNumHops() {
        return this.path.size() - 1;
    }

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
     */
    private int[] findSizeReqForModulationAndBitrate() {
        
        ModulationLevelType[] allModulationLevels = ParametersSimulation.getMudulationLevelType();
        PhysicalLayerOption physicalLayerOption = ParametersSimulation.getPhysicalLayerOption();

        int numberOfBitrates = allBitrates.length;   

        int[] allReqSizesAux = new int[numberOfBitrates];

        //Percorre todos os bitrates
        FOR_BITRATE:for (int indexBitrate = 0; indexBitrate < numberOfBitrates; indexBitrate++){
            //Percorre todas modulações
            for (ModulationLevelType mLevelType: allModulationLevels){

                int bitRate = allBitrates[indexBitrate];

                if (physicalLayerOption.equals(PhysicalLayerOption.Disabled)){
                    allReqSizesAux[indexBitrate] = AuxiliaryFunctions.getNumberSlots(mLevelType, bitRate);
                    continue FOR_BITRATE;
                } else {

                }
            }
        }

        return allReqSizesAux;
    }


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
        return true; //TODO Equação do QoT
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

    public void incrementSlotsOcupy(List<Integer> fSlotsIndex) {

        for (int s: fSlotsIndex){
			this.incrementSlots(s);

			for (Route route : this.allConflictRoutes){
				route.incrementSlots(s);
			}
		}
    }

    private void incrementSlots(int slot) {
        if (this.slotOcupationLink[slot] == 0){
            this.numberOfSlotOcupation++;
        }
		this.slotOcupationLink[slot]++;
    }


    public void decreasesSlotsOcupy(List<Integer> fSlotsIndex){

		for (int s: fSlotsIndex){
			this.decreasesSlots(s);

			for (Route route : this.allConflictRoutes){
				route.decreasesSlots(s);
			}
		}
	}

    private void decreasesSlots(int slot){
        this.slotOcupationLink[slot]--;
        if (this.slotOcupationLink[slot] == 0){
            this.numberOfSlotOcupation--;
        }
	}

}
