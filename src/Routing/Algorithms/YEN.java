package Routing.Algorithms;

import java.util.ArrayList;
import java.util.List;
import Network.TopologyManager;
import Network.Structure.OpticalLink;
import Network.Structure.OpticalSwitch;
import Routing.Route;

public class YEN extends RoutingAlgorithm{
    

    /**
     * Construtor da classe YEN
     */
    public YEN(){
        
    }

    /**
     * Método YEN para encontrar o melhor conjunto de K rotas para a topologia para um determinado para origem destino
     * 
     * @param orNode ID do nó de origem 
     * @param deNode ID do nó de destino
     * @param topology Classe que controla a topologia
     * @param K Tamanho do conjunto a ser buscado
     * @return Lista com a rotas encontradas
     */
    public static List<Route> findRoute(int orNode, int deNode, TopologyManager topology, final int K) {
    
        // Lista A contém os caminhos já encontrados
        List<Route> routesYEN = new ArrayList<>();
        int kValue = 1;
        // Fila B contém os caminhos candidatos
        List<Route> candidateRoutes = new ArrayList<>();
        
        // Encontra o primeiro caminho mais curto usando Dijkstra
        Route shortestRoute = Dijkstra.findRoute(orNode, deNode, topology);

        if (shortestRoute == null) {
            return routesYEN; // não há caminho entre origem e destino
        }
        routesYEN.add(shortestRoute); // adiciona o primeiro caminho à lista routesYEN

        for (int k = 1; k < K; k++) { // repete até encontrar K caminhos

            Route previousRoute = routesYEN.get(k - 1); // obtém o último caminho encontrado

            int routePath = previousRoute.getPath().size();

            for (int i = 0; i < routePath - 1; i++) { // para cada nó no caminho anterior, exceto o destino

                int spurNodeID = previousRoute.getPath().get(i); // ID do nó atual do caminho anterior

                Route rootRoute = YEN.createSubRoute(previousRoute, 0, i, topology);

                List<OpticalSwitch> removedNodes = new ArrayList<>(); // lista de nós removidos temporariamente do grafo
                List<OpticalLink> removedEdges = new ArrayList<>(); // lista de arestas removidas temporariamente do grafo

                for (Route r : routesYEN) { 

                    if(i < r.getNumNodes()){
                        shortestRoute = YEN.createSubRoute(r, 0, i, topology);

                        if (YEN.checkPathEquals(rootRoute.getPath(), shortestRoute.getPath())){ 
                            int nextNode = r.getNodeID(i + 1); 

                            OpticalLink link = topology.getLink(spurNodeID, nextNode);

                            link.setLinkState(false);
                            removedEdges.add(link); 
                        }
                    }
                }

                for (int n : rootRoute.getPath()) { 
                    if (n != spurNodeID) {

                        OpticalSwitch opticalSwitch = topology.getNode(n); 
                        opticalSwitch.setNodeState(false);
                        removedNodes.add(opticalSwitch);
                    }
                }

                Route spurPath = Dijkstra.findRoute(spurNodeID, deNode, topology); 

                if (spurPath != null) {
                    Route totalRoute = YEN.mergeRoute(rootRoute, spurPath, topology); 

                    if (!YEN.isRouteIn(totalRoute, candidateRoutes)){
                        candidateRoutes.add(totalRoute);
                    }
                }

                topology.restoreEdges(removedEdges);
                topology.restoreNodes(rootRoute.getPath());
            }

            if (candidateRoutes.isEmpty()) {
                break; 
            }

            // Sorteia as potenciais rotas pelo custo
            Route newPath = YEN.sortRoutesByCost(candidateRoutes);
            
            newPath.setKFindIndex(kValue);
            kValue++;
            routesYEN.add(newPath); 

            candidateRoutes.remove(newPath);

        }

        return routesYEN; 
    }

