/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bootsie;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Justin Payne
 */
public class PaupDataExporter extends DataExporter {
    
    StringBuilder export = new StringBuilder();
    static final String fileExtention = ".nex";

    @Override
    public void dataExport(File file, ArrayList<PopulationMatrixModel> data, Boolean combine) {
        //header
        export.append("#NEXUS\n[!Data from:\n\tNEXUS export by Bootsie]\n\n");
    }

    @Override
    public StringBuilder generateString(PopulationMatrixModel data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getFileExtention() {
        return fileExtention;
    }
    
}
