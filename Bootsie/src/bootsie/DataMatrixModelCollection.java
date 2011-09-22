/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.util.ArrayList;



/**
 *
 * @author jpayne
 */
public final class DataMatrixModelCollection {
   //a singleton collection to hold all DataMatrixModels.
   private ArrayList dataMatrices = new <DataMatrixModel>ArrayList();
   
   private DataMatrixModelCollection(){
      //boring constructor
   }

   private static DataMatrixModelCollection instance;

   public static DataMatrixModelCollection getInstance(){
      if (instance == null){
         instance = new DataMatrixModelCollection();
      }
      return instance;
   }

   public void addMatrix(DataMatrixModel m){
      dataMatrices.add(m);
   }
}
