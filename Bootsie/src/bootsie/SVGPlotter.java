/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //set up header for SVG file
        header = new StringBuilder(1024);
        File SVGHeader = new File("/resources/SVGHeader.txt");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(SVGHeader));


            char[] chars = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(chars)) > -1) {
                header.append(String.valueOf(chars));
            }

            reader.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SVGPlotter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.io.IOException ex) {
            Logger.getLogger(SVGPlotter.class.getName()).log(Level.SEVERE, null, ex);
        }
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
