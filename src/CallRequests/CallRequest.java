package CallRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import GeneralClasses.AuxiliaryFunctions;
import GeneralClasses.ProbabilityFunctions;
import Network.Structure.OpticalLink;
import Network.Structure.OpticalSwitch;
import Routing.Route;
import Types.GeneralTypes.CallRequestType;

/**
 * Classe que representa uma requisição da rede.
 * 
 * @author Matheus
 */
public class CallRequest {

    private int callRequestID;
    private double decayTime;
    private double duration;
    private Route route;
    private List<Integer> frequencySlots;
    private final transient List<Integer> possibleBitRates;
    private int selectedBitRate;
    private CallRequestType callRequestType;
    private int sourceNodeID;
    private int destinationNodeID;
    private int reqNumbOfSlots;

    public CallRequest(final int callRequestId, final int sourceNodeID, final int destinationNodeID, final CallRequestType callRequestType, final int[] possibleBitRates, final double time, final double meanDurationRate, Random randomGeneration){
        this.callRequestID = callRequestId;
        this.sourceNodeID = sourceNodeID;
        this.destinationNodeID = destinationNodeID;
        this.callRequestType = callRequestType; 	
        
        this.possibleBitRates = Arrays.stream(possibleBitRates).boxed().collect(Collectors.toList());
        
        this.frequencySlots = new ArrayList<Integer>();
        this.route = null;

        this.setTime(time, meanDurationRate, randomGeneration);
        this.sortBitRate(randomGeneration);
    }

    public void setFrequencySlots(List<Integer> frequencySlots) {
        this.frequencySlots = frequencySlots;
    }

    public void setReqNumbOfSlots(int reqNumbOfSlots) {
        this.reqNumbOfSlots = reqNumbOfSlots;
    }

    public int getReqNumbOfSlots(){
        return this.reqNumbOfSlots;
    }

    /**
     * Método para sortear a taxa de transmissão.
     * 
     * @param randomGeneration
     */
    public void sortBitRate(Random randomGeneration){
		final int size = this.possibleBitRates.size();
		final int number = (int) (randomGeneration.nextDouble() * size);
        this.selectedBitRate = this.possibleBitRates.get(number);
	}

    public int getSelectedBitRate(){
        return selectedBitRate;
    }

    /**
     * Método para configurar o tempo de duração e queda da requisição de chamada.
     * 
     * @param time
     * @param meanDurationRate
     * @param randomGeneration
     */
    private void setTime(final double time, final double meanDurationRate, Random randomGeneration){
		
		this.duration = ProbabilityFunctions.exponentialDistribution(meanDurationRate, randomGeneration);;
		this.decayTime = time + this.duration;
	}

    /**
     * Método para retirar a requisição de chamada da rede.
     */
    public void desallocate(){

        List<OpticalLink> upLinks = this.route.getUpLink();
        List<OpticalLink> downLinks = this.route.getDownLink();

        for(int slot : this.frequencySlots){
            
            for(OpticalLink link : upLinks){
                link.deallocate(slot);
            }

            if (this.callRequestType == CallRequestType.Bidirectional){
                for(OpticalLink link : downLinks){
                    link.deallocate(slot);
                }
            }
        }
    }     


    public double getDecayTime() {
        return decayTime;
    }

    
    public void setDecayTime(double decayTime) {
        this.decayTime = decayTime;
    }


    public Route getRoute() {
        return route;
    }


    public void setRoute(Route route) {
        this.route = route;
    }

    public void allocate(OpticalSwitch[] listOfNodes){
		
        List<OpticalLink> uplink = route.getUpLink();
        List<OpticalLink> downlink = route.getDownLink();

		for(int FreqSlot : this.frequencySlots){
			
			final OpticalLink optLinkInbound = uplink.get(0);
			final OpticalSwitch optSwitch = AuxiliaryFunctions.getNodeByID(this.sourceNodeID, listOfNodes);
			final double laserPower = optSwitch.getLaserPower();

            optLinkInbound.allocate(FreqSlot, laserPower);

			for(int iUplink = 1; iUplink < uplink.size(); iUplink++){
				
				final int source = uplink.get(iUplink).getSourceNode();
                
				final OpticalLink optLink = uplink.get(iUplink - 1);

				final OpticalSwitch opticalSwitch = AuxiliaryFunctions.getNodeByID(source, listOfNodes);

                final double powerBinSlot = optLink.getPowerB(FreqSlot); //Pega a potência na saída do último optical link já analisado.

				final double switchAten = opticalSwitch.getSwitchAtenuation();	

				final double potOut  = powerBinSlot * switchAten;	

				uplink.get(iUplink).allocate(FreqSlot, potOut);

			}
			
			//Alocar a volta da chamada
			if(this.callRequestType == CallRequestType.Bidirectional){
				
                final OpticalLink optLinkOutbound = downlink.get(0);

                optLinkOutbound.allocate(FreqSlot, laserPower);
				
				for(int iDownlink = 1; iDownlink < downlink.size(); iDownlink++){    

            		final int source = downlink.get(iDownlink).getSourceNode();    

            		final OpticalLink optLink = downlink.get(iDownlink - 1);

    				final OpticalSwitch opticalSwitch = AuxiliaryFunctions.getNodeByID(source, listOfNodes);

                    final double powerBinSlot = optLink.getPowerB(FreqSlot); //Pega a potência na saída do último optical link já analisado.

                    final double switchAten = opticalSwitch.getSwitchAtenuation();
				
    				final double potOut  = powerBinSlot * switchAten;    

                    downlink.get(iDownlink).allocate(FreqSlot, potOut);
            	}
			}
		}		
	}

    /**
	 * Método para retornar os slots requeridos.	
	 */    
    public List<Integer> getFrequencySlots(){
    	return this.frequencySlots;
    }
}
