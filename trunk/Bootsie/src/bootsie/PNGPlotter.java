/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author jpayne
 */
public class PNGPlotter extends BufferedImage implements Plotter {
    
    static final String YLabel = "% Coefficient of Variation";
    static final String XLabel = "Number of AFLP markers";

   public final PlotterType plotterType = PlotterType.PNG_PLOTTER;
   Graphics2D context;
   Point.Double lastPoint;
   int offset = 30; //pixel distance from edge of image to edge of graph axes
   static final int width = 750;
   static final int height = 450;
   static final int tickLength = 5;
   static final int numYTicks = 5;
   int xPlotWidth;
   int x_axis_space;
   int y_axis_space;
   static int numXTicks;
   ArrayList<Integer> x_tick_labels;
   PopulationMatrixModel data;

   
   
   public PNGPlotter(PopulationMatrixModel d) {
      super(width, height, BufferedImage.TYPE_BYTE_GRAY);
      data = d;
      context = this.createGraphics();
      context.setFont(new Font("Serif", Font.PLAIN, 12));
      //create a stroke object for lines
      context.setStroke(new BasicStroke(1.5f));
      context.setPaint(Color.WHITE);
      context.fill(new Rectangle2D.Float(0, 0, width, height));
      //create axes, ticks
      context.setPaint(Color.BLACK);
      context.draw(new Line2D.Float(new Point(offset + tickLength, offset), new Point(offset + tickLength, height-offset - 5))); //draw Y
      context.draw(new Line2D.Float(new Point(offset, height - offset - 5), new Point(width - offset, height - offset - 5))); //draw x
      
      x_axis_space = width - (offset * 2);
      y_axis_space = height - (offset * 2) - 5;
      int y_axis_tick_space = y_axis_space / numYTicks;
      numXTicks = determineXTicks();
      int x_axis_tick_space = x_axis_space / numXTicks;
      
      int yPosition = offset;
      Integer yNumber = 100;
      for (int i = 0; i < numYTicks; i++){
          context.draw(new Line2D.Float(new Point(offset, yPosition), new Point(offset + tickLength, yPosition)));
          context.drawString(yNumber.toString(), offset - 16, yPosition + 5);
          yPosition += y_axis_tick_space;
          yNumber -= 20;
      }
      context.drawString(new Integer(0).toString(), offset - 16, yPosition + 5);
      int xPosition = offset + x_axis_tick_space;
      Iterator<Integer> numbers = x_tick_labels.iterator();
      for (int i = 0; i < numXTicks; i++){
          context.draw(new Line2D.Float(new Point(xPosition, height - offset - 5), new Point(xPosition, height - offset - 5 + tickLength)));
          context.drawString(numbers.next().toString(), xPosition - 6, height - offset + tickLength + 7);
          
          xPosition += x_axis_tick_space;
      }
      
       //create text labels
       context.drawString(XLabel, (width / 2) - 48, height - (offset / 2) + 8);
       Font oldFont = context.getFont();
       Font f = oldFont.deriveFont(AffineTransform.getRotateInstance(-Math.PI / 2.0));
       context.setFont(f);
       context.drawString(YLabel, (offset / 2) - 4, (height / 2) + 53);
       context.setFont(oldFont);
       context.setStroke(new BasicStroke(2.0f));
   }
   
   private void extendPath(Point.Double newPoint){
      context.draw(new Line2D.Float(lastPoint, newPoint));
      lastPoint = newPoint;
   }



   @Override
   public void plotVariance() {
      
      float offsetX = (float) x_axis_space / (float) xPlotWidth;
      
      Iterator<Double> it = data.coefficientsOfVariation.iterator();
      while (it.hasNext()){
         Double cov = it.next();
         if (lastPoint == null){
             //lastPoint = new Point(offset + 1, (int) (offset + y_axis_space - (y_axis_space * cov)));
             lastPoint = new Point.Double(offset + tickLength + 3, (offset));
         } else {
             extendPath(new Point.Double((double) (lastPoint.x + offsetX), (offset + y_axis_space - (y_axis_space * cov))));
         }
      }
   }

   @Override
   public void savePlot() {
      BootsieApp.getApplication().exportToReportDirectory(this, data.getName(), "cov.png");
   }

    private int determineXTicks() {
        x_tick_labels = new ArrayList<>();
        Integer y = 40;
        int i;
        for (i = 0; i < 5; i++){
            x_tick_labels.add(y);
            y = y + 40;
        }
        xPlotWidth = 200;
        
        return i;
    }

}

