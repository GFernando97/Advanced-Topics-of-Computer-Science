/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moqo.mri;

/**
 *
 * @author omidvarb
 */
public class Stylizer {
    public String to_percentage(double n)
    {
        String out = Double.toString(Math.round(n*100))+"%";
        return out;
    }
    
    public String to_nice_double(double n)
    {
        String str = Double.toString(n);
        if(str.length()<4)
            return str;
        else
            return str.substring(0, 4);
    }
    
    public String objective_title(char op)
    {
        switch(op)
        {
            case 'e':
                return "error";
            case 'd':
                return "diversity";
            case 'c':
                return "coverage";
        }
        return "";
    }
    
    public int factorial(int n)
    {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}
