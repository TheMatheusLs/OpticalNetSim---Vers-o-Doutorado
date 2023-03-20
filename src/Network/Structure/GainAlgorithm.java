package Network.Structure;

import java.util.List;

import Config.ConfigSimulator;
import Types.GeneralTypes.GainAlgorithmType;

public class GainAlgorithm {
    
    /**
     * Instância da classe.
     */
    private static final GainAlgorithm GAIN_INSTANCE = new GainAlgorithm(GainAlgorithmType.Basic);
    /**
     * Tipo de ganho selecionado
     */
    private GainAlgorithmType metricType;

    /**
     * Construtor da Classe
     * @param metricType
     */
    public GainAlgorithm(GainAlgorithmType metricType) {
        this.metricType = metricType;
    }

    /**
     * Método para configurar o ganho dos amplificadores do optical link.
     * 
     * @param link
     */
    public void configureGain(final OpticalLink link) {
        if (GainAlgorithmType.Basic == metricType) {
            this.configureGainInLink(link);
        }
    }

    /**
     * Método para configurar o ganho dos amplificadores do optical link. As perdas do link são somadas e divididas pelo número de amplificadores, para então configurar o ganho de cada amplificador no optical link.
     * 
     * @param link
     */
    private void configureGainInLink(final OpticalLink link) {

        final List<OpticalSpan> spans = link.getSpans();
        final int spansSize = link.getSpanSize();           // Quantidade de Span no link	 	 
        final double fiberLoss = -link.getLength()* ConfigSimulator.getFiberAtenuationCoefficient();    // km * (dB/ Km)  = dB
        final double connectorsLoss = (double) (spansSize * (-2.0 * ConfigSimulator.getDioLoss()));     // dB
        final double nodesLoss = - ConfigSimulator.getMuxLoss() * 2.0 - ConfigSimulator.getSwitchLoss();// dB
        final double totalLoss = fiberLoss + connectorsLoss + nodesLoss;							    // dB
        final double numberOfAmpl = (double) (spansSize+1);										        // Quantidade de amplificadores
        
        final double gain = totalLoss / numberOfAmpl;												    // dB
        final double noiseFactorIndB = ConfigSimulator.getNoiseFigureIndB();							// dB

        //Configurando o amplificador
        OpticalAmplifier linkAmplifier = link.getBooster();
        linkAmplifier.setGainIndB(gain);
        linkAmplifier.setNoiseFactorIndB(noiseFactorIndB);

        //Configura os amplificadores ópticos no span
        for (OpticalSpan span : spans) {
            final OpticalAmplifier spanAmplifier = span.getOpticalAmplifier();
            spanAmplifier.setGainIndB(gain);	
            spanAmplifier.setNoiseFactorIndB(noiseFactorIndB);
		} 
    }

    public static GainAlgorithm getGainInstance(){
		return GAIN_INSTANCE;
	}
}
