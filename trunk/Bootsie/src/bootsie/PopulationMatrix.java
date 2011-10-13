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
public class PopulationMatrix {
    
    protected String popName;
    protected DistanceMatrix geneticDistanceMatrix;
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
        
        public ArrayList<Double> getGeneticDistances(){
            return geneticDistanceMatrix.getDistances();
        }
    
    public void populateGeneticDistanceMatrix(){
        geneticDistanceMatrix = new DistanceMatrix(samples);
        geneticDistanceMatrix.populateGeneticDistanceMatrix();
    }
  
    
    public ArrayList<Byte> getAllNthLoci(Integer n){
       ArrayList<Byte> loci = new ArrayList<>();
       Iterator<DataSample> it = samples.iterator();
       while(it.hasNext()){
           try {
               Byte b = it.next().getLoci().get(n);

                   loci.add(b);
               
           } catch (IndexOutOfBoundsException ex){
               //it's Saga's responsibility to make sure all data strings are
               //the same length, but just in case they're not, it's ok if we
               //go for the Nth loci in a sample but there's no data there.
           }
       }
       return loci;
   }
    
    public int getLength() {
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
