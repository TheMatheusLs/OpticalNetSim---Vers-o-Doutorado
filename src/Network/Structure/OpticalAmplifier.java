package Network.Structure;

public class OpticalAmplifier {
    
    /**
     * Ganho do amplificador em dB.
     */
    private double gainIndB;
    /**
     * Fator de ruído do amplificador em dB.
     */
    private double noiseFactorIndB;

    public OpticalAmplifier() {
        this.gainIndB = -1;
        this.noiseFactorIndB = -1;
    }

    /**
     * Construtor da classe
     * 
     * @param gainIndB
     * @param noiseFactorIndB
     */
    public OpticalAmplifier(final double gainIndB, final double noiseFactorIndB) {
        this.gainIndB = gainIndB;
        this.noiseFactorIndB = noiseFactorIndB;
    }

    /**
     * Método para retornar o valor do ganho (linear) do amplificador.
     * 
     * @return O atributo gainIndB no valor linear.
     */
    public double getGainInLinear() {
        return Math.pow(10, gainIndB/10);
    }
    
    /**
     * Método para retornar o valor do fator de ruído (linear) do amplificador.
     * 
     * @return O atributo noiseFactorIndB no valor linear.
     */
    public double getNoiseFactorInLinear() {
		return Math.pow(10, noiseFactorIndB/10);
	}

    public double getGainIndB() {
        return gainIndB;
    }

    public void setGainIndB(final double gainIndB) {
        this.gainIndB = gainIndB;
    }

    public double getNoiseFactorIndB() {
        return noiseFactorIndB;
    }

    public void setNoiseFactorIndB(final double noiseFactorIndB) {
        this.noiseFactorIndB = noiseFactorIndB;
    } 
}
