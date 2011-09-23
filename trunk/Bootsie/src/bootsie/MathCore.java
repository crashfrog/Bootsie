/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bootsie;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author John Foster
 */
public abstract class MathCore {
    
    
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
    
}
