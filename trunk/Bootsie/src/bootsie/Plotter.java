/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

/**
 *
 * @author jpayne
 */
public interface Plotter {
   //interface for PNG and SVG plotters

   public void plotVariance(PopulationMatrixModel model);

   public void savePlot();

}
