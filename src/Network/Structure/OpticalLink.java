package Network.Structure;

import java.util.ArrayList;
import java.util.List;

import Config.ConfigSimulator;
import Config.ParametersSimulation;

public class OpticalLink {
    
    /**
     * ID do Link
     */
    private int opticalLinkID;
    /**
     * Tamanho do Link
     */
    private double length;
    /**
     *
     */
    private int srlg;
    /**
     * ID do nó de origem
     */
    private int sourceNode;
    /**
     * ID do nó de destino
     */
    private int destinationNode;
    /**
     * Anplificado do link
     */
    private OpticalAmplifier booster;
    /**
     * Lista de spans para o link
     */
    private List<OpticalSpan> spans;
    /**
     * Custo do link com base na métrica escolhida
     */
    private double cost;
    /**
     * Lista de força em A
     */
    private double[]  powersA;
    /**
     * Potência total em A
     */
    private double  totalPowerInA;
    /**
     * Lista de força em B
     */
    private double[]  powersB;
    /**
     * Potência total em B
     */
    private double  totalPowerInB;
    /**
     * Lista de frequência no espectro
     */
    private double[] frequencies;
    /**
     * Estado no link
     */
    private boolean linkState;

    
    /**
     * Construtor da classe OpticalLink
     * 
     * @param opticalLinkID
     * @param sourceNode
     * @param destinationNode
     * @param srlg
     * @param length
     * @throws Exception
     */
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

    /**
     * Método para configurar os spans no link
     * 
     * @param length
     * @return Uma lista com os span ópticos
     * @throws Exception
     */
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
     * Método para verificar se o espectro está disponível no slot
     * 
     * @param slot slot
     * @return true se o espectro está disponível no slot, false caso o contrário
     */
    public boolean isAvailableSlotAt(final int slot){
    	return this.powersA[slot] == 0.0;
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

    /**
	 * Método para configurar a potência do slot no optical link.
	 * @param slot
	 * @param initialPower
	 * @author André 			
	 */	
	public void allocate(final int slot, final double initialPower){		
		
        final double dioLoss =  Math.pow(10, ConfigSimulator.getDioLoss() / 10);
		final double muxGain = Math.pow(10, ConfigSimulator.getMuxLoss() / 10);
		final double powBefBooster = initialPower * muxGain;			
		this.setPowerA(powBefBooster, slot);		
		
		double signal = powBefBooster * this.booster.getGainInLinear();
		
        for (OpticalSpan span : this.spans) {
            final double fiberGain = span.getOpticalFiber().getLength() * ConfigSimulator.getFiberAtenuationCoefficient();

            signal *= dioLoss;
            signal *= Math.pow(10, fiberGain / 10);
            signal *= dioLoss;	

            span.setPower(signal, slot);

            signal *= span.getOpticalAmplifier().getGainInLinear();
        }

		this.setPowerB(signal * muxGain, slot);		
	}

    /**
	 * Método para configurar a potência do slot antes do booster no optical link.
	 * @param powerValue
	 * @param slot
	 * @author André 			
	 */	
	private void setPowerA(final double powerValue, final int slot){
		this.totalPowerInA -= this.powersA[slot];
		this.totalPowerInA += powerValue;
		this.powersA[slot] = powerValue;
	}

    /**
     * Método para configurar a potência do slot depois do booster no optical link.
     * 
     * @param powerValue Potência 
     * @param slot Slot
     */
    private void setPowerB(final double powerValue, final int slot){
		this.totalPowerInB -= this.powersB[slot];
		this.totalPowerInB += powerValue;
		this.powersB[slot] = powerValue;
	}

    /**
	 * M�todo para retornar a pot�ncia do slot antes do booster
	 * no optical link.
	 * @return A pot�ncia do slot antes do booster. 
	 * @param slot
	 * @author Andr� 			
	 */		
	public double getPowerA(final int slot){
		return this.powersA[slot];
	}

    /**
	 * M�todo para retornar a pot�ncia do slot depois do demux
	 * no optical link.
	 * @param slot
	 * @return A pot�ncia do slot depois do pr�-amplificador.
	 * @author Andr� 			
	 */		   
	public double getPowerB(final int slot){
		return this.powersB[slot];
	}

    public void setLinkState(Boolean linkState) {
        this.linkState = linkState;
    }
}
