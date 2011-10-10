/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bootsie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Justin Payne
 */
public class PopulationMatrix {
    
    protected String popName;
    protected HashMap<DataSample, HashMap<DataSample, Double>> geneticDistanceMatrix;
    protected ArrayList<DataSample> samples;
    protected int numLoci = 0;
    
    public PopulationMatrix(String n){
        popName = n;
        samples = new ArrayList<>();
    }
    
    public PopulationMatrix(ArrayList<DataSample> s){
        this("bootstrap");
        samples = s;
    }
    
    public void addDataSample(DataSample d){
      samples.add(d);
   }
        @Override
   public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(popName).append(" (").append(samples.size()).append(" samples, ").append(getLength()).append(" loci)");
       return s.toString();

   }
    
    
    synchronized void populateGeneticDistanceMatrix(){
       //calculate pairwise genetic distance using MathCore method
       geneticDistanceMatrix = new HashMap<>(samples.size());
       for(DataSample sample_x: samples){
           HashMap<DataSample, Double> hash = new HashMap<>(samples.size());
           geneticDistanceMatrix.put(sample_x, hash);
       }
       //distances are pairwise so we can just copy the reciprocal distance
       for (DataSample sample_x: samples){
           for (DataSample sample_y: samples){
               if (sample_x == sample_y){
                   geneticDistanceMatrix.get(sample_x).put(sample_x, new Double(0.0));
               } else {
                   //check to see if reciprocal value has been calculated
                   if (geneticDistanceMatrix.get(sample_y).get(sample_x) != null){
                       geneticDistanceMatrix.get(sample_x).put(sample_y, geneticDistanceMatrix.get(sample_y).get(sample_x));
                   } else {
                       geneticDistanceMatrix.get(sample_x).put(sample_y, MathCore.geneticDistance(sample_x, sample_y));
                   }
               }
           }
       }
   }
    
    public ArrayList<Byte> getAllNthLoci(Integer n){
       ArrayList<Byte> loci = new ArrayList<>();
       Iterator<DataSample> it = samples.iterator();
       while(it.hasNext()){
           try {
               Byte b = it.next().getLoci().get(n);
               if (b == 1 || b == 0) {
                   loci.add(b);
               }
           } catch (IndexOutOfBoundsException ex){
               //it's Saga's responsibility to make sure all data strings are
               //the same length, but just in case they're not, it's ok if we
               //go for the Nth loci in a sample but there's no data there.
           }
       }
       return loci;
   }
    
    int getLength() {
        if (numLoci == 0){
            //return number of loci in population, which is the most loci in any sample.
            Iterator<DataSample> i = samples.iterator();
            int length = 0;
            while (i.hasNext()) {
                DataSample d = i.next();
                if (d.size() > length) {
                    length = d.size();
                }
            }
            return length;
        } else {
            return numLoci;
        }
    }
    
    public int getSize(){
        return samples.size();
    }
    
    public String getName(){
       return popName;
   }
    
     public Iterator iterator(){
       return samples.iterator();
   }
    
}
