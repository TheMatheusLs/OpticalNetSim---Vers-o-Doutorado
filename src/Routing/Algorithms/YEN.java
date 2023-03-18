package Routing.Algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import Network.TopologyManager;
import Network.Structure.OpticalLink;
import Network.Structure.OpticalSwitch;
import Routing.Route;

public class YEN {
    

    public YEN(){
        
    }

    public static List<Route> findRoute(int orNode, int deNode, TopologyManager topology, final int K) {
        

        if (orNode == 1 && deNode == 0){
            System.out.println("debug");
        }

        // Lista A contém os caminhos já encontrados
        List<Route> routesYEN = new ArrayList<>();
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

            List<Integer> routePath = previousRoute.getPath();

            for (int i = 0; i < routePath.size() - 1; i++) { // para cada nó no caminho anterior, exceto o destino

                int spurNodeID = routePath.get(i); // ID do nó atual do caminho anterior

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
                    candidateRoutes.add(totalRoute);
                }

                topology.restoreEdges(removedEdges);
                topology.restoreNodes(removedNodes);
            }

            if (candidateRoutes.isEmpty()) {
                break; 
            }

            Route newPath = candidateRoutes.get(0); 
            routesYEN.add(newPath); 

            candidateRoutes.clear();

        }

        return routesYEN;
        
    }


    private static Route createSubRoute(Route route, int ind1, int ind2, TopologyManager topology) {
        
        List<Integer> newPath = new ArrayList<Integer>();
        
        for(int a = ind1; a <= ind2; a++){
            newPath.add(route.getNodeID(a));
        }
        
        return new Route(newPath, topology);

    }


    private static Route mergeRoute(Route route1, Route route2, TopologyManager topology) {

        List<Integer> newPath = route1.getPath();
        
        for(int a = 1; a < route2.getNumNodes(); a++){
            newPath.add(route2.getNodeID(a));
        }
        
        return new Route(newPath, topology);
    }


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
