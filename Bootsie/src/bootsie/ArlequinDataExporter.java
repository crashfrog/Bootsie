/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author jpayne
 */
public class ArlequinDataExporter extends DataExporter {
   
   private static String fileExtention = ".arp";
   private static String header = "#============================================\n#Project file created for Arlequin by Bootsie\n#============================================\n\n#Use # to comment to end of line.\n#Some optional features of the Arlequin data format not supported by Bootsie.\n\n[Profile]\n";
   private static String nullChar = ".";
   private static String delimitChar = "";

   @Override
   public void dataExport(File file, ArrayList<PopulationMatrixModel> data, Boolean combine) {

       if (file.getName().contains(fileExtention)){
      } else {
          file = new File(file.toString() + fileExtention);
      }
      StringBuilder export = new StringBuilder(header);
      export.append("\tNbSamples=").append(data.size());
      export.append("\n\tDataType=RFLP\n\tGenotypicData=0\n\tGameticPhase=1\n\tLocusSeparator=NONE\n\tRecessiveData=0\n\tMissingData=\'.\'\n\n[Data]\n\n[[Samples]]\n\n");
      Iterator<PopulationMatrixModel> it = data.iterator();
      while (it.hasNext()){
          export.append(generateString(it.next()));
      }
      
      BootsieApp.getApplication().exportFile(file, export);
      
   }

   @Override
   public StringBuilder generateString(PopulationMatrixModel data) {
      StringBuilder export = new StringBuilder();
      export.append("\tSampleName=\"" + data.getName() + "\"\n");
      export.append("\tSampleSize= " + data.getSize() + "\n");
      export.append("\tSampleData = {\n");
      Iterator<DataSample> it = data.iterator();
      while(it.hasNext()){
         DataSample s = it.next();
         export.append("\t\t" + s.getName() + "\t1\t");
         ArrayList<Byte> loci = s.getLoci();
         Iterator<Byte> i = loci.iterator();
         while(i.hasNext()){
            Byte b = i.next();
            if (b != 0 && b != 1){
               export.append(nullChar);
            } else {
               export.append(b.toString());
            }
            //export.append(delimitChar);
         }
         export.append("\n");
      }
      export.append("}\n");
      return export;
   }

   @Override
   public String getFileExtention() {
      return fileExtention;
   }

}
