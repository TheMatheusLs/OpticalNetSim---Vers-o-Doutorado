package Network.Structure;

import java.util.ArrayList;
import java.util.List;

import Config.ConfigSimulator;
import Config.ParametersSimulation;

public class OpticalLink {
    
    private int opticalLinkID;
    private double length;
    private int srlg;
    private int sourceNode;
    private int destinationNode;
    private OpticalAmplifier booster;
    private List<OpticalSpan> spans;

    private double cost;
    private double[]  powersA;
    private double  totalPowerInA;
    private double[]  powersB;
    private double  totalPowerInB;
    private double[] frequencies;

    private boolean linkState;

    
    public OpticalLink(final int opticalLinkID, final int sourceNode, final int destinationNode, final int srlg, final double length) throws Exception {
        this.opticalLinkID = opticalLinkID;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.srlg = srlg;
        this.length = length;
        
        if(length < ConfigSimulator.getSpanSize()){
            throw new Exception("Fiber length between nodes "+ sourceNode +" and "+ destinationNode + " is invalid.");
		}
        
        this.booster = new OpticalAmplifier();    
        this.spans = this.configureSpansInLink(length);
        
        this.inicializePowersAndFrequencies();
        
        this.cost = 0.0;
        this.linkState = true;
    }
    

    /**
     * Método para iniciar a força e as frequências no link óptico
     */
    private void inicializePowersAndFrequencies(){
    	
        final int numberOfSlots = ParametersSimulation.getNumberOfSlotsPerLink();

    	this.powersA = new double[numberOfSlots];
    	this.powersB = new double[numberOfSlots];
    	this.frequencies = new double[numberOfSlots];
    	
    	for(int i = 0; i < numberOfSlots; i++){    		
    		this.powersA[i] = 0.0;
    		this.powersB[i] = 0.0;
    		this.frequencies[i] = (ConfigSimulator.getFinalFrequency() - ((i + 1) * ConfigSimulator.getSpacing()));    		
    	}
    	
    	this.totalPowerInA = 0.0;
    	this.totalPowerInB = 0.0;
    }


    private List<OpticalSpan> configureSpansInLink(final double length) throws Exception {
		
		final double spanSize = ConfigSimulator.getSpanSize();
		
		if(length % spanSize != 0){
			throw new Exception("The fiber size is not multiple of the span size.");
		}
				
		final int count = (int) Math.ceil(length/spanSize); //(int) (length/spanSize);
		final List<OpticalSpan> spans = new ArrayList<OpticalSpan>();
		
		for(int i=0; i<count; i++){
            spans.add(new OpticalSpan (i, ParametersSimulation.getNumberOfSlotsPerLink(), new OpticalFiber(spanSize), new OpticalAmplifier()));
		}
		return spans;
	}


    /**
     * Remove um slot de uso
     * 
     * @param slot
     */
    public void deallocate(final int slot){

		this.deallocateTotalPower(slot);
		this.powersA[slot] = 0.0;
		this.powersB[slot] = 0.0;
		
		// Desaloca os slots no span desalloacate slots in the spans.
    	for(int i = 0; i < this.spans.size(); i++){
            OpticalSpan span = this.spans.get(i);
            span.deallocate(slot);
    	}
	}

    /**
     * Remove a força que o slot consumia na rede
     * 
     * @param slot
     */
    public void deallocateTotalPower(final int slot){
		this.totalPowerInA -= this.powersA[slot];
		this.totalPowerInB -= this.powersB[slot];
	}


    public List<OpticalSpan> getSpans() {
		return spans;
	}

    public double getLength() {
        return length;
    }

    public OpticalAmplifier getBooster() {
		return booster;
	}

    /**
     * Método para retornar o OpticalSpan na posição spanIndex
     * 
     * @param spanIndex Índice da lista de spans
     * @return Retorna o OpticalSpan na posição spanIndex
     */
    public OpticalSpan getSpan(int spanIndex) {
        return spans.get(spanIndex);
    }


    public void setCost(double cost){
        this.cost = cost;
    }


    @Override
	public String toString() {
		
		String txt = String.format("Link %d - Source = %d, Destination = %d, Length = %f", this.opticalLinkID, this.sourceNode, this.destinationNode, this.length);

		return txt;
	}

    public boolean isLinkWorking(){
        return this.linkState;
    }

    public int getSourceNode() {
        return this.sourceNode;
    }

    public int getDestinationNode() {
        return this.destinationNode;
    }

    public double getCost(){
        return this.cost;
    }

    public int getSpanSize() {
        return spans.size();
    }
}
