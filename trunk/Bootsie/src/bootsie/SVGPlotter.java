/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author jpayne
 */
public class SVGPlotter implements Plotter {

   public final PlotterType plotterType = PlotterType.SVG_PLOTTER;
   
   PopulationMatrixModel data;
   StringBuilder svgOutput;
   java.awt.Point lastPoint;

    SVGPlotter(PopulationMatrixModel d) {
        data = d;
        svgOutput = new StringBuilder();
        //set up header for SVG file
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bootsie/resources/SVGHeader.txt");
        StringBuilder hdr = new StringBuilder();
        byte[] chars = new byte[1024];
  	int bytesRead = 0;
        try {
            while( (bytesRead = inputStream.read(chars)) > -1){
                    hdr.append(new String(chars, 0, bytesRead));
          }
        inputStream.close();    
        } catch (IOException ex) {
            ex.printStackTrace();
        }
  	header = hdr;
        //tick marks
        
        //labels
    }

   public void plotVariance() {
       offsetX = 630 / data.coefficientsOfVariation.size();
      //beginning of SVG path object
       path = new StringBuilder();
       path.append("<path id=\"CovPlot\" stroke=\"black\" stroke-width=\"3\" fill=\"none\" d=\"m 73,43");
      for (Double number : data.coefficientsOfVariation){
          extendLine(number);
      }
      //end of SVG path object
      path.append("\" />");
   }
   

   public void savePlot() {
      svgOutput.append(header).append(path).append(footer);
      BootsieApp.getApplication().exportToReportDirectory(svgOutput, data.getName(), "cov.svg");
   }
   
   int offsetX;
   String footer = "</g>\n</svg>\n";
   StringBuilder header;
   StringBuilder path;

    private void extendLine(Double number) {
        path.append("l ").append(offsetX).append(" ").append(43 + 361 - (361 * number)).append(" ");

    }
}

//73, 43 is the top zero of the graph area
//its 361 tall
//and 630 wide
