/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.util.ArrayList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;



/**
 *
 * @author jpayne
 */
public final class PopulationMatrixModelCollection implements ListModel {
   //a singleton collection to hold all DataMatrixModels.
   private ArrayList<PopulationMatrixModel> populations = new ArrayList<PopulationMatrixModel>();
   
   private PopulationMatrixModelCollection(){
      //boring constructor
   }

   private static PopulationMatrixModelCollection instance;

   public static PopulationMatrixModelCollection getInstance(){
      if (instance == null){
         instance = new PopulationMatrixModelCollection();
      }
      return instance;
   }

   public void addMatrix(PopulationMatrixModel m){
      populations.add(m);
   }

    public int getSize() {
        return populations.size();
    }

    public Object getElementAt(int index) {
        return populations.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        
    }

    public void removeListDataListener(ListDataListener l) {
        
    }
}
