/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author jpayne
 */
public class PopulationMatrixModel extends PopulationMatrix implements ActionListener, MouseListener, ListModel{
   //association to UI element
   private DataSetPanel panel;
   protected PlotterType plotter = PlotterType.NO_PLOTTER;
   protected int numBootstraps = BootsieApp.defaultNumBootstraps;
   protected ArrayList<Double> coefficientsOfVariation;
   protected ArrayList<ArrayList<Byte>> bootstrapCache = null;

   public PlotterType getPlotterType(){
      return plotter;
   }
   

   public PopulationMatrixModel(String n, DataSetPanel p){
      super(n);
      panel = p;
   }

   @Override
   public ArrayList<Byte> getAllNthLoci(Integer n){
       //performance-enhancing cache for bootstrap grabs
       ArrayList<Byte> bootstrapGrabs;
       
       try {
         
       } catch (Exception e) {
           bootstrapCache = new ArrayList<>(this.getLength());
       }
       
       try {
           bootstrapGrabs = bootstrapCache.get(n);
       } catch (Exception ex){
           bootstrapGrabs = super.getAllNthLoci(n);
           
           try {
               bootstrapCache.add(n, bootstrapGrabs);
           } catch (Exception e) {
               bootstrapCache = new ArrayList<>(this.getLength());
           }
           
       }
       return bootstrapGrabs;
   }
   
   public String distanceMatrixToString(){
       return geneticDistanceMatrix.toString();
   }
   
   public PopulationMatrix getBootstrap(ArrayList<Integer> picks){
       //produce a bootstrap subsample population from a list of
       //integer "picks"
       ArrayList<DataSample> bootSamples = new ArrayList<>(samples.size());
       for (DataSample sample: samples){
           bootSamples.add(new DataSample(sample.getName()));
       }

       for (Integer pick : picks){
           Iterator<DataSample> ibs = bootSamples.iterator();
           ArrayList<Byte> nthLoci = getAllNthLoci(pick);
           for (Byte locus : nthLoci){
               ibs.next().add(locus);
           }
       }
       return new PopulationMatrix(bootSamples);
       
   }
   

   
  

   public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().equals("setNumBootstraps")){
         JTextField field = (JTextField) e.getSource();
         numBootstraps = Integer.parseInt(field.getText());
         new Thread(new BootsieEstimator()).start();
      } else if (e.getActionCommand().equals("setPlotterNone")){
         plotter = PlotterType.NO_PLOTTER;
      } else if (e.getActionCommand().equals("setPlotterSVG")){
         plotter = PlotterType.SVG_PLOTTER;
      } else if (e.getActionCommand().equals("setPlotterPNG")){
         plotter = PlotterType.PNG_PLOTTER;
      
      } else if (e.getActionCommand().equals("beginAnalysis")){
          //tell panel to indicate beginning of analysis
          panel.displayWaitingComputation();
      } else if (e.getActionCommand().equals("updateAnalysis")){
          BootstrapMonitor monitor = (BootstrapMonitor) e.getSource();
          panel.displayComputationProgress(monitor.getComputingProgress());
      } else if (e.getActionCommand().equals("completeAnalysis")){
          panel.displayFinishedComputation();
          saveCoV();
          createPlot();
      } else if (e.getActionCommand().equals("beginComputation")){
          panel.displayBeginComputation();
      }
   }
   




  
 

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2){
            BootsieApp.getApplication().report("Showing " + popName);
            DataViewer.getViewer().showDataMatrix(this);
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    

    public Object getElementAt(int index) {
        return samples.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        //the model doesn't need to know which populations are selected
    }

    public void removeListDataListener(ListDataListener l) {
        
    }

    private void createPlot() {
        Plotter plot;
        if (plotter == PlotterType.PNG_PLOTTER){
            plot = new PNGPlotter(this);
            plot.plotVariance();
            plot.savePlot();
        } else if (plotter == PlotterType.SVG_PLOTTER){
            plot = new SVGPlotter(this);
            plot.plotVariance();
            plot.savePlot();
        }
    }

    private void saveCoV() {
        StringBuilder export = new StringBuilder();
        //make tab-delimited export of CoV data
        int i = 0;
        for (Double cov: coefficientsOfVariation){
            export.append(++i).append("\t").append(cov).append("\n");
        }
        BootsieApp.getApplication().exportToReportDirectory(export, this.popName, "cov.txt");
    }

}
