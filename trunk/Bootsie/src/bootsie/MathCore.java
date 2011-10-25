package bootsie;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Justin Payne
 */
public abstract class MathCore {
    //catch-all class of static methods for these statistical tests.
    
    public static double doubleStdDev(ArrayList<Double> numbers, double mean){
        //determine coefficient of variation for an array of double-precision
        //floating-point values.
        double stdDev = 0.0;
        double variance = 0.0;
        for (Double value: numbers){ //for each locus in loci
            variance += Math.pow((value - mean), 2); //(value - mean) squared
        }
        variance /= (double) (numbers.size() - 1);
        stdDev = Math.sqrt(variance);
        return stdDev;
    }
    
    public static double doubleMean(ArrayList<Double> numbers){
        //determine arithmetic mean for an array of double-precision floating-
        //point values.
        //System.out.println(numbers);
        double mean = 0.0;
        for (Double value: numbers){ //for each value in numbers
            mean += value;
        }
        mean = mean / (double) numbers.size();
        return mean;
    }
    
    public static ArrayList<Double> getCoVOfOneBootstrap(PopulationMatrixModel data, int bootstrapVal){
        //a method to produce a random-with-replacement sample of N loci from the population
        ArrayList<Integer> picks = new ArrayList<Integer>();
        int lociSize = data.getLength();
        for (int i = 0; i < bootstrapVal; i++){
            picks.add(new Integer((int) (Math.random() * lociSize - 1) + 1));
        }
        PopulationMatrix bootstrap = data.getBootstrap(picks);
        bootstrap.populateGeneticDistanceMatrix();
        ArrayList<Double> values = bootstrap.getGeneticDistances();
//        double mean = doubleMean(values);
//        double stdDev = doubleStdDev(values, mean);
//        double cov = Double.NaN;
//        if (mean != 0.0){
//            cov = stdDev / mean;
//        }
        return values;
    }
    
    public static void bootstrapCoefficientOfVariance(PopulationMatrixModel data, BootstrapMonitor monitor){
        monitor.startingOp();
        ArrayList<Double> covResultsArray = new ArrayList<Double>();
        ArrayList<Double> meanResultsArray = new ArrayList<Double>();
        ArrayList<Double> stdDevResultsArray = new ArrayList<Double>();
        covResultsArray.add(1.0);
        meanResultsArray.add(1.0);
        stdDevResultsArray.add(1.0);
        int lociSize = data.getLength();
        int numBadStraps = 0;
        for (int i = 2; i <= lociSize; i++){
            ArrayList<Double> distances = new ArrayList<Double>();
            double mean;
            double stdDev;
            double cov;
            int n;
            for (n = 1; n <= data.numBootstraps; n++){
                
                distances.addAll(MathCore.getCoVOfOneBootstrap(data, i));
                monitor.completeOneOp();
//                if ((results.cov.equals(Double.NaN)) == false){ //div by zero or something
//                    coefficients.add(results.cov);
//                    means.add(results.mean);
//                    stdDevs.add(results.standardDev);
//                    monitor.completeOneOp();
//                } else {
//                    numBadStraps++;
//                    //System.out.println(numBadStraps + " th bad boostrap.");
//                }
            }
            mean = MathCore.doubleMean(distances);
            stdDev = MathCore.doubleStdDev(distances, mean);
            cov = Double.NaN;
            if (mean != 0.0){
                cov = stdDev / mean;
            } else {
                numBadStraps++;
            }
            covResultsArray.add(cov);
            meanResultsArray.add(mean);
            stdDevResultsArray.add(stdDev);
            
        }
        
        data.coefficientsOfVariation = new BootsieCoVReport(covResultsArray, meanResultsArray, stdDevResultsArray, numBadStraps);
        monitor.completeAllOps();
    }
    
