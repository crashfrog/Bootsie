/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author jpayne
 */
public abstract class DataExporter {

   private static String fileExtention;

   public abstract void dataExport(File file, ArrayList<PopulationMatrixModel> data, Boolean combine);

   public abstract StringBuilder generateString(PopulationMatrixModel data);

   public abstract String getFileExtention();


}
