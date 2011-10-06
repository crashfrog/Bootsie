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

/*
 void setup(){
  //setup graph axes
  size(sizeWidth, sizeHeight);
  //background(255);
  stroke(0);

  line(offset, offset, offset, height-offset);
  line(offset, height-offset, width-offset, height-offset);
  
  textFont(loadFont("Labels.vlw"));
  textAlign(RIGHT);
  fill(0);
  int value = 100;
  
  int vertSpace = height-(offset*2);
  int vertLines = 10;
  int vertLineSpace = vertSpace / vertLines;
  int horizSpace =  width-(offset*2);
  int horizLines = 20;
  int horizLineSpace = horizSpace / horizLines;
  secsPerHash = (readMinutes * 60) / horizLines;
  
  int vertLineY = offset;
  line(offset, vertLineY, offset-hashLineLength, vertLineY);
  text(value, offset - 15, offset + 4);
  vertLineY += vertLineSpace / 2;
  for (int i = 0; i < vertLines; i++){
    line(offset, vertLineY, offset-(hashLineLength/2), vertLineY);
    vertLineY += vertLineSpace / 2;
    value -= 10;
    text(value, offset - 15, vertLineY + 4);
    line(offset, vertLineY, offset-hashLineLength, vertLineY);
    vertLineY += vertLineSpace / 2;
  }
  
  value = secsPerHash;
  
  int horizLineX = offset+horizLineSpace;
  for (int i = 0; i < horizLines; i++){
    line(horizLineX, height-offset, horizLineX, height-offset+hashLineLength);
    text(value, horizLineX + 8, height-offset+hashLineLength + 10);
    horizLineX += horizLineSpace;
    value += secsPerHash;
  }
  
  textAlign(CENTER);
  text(newTitle, (width / 2), (offset/2));
  
  thermalColor(targetTemp1);
  line(offset, tempY(targetTemp1), width-offset, tempY(targetTemp1));
  thermalColor(targetTemp2);
  line(offset, tempY(targetTemp2), width-offset, tempY(targetTemp2));
  thermalColor(targetTemp3);
  line(offset, tempY(targetTemp3), width-offset, tempY(targetTemp3));
  
  strokeWeight(2);
  stroke(0);
  reportDeltaX =((float) horizLineSpace / ((float) secsPerHash / readEvery));
 */
