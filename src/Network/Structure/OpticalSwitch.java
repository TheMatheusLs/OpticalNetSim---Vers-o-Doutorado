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
    private double laserPower;
    /**
	 * Valor de atenuação (linear) do optical switch.			
	 */	
	private transient double switchAtenuation;


    public OpticalSwitch(final int opticalSwitchID, final double atenuationIndB, final double laserPowerIndBm, final double laserOSNRindB) {
        this.opticalSwitchID = opticalSwitchID;

        this.switchAtenuation = Math.pow(10,atenuationIndB / 10);
        this.laserPower = Math.pow(10, laserPowerIndBm / 10) * Math.pow(10, atenuationIndB / 10);

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

    public double getLaserPower(){
    	return this.laserPower;
    }

    /**
     * M�todo para retornar a atenua��o do optical switch.
     * @return O atributo switchAtenuation
     * @author Andr� 			
     */
    public double getSwitchAtenuation(){
    	return this.switchAtenuation;
    }
}
