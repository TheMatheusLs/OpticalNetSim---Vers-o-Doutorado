package Routing.Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Network.TopologyManager;
import Network.Structure.OpticalLink;
import Routing.Route;

public class Dijkstra extends RoutingAlgorithm{

    /**
     * Construtor da Classe
     */
    public Dijkstra(){
        
    }


    @Override
    public Route selectRoute(List<Route> routes){
        return routes.get(0);
    }

    /**
     * Método Dijkstra para encontrar a melhor rota para a topologia para um determinado para origem destino
     * 
     * @param orNode ID do nó de origem 
     * @param deNode ID do nó de destino
     * @param topology Classe que controla a topologia
     * @return rota encontrada
     */
    public static Route findRoute(int orNode, int deNode, TopologyManager topology) {
        
        int k = -1, h, hops;
        int numNodes = topology.getNumberOfNodes();
        List<Integer> path = new ArrayList<Integer>();
        List<Integer> invPath = new ArrayList<Integer>();
        OpticalLink auxLink;
        Route routeDJK = null;
        boolean networkDisconnected = false;

        double[] custoVertice = new double[numNodes];
        int[] precedente = new int[numNodes];
        boolean[] status = new boolean[numNodes];

        Arrays.fill(custoVertice, Double.MAX_VALUE);
        Arrays.fill(precedente, -1);
        Arrays.fill(status, false);

        
        custoVertice[orNode] = 0;
        int setVertexes = numNodes;
    
        while(setVertexes > 0 && !networkDisconnected){
    
            double min = Double.MAX_VALUE;
            
            for(int i = 0; i < numNodes; i++) {
                if((status[i] == false) && (custoVertice[i] < min)){
                    min = custoVertice[i];
                    k = i;
                }
            }

            if(k == deNode)
                break;
            
            status[k] = true;
            setVertexes--;

            Boolean outputLinkFound = false;
    
            for(int j = 0; j < numNodes; j++){

                auxLink = topology.getLink(k, j);

                if((auxLink != null) && (auxLink.isLinkWorking()) && (topology.getNode(auxLink.getSourceNode()).isNodeWorking()) && (topology.getNode(auxLink.getDestinationNode()).isNodeWorking())){
                    outputLinkFound = true;
                    
                    if( (status[j] == false) && (custoVertice[k] + auxLink.getCost() < custoVertice[j]) ){
                       custoVertice[j] = (custoVertice[k] + auxLink.getCost());
                       precedente[j] = k;
                    }
                }
            }
            
            if(!outputLinkFound)
                networkDisconnected = true;
        }
        
        if(!networkDisconnected){
            path.add(deNode);
            hops = 0;
            int j = deNode;
            
            while(j != orNode){
                hops++;
                if(precedente[j] != -1){
                    path.add(precedente[j]);
                    j = precedente[j];
                }
                else{
                    networkDisconnected = true;
                    break;
                }
            }
            if(!networkDisconnected){     
                
                for(h = 0; h <= hops; h++)
                    invPath.add(path.get(hops-h));

                routeDJK = new Route(invPath, topology);
            }
        }
        
        return routeDJK;
    }   
}
