/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTextField;

/**
 *
 * @author jpayne
 */
class DataMatrixModel implements ActionListener{
   //association to UI element
   private DataSetPanel panel;
   protected PlotterType plotter = PlotterType.NO_PLOTTER;
   protected String popName;
   int numBootstraps = 1000;
   int numLoci = 0;
   private ArrayList coefficientsOfVariation;

   public PlotterType getPlotterType(){
      return plotter;
   }

   //some kind of keyed table of vectors to store values
   private ArrayList<DataSample> populations = new <DataSample>ArrayList();

   public DataMatrixModel(String n, DataSetPanel p){
      popName = n;
      panel = p;
   }

   public void addDataSample(DataSample d){
      populations.add(d);
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().equals("setNumBootstraps")){
         JTextField field = (JTextField) e.getSource();
         numBootstraps = Integer.parseInt(field.getText());
      } else if (e.getActionCommand().equals("setPlotterNone")){
         plotter = PlotterType.NO_PLOTTER;
      } else if (e.getActionCommand().equals("setPlotterSVG")){
         plotter = PlotterType.SVG_PLOTTER;
      } else if (e.getActionCommand().equals("setPlotterPNG")){
         plotter = PlotterType.PNG_PLOTTER;
      }
   }
   
   public ArrayList getAllNthLoci(int n){
       ArrayList loci = new <Byte>ArrayList();
       
       return loci;
   }

   //event logic to respond to UI


   //event logic to update UI


   //dispatch a boostrapping thread for the actual computation

    int getLength() {
        //return number of loci in population, which is the most loci in any sample.
        Iterator<DataSample> i = populations.iterator();
        int length = 0;
        while (i.hasNext()){
            DataSample d = i.next();
            if (d.size() > length){
                length = d.size();
            }
        }
        return length;
    }

}
