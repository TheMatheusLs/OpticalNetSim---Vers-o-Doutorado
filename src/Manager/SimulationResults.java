package Manager;

/*
 * Classe para armazenar os resultados de cada simulação
 */
public class SimulationResults {
    
    private double networkLoad;
    private int nSim;
    private double probabilityBlocking;
    private double executionTime;
    private double MSCLCycle;
    private long currentRandomSeed;

    
    public SimulationResults(double networkLoad, int nSim, long currentRandomSeed) {
        this.networkLoad = networkLoad;
        this.nSim = nSim;
        this.currentRandomSeed = currentRandomSeed;
    }

    @Override
    public String toString() {
        return "networkLoad = " + networkLoad + ", \nnSim = " + nSim + ", \nprobabilityBlocking = " + probabilityBlocking + ", \nexecutionTime = " + executionTime + ", \nMSCLCycle = " + MSCLCycle + ", \ncurrentRandomSeed = " + currentRandomSeed +"\n\n";
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
}
