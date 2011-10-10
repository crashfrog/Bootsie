/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bootsie;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Justin Payne
 */
public abstract class MathCore {
    //catch-all class of static methods for these statistical tests.
    //TODO: re-write this in most pseudo-code-like syntax - try to avoid "Java-ese."
    
    public static double locusCoV(ArrayList <Byte>loci){
        double cov = 0;
        double stdDev = 0;
        double mean = MathCore.locusMean(loci);
        for (Byte locus: loci){ //for each locus in loci
            double v = locus;
            stdDev = Math.pow((v - mean), 2);
        }
        stdDev = Math.sqrt(stdDev / (loci.size() - 1));
        
        if (mean != 0) {
            cov = stdDev / Math.abs(mean);
        }
        return cov;
        
    }
    
    public static double locusMean(ArrayList <Byte>loci){
        double mean = 0;
        for (Byte locus: loci){ //for each locus in loci
            mean += locus;
        }
        mean /= loci.size();
        return mean;
    }
    
    public static ArrayList<Byte> getBootstrap(PopulationMatrixModel data, int bootstrapVal){
        //a method to produce a random-with-replacement sample of N loci from the population
        ArrayList<Byte> bootstrapLoci = new ArrayList<>();
        ArrayList<Integer> picks = new ArrayList<>();
        int lociSize = data.getLength();
        for (int i = 0; i < bootstrapVal; i++){
            picks.add(new Integer((int) (Math.random() * lociSize)));
        }
        for (Integer randomNumber: picks){
            bootstrapLoci.addAll(data.getAllNthLoci(randomNumber));
        }
        
        return bootstrapLoci;
    }
    
    public static void bootstrapCoefficientOfVariance(PopulationMatrixModel data, BootstrapMonitor monitor){
        monitor.startingOp();
        ArrayList<Double> covArray = new ArrayList<>();
        int lociSize = data.getLength();
        for (int i = 1; i <= lociSize; i++){
            double covLocus = 0;
            int n;
            for (n = 1; n <= data.numBootstraps; n++){
                double cov = MathCore.locusCoV(MathCore.getBootstrap(data, i));
                if (cov != 0){
                    covLocus += cov;
                } else {
                    //if cov was zero, don't count this bootstrap
                    n--;
                }
            }
            covLocus = covLocus / n;
            covArray.add(covLocus);
            monitor.completeOneOp();
        }
        
        data.coefficientsOfVariation = covArray;
        monitor.completeAllOps();
    }
    
    public static ArrayList<Double> bootstrapCovTest(PopulationMatrixModel data, int numTests){
        //estimator function. Has to return actual CoV values to evade compiler optimization.
        ArrayList<Double> covArray = new ArrayList<>();
        int lociSize = data.getLength();
        int halfSize = (lociSize + 1) / 2;
        double covLocus = 0;
        int n;
        for (n = 0; n < numTests; n++) {
            covLocus += MathCore.locusCoV(MathCore.getBootstrap(data, halfSize));
        }
        covLocus = covLocus / n;
        covArray.add(covLocus);


        return covArray;
    }
    
    public static double geneticDistance(DataSample a, DataSample b){
        double geneticDistance = 0.0;
        
        Iterator<Byte> ia = a.iterator();
        Iterator<Byte> ib = a.iterator();
        double match = 0.0;
        double mismatch = 0.0;
        while (ia.hasNext() && ib.hasNext()){
            Byte i = ia.next();
            Byte j = ia.next();
            if (MathCore.isMissing(i) || MathCore.isMissing(j)){
                //do nothing; ignore loci where data cannot be compared
            } else {
                if (i == j){
                    match++;
                } else {
                    mismatch++;
                }
            }
            
        }
        geneticDistance = mismatch / (mismatch + match);
        return geneticDistance;
    }
    
    
    public static boolean isMissing(Byte b){
        if (b != 1 && b != 0){
            return true;
        } else {
            return false;
        }
    }
    
}
