package Routing;

import java.util.ArrayList;
import java.util.List;

import Config.ParametersSimulation;
import GeneralClasses.AuxiliaryFunctions;
import Network.Structure.OpticalLink;
import Types.ModulationLevelType;
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

    public Route(List<Integer> path, int originNone, int destinationNone, List<OpticalLink> upLink, List<OpticalLink> downLink) {
        this.path = path;
        this.originNone = originNone;
        this.destinationNone = destinationNone;
        this.upLink = upLink;
        this.downLink = downLink;

        this.allBitrates = ParametersSimulation.getTrafficOption();
        this.allReqSizes = this.findSizeReqForModulationAndBitrate();
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
}
