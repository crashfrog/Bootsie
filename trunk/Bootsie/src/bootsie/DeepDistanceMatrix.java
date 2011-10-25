/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bootsie;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Justin Payne
 */
public class DeepDistanceMatrix extends HashMap<DataSample, HashMap<DataSample, ArrayList<Double>>>{
    //hashmap of hashmaps of arrays of doubles
    
    public DeepDistanceMatrix(ArrayList<DataSample> keys){
        super();
        for (DataSample sample : keys){
            HashMap<DataSample, ArrayList<Double>> hash = new HashMap<DataSample, ArrayList<Double>>(keys.size() * 2);
            this.put(sample, hash);
        }
    }
    
    public void put(DataSample a, DataSample b, Double d){
        ArrayList<Double> values = null;
        values = this.get(a, b);
        if (values == null){
            //first miss - try reciprocal location
            if (values == null){
                //second miss - initialize array and add
                values = new ArrayList<Double>();
                values.add(d);
                super.get(a).put(b, values);
            } else {
                values.add(d);
            }
        } else {
            values.add(d);
        }
    }
    
    public ArrayList<Double> get(DataSample a, DataSample b){
        ArrayList<Double> list = null;
        list = this.get(a).get(b);
        if (list == null){
            list = this.get(b).get(a);
        }
        return list;
    }
    
    public ArrayList<ArrayList<Double>> getValues() {
        ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
        ArrayList<DataSample> keyList = new ArrayList<DataSample>(this.keySet().size());
        keyList.addAll(this.keySet());
        for (DataSample a : this.keySet()) {
            keyList.remove(a);
            for (DataSample b : keyList) {
                if (a.equals(b) == false) {
                    ArrayList<Double> value = this.get(a, b);
                    //System.out.println(value);
                    if (value != null) {
                        list.add(value);
                    }
                }
            }
        }
        return list;
    }
    
    public void addDistanceMatrix(DistanceMatrix d){
        ArrayList<DataSample> keySet2ndOrder = new ArrayList<DataSample>();
        keySet2ndOrder.addAll(d.keySet());
        for (DataSample a : d.keySet()){
            keySet2ndOrder.remove(a);
            for (DataSample b : keySet2ndOrder){
                Double value = d.get(a, b);
                this.put(a.getEquiv(), b.getEquiv(), value);
            }
        }
    }
    
}