    /**
     * Método para verificar se a rota totalRoute esta contida no conjunto candidateRoutes
     * 
     * @param totalRoute Rota
     * @param candidateRoutes Conjunto para verificar
     * @return True se a rota totalRoute esta contida no conjunto candidateRoutes, e false caso contrario
     */
    private static boolean isRouteIn(Route totalRoute, List<Route> candidateRoutes) {

        GOTO: for (Route route : candidateRoutes) {
            
            if (totalRoute.getPath().size() == route.getPath().size()) {
                int same = 0;
                for (int iPathIndex = 0; iPathIndex < totalRoute.getPath().size(); iPathIndex++) {
                    if (totalRoute.getPath().get(iPathIndex) == route.getPath().get(iPathIndex)) {
                        same++;
                    } else {
                        continue GOTO;
                    }
                }

                if (same == totalRoute.getPath().size()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Método para ordenar a rota de acordo com o menor custo das rotas e retornar a melhor rota com base no custo
     * 
     * @param candidateRoutes Conjunto de rotas canditadas a ser ordenada
     * @return Retorna a melhor rota com base no custo
     */
    private static Route sortRoutesByCost(final List<Route> candidateRoutes) {

        List<Route> candidateRoutesClone = new ArrayList<Route>();

        for (Route route : candidateRoutes) {
            candidateRoutesClone.add(route);
        }

        List<Route> routesOrder = new ArrayList<Route>();

        LOOP_ROUTE : while ((routesOrder.size() < 1) && (candidateRoutesClone.size() > 0)){
            double minCost = Double.MAX_VALUE;
            int bestRouteIndex = 0;
            for (int r = 0; r < candidateRoutesClone.size(); r++){
                if (minCost > candidateRoutesClone.get(r).getCost()){
                    minCost = candidateRoutesClone.get(r).getCost();
                    bestRouteIndex = r;
                }
            }

            Route route = candidateRoutesClone.get(bestRouteIndex);

            //FIXME:
            // Avalia se a rota aceita o SNR, quando utilizada a camada física
            // if (ParametersSimulatison.getPhysicalLayerOption().equals(PhysicalLayerOption.Enabled)){
            //     ModulationLevelType lsessModulation = ParametersSimulation.getMudulationLevelType()[ParametersSimulation.getMudulationLevelType().length - 1];
    
            //     int biggestBitRate = ParametersSimulation.getTrafficOption()[ParametersSimulation.getTrafficOption().length - 1]; 
    
            //     final double snrLinear = Math.pow(10, lessModulation.getSNRIndB()/10);

            //     final double osnrLinear = (((double) biggestBitRate * 1e9) / (2 * SimulationParameters.getSpacing())) * snrLinear;

            //     final double inBoundQot = Function.evaluateOSNR(route);

            //     if(inBoundQot < osnrLinear){
            //         routesYEN.remove(bestRouteIndex);
            //         continue LOOP_ROUTE;			
            //     }
            // }

            routesOrder.add(route);
            candidateRoutesClone.remove(bestRouteIndex);
        }

        return routesOrder.get(0);
    }

    /**
     * Cria uma nova rota contendo um fragmento da rota original. 
     * 
     * @param route Rota original
     * @param ind1 ID inicial da nova rota
     * @param ind2 ID final da nova rota
     * @param topology Classe de topologia 
     * @return A nova rota 
     */
    private static Route createSubRoute(Route route, int ind1, int ind2, TopologyManager topology) {
        
        List<Integer> newPath = new ArrayList<Integer>();
        
        for(int a = ind1; a <= ind2; a++){
            newPath.add(route.getNodeID(a));
        }
        
        return new Route(newPath, topology);
    }

    /**
     * Cria uma nova rota com o começo do caminho da rota 1 e o continuando com a rota 2 
     * 
     * @param route1 Rota 1: Ínicio
     * @param route2 Rota 2: Final
     * @param topology Classe com a topologia da rede
     * @return A nova rota com a juntão de ambas
     */
    private static Route mergeRoute(Route route1, Route route2, TopologyManager topology) {

        List<Integer> newPath = route1.getPath();
        
        for(int a = 1; a < route2.getNumNodes(); a++){
            newPath.add(route2.getNodeID(a));
        }
        
        return new Route(newPath, topology);
    }

    /**
     * Verifica se o caminho de ambas as rotas são idênticos, retorna true se forem iguais
     * 
     * @param path1 Caminho do uplink da primeira rota
     * @param path2 Caminho do uplink da segunda rota
     * @return True se os caminhos forem iguais e false se os caminhos forem diferentes
     */
    private static boolean checkPathEquals(List<Integer> path1, List<Integer> path2){
        
        if (path1.size() != path2.size()){
            return false;
        }

        for (int index = 0; index < path1.size(); index++){
            if (path1.get(index) != path2.get(index)){
                return false; 
            }
        }

        return true;
    }
}
