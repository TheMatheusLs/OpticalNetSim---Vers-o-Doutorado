package Config;

/*
 * Classe para armazenar as configurações do simulador. As configurações do simulador devem permanecer constantes entre a comparação dos algoritmos.
 */
public class ConfigSimulator {

    /** 
     * Constante da luz.
     */
    private static final double LIGHT_CONSTANT = 2.99792458E8;  // Unidade: m/s (metro/segundo)
    /**
     * Comprimento de onda inicial.
     */
    private static final double INITIAL_LAMBDA = 1528.77E-9; // Unidade: m (metro)
    /**
     * Frequencia final.
     */
    private static final double FINAL_FREQUENCY = (LIGHT_CONSTANT)/INITIAL_LAMBDA; // Unidade: Hz (hertz)
    /**
     * Espaçamento do canal.
     */
    private static final double SPACING = 12.5E9; 		// Unidade: bits
    /**
     * Constante de Planck.
     */
    private static final double PLANCK = 6.626068E-34;  // Unidade: J * s
    /**
     * Figura de ruído.
     */
    private static final double NOISE_FIGURE = 5.5;
    /**
     * Tamanho do span.
     */
    private static final double SPAN_SIZE = 100.0;
    /**
     * Perda do mux/demux.
     */
    private static final double MUX_LOSS = -3.0;
    /**
     * Coeficiente de atenuação da fibra.
     */
    private static final double ATENU_COEFFIC = -0.23;
    /**
     * Perda do conector.
     */
    private static final double DIO_LOSS = -3.0;
    /**
     * Perda do comutador.
     */
    private static final double SWITCH_LOSS = -10.0;
    /**
     * OSNR de entrada.
     */
    private static final double OSNR_IN = 40.0;
    /**
     * Potência do laser.
     */
    private static final double LASER_POWER = 0.0;
    /**
     * Tempo máximo.
     */
    private static final double MAX_TIME = 100000.0;
    /**
     * Taxa média de duração da chamada.
     */
    private static final double MEAN_RATE = 1.0;

    public static double getDioLoss() {
        return DIO_LOSS;
    }


    public static double getFiberAtenuationCoefficient() {
        return ATENU_COEFFIC;
    }


    public static double getMuxLoss () {
        return MUX_LOSS;
    }


    public static double getSwitchLoss() {
		return SWITCH_LOSS;
	}


    public static double getNoiseFigureIndB() {
		return NOISE_FIGURE;
	}


    public static double getLaserPower() {
		return LASER_POWER;
	}


    public static double getOSNRIn() {
		return OSNR_IN;
	}


    public static double getSpanSize() {
		return SPAN_SIZE;
	}


    public static double getFinalFrequency() {
		return FINAL_FREQUENCY;
	}


    public static double getSpacing() {
		return SPACING;
	}

    public static double getMeanRateOfCallsDuration() {
        return MEAN_RATE;
    }
}
