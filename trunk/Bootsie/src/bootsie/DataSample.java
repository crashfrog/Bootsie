/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bootsie;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author jpayne
 */
public class DataSample{

   String sampleName;
   ArrayList<Byte> loci = new ArrayList<Byte>();

   public DataSample(String n){
      sampleName = n;
   }

   public String getName(){
      return sampleName;
   }

   public ArrayList<Byte> getLoci(){
      return loci;
   }
   
   public int size(){
       return loci.size();
   }
   @Override
   public String toString(){
       StringBuilder s = new StringBuilder();
       s.append(sampleName + "\t");
       Iterator<Byte> it = loci.iterator();
       while (it.hasNext()){
           s.append(it.next());
       }
       return s.toString();
   }

}
