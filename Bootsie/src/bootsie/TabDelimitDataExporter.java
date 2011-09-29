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
public class TabDelimitDataExporter extends DataExporter {

   private static String fileExtention = ".txt";

   @Override
   public void dataExport(File file, ArrayList<DataMatrixModel> data, Boolean combine) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public StringBuilder generateString(DataMatrixModel data) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public String getFileExtention() {
      return fileExtention;
   }

}
