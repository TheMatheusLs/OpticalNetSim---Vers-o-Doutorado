classDiagram
    
    Runner o-- FolderManager
    Runner o-- Simulation
    Simulation o-- FolderManager
    Simulation o-- Topology
    Simulation o-- RoutesManager
    OpticalSpan o-- OpticalAmplifier
    TopologyGeneral o-- OpticalSwitch
    TopologyGeneral <-- TopologyNSFNet
    TopologyGeneral <-- GainAlgorithm
    TopologyGeneral o-- OpticalLink
    Topology o-- OpticalSwitch
    Topology <-- TopologyGeneral
    Topology o-- OpticalLink
    OpticalLink o-- OpticalAmplifier
    OpticalSpan o-- OpticalFiber
    OpticalLink o-- OpticalSpan

    class Runner {
        + main()
    }

    class FolderManager {
        - String folderName
        - String folderPath
        - boolean status

        + FolderManager(String tagName)
        - void writeFile(String fileName, String content)
        + void writeDone(double totalTime)
        + void writeParameters()
        + void writeTopology(String content)
        + void writeRoutes(String content)
        + void writeResults(String content)
    }

    class Simulation {
        - FolderManager folderManager
        - int[] seedsForLoad
        - Random randomGeneration
        - long currentRandomSeed

        - Topology topology

        + Simulation(FolderManager folderManager)
        - int[] generateRandomSeeds()
        + void inicialize()
    }

    %% Estrutura de Roteamento
    class RoutesManager {




    }


    %% Estrutura da rede

    class GainAlgorithm {
        - GainAlgorithm GAIN_INSTANCE
        - GainAlgorithmType metricType
        + GainAlgorithm(GainAlgorithmType metricType)
        + void configureGain(OpticalLink link)
        - void configureGainInLink(OpticalLink link)
        - GainAlgorithm getGainInstance()
    }

    class OpticalSwitch {
        - int opticalSwitchID
        - boolean nodeWorking
        - List<OpticalSwitch> neighborNodes

        + OpticalSwitch(int opticalSwitchID, double atenuationIndB, double laserPowerIndBm, double laserOSNRindB)
        + boolean isEquals(OpticalSwitch right)
        + void addNeighborNode(OpticalSwitch node)
    }

    class OpticalAmplifier{
        - double gainIndB
        - double noiseFactorIndB

        + OpticalAmplifier()
        + OpticalAmplifier(gainIndB: double, noiseFactorIndB: double)

        + double getNoiseFactorInLinear()
    }

    class OpticalFiber {
        - double length

        + OpticalFiber(double length)
    }

    class OpticalSpan {
        - int spanID
        - OpticalFiber opticalFiber
        - OpticalAmplifier opticalAmplifier
        - int numberOfSlots
        - double totalPower    
        - double[] powers

        + OpticalSpan(int spanID, int numberOfSlots, OpticalFiber opticalFiber, OpticalAmplifier opticalAmplifier)
        - void inicializePowersInSpan(int numberOfSlots)
        + void deallocate(int slot)
        + void deallocateTotalPower(int slot)
    }

    class OpticalLink {
        - int opticalLinkID
        - double length
        - int srlg
        - int sourceNode
        - int destinationNode
        - OpticalAmplifier booster
        - List<OpticalSpan> spans
        - double cost;
        - double[]  powersA;
        - double  totalPowerInA;
        - double[]  powersB;
        - double  totalPowerInB;
        - double[] frequencies;

        - boolean linkState;

        + OpticalLink(int opticalLinkID, int sourceNode, int destinationNode, int srlg, double length)
        - void inicializePowersAndFrequencies()
        - List<OpticalSpan> configureSpansInLink(double length)
        + void deallocate(int slot)
        + void deallocateTotalPower(int slot)
    }

    class Topology {
        - OpticalSwitch[] listOfNodes;
        - OpticalLink[][] networkOpticalLinks;
        - int numberOfNodes;
        - double maxLinkLength;
        - double[][] linksLengths;

        + Topology()
        + void inicialize()
        - void setNodesNeighbors()
        - void setLinksInitCost()
        + OpticalLink getOpticalLink(int indexSource, int indexDestination)
    }

    class TopologyGeneral {
        - GainAlgorithm gainAlgorithm = GainAlgorithm.getGainInstance()
        - double[][] lengthsTopology
        - int numberOfNodes
        - double maxLength
        - OpticalLink[][] linksAdjacencyMatrix 
        - OpticalSwitch[] opticalNodes

        - void buildNetworkAdjacencyMatrix()
        - void buildNodes()
    }

    class TopologyNSFNet{
        - int numberOfNodes = 14
        + TopologyNSFNet()
        + double[][] getLength()
    }
