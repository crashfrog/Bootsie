/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.awt.Color;
import java.awt.event.ActionListener;

/**
 *
 * @author jpayne
 */

//a singleton UI object to edit all the DataSetPanels at once.
public class DataSetMasterPanel extends DataSetPanel {

   private static DataSetMasterPanel instance;

   public static DataSetMasterPanel getInstance(){
      if (instance == null){
         instance = new DataSetMasterPanel();
      }
      return instance;
   }

   public DataSetMasterPanel(){
      this.DataSetName.setText("Apply to all:");
      this.setBackground(Color.GRAY);
      PlotCVSVG.setEnabled(BootsieApp.getApplication().svgWorks);
   }

   public void addNumBootstrapsListener(ActionListener l){
      NumBootstraps.addActionListener(l);

   }
   public void addPlotCVNoneListener(ActionListener l){
      PlotCVNone.addActionListener(l);
   }
   public void addPlotCVPNGListener(ActionListener l){
      PlotCVPNG.addActionListener(l);
   }
   public void addPlotCVSVGListener(ActionListener l){
      PlotCVSVG.addActionListener(l);
   }
}
