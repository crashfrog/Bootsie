/*
 * BootsieApp.java
 */

package bootsie;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
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
    //environment variables. TODO: read and write these as "preferences"
    
   public BootsieView view;
   public static final int defaultNumBootstraps = 100;
   public static final int defaultNumThreads = 2;
   File exportDirectory;

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
   
   public void estimate(String s){
       view.setEstimate(s);
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
         PopulationMatrixModel dataModel = BootsieApp.getApplication().view.newDataModel(popName);

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

   
    void exportFile(File file, StringBuilder export) {
        Charset charset = Charset.forName("US-ASCII");
        try {
            boolean b = file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String s = export.toString();
            writer.write(s, 0, s.length());
            BootsieApp.getApplication().report("Wrote " + file.toString() + ".");
            writer.close();
        } catch (IOException x) {
            BootsieApp.getApplication().report("An error occured while writing " + file.getName() + ":");
            BootsieApp.getApplication().report(x.toString());
        }
    }

   void exportFiles(ArrayList<File> files, ArrayList<StringBuilder> exports) {
      Iterator<StringBuilder> ie = exports.iterator();
      Iterator<File> it = files.iterator();
      while (it.hasNext()){
          exportFile(it.next(), ie.next());
      }
   }
   
   public static String calculateElapsed(long timeInMilis) {
      int hours, minutes, seconds;
      int timeInSeconds = (int) (timeInMilis / 1000);
      hours = timeInSeconds / 3600;
      timeInSeconds = timeInSeconds - (hours * 3600);
      minutes = timeInSeconds / 60;
      timeInSeconds = timeInSeconds - (minutes * 60);
      seconds = timeInSeconds;
      return hours + " hour(s) " + minutes + " minute(s) " + seconds + " second(s).";
      //return new Long(timeInMilis).toString();
      //return hours + " hour(s) " + minutes + " minute(s).";
      
   }
   
   void createReportDirectory(){
      DateFormat formatter = new SimpleDateFormat("MMM d, hh-mm a");
      String timeStamp = formatter.format(new Date());
      JFileChooser fr = new JFileChooser();
      FileSystemView fw = fr.getFileSystemView();
      File documents = fw.getDefaultDirectory();
      exportDirectory = new File(documents, "/Bootsie Output/" + timeStamp + "/");
      BootsieApp.getApplication().report("Making report directory at " + exportDirectory.toString() + ".");
      boolean b;

      b = exportDirectory.mkdirs();

   }

    void exportToReportDirectory(StringBuilder export, String dirName, String fileName) {
        exportToReportDirectory(export.toString(), dirName, fileName);
    }

    private void exportToReportDirectory(String export, String dirName, String filename) {
        File exportFile = new File(exportDirectory, dirName + "/" + filename);
        try {
            System.out.println("Writing " + exportFile.toString());
            new File(exportDirectory + "/" + dirName).mkdir();
            exportFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile));
            String s = export.toString();
            writer.write(s, 0, s.length());
            BootsieApp.getApplication().report("Wrote " + exportFile.toString() + ".");
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(BootsieApp.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    void exportToReportDirectory(java.awt.image.RenderedImage image, String dirName, String filename){
        File exportFile = new File(exportDirectory, dirName + "/" + filename);
        System.out.println("Writing " + exportFile.toString());
        new File(exportDirectory + "/" + dirName).mkdir();
        try {
            javax.imageio.ImageIO.write(image, "png", exportFile);
        } catch (IOException ex) {
            Logger.getLogger(BootsieApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
