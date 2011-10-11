
package bootsie;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Justin Payne
 */
public abstract class MathCore {
    //catch-all class of static methods for these statistical tests.
    
    public static double doubleCoV(ArrayList<Double> numbers){
        //determine coefficient of variation for an array of double-precision
        //floating-point values.
        double cov = 0.0;
        double stdDev = 0.0;
        double mean = MathCore.doubleMean(numbers);
        for (Double value: numbers){ //for each locus in loci
            stdDev += Math.pow((value - mean), 2); //(value - mean) squared
        }
        stdDev = Math.sqrt(stdDev / (double) numbers.size());
        
        if (mean == 0.0) {
            cov = Double.NaN; //return a non-number (NotANumber)
        } else {
            cov = stdDev / mean;
        }
        return cov;
        
    }
    
    public static double doubleMean(ArrayList<Double> numbers){
        //determine arithmetic mean for an array of double-precision floating-
        //point values.
        double mean = 0.0;
        for (Double value: numbers){ //for each value in numbers
            mean += value;
        }
        mean = mean / (double) numbers.size();
        return mean;
    }
    
    public static Double getCoVOfOneBootstrap(PopulationMatrixModel data, int bootstrapVal){
        //a method to produce a random-with-replacement sample of N loci from the population
        ArrayList<Integer> picks = new ArrayList<>();
        int lociSize = data.getLength();
        for (int i = 0; i < bootstrapVal; i++){
            picks.add(new Integer((int) (Math.random() * lociSize)));
        }
        PopulationMatrix bootstrap = data.getBootstrap(picks);
        bootstrap.populateGeneticDistanceMatrix();
        return MathCore.doubleCoV(bootstrap.getGeneticDistances());
        
    }
    
    public static void bootstrapCoefficientOfVariance(PopulationMatrixModel data, BootstrapMonitor monitor){
        monitor.startingOp();
        ArrayList<Double> covResultsArray = new ArrayList<>();
        int lociSize = data.getLength();
        for (int i = 1; i <= lociSize; i++){
            ArrayList<Double> coefficients = new ArrayList<>();
            int n;
            for (n = 1; n <= data.numBootstraps; n++){
                Double cov = MathCore.getCoVOfOneBootstrap(data, i);
                if ((cov.equals(Double.NaN)) == false){ //div by zero or something
                    coefficients.add(cov);
                    monitor.completeOneOp();
                } else {
                    //if cov was not a number, don't count this bootstrap
                    n--;
                }
            }
            double meanCoV = MathCore.doubleMean(coefficients);
            covResultsArray.add(meanCoV);
            
        }
        
        data.coefficientsOfVariation = covResultsArray;
        monitor.completeAllOps();
    }
    
    public static ArrayList<Double> bootstrapCovTest(PopulationMatrixModel data, int numTests) {
        //estimator function. Returns actual CoV values to evade compiler optimization.
        ArrayList<Double> covArray = new ArrayList<>();
        double covLocus = 0;
        int n;
        for (n = 0; n <= numTests; n++) {
            double cov = MathCore.getCoVOfOneBootstrap(data, data.getLength() / 2);
            covArray.add(cov);
        }
        return covArray;
    }
    
    public static double geneticDistance(DataSample a, DataSample b){
        double geneticDistance = 0.0;
        Iterator<Byte> ia = a.iterator();
        Iterator<Byte> ib = b.iterator();
        double match = 0.0;
        double mismatch = 0.0;
        while (ia.hasNext() && ib.hasNext()){
            Byte i = ia.next();
            Byte j = ib.next();
            //System.out.println(i + " and " + j);
            if (i == -1 || j == -1){
                //do nothing; ignore loci where data cannot be compared
            } else {
                if (i.equals(j)){
                    match++;
                } else {
                    mismatch++;
                }
            }
            
        }
        geneticDistance = mismatch / (mismatch + match);
        return geneticDistance;
    }
}
