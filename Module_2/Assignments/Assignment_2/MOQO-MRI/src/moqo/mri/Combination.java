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

public class Combination {
    
    private int nbcombs;
    private String [] combs;
    private int comb_cnt = 0;
    
    public Combination(int nb)
    {
        nbcombs = nb;
        combs = new String[nbcombs];
        combs[0]="";
    }
    
    public int count()
    {
        return comb_cnt;
    }
    
    public String[] get_comb(int i)
    {
        String [] comb_str = this.combs[i].split(";");
        return comb_str;
    }
    
    public void make_combination(int[] arr, int len, int startPosition, int[] result)
    {
        if (len == 0)
        {
            String out=Integer.toString(result[0]);
            for(int i=1; i<result.length; i++)
                out+=";"+Integer.toString(result[i]);
            combs[comb_cnt++]=out;
            return;
        }       
        for (int i = startPosition; i <= arr.length-len; i++)
        {
            result[result.length - len] = arr[i];
            make_combination(arr, len-1, i+1, result);
        }
    }
    
}
