package Network.Topologies;

import Config.ConfigSimulator;
import Network.Structure.GainAlgorithm;
import Network.Structure.OpticalLink;
import Network.Structure.OpticalSwitch;

public class TopologyGeneral {

    /**
	 * Instância do algoritmo de configuração de ganho dos amplificadores
	 */
	private final transient GainAlgorithm gainAlgorithm = GainAlgorithm.getGainInstance();
    /**
     * Matriz com os tamanhos dos links da rede
     */
    private double[][] lengthsTopology;
    /**
     * Número de nós para a rede
     */
    private int numberOfNodes;
    /**
     * Tamanho do maior link da rede
     */
    private double maxLength;
    /**
     * Matriz de adjacência com todos os links ópticos da rede
     */
    private OpticalLink[][] linksAdjacencyMatrix;
    /**
     * Lista com todos os nó ópticos da rede
     */
    private OpticalSwitch[] opticalNodes;

    public TopologyGeneral(double[][] lengthsTopology) {
        this.lengthsTopology = lengthsTopology;
        this.numberOfNodes = this.lengthsTopology.length;

        this.buildNetworkAdjacencyMatrix();
        this.buildNodes();
    }
    
    /**
     * Método para construit a matriz de adjacência da rede.
     */
    private void buildNetworkAdjacencyMatrix() {
		//TODO: Verificar se todos os links existem
		double length = 0.0;
		int linkId = 0;
		int srlgId = 0;
			
		this.linksAdjacencyMatrix = new OpticalLink[this.numberOfNodes][this.numberOfNodes];			

		for(int source = 0; source < this.numberOfNodes; source++) {
			for(int destination = 0; destination < this.numberOfNodes; destination++) {
				if(this.lengthsTopology[source][destination] != Double.MAX_VALUE && linksAdjacencyMatrix[source][destination] == null){
										
					length = this.lengthsTopology[source][destination];
					linkId++;
					srlgId++;

					if (this.maxLength < length){
						this.maxLength = length;
					}
					
					try {
						linksAdjacencyMatrix[source][destination] = new OpticalLink(linkId, source, destination, srlgId, length);
					} catch (Exception e) {
						e.printStackTrace();
					}
					gainAlgorithm.configureGain(linksAdjacencyMatrix[source][destination]);
					
					if(linksAdjacencyMatrix[destination][source]==null){							
						linkId++;							
						try {
							linksAdjacencyMatrix[destination][source] = new OpticalLink(linkId,destination,source,srlgId,length);
						} catch (Exception e) {
							e.printStackTrace();
						}
						gainAlgorithm.configureGain(linksAdjacencyMatrix[destination][source]);
					}						
				}
			}
		}			
	}

    /**
     * Constroe e configura os nós da ree
     */
    private void buildNodes(){		

		final double switchLoss = ConfigSimulator.getSwitchLoss();
		final double laserPower = ConfigSimulator.getLaserPower();
		final double osnr = ConfigSimulator.getOSNRIn();

		this.opticalNodes = new OpticalSwitch[this.numberOfNodes];		
		for(int x = 0; x < this.numberOfNodes; x++) { 
			final OpticalSwitch node = new OpticalSwitch(x, switchLoss, laserPower, osnr);
			this.opticalNodes[x] = node;			
		}		
	}

    /**
	 * Método para retornar a matriz de adjacência da rede.
	 * 
	 * @return Retorna a matriz de adjacência da rede
	 */
	public OpticalLink[][] getNetworkAdjacencyMatrix() {
		return this.linksAdjacencyMatrix;
	}

    public OpticalSwitch[] getListOfNodes() {
		return this.opticalNodes;
	}

	public double getMaxLength(){
		return this.maxLength;
	}

	public int getNumberOfNodes(){
		return this.numberOfNodes;
	}
}
