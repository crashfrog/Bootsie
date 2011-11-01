/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Justin Payne
 */
public class BootsieEstimator extends Thread{
    
    int numTests = 3;
    
    @Override
    public void run(){
        PopulationMatrixModelCollection collection = PopulationMatrixModelCollection.getInstance();
        StringBuilder s = new StringBuilder();
        long projectedElapsedTotalTime = 0;
        Iterator<PopulationMatrixModel> it = collection.iterator();
        while(it.hasNext()){
            Calendar beginTime = Calendar.getInstance();
            PopulationMatrixModel data = it.next();
            EstimatorThread estimator = new EstimatorThread(data);
            new Thread(estimator).start();
            try {
                sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BootsieEstimator.class.getName()).log(Level.SEVERE, null, ex);
            }
            estimator.stop();
            Calendar endTime = Calendar.getInstance();
            double elapsedTime = endTime.getTimeInMillis() - beginTime.getTimeInMillis();
            double numTests = (double) estimator.numTests();
            double timePerOp = elapsedTime / numTests;
            long totalBootstrapOps = (long) ((data.getSize()*(data.getSize() + 1)) * .5 * data.numBootstraps * data.getLength());
            long projectedElapsedTime = (long) (totalBootstrapOps * timePerOp);
            projectedElapsedTotalTime += projectedElapsedTime;
        }
        s.append("Estimated completion time: ");
        s.append(BootsieApp.calculateElapsed(projectedElapsedTotalTime));
        BootsieApp.getApplication().estimate(s.toString());
    }
    
    


      private class EstimatorThread implements Runnable {
      
        int numTestsRan = 0;
        boolean keepGoing = true;
        PopulationMatrixModel data;
        
        private EstimatorThread(PopulationMatrixModel d){
          data = d;
        }
        
        @Override
          public void run() {
              Iterator<DataSample> it = data.iterator();
              DataSample a = data.samples.get(0);
              while (keepGoing) {
                  //do the test
                  if (it.hasNext()) {
                      DataSample b = it.next();
                      DataSample ab = new DataSample(a);
                      DataSample bb = new DataSample(b);
                      for (int p = 0; p < (data.getLength() * .5); p++) {
                          Integer r = new Integer((int) (Math.random() * data.getLength() - 1) + 1);
                          ab.loci.add(a.loci.get(r));
                          bb.loci.add(b.loci.get(r));
                      }
                      Double gd = MathCore.defaultCalculator.distance(a, b);
                      numTestsRan++;
                  } else {
                      it = data.iterator();
                  }

              }
          }
        
        public int numTests(){
          return numTestsRan;
        }
          
        public synchronized void stop(){
          keepGoing = false;
        }
      }
}
