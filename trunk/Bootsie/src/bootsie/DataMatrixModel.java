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

/**
 *
 * @author jpayne
 */
class DataMatrixModel implements ActionListener, MouseListener{
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
   
   public Iterator iterator(){
       return populations.iterator();
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
       Iterator<DataSample> it = populations.iterator();
       while(it.hasNext()){
           try {
               Byte b = it.next().getLoci().get(n);
               if (b == 1 || b == 0) {
                   loci.add(b);
               }
           } catch (IndexOutOfBoundsException ex){
               //it's Saga's responsibility to make sure all data strings are
               //the same length, but just in case they're not, it's ok if we
               //go for the Nth loci in a sample but there's no data there.
           }
       }
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
    
    public Integer getNumSamples(){
        return new Integer(populations.size());
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

}
