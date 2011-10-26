/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bootsie;

import java.util.ArrayList;

/**
 *
 * @author Justin Payne
 */
public class BootsieCoVReport {
    public ArrayList<Double> coefficientsOfVariation;
    public ArrayList<Double> means;
    public ArrayList<Double> stdDevs;
    public int badBootstraps;
    
    public BootsieCoVReport(ArrayList<Double> c, ArrayList<Double> m, ArrayList<Double> s, int b){
        coefficientsOfVariation = c;
        means = m;
        stdDevs = s;
        badBootstraps = b;
    }
    
    @Override
    public String toString(){
        java.text.NumberFormat f = BootsieApp.defaultFormat;
        StringBuilder export = new StringBuilder();
        export.append("Analysis completed with ").append(badBootstraps).append(" bootstraps rejected (probably for zero mean.)\n");
        export.append("No.\tCoV\n");
        for (int i = 1; i <= coefficientsOfVariation.size(); i++){
            export.append(i).append("\t").append(new java.text.DecimalFormat("#0.00%").format(coefficientsOfVariation.get(i - 1))).append("\n");
        }
        
        return export.toString();
    }
    
    
}
