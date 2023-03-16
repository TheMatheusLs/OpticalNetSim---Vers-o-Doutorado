package Network.Structure;

import java.util.ArrayList;
import java.util.List;

/*
 * Descreve o componente optical switch usado no simulador.
 * 
 * @author Matheus
 */
public class OpticalSwitch {
    
    private int opticalSwitchID;
    private boolean nodeWorking;
    private List<OpticalSwitch> neighborNodes;



    public OpticalSwitch(final int opticalSwitchID, final double atenuationIndB, final double laserPowerIndBm, final double laserOSNRindB) {
        this.opticalSwitchID = opticalSwitchID;

        this.nodeWorking = true;
        this.neighborNodes = new ArrayList<OpticalSwitch>();
    }


    public boolean isEquals(OpticalSwitch right){
        if (this.opticalSwitchID == right.opticalSwitchID){
            return true;
        }
    
        return false;
    }


    public int getOpticalSwitchID() {
        return this.opticalSwitchID;
    }


    public void addNeighborNode(OpticalSwitch node){
        this.neighborNodes.add(node);
    }


    @Override
    public String toString() {
        return "OpticalSwitch ID:" + this.opticalSwitchID;
    }

    public boolean isNodeWorking() {
        return this.nodeWorking;
    }
}
