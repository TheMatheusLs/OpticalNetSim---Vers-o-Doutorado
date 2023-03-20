package Routing;

import java.util.ArrayList;
import java.util.List;

import Config.ParametersSimulation;
import Manager.FolderManager;
import Network.TopologyManager;
import Routing.Algorithms.Dijkstra;
import Routing.Algorithms.YEN;
import Types.GeneralTypes.RoutingAlgorithmType;

public class RoutesManager {

    /**
     * Estrutura para armazenar todas as rotas encontradas para a topologia
     */
    private List<List<Route>> allRoutes;
    /**
     * Classe que controla a topologia escolhida
     */
    private TopologyManager networkTopology;
    /**
     * Número de nós presentes na topologia escolhida
     */
    private int numberOfNodesInTopology;
    /**
     * Tipo do algoritmo de roteamento selecionado
     */
    private RoutingAlgorithmType routingOption;
    /**
     * Número de rotas que o algoritmo YEN irá buscar.
     */
    private int numberOfRoutesToFind;
    
    /**
     * Construtor da classe RoutesManager
     * 
     * @param topology Topologia de rede
     */
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

    /**
     * Método para criar a estrutura para armazenar todas as rotas separadas entre os pares origem destino 
     * 
     * @return Uma lista para cada par origem destino, contendo uma lista de k rotas entre eles
     */
    private List<List<Route>> createAllRoutes() {

        List<List<Route>> routesInit = new ArrayList<List<Route>>();

        for (int OD = 0; OD < (this.numberOfNodesInTopology * this.numberOfNodesInTopology); OD++) {

            List<Route> routeAux = new ArrayList<Route>();

            routesInit.add(routeAux);
        }

        return routesInit;
    }

    /**
     * Método para criar todas as K rotas para a topologia utilizando o algoritmo YEN
     */
    private void RoutingByYEN() {

        List<Route> routes;

        for(int orN = 0; orN < this.numberOfNodesInTopology; orN++){
            for(int deN = 0; deN < this.numberOfNodesInTopology; deN++){

                if(orN != deN){
                    routes = YEN.findRoute(orN, deN, this.networkTopology, this.numberOfRoutesToFind);
                    this.setRoutes(orN, deN, routes);
                } else{
                    routes = null;
                    this.setRoute(orN, deN, null);
                }
            }
        }
    }

    /**
     * Método para criar todas as rotas para a topologia utilizando o algoritmo Dijkstra
     */
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

    /**
     * Método para configurar uma rota a lista de rotas por par origem destino
     * 
     * @param orN Nó de origem 
     * @param deN Nó de destino
     * @param route Routa a ser adicionada
     */
    private void setRoute(int orN, int deN, Route route) {
        this.clearRoutes(orN, deN);
        this.addRoute(orN, deN, route);
    }

    /**
     * Método para limpar e adicionar uma lista de k rotas para um determinado origem destino
     * 
     * @param orN Nó de origem 
     * @param deN Nó de destino
     * @param routes Lista de k rotas
     */
    private void setRoutes(int orN, int deN, List<Route> routes) {
        this.clearRoutes(orN, deN);
        this.addRoutes(orN, deN, routes);
    }
    
    /**
     * Método para armazenar uma lista de k rotas para um determinado origem destino
     * 
     * @param orN Nó de origem 
     * @param deN Nó de destino
     * @param routes Lista de k rotas 
     */
    private void addRoutes(int orN, int deN, List<Route> routes) {
        
        if (routes != null) {
            for(Route it : routes)
                this.addRoute(orN, deN, it);
        } else {
            this.addRoute(orN, deN, null);
        }
    }

    /**
     * Método para limpar todas as k rotas entre um determinado para origem destino
     * 
     * @param orN Nó de origem 
     * @param deN Nó de destino
     */
    private void clearRoutes(int orN, int deN) {
        allRoutes.get(orN * this.numberOfNodesInTopology + deN).clear();
    }

    /**
     * Método para adicionar uma rota a lista de rotas por par origem destino
     * 
     * @param orN Nó de origem 
     * @param deN Nó de destino
     * @param route Routa a ser adicionada
     */
    private void addRoute(int orN, int deN, Route route) {
        allRoutes.get(orN * this.numberOfNodesInTopology + deN).add(route);
    }


    @Override
    public String toString() {

        String txt = "";

        for (List<Route> routes : allRoutes){
            for (Route route : routes){
                if (route != null){
                    txt += route;
                }
            }
        }

        return txt;
    }
    
    /**
     * Método para salvar todas as rotas em um arquivo de texto
     * 
     * @param folderManager
     */
    public void save(FolderManager folderManager) {
        folderManager.writeRoutes((String.valueOf(this)));
    }

    /**
     * Método para retornar a lista das K rotas para um determinado para origem destino.
     * 
     * @param source Nó de origem
     * @param destination Nó de destino
     * @return Uma lista com K rotas 
     */
    public List<Route> getRoutesForOD(int source, int destination) {
        return allRoutes.get(source * this.numberOfNodesInTopology + destination);
    }
}
