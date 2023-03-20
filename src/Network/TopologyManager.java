package Network;

import java.util.List;

import Config.ParametersSimulation;
import Manager.FolderManager;
import Network.Structure.OpticalLink;
import Network.Structure.OpticalSwitch;
import Network.Topologies.TopologyGeneral;
import Network.Topologies.TopologyNSFNet;
import Types.GeneralTypes.LinkCostType;
import Types.GeneralTypes.TopologyType;

public class TopologyManager {
    /**
     * Lista com todos os OpticalSwicths da rede. Os nós
     */
    private OpticalSwitch[] listOfNodes;
    /**
     * Matriz com todos os OpticalLinks da rede.
     */
    private OpticalLink[][] networkOpticalLinks;
    /**
     * Número de nós das redes
     */
    private int numberOfNodes;
    /**
     * Tamanho do maior link da rede
     */
    private double maxLinkLength;

    /**
     * Construtor da classe TopologyManager
     * @throws Exception
     */
    public TopologyManager() throws Exception {
        
        this.inicialize();

        // Imprime na tela a topologia usada
        System.out.println(this);
    }

    /**
     * Método para inicializar a topologia da rede
     * 
     * @throws Exception
     */
    public void inicialize() throws Exception {

        TopologyGeneral NetworkTopologyInstance = null;

        if (ParametersSimulation.getTopologyType().equals(TopologyType.NSFNet)){
            NetworkTopologyInstance = new TopologyNSFNet();
        } else {
            throw new Exception("Topologia inválida!");            
        }

        this.networkOpticalLinks = NetworkTopologyInstance.getNetworkAdjacencyMatrix();
        this.listOfNodes = NetworkTopologyInstance.getListOfNodes();
        
        this.maxLinkLength = NetworkTopologyInstance.getMaxLength();
        this.numberOfNodes = NetworkTopologyInstance.getNumberOfNodes();
        
        this.setLinksInitCost();

        this.setNodesNeighbors();
    }

    /*
     * Método para encontrar os nós vizinhos
     */
    private void setNodesNeighbors() {

        OpticalLink auxLink;
        
        for(OpticalSwitch node1: listOfNodes){
            
            for(OpticalSwitch node2: listOfNodes) {
                
                if(node1.isEquals(node2)){
                    continue;
                }

                auxLink = this.getOpticalLink(node1.getOpticalSwitchID(), node2.getOpticalSwitchID());
                
                if(auxLink == null)
                    continue;

                node1.addNeighborNode(node2);
            }
        }
    }
    
    /*
     * Configura os custos de cada link da rede
     */
    private void setLinksInitCost() throws Exception {

        if (ParametersSimulation.getLinkCostType().equals(LinkCostType.Hops)){
            for (int o = 0; o < this.numberOfNodes; o++){
                for (int d = 0; d < this.numberOfNodes; d++){
                    OpticalLink opticalLink = this.getOpticalLink(o, d);
                    if (opticalLink != null){
                        opticalLink.setCost(1.0);
                    }
                }
            }
        } else {
            if (ParametersSimulation.getLinkCostType().equals(LinkCostType.Length)){
                for (int o = 0; o < this.numberOfNodes; o++){
                    for (int d = 0; d < this.numberOfNodes; d++){
                        OpticalLink opticalLink = this.getOpticalLink(o, d);
                        if (opticalLink != null){
                            opticalLink.setCost(opticalLink.getLength());
                        }
                    }
                }
            } else {
                if (ParametersSimulation.getLinkCostType().equals(LinkCostType.LengthNormalized)){
                    for (int o = 0; o < this.numberOfNodes; o++){
                        for (int d = 0; d < this.numberOfNodes; d++){
                            OpticalLink opticalLink = this.getOpticalLink(o, d);
                            if (opticalLink != null){
                                opticalLink.setCost(opticalLink.getLength() / this.maxLinkLength);
                            }
                        }
                    }
                } else {
                    throw new Exception("Invalid link cost type");
                }
            }
        }
    }

    /**
     * Método para retornar o OpticalLink
     * 
     * @param indexSource 
     * @param indexDestination
     * @return
     */
    public OpticalLink getOpticalLink(int indexSource, int indexDestination) {
        return this.networkOpticalLinks[indexSource][indexDestination];
    }

    @Override
    public String toString() {
        String txt = String.format("\t*** Topology ***\nNumber of nodes: %d\n", this.numberOfNodes);

        txt += "** Nodes: \n";

        for (OpticalSwitch node : this.listOfNodes){
            txt += node + "\n";
        } 

        txt += "** Links: \n";

        for (int o = 0; o < this.numberOfNodes; o++){
            for (int d = 0; d < this.numberOfNodes; d++){
                if (this.getOpticalLink(o, d) != null){
                    txt += this.getOpticalLink(o, d) + "\n";
                }
            }
        }

        return txt;
    }

    /**
     * Método para salvar a topologia em um arquivo de texto
     * 
     * @param folderManager
     */
    public void save(FolderManager folderManager) {
        folderManager.writeTopology((String.valueOf(this)));
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public OpticalLink getLink(int indexOrNode, int indexDeNode) {
        return this.networkOpticalLinks[indexOrNode][indexDeNode];
    }

    public OpticalSwitch getNode(int index) {

        return this.listOfNodes[index];
    }

    public OpticalSwitch[] getListOfNodes() {
        return this.listOfNodes;
    }

    /**
     * Verifica se todos os links da rede foram limpos corretamente.
     * 
     * @throws Exception
     */
    public void checkIfIsClean() throws Exception {
        for (int s = 0; s < networkOpticalLinks.length;s++){
			for (int d = 0; d < networkOpticalLinks.length;d++){
				if (networkOpticalLinks[s][d] != null){
					for (int i = 0; i < ParametersSimulation.getNumberOfSlotsPerLink();i++){
						if (networkOpticalLinks[s][d].getPowerA(i) != 0){
							throw new Exception("As rotas não foram limpas corretamente");
						}
					}
				}
			}
		}
    }

    /**
     * Restaura o estado dos links para a lista de links informada
     * 
     * @param removedEdges Lista de links para serem restauradas
     */
    public void restoreEdges(List<OpticalLink> removedEdges) {
        for (OpticalLink link : removedEdges){
            link.setLinkState(true);
        }
    }

    /**
     * Restaura o estado dos OpticalSwichts 
     * 
     * @param rootRoutePath
     */
    public void restoreNodes(List<Integer> rootRoutePath) {
        for (int oSwitchID: rootRoutePath){
            OpticalSwitch opticalSwitch = this.getNode(oSwitchID);
            opticalSwitch.setNodeState(true);
        }
    }
}
