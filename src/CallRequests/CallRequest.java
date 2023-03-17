package CallRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import GeneralClasses.ProbabilityFunctions;
import Network.Structure.OpticalLink;
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
    private final List<Integer> frequencySlots;
    private final transient List<Integer> possibleBitRates;
    private int selectedBitRate;
    private CallRequestType callRequestType;
    private int sourceNodeID;
    private int destinationNodeID;

    
    public CallRequest(final int callRequestId, final int sourceNodeID, final int destinationNodeID, final CallRequestType callRequestType, final int[] possibleBitRates, final double time, final double meanDurationRate, Random randomGeneration){
        this.callRequestID = callRequestId;
        this.sourceNodeID = sourceNodeID;
        this.destinationNodeID = destinationNodeID;
        this.callRequestType = callRequestType; 	
        
        this.possibleBitRates = Arrays.stream(possibleBitRates).boxed().collect(Collectors.toList());
        
        this.frequencySlots = new ArrayList<Integer>();
        this.route = null;

        this.setTime(time, meanDurationRate, randomGeneration);
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
}
