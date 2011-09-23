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
public class DataSample{

   String sampleName;
   ArrayList loci = new <Byte>ArrayList();

   public DataSample(String n){
      sampleName = n;
   }

   public String getName(){
      return sampleName;
   }

   public ArrayList getLoci(){
      return loci;
   }
   
   public int size(){
       return loci.size();
   }

}
