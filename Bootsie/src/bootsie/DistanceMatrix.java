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
        ArrayList<Double> list = new ArrayList<Double>();
        for(HashMap<DataSample, Double> hash: this.values()){
            for(Double value: hash.values()){
                if (value != null){
                    list.add(value);
                }
            }
        }
        return list;
    }
    
    public ArrayList<Double> getDistances(){
        ArrayList<Double> list = new ArrayList<Double>();
        
        ArrayList<DataSample> keyList = new ArrayList<DataSample>();
       keyList.addAll(this.keySet());
        for (DataSample a: this.keySet()){
            
           keyList.remove(a); 
           Iterator<DataSample> it = keyList.iterator();
           while (it.hasNext()){
               DataSample b = it.next();
               list.add(this.get(a, b));
           }
           
       }
        
        return list;
    }
    
    synchronized void populateGeneticDistanceMatrix(){
       //calculate pairwise genetic distance using MathCore method
       //only fill half the table
       ArrayList<DataSample> keyList = new ArrayList<DataSample>();
       keyList.addAll(this.keySet());
        for (DataSample a: this.keySet()){
            
           keyList.remove(a); 
           for(DataSample b : keyList){
               this.put(a, b, MathCore.defaultCalculator.distance(a, b));
           }
           
           
           
       }
   }
    
    @Override
    public String toString(){
        this.populateGeneticDistanceMatrix();
        StringBuilder output = new StringBuilder();
        ArrayList<DataSample> keyList = new ArrayList<DataSample>();
        keyList.addAll(this.keySet());
        for (int i = 0; i < (keyList.size() - 1); i++){
            DataSample a = keyList.get(i);
            output.append("\t").append(a.getName());
        }
        output.append("\n");
        java.text.NumberFormat f = new java.text.DecimalFormat("0.0000");

        ArrayList<DataSample> vertKeyList = new ArrayList<DataSample>();
        for (int i = 1; i < keyList.size(); i++){
            output.append(keyList.get(i).getName());
            vertKeyList.add(keyList.get(i));
            for (int k = 0; k < vertKeyList.size(); k++){
                DataSample a = keyList.get(i);
                DataSample b = keyList.get(k);
                output.append("\t").append(f.format(this.get(a, b)));
            }
            output.append("\n");
        }
        
        return output.toString();
    }
}
