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
public class CandidateItem {
    private GroupSet gs;
    public double er;
    private double cov;
    private double dvs;
    
    public double getval(char op)
    {
        switch(op)
        {
            case 'e':
                return er;
            case 'd':
                return dvs;
            case 'c':
                return cov;
        }
        return 0;
    }
    
    public void set(GroupSet gs, double er, double cov, double dvs)
    {
        this.gs = gs;
        this.er = er;
        this.cov = cov;
        this.dvs= dvs;
    }
    
    public String show()
    {
        Stylizer style=new Stylizer();
        String str="";
        System.out.println(gs.getsize());
        for (int i=0; i<gs.getsize(); i++)
            str+=gs.getgroup(i).getlabel()+" \n";
        str += style.to_percentage(er);
        return str;
    }
    
    public GroupSet getgroups()
    {
        return this.gs;
    }
    
    
}
