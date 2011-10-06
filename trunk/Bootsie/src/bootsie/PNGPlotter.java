/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author jpayne
 */
public class PNGPlotter extends BufferedImage implements Plotter {

   public final PlotterType plotterType = PlotterType.PNG_PLOTTER;
   Graphics2D context;
   Point lastPoint;
   
   public PNGPlotter() {
      super(400, 300, BufferedImage.TYPE_BYTE_GRAY);
      context = this.createGraphics();
      //create a stroke object for lines
      
      //create axes, ticks
      
      //create text labels
   }
   
   private void extendPath(Point newPoint){
      context.draw(new Line2D.Float(lastPoint, newPoint));
      lastPoint = newPoint;
   }



   @Override
   public void plotVariance(PopulationMatrixModel data) {
      int size = data.coefficientsOfVariation.size();
      float offsetX;
      
      Iterator<Double> it = data.coefficientsOfVariation.iterator();
      while (it.hasNext()){
         Double cov = it.next();
      }
   }

   @Override
   public void savePlot() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

}
