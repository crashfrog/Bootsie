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

   public abstract void dataExport(File file, ArrayList<DataMatrixModel> data, Boolean combine);

   public abstract StringBuilder generateString(DataMatrixModel data);


}
