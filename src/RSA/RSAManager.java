package RSA;

import java.util.ArrayList;
import java.util.List;

import CallRequests.CallRequest;
import Config.ParametersSimulation;
import Network.TopologyManager;
import Routing.Route;
import Routing.RoutesManager;
import Routing.Algorithms.Dijkstra;
import Routing.Algorithms.RoutingAlgorithm;
import Routing.Algorithms.YEN;
import Spectrum.Algorithms.FirstFit;
import Spectrum.Algorithms.SpectrumAlgorithm;
import Types.GeneralTypes.KSortedRoutesByType;
import Types.GeneralTypes.RoutingAlgorithmType;
import Types.GeneralTypes.SpectralAllocationAlgorithmType;

public class RSAManager {

    private RoutingAlgorithm routingAlgorithm;
    private SpectrumAlgorithm spectrumAlgorithm;
    private TopologyManager topologyManager;
    private RoutesManager routesManager;

    private Route route;
    private List<Integer> fSlots;

    private int numberOfRoutesToFind;

    public RSAManager(TopologyManager topology, RoutesManager routesManager) {
        this.topologyManager = topology;
        this.routesManager = routesManager;

        this.route = null;
        this.fSlots = new ArrayList<Integer>();

        this.routingAlgorithm = null;
        this.spectrumAlgorithm = null;

        RoutingAlgorithmType routingOption = ParametersSimulation.getRoutingAlgorithmType();
        SpectralAllocationAlgorithmType spectrumOption = ParametersSimulation.getSpectralAllocationAlgorithmType();

        // Determina o número de rotas a ser encontrada pelo algoritmo
        // Verifica se o Roteamento escolhido é Dijkstra e o First-Fit
        if (routingOption.equals(RoutingAlgorithmType.Dijstra)){
            this.numberOfRoutesToFind = 1;
            this.routingAlgorithm = new Dijkstra();
        } else {
            // Usado para o algoritmo de roteamento YEN
            this.routingAlgorithm = new YEN();
            this.numberOfRoutesToFind = ParametersSimulation.getKShortestRoutes();
        }

        // Verifica se o Spectrum escolhido é o FF
        if (spectrumOption.equals(SpectralAllocationAlgorithmType.FirstFit)){
            this.spectrumAlgorithm = new FirstFit();
        }
    }

    public void findRouteAndSlots(int source, int destination, CallRequest callRequest) {

        this.findRoutingSA(source, destination, callRequest);

    }

    private void findRoutingSA(int source, int destination, CallRequest callRequest) {
        // Captura as rotas para o par origem destino
        List<Route> routeSolution = this.routesManager.getRoutesForOD(source, destination);

        // Realiza a ordenacao do conjunto de rotas conforme seleção
        if (ParametersSimulation.getKSortedRoutesByType() != KSortedRoutesByType.None){
            //TODO: Ordenar as rotas por ocupação e demais formas
        }

        this.route = null;
        this.fSlots = null;

        // Algoritmo para o RSA
        for (Route currentRoute : routeSolution){

            // Calcula o tamanho da requisição
            int reqNumbOfSlots = currentRoute.getReqSize(callRequest.getSelectedBitRate());
            
            List<Integer> slots = this.spectrumAlgorithm.findFrequencySlots(reqNumbOfSlots, currentRoute);
            
            if(!slots.isEmpty() && slots.size() == reqNumbOfSlots){
                this.route = currentRoute;
                this.fSlots = slots;
                callRequest.setReqNumbOfSlots(reqNumbOfSlots);
                break;
            } 
        }
    }


    private void routingSA(List<Route> routesOD, int reqNumbOfSlots, SpectrumAlgorithm spectrumAlgorithm){

    }

    public Route getRoute() {
        return this.route;
    }

    public List<Integer> getSlotsIndex() {
        return this.fSlots;
    }
    
}
