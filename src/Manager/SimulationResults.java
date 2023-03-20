package Manager;

/*
 * Classe para armazenar os resultados de cada simulação
 */
public class SimulationResults {
    
    /**
     * Carga da rede em Erlangs
     */
    private double networkLoad;
    /**
     * Número da simulação
     */
    private int nSim;
    /**
     * Probabilidade de bloqueio 
     */
    private double probabilityBlocking;
    /**
     * Tempo de execução da simulação
     */
    private double executionTime;
    /**
     * Número de ciclos MSCL da simulação
     */
    private double MSCLCycle;
    /**
     * Seed da simulação
     */
    private long currentRandomSeed;
    /**
     * Número de slots bloqueados por falta de slots
     */
    private long numBlockBySlots;
    /**
     * Número de slots bloqueados por falta de QoT
     */
    private long numBlockByQoT;

    /**
     * Construtor da classe
     * 
     * @param networkLoad
     * @param nSim
     * @param currentRandomSeed
     */
    public SimulationResults(double networkLoad, int nSim, long currentRandomSeed) {
        this.networkLoad = networkLoad;
        this.nSim = nSim;
        this.currentRandomSeed = currentRandomSeed;
    }

    public double getNetworkLoad() {
        return networkLoad;
    }

    public void setNetworkLoad(double networkLoad) {
        this.networkLoad = networkLoad;
    }

    public int getnSim() {
        return nSim;
    }

    public void setnSim(int nSim) {
        this.nSim = nSim;
    }

    public double getProbabilityBlocking() {
        return probabilityBlocking;
    }

    public void setProbabilityBlocking(double probabilityBlocking) {
        this.probabilityBlocking = probabilityBlocking;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public double getMSCLCycle() {
        return MSCLCycle;
    }

    public void setMSCLCycle(double mSCLCycle) {
        MSCLCycle = mSCLCycle;
    }

    public long getCurrentRandomSeed() {
        return currentRandomSeed;
    }

    public void setCurrentRandomSeed(long currentRandomSeed) {
        this.currentRandomSeed = currentRandomSeed;
    }

    public long getNumBlockByQoT() {
        return numBlockByQoT;
    }

    public void setNumBlockByQoT(long numBlockByQoT) {
        this.numBlockByQoT = numBlockByQoT;
    }

    public long getNumBlockBySlots() {
        return numBlockBySlots;
    }

    public void setNumBlockBySlots(long numBlockBySlots) {
        this.numBlockBySlots = numBlockBySlots;
    }

    @Override
    public String toString() {
        return "*** SimulationResults ***\nnetworkLoad=" + networkLoad + ", \nnSim=" + nSim + ", \nprobabilityBlocking="
                + probabilityBlocking + ", \nexecutionTime=" + executionTime + ", \nMSCLCycle=" + MSCLCycle
                + ", \ncurrentRandomSeed=" + currentRandomSeed + ", \nnumBlockBySlots=" + numBlockBySlots
                + ", \nnumBlockByQoT=" + numBlockByQoT + "\n\n";
    }
}
