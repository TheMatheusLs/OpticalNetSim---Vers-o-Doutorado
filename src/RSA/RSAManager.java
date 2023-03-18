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
import Spectrum.Algorithms.FirstFit;
import Spectrum.Algorithms.SpectrumAlgorithm;
import Types.GeneralTypes.KSortedRoutesByType;
import Types.GeneralTypes.RoutingAlgorithmType;
import Types.GeneralTypes.SpectralAllocationAlgorithmType;

public class RSAManager {

    RoutingAlgorithm routingAlgorithm;
    SpectrumAlgorithm spectrumAlgorithm;
    TopologyManager topologyManager;
    RoutesManager routesManager;

    Route route;
    List<Integer> fSlots;

    public RSAManager(TopologyManager topology, RoutesManager routesManager) {
        this.topologyManager = topology;
        this.routesManager = routesManager;

        this.route = null;
        this.fSlots = new ArrayList<Integer>();

        this.routingAlgorithm = null;
        this.spectrumAlgorithm = null;

        RoutingAlgorithmType routingOption = ParametersSimulation.getRoutingAlgorithmType();
        SpectralAllocationAlgorithmType spectrumOption = ParametersSimulation.getSpectralAllocationAlgorithmType();

        // Verifica se o Roteamento escolhido é Dijkstra e o First-Fit
        if (routingOption.equals(RoutingAlgorithmType.Dijstra)){
            this.routingAlgorithm = new Dijkstra();
        }


        // Verifica se o Spectrum escolhido é o FF
        if (spectrumOption.equals(SpectralAllocationAlgorithmType.FirstFit)){
            this.spectrumAlgorithm = new FirstFit();
        }

    }

    public void findRSA(int source, int destination, CallRequest callRequest) {
        // Captura as rotas para o par origem destino
        List<Route> routeSolution = this.routesManager.getRoutesForOD(source, destination);

        // Realiza a ordenacao do conjunto de rotas conforme seleção
        if (ParametersSimulation.getKSortedRoutesByType() != KSortedRoutesByType.None){
            //TODO: Ordenar as rotas por ocupação e demais formas
        }

        this.route = this.routingAlgorithm.selectRoute(routeSolution);

        // Calcula o tamanho da requisição
        int reqNumbOfSlots = this.route.getReqSize(callRequest.getSelectedBitRate());
        callRequest.setReqNumbOfSlots(reqNumbOfSlots);
        
        this.fSlots = this.spectrumAlgorithm.findFrequencySlots(reqNumbOfSlots, this.route);
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
