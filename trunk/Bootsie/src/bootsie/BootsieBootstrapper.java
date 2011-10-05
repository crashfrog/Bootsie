/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jpayne
 */
public class BootsieBootstrapper extends Thread {
//singleton bootstrap operation dispatcher with inner class computation threads.
    
   ArrayList<Thread> computationThreads;
   
   protected int numRunning;
   
   private BootsieBootstrapper(){
       numRunning = 0;
       computationThreads = new ArrayList<Thread>();
       PopulationMatrixModelCollection collection = PopulationMatrixModelCollection.getInstance();
       Iterator<PopulationMatrixModel> it = collection.iterator();
       while (it.hasNext()){
           computationThreads.add(new Thread(new BootstrapRunnable(it.next())));
       }
   }
   
   private static BootsieBootstrapper instance;
   
   public static BootsieBootstrapper getInstance(){
       if (instance == null){
           instance = new BootsieBootstrapper();
       }
       return instance;
   }
    
    @Override
   public void run() {
      BootsieApp.getApplication().createReportDirectory();
      Calendar startedTime = Calendar.getInstance();
      Iterator<Thread> threadsIterator = computationThreads.iterator();
      while(threadsIterator.hasNext() || numRunning > 0){
          Calendar currentTime = Calendar.getInstance();
          if (numRunning < BootsieApp.defaultNumThreads && threadsIterator.hasNext()){
              dispatchComputation(threadsIterator);
          }
          long elapsedTime = currentTime.getTimeInMillis() - startedTime.getTimeInMillis();
          StringBuilder s = new StringBuilder();
          s.append("Computing. Elapsed time ").append(BootsieApp.calculateElapsed(elapsedTime));
          BootsieApp.getApplication().estimate(s.toString());
          
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BootsieBootstrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
          
      }
      StringBuilder s = new StringBuilder();
      Calendar endTime = Calendar.getInstance();
      long totalElapsedTime = endTime.getTimeInMillis() - startedTime.getTimeInMillis();
      s.append("Finished. Total elapsed time ").append(BootsieApp.calculateElapsed(totalElapsedTime));
      BootsieApp.getApplication().estimate(s.toString());
      BootsieApp.getApplication().view.finished();
   }
   
   public void dispatchComputation(Iterator<Thread> it){
           Thread nextThread = it.next();
           nextThread.start();
           it.remove();
           numRunning++;
   }
   
   public synchronized void notifyFinished(){
       numRunning--;
   }
   
   private class BootstrapRunnable implements Runnable {
       
       PopulationMatrixModel data;
       
       private BootstrapRunnable(PopulationMatrixModel d){
           data = d;
       }

        public void run() {
            MathCore.bootstrapCoefficientOfVariance(data, new BootstrapMonitor(data));
        }
       
   }
   

}
