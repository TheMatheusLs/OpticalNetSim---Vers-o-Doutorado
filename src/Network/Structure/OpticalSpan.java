package Network.Structure;

public class OpticalSpan {
    
    /**
     * ID do span
     */
    private int spanID;
    /**
     * Fibra óptica
     */
    private OpticalFiber opticalFiber;
    /**
     * Amplificado óptico
     */
    private OpticalAmplifier opticalAmplifier;
    /**
     * Número de slots
     */
    private int numberOfSlots;
    /**
     * Potência total na entrada do amplificador.
     */
    private double totalPower;
    /**
     * Lista de potências de cada slot de frequencia.
     */
    private double[] powers;   
    
    public OpticalSpan(final int spanID, final int numberOfSlots, final OpticalFiber opticalFiber, final OpticalAmplifier opticalAmplifier) {
        this.spanID = spanID;
        this.numberOfSlots = numberOfSlots;
        this.opticalFiber = opticalFiber;
        this.opticalAmplifier = opticalAmplifier;

        this.inicializePowersInSpan(numberOfSlots);
    }

    /**
     * Método para inicializar a lista de potência da fibra óptica do Span.
     * 
     * @param numberOfSlots
     */
    private void inicializePowersInSpan(final int numberOfSlots) {
        this.powers = new double[numberOfSlots];    	
    	for(int i = 0;i < numberOfSlots; i++){    		
    		this.powers[i] = 0.0;
    	}    	
    	this.totalPower = 0.0;
    }

    /**
     * Método para remover a potência de um determinado slot da fibra.
     * 
     * @param slot
     */
    public void deallocate(int slot) {
        this.deallocateTotalPower(slot);
		this.powers[slot] = 0.0;
    }

    /**
     * Método para remover a potência de um determinado slot na potência total da fibra do Span.
     * 
     * @param slot
     */
    public void deallocateTotalPower(final int slot){
		this.totalPower -= this.powers[slot];
	}

    public int getSpanID() {
        return this.spanID;
    }

    public void setSpanID(int spanID) {
        this.spanID = spanID;
    }

    public OpticalAmplifier getOpticalAmplifier() {
		return this.opticalAmplifier;
	}

    /**
	 * M�todo para retornar a fibra �ptica do Span.
	 * @return O atributo OpticalFiber
	 * @author Andr� 			
	 */		
	public OpticalFiber getOpticalFiber() {
		return this.opticalFiber;
	}

    /**
	 * M�todo para configurar a pot�ncia do slot alocado na fibra �ptica do Span.
	 * @param powerValue
	 * @param slot
	 * @author Andr� 			
	 */
	public void setPower(final double powerValue, final int slot){
		this.totalPower -= this.powers[slot];
		this.totalPower += powerValue;
		this.powers[slot] = powerValue;
	}
}
