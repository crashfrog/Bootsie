/*
 * BootsieApp.java
 */

package bootsie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class BootsieApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
   public BootsieView view;

    @Override protected void startup() {
        view = new BootsieView(this);
        show(view);
        BootsieApp.getApplication().report("Initialized.");
    }

   public BootsieView getView(){
       return view;
    }

   public void report(String s){
      view.addReport(s);
   }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of BootsieApp
     */
    public static BootsieApp getApplication() {
        return Application.getInstance(BootsieApp.class);

    }
    
    private static String NTSYS_REPORT_SAMPLE_NAME_TAB_DATA_REGEX = "[a-zA-Z0-9| |-]+\t[0|1|9|.|?|-]+";
    private static String NTSYS_REPORT_SAMPLE_NAME_REGEX = "[a-zA-Z0-9| |-]+";
    private static String NTSYS_REPORT_DATA_REGEX = "\t[0-9|?|.|-]+";
    private static String NTSYS_REPORT_FIRST_LINE_IS_NOT_NAME = "[0-9]+\t[0-9]+";
    private static String TEXT_FILE_EXTENSION = ".txt";

    public static void parseFile(File source){
       //parse data file and fill DataMatrixModelCollection
       String line;
      try {
         BootsieApp.getApplication().report("Opening " + source.getName() + "...");
         BufferedReader io = new BufferedReader(new FileReader(source));

         String popName;
         //check to see if first line is name of a population or the number of loci/samples
         line = io.readLine();
         if (line.matches(NTSYS_REPORT_FIRST_LINE_IS_NOT_NAME)){
             popName = source.getName().replaceFirst(TEXT_FILE_EXTENSION, "");//trim ".txt" when using filename as popname
         } else {
             popName = line;
         }
         

         int loci = 0;
         int samples = 0;
         DataMatrixModel dataModel = BootsieApp.getApplication().view.newDataModel(popName);

         while ((line = io.readLine()) != null) {
            if (line.contains("end") || line.equals("\r") || line.equals("\n") || line.equals("\r\n")) {
               //finish parsing and close datamatrixmodel
               BootsieApp.getApplication().report("Loaded " + popName + ".");
            } else if (line.matches(NTSYS_REPORT_SAMPLE_NAME_TAB_DATA_REGEX)) {
               //get the sample name

               Pattern pattern;
               Matcher m;
               pattern = Pattern.compile(NTSYS_REPORT_SAMPLE_NAME_REGEX);
               m = pattern.matcher(line);

               m.find();
               String sampleName = "";
               sampleName = m.group();

               pattern = Pattern.compile(NTSYS_REPORT_DATA_REGEX);
               m = pattern.matcher(line);
               String matrixLineRaw = "";
               m.find();
               try {
                  matrixLineRaw = m.group();
                  //System.out.println(matrixLineRaw);
               } catch (Exception ex) {
                  //System.out.print(ex);
                  //System.out.println(line);
               }

               char c;
               int length = matrixLineRaw.length();
               DataSample dataSample = new DataSample(sampleName);
               for (int i = 0; i < length; i++) {
                  c = matrixLineRaw.charAt(i);
                  if (c == 45 || c == 57 || c== 46) { //-, ., and 9 are 'no data' characters
                     c = '?';
                     dataSample.getLoci().add(new Byte((byte) '?'));
                     //System.out.print('?');
                  }
                  if (c != '\t') {
                     //dataSample.getLoci().add(Byte.parseByte(Character.toString(c)));
                     //or
                     
                      if (c == (byte) 49){ //1
                        dataSample.getLoci().add(new Byte((byte) 1));
                      } else if (c == (byte) 48){ //0
                        dataSample.getLoci().add(new Byte((byte) 0));
                      }
                      
                  }
               }
               //debug
               //System.out.println(dataSample.getLoci());
               dataModel.addDataSample(dataSample);

            } else {
               popName = line;
               dataModel = BootsieApp.getApplication().view.newDataModel(popName);
            }
         }
      } catch (FileNotFoundException ex) {
         //System.out.println(ex);
          BootsieApp.getApplication().report("Unable to load: " + source.getName() + " not found.");
      } catch (IOException ex) {
         BootsieApp.getApplication().report("An error occured while reading " + source.getName() + ". Please terminate and restart Bootsie.");
      }
      BootsieApp.getApplication().report("Done.");
       
    }

    public static void parseFile(File[] files){
       for (File f : files){
          parseFile(f);
       }
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(BootsieApp.class, args);
    }

   @Action
   public void beginAnalysis() {
      //dispatch computation threads
   }
}
