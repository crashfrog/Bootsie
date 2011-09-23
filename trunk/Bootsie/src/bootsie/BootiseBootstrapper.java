/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author jpayne
 */
public class BootiseBootstrapper implements Runnable {

   private DataMatrixModel matrix;
   
   // mean of means, stdDev of means, CoV of means
   
   public void BootsieBootstrapper(DataMatrixModel m){
       matrix = m;
   }
    
   public void run() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
   
   private ArrayList<ArrayList> singleBootstrap(int sampleSize){
       ArrayList<Integer> picks = new ArrayList();
       ArrayList<ArrayList> sample = new ArrayList();
       for(int i = 0; i < sampleSize; i++){
           picks.add((int) Math.random() * matrix.getLength());
       }
       Iterator<Integer> it = picks.iterator();
       while (it.hasNext()){
           sample.add(matrix.getAllNthLoci(it.next().intValue()));
       }
       
       return sample;
   }

}