    public static void bootstrapPairwiseCoefficientOfVariation(PopulationMatrixModel data, BootstrapMonitor monitor){
        monitor.startingOp();
        ArrayList<Double> covResultsArray = new ArrayList<Double>();
        ArrayList<Double> meanResultsArray = new ArrayList<Double>();
        ArrayList<Double> stdDevResultsArray = new ArrayList<Double>();
        covResultsArray.add(1.0);
        meanResultsArray.add(1.0);
        stdDevResultsArray.add(1.0);
        int lociSize = data.getLength();
        int numBadStraps = 0;
        for (int i = 2; i <= lociSize; i++){
            ArrayList<Double> covArray = new ArrayList<Double>();
            ArrayList<Double> meanArray = new ArrayList<Double>();
            ArrayList<Double> stdDevArray = new ArrayList<Double>();
            DeepDistanceMatrix matrix = new DeepDistanceMatrix(data.samples);
            for (int n = 0; n < data.numBootstraps; n++){
                ArrayList<Integer> picks = new ArrayList<Integer>(i);
                for (int p = 0; p < i; p++){
                    //pick i random loci
                    picks.add(new Integer((int) (Math.random() * (lociSize - 1)) + 1));
                }
                PopulationMatrix bootstrap = data.getBootstrap(picks);
                bootstrap.populateGeneticDistanceMatrix();
                matrix.addDistanceMatrix(bootstrap.geneticDistanceMatrix);
                monitor.completeOneOp();
            }
            //we now have a deep distance matrix of N genetic distances from N bootstraps
            ArrayList<ArrayList<Double>> matrixValues = matrix.getValues();
            for (ArrayList<Double> values : matrixValues){
                double mean = MathCore.doubleMean(values);
                double stdDev = MathCore.doubleStdDev(values, mean);
                double cov = Double.NaN;
                if (mean != 0.0) {
                    cov = stdDev / mean;
                } else {
                    numBadStraps++;
                }
                covArray.add(cov);
                meanArray.add(mean);
                stdDevArray.add(stdDev);
                //monitor.completeOneOp();
            }
            double mean = MathCore.doubleMean(covArray);
            double stdDev = MathCore.doubleStdDev(covArray, mean);
            double cov = stdDev / mean;
            covResultsArray.add(cov);
            meanResultsArray.add(mean);
            stdDevResultsArray.add(stdDev);
            
        }
        
        
        data.coefficientsOfVariation = new BootsieCoVReport(covResultsArray, meanResultsArray, stdDevResultsArray, numBadStraps);
        monitor.completeAllOps();
    }
    
    public static ArrayList<Double> bootstrapCovTest(PopulationMatrixModel data, int numTests) {
        //estimator function. Returns actual CoV values to evade compiler optimization.
        ArrayList<Double> covArray = new ArrayList<Double>();
        int n;
        for (n = 0; n <= numTests; n++) {
            MathCore.getCoVOfOneBootstrap(data, (int) (data.getLength() * .40));
        }
        return covArray;
    }
    

    
        public static double simpleGeneticSimilarity(DataSample a, DataSample b){
        //simple coincidence genetic distance; GD_ij = sum(i = j) / sum(i = j) + sum (i != j)
       //Sokal and Michener 1958
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
        geneticDistance = match / (mismatch + match);
        return geneticDistance;
    }
        
        
        public static double simpleGeneticDistance(DataSample a, DataSample b){
        //simple coincidence genetic distance; GD_ij = sum(i != j) / sum(i = j) + sum (i != j)
       //Sokal and Michener 1958
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
        
    public static double jaccardGeneticDistance(DataSample a, DataSample b){
        //compliment of jaccard's similarity
       //Jaccard 1908
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
                if (i == 1 && j == 1){
                    match++;
                } else if (i == 0 && j == 0) {
                   //discard 00
                } else{
                    mismatch++;
                }
            }
            
        }
        geneticDistance = mismatch / (mismatch + match);
        return geneticDistance;
    }
    
    public static double diceNeiGeneticDistance(DataSample a, DataSample b){
       //Dice 1945, Nei and Li 1979
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
                if (i == 1 && j == 1){
                    match++;
                } else if (i == 0 && j == 0) {
                   //discard 00
                } else {
                    mismatch++;
                }
            }
            
        }
        geneticDistance = 1 - ((match * 2) / ((match * 2) + mismatch));
        return geneticDistance;
    }
}
//TODO: implement genetic distance/similarity measures as inner classes that implement
//"genetic distance" interface