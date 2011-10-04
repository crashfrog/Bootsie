/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bootsie;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.Format;

/**
 *
 * @author John Foster
 */
public class BootstrapMonitor {
    
    double reportProgress = 0;
    double absoluteProgress = 0;
    double lastRelativeProgress = 0;
    double totalBootstrapOps = 0;
    PopulationMatrixModel data;
    

    public BootstrapMonitor(PopulationMatrixModel d) {
        data = d;
        totalBootstrapOps = data.getLength() * data.numBootstraps;
    }
    //a monitor object to be updated by the MathCore bootstrapping routine.
    //Updates DataSetPane in some way at certain intervals.

    public double getComputingProgress() {
        return reportProgress;
    }
    
    public void completeOneOp(){
        
        absoluteProgress += data.numBootstraps;
        double relativeProgressFrac = absoluteProgress / totalBootstrapOps;
        //reportProgress = relativeProgressFrac;
        reportProgress = Double.valueOf(new DecimalFormat("#0.000").format(relativeProgressFrac));
        if (reportProgress != lastRelativeProgress){
            lastRelativeProgress = reportProgress;
            data.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "updateAnalysis"));
        }
        
    }
    
    public void completeAllOps(){
        BootsieBootstrapper.getInstance().notifyFinished();
        data.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "completeAnalysis"));
    }

    void startingOp() {
        data.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "beginComputation"));
    }
}
