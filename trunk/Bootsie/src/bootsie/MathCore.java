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
    
    public static double locusCoV(ArrayList <Byte>loci){
        double cov = 0;
        double stdDev = 0;
        //iterate over loci and produce the std dev and coefficent of variance
        double mean = MathCore.locusMean(loci);
        Iterator<Byte> i = loci.iterator();
        while (i.hasNext()){
            double v = i.next();
            stdDev = Math.pow((mean - v), 2);
        }
        stdDev = Math.sqrt(stdDev / (loci.size() - 1));
        
        cov = stdDev / Math.abs(mean);
        return cov;
        
    }
    
    public static double locusMean(ArrayList <Byte>loci){
        double mean = 0;
        //iterate over loci and produce the mean
        Iterator<Byte> i = loci.iterator();
        while (i.hasNext()){
            mean += i.next();
        }
        mean /= loci.size();
        return mean;
    }
    
    public static ArrayList<Byte> getBootstrap(PopulationMatrixModel data, int bootstrapVal){
        ArrayList<Byte> bootstrapLoci = new ArrayList<Byte>();
        ArrayList<Integer> picks = new ArrayList<Integer>();
        int lociSize = data.getLength();
        for (int i = 0; i < bootstrapVal; i++){
            picks.add(new Integer((int) (Math.random() * lociSize)));
        }
        Iterator<Integer> it = picks.iterator();
        while (it.hasNext()){
            bootstrapLoci.addAll(data.getAllNthLoci(it.next()));
        }
        
        return bootstrapLoci;
    }
    
    public static void bootstrapCoefficientOfVariance(PopulationMatrixModel data, BootstrapMonitor monitor){
        monitor.startingOp();
        ArrayList<Double> covArray = new ArrayList<Double>();
        int lociSize = data.getLength();
        for (int i = 1; i <= lociSize; i++){
            double covLocus = 0;
            int n;
            for (n = 1; n <= data.numBootstraps; n++){
                covLocus += MathCore.locusCoV(MathCore.getBootstrap(data, i));
            }
            covLocus = covLocus / (n - 1);
            covArray.add(covLocus);
            monitor.completeOneOp();
        }
        
        data.coefficientsOfVariation = covArray;
        monitor.completeAllOps();
    }
    
    public static ArrayList<Double> bootstrapCovTest(PopulationMatrixModel data, int numTests){
        //estimator function. Has to return actual CoV values to evade compiler optimization.
        ArrayList<Double> covArray = new ArrayList<Double>();
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
    
}
