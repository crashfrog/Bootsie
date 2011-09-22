/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

   public PlotterType getPlotterType(){
      return plotter;
   }

   //some kind of keyed table of vectors to store values
   private ArrayList populations = new <DataSample>ArrayList();

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

   //event logic to respond to UI


   //event logic to update UI


   //dispatch a boostrapping thread for the actual computation

}
