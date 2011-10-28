/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 *
 * @author jpayne
 */
public class BootsieEstimator implements Runnable{
    
    int numTests = 3;
    
    
    public void run(){
        PopulationMatrixModelCollection collection = PopulationMatrixModelCollection.getInstance();
        StringBuilder s = new StringBuilder();
        Calendar beginTime = Calendar.getInstance();
        ArrayList<Long> elapsedTimes = new ArrayList<Long>();
        Iterator<PopulationMatrixModel> it = collection.iterator();
        while(it.hasNext()){
            PopulationMatrixModel data = it.next();
            MathCore.bootstrapCovTest(data, numTests);


            Calendar endTime = Calendar.getInstance();
            long elapsedTime = endTime.getTimeInMillis() - beginTime.getTimeInMillis();
            //extrapolate elapsed time
            elapsedTime *= data.numBootstraps * data.getLength();
            elapsedTime /= numTests;
            elapsedTimes.add(new Long(elapsedTime));
        
        }
        long projectedElapsedTime = 0;
        Iterator<Long> il = elapsedTimes.iterator();
        while (il.hasNext()){
            projectedElapsedTime += il.next();
        }
        
        s.append("Estimated completion time: ");
        s.append(BootsieApp.calculateElapsed(projectedElapsedTime));
        BootsieApp.getApplication().estimate(s.toString());
    }

      private class EstimatorThread implements Runnable {
      
        int numTestsRan = 0;
        boolean keepGoing = true;
        PopulationMatrixModel data;
        
  private EstimatorThread(PopulationMatrixModel d){
    data = d;
  }
        
        public void run(){
          while (keepGoing){
          //do the test
          }
        }
        
  public int numTests(){
    return numTestsRan;
  }
        
        public synchronized stop(){
          keepGoing = false;
        }
      }
}
