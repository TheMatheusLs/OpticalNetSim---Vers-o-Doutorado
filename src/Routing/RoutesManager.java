package Routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Config.ParametersSimulation;
import Manager.FolderManager;
import Network.TopologyManager;
import Routing.Algorithms.Dijkstra;
import Routing.Algorithms.YEN;
import Types.GeneralTypes.RoutingAlgorithmType;

public class RoutesManager {

    private List<List<Route>> allRoutes;
    private TopologyManager networkTopology;
    private int numberOfNodesInTopology;
    private RoutingAlgorithmType routingOption;

    private int numberOfRoutesToFind;
    
    public RoutesManager(TopologyManager topology) {
        this.networkTopology = topology;
        this.numberOfNodesInTopology = topology.getNumberOfNodes();
        this.routingOption = ParametersSimulation.getRoutingAlgorithmType();

        // Cria a estrutura para armazenar todas as rotas
        this.allRoutes = this.createAllRoutes();


        // Inicializa o processo de roteamento estático
        if (this.routingOption.equals(RoutingAlgorithmType.Dijstra)) {
            this.numberOfRoutesToFind = 1;
            this.RoutingByDijkstra();
        } else {
            // Usado para o algoritmo de roteamento YEN
            this.numberOfRoutesToFind = ParametersSimulation.getKShortestRoutes();
            this.RoutingByYEN();
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


    private void RoutingByYEN() {

        List<Route> routes;

        for(int orN = 0; orN < this.numberOfNodesInTopology; orN++){
            for(int deN = 0; deN < this.numberOfNodesInTopology; deN++){

                if(orN != deN){
                    routes = YEN.findRoute(orN, deN, this.networkTopology, this.numberOfRoutesToFind);
                } else{
                    routes = null;
                }

                this.setRoutes(orN, deN, routes);
            }
        }

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


    private void setRoutes(int orN, int deN, List<Route> routes) {
        this.clearRoutes(orN, deN);
        this.addRoutes(orN, deN, routes);
    }
    
    private void addRoutes(int orN, int deN, List<Route> routes) {
        
        if (routes != null) {
            for(Route it : routes)
                this.addRoute(orN, deN, it);
        } else {
            this.addRoute(orN, deN, null);
        }
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


    public List<Route> getRoutesForOD(int source, int destination) {
        return allRoutes.get(source * this.numberOfNodesInTopology + destination);
    }


    public void checkIfIsClean() {
        // for (List<Route> routes: allRoutes){
        //     for (Route route : routes){
        //         if (route != null){
        //             for (int i = 0; i < ParametersSimulation.getNumberOfSlotsPerLink();i++){
        //                 if (route.getSlotValue(i) != 0){
        //                     throw new Exception("As rotas não foram limpas corretamente");
        //                 }
        //             }
        //         }
        //     }
		// }
    }
}
