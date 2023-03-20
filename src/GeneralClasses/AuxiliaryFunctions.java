package GeneralClasses;

import Config.ConfigSimulator;
import Config.ParametersSimulation;
import Network.Structure.OpticalSwitch;
import Types.ModulationLevelType;

public class AuxiliaryFunctions {
    

    public static int getNumberSlots(ModulationLevelType modulation, int bitRate) {

        double bandwidth = bandwidthQAM(modulation, bitRate * 1e9);
        
        int numSlots = (int) Math.ceil(bandwidth / ConfigSimulator.getSpacing());
        numSlots += ParametersSimulation.getGuardBandSize();

        return numSlots;
    }

    private static double bandwidthQAM(ModulationLevelType M, double Rbps) { 
		double value = Math.log10(M.getConstelation()) / Math.log10(2);

        return ((1.0 + 0.0) * Rbps) / (ParametersSimulation.getNumberofPolarizations() * value);
    }

    /**
     * Retorna o OpticalSwitch que corresponde ao ID
     * 
     * @param sourceNodeID
     * @param listOfNodes
     * @return
     */
    public static OpticalSwitch getNodeByID(int sourceNodeID, OpticalSwitch[] listOfNodes) {

        OpticalSwitch optSwi = null;

        for (OpticalSwitch optSwitchAux : listOfNodes ) {
            int nodeID = optSwitchAux.getOpticalSwitchID();
            if (nodeID == sourceNodeID) {
                optSwi = optSwitchAux;
                break;
            }
        }

        return optSwi;
    }
}
