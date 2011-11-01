package bootsie;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Justin Payne
 */
public class MathCore {
    //catch-all singleton class of static methods for these statistical tests.

    public static BootsieDistanceCalculator defaultCalculator;
    public static final MathCore instance = new MathCore();

    public MathCore() {
        defaultCalculator = new simpleGeneticSimilarityCalculator();
    }

    public static MathCore getInstance() {
        return instance;
    }

    public static double doubleStdDev(ArrayList<Double> numbers, double mean) {
        //determine coefficient of variation for an array of double-precision
        //floating-point values.
        double stdDev = 0.0;
        double variance = 0.0;
        for (Double value : numbers) { //for each locus in loci
            variance += Math.pow((value - mean), 2); //(value - mean) squared
        }
        variance /= (double) (numbers.size() - 1);
        stdDev = Math.sqrt(variance);
        return stdDev;
    }

    public static double doubleMean(ArrayList<Double> numbers) {
        //determine arithmetic mean for an array of double-precision floating-
        //point values.
        //System.out.println(numbers);
        double mean = 0.0;
        for (Double value : numbers) { //for each value in numbers
            mean += value;
        }
        mean = mean / (double) numbers.size();
        return mean;
    }
    
    public static double doubleCoV(ArrayList<Double> numbers){
       Double mean = doubleMean(numbers);
       Double stdDev = doubleStdDev(numbers, mean);
       Double cov = Double.NaN;
       if (mean != 0.0){
          cov = stdDev / mean;
       }
       return cov;
    }

   
    public static void bootstrapLinearPairwiseCoefficientofVariation(PopulationMatrixModel data, BootstrapMonitor monitor){
       monitor.startingOp();
       ArrayList<Double> covResultsArray = new ArrayList<Double>();
       int lociSize = data.getLength();
       int numBadStraps = 0;
       for (int i = 1; i <= lociSize; i++){
          ArrayList<DataSample> s = (ArrayList<DataSample>) data.samples.clone();
          ArrayList<Double> covs = new ArrayList<Double>();
          for (DataSample a : data.samples){
             s.remove(a);
             for (DataSample b : s) {
                ArrayList<Double> distances = new ArrayList<Double>();
                for (int n = 0; n < data.numBootstraps; n++) {
                   //random bootstrap with replacement
                   DataSample ab = new DataSample(a);
                   DataSample bb = new DataSample(b);
                   for (int p = 0; p < i; p++){
                      Integer r = new Integer((int) (Math.random() * lociSize - 1) + 1);
                      ab.loci.add(a.loci.get(r));
                      bb.loci.add(b.loci.get(r));
                   }
                   Double gd = MathCore.defaultCalculator.distance(a, b);
                   distances.add(gd);
                   monitor.completeOneOp();
                }
                Double cov = doubleCoV(distances);
                if (cov == Double.NaN){
                   numBadStraps++;
                } else {
                   covs.add(cov);
                }
             }
          }
          Double meanCov = doubleMean(covs);
          covResultsArray.add(meanCov);
       }
       data.coefficientsOfVariation = new BootsieCoVReport(covResultsArray, null, null, numBadStraps);
       monitor.completeAllOps();
    }

    protected class simpleGeneticSimilarityCalculator implements BootsieDistanceCalculator {

        @Override
        public double distance(DataSample a, DataSample b) {
            //simple coincidence genetic distance; GD_ij = sum(i = j) / sum(i = j) + sum (i != j)
            //Sokal and Michener 1958
            double geneticDistance = 0.0;
            Iterator<Byte> ia = a.iterator();
            Iterator<Byte> ib = b.iterator();
            double match = 0.0;
            double mismatch = 0.0;
            while (ia.hasNext() && ib.hasNext()) {
                Byte i = ia.next();
                Byte j = ib.next();
                //System.out.println(i + " and " + j);
                if (i == -1 || j == -1) {
                    //do nothing; ignore loci where data cannot be compared
                } else {
                    if (i.equals(j)) {
                        match++;
                    } else {
                        mismatch++;
                    }
                }

            }
            geneticDistance = match / (mismatch + match);
            return geneticDistance;
        }
    }

    protected class simpleGeneticDistanceCalculator implements BootsieDistanceCalculator {

        @Override
        public double distance(DataSample a, DataSample b) {
            //simple coincidence genetic distance; GD_ij = sum(i != j) / sum(i = j) + sum (i != j)
            //Sokal and Michener 1958
            double geneticDistance = 0.0;
            Iterator<Byte> ia = a.iterator();
            Iterator<Byte> ib = b.iterator();
            double match = 0.0;
            double mismatch = 0.0;
            while (ia.hasNext() && ib.hasNext()) {
                Byte i = ia.next();
                Byte j = ib.next();
                //System.out.println(i + " and " + j);
                if (i == -1 || j == -1) {
                    //do nothing; ignore loci where data cannot be compared
                } else {
                    if (i.equals(j)) {
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

    protected class jaccardDistanceCalculator implements BootsieDistanceCalculator {

        @Override
        public double distance(DataSample a, DataSample b) {
            //compliment of jaccard's similarity
            //Jaccard 1908
            double geneticDistance = 0.0;
            Iterator<Byte> ia = a.iterator();
            Iterator<Byte> ib = b.iterator();
            double match = 0.0;
            double mismatch = 0.0;
            while (ia.hasNext() && ib.hasNext()) {
                Byte i = ia.next();
                Byte j = ib.next();
                //System.out.println(i + " and " + j);
                if (i == -1 || j == -1) {
                    //do nothing; ignore loci where data cannot be compared
                } else {
                    if (i == 1 && j == 1) {
                        match++;
                    } else if (i == 0 && j == 0) {
                        //discard 00
                    } else {
                        mismatch++;
                    }
                }

            }
            geneticDistance = mismatch / (mismatch + match);
            return geneticDistance;
        }
    }

    protected class diceNeiDistanceCalculator implements BootsieDistanceCalculator {

        @Override
        public double distance(DataSample a, DataSample b) {
            //Dice 1945, Nei and Li 1979
            double geneticDistance = 0.0;
            Iterator<Byte> ia = a.iterator();
            Iterator<Byte> ib = b.iterator();
            double match = 0.0;
            double mismatch = 0.0;
            while (ia.hasNext() && ib.hasNext()) {
                Byte i = ia.next();
                Byte j = ib.next();
                //System.out.println(i + " and " + j);
                if (i == -1 || j == -1) {
                    //do nothing; ignore loci where data cannot be compared
                } else {
                    if (i == 1 && j == 1) {
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
}
