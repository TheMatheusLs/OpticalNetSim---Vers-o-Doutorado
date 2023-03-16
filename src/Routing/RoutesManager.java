package Routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Config.ParametersSimulation;
import Manager.FolderManager;
import Network.TopologyManager;
import Routing.Algorithms.Dijkstra;
import Types.GeneralTypes.RoutingAlgorithmType;

public class RoutesManager {

    private List<List<Route>> allRoutes;
    private TopologyManager networkTopology;
    private int numberOfNodesInTopology;
    private int numberOfRoutesToFind;
    private RoutingAlgorithmType routingOption;

    
    public RoutesManager(TopologyManager topology) {
        this.networkTopology = topology;
        this.numberOfNodesInTopology = topology.getNumberOfNodes();
        this.routingOption = ParametersSimulation.getRoutingAlgorithmType();

        // Determina o número de rotas a ser encontrada pelo algoritmo
        if (this.routingOption.equals(RoutingAlgorithmType.Dijstra)) {
            this.numberOfRoutesToFind = 1;
        } else {
            // Usado para o algoritmo de roteamento YEN
            this.numberOfRoutesToFind = ParametersSimulation.getKShortestRoutes();
        }

        // Cria a estrutura para armazenar todas as rotas
        this.allRoutes = this.createAllRoutes();


        // Inicializa o processo de roteamento estático
        if (this.routingOption.equals(RoutingAlgorithmType.Dijstra)) {
            this.RoutingByDijkstra();
        }

        // Imprime na tela as rotas
        System.out.println(this);
    }


    private List<List<Route>> createAllRoutes() {

        List<List<Route>> routesInit = new ArrayList<List<Route>>();

        for (int OD = 0; OD < (this.numberOfNodesInTopology * this.numberOfNodesInTopology); OD++) {

            List<Route> routeAux = new ArrayList<Route>();

            routesInit.add(routeAux);
        }

        return routesInit;
    }

    
    private void RoutingByDijkstra() {

        Route routeAux;

        for(int orN = 0; orN < this.numberOfNodesInTopology; orN++){
            for(int deN = 0; deN < this.numberOfNodesInTopology; deN++){

                if(orN != deN){
                    routeAux = Dijkstra.findRoute(orN, deN, this.networkTopology);
                    this.setRoute(orN, deN, routeAux);
                }
            }
        }
    }


    private void setRoute(int orN, int deN, Route route) {
        this.clearRoutes(orN, deN);
        this.addRoute(orN, deN, route);
    }


    private void clearRoutes(int orN, int deN) {
        allRoutes.get(orN * this.numberOfNodesInTopology + deN).clear();
    }


    private void addRoute(int orN, int deN, Route route) {
        allRoutes.get(orN * this.numberOfNodesInTopology + deN).add(route);
    }


    @Override
    public String toString() {

        String txt = ""; //"\t*** Routing ***\n";

        for (List<Route> routes : allRoutes){
            for (Route route : routes){
                if (route != null){
                    txt += route;
                }
            }
        }

        return txt;
    }
    

    public void save(FolderManager folderManager) {
        folderManager.writeRoutes((String.valueOf(this)));
    }
}
