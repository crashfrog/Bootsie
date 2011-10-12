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
public class DistanceMatrix extends HashMap<DataSample, HashMap> {
    
    public DistanceMatrix(ArrayList<DataSample> keys){
        super(keys.size() * 2);
        for (DataSample sample: keys){
            this.put(sample, new HashMap<DataSample, Double>(keys.size() * 2));
        }
    }
    
    public void put(DataSample a, DataSample b, Double distance){
        this.get(a).put(b, distance);
    }
    
    public Double get(DataSample a, DataSample b){
        //could be null if a = b
        Double val;
        val = (Double) this.get(a).get(b);
        if (val == null){
            val = (Double) this.get(b).get(a);
        }
        return val;
    }
    
    public ArrayList<Double> distances(){//return distance values in no particular order
        ArrayList<Double> list = new ArrayList<>();
        for(HashMap<DataSample, Double> hash: this.values()){
            for(Double value: hash.values()){
                if (value != null){
                    list.add(value);
                }
            }
        }
        return list;
    }
    
    synchronized void populateGeneticDistanceMatrix(){
       //calculate pairwise genetic distance using MathCore method
       //only fill half the table
       ArrayList<DataSample> keyList = new ArrayList<>();
       keyList.addAll(this.keySet());
        for (DataSample a: this.keySet()){
            
           keyList.remove(a); 
           Iterator<DataSample> it = keyList.iterator();
           while (it.hasNext()){
               DataSample b = it.next();
               this.put(a, b, MathCore.simpleGeneticDistance(a, b));
               //this.put(a, b, MathCore.jaccardGeneticDistance(a, b));
           }
           
       }
   }
    
    public String toString(){
        StringBuilder output = new StringBuilder();
        ArrayList<DataSample> keyList = new ArrayList<>();
        keyList.addAll(this.keySet());
        for (DataSample a: keyList){
            output.append(a.getName()).append("\t");
        }
        output.append("\n");

        for (DataSample a: this.keySet()){
           output.append(a.getName()).append("\t");
           Iterator<DataSample> it = keyList.iterator();
           while (it.hasNext()){
               DataSample b = it.next();
               Double val = this.get(a, b);
               if (val != null){
                   output.append(val.toString());
               }
               output.append("\t");
           }
           keyList.remove(a);
           output.append("\n");
       }
        
        return output.toString();
    }

    
}
