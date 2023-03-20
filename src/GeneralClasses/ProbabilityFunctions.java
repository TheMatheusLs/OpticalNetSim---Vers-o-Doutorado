package GeneralClasses;

import java.util.Random;

/*
 * Classe para gerar número aleatório
 */
public class ProbabilityFunctions {
    
    /**
     * Método para gerar um número aleatório com distribuição uniforme.
     * 
     * @param networkLoad
     * @param rand
     * @return
     */
    public static double exponentialDistribution(double networkLoad, Random rand) {
        return - Math.log(1 - rand.nextDouble())  / networkLoad;
    }
}
