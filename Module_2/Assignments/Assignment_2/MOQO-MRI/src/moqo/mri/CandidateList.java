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
public class CandidateList {
    private int nbitems;
    private CandidateItem items [];
    
    public CandidateList()
    {
        this.items = new CandidateItem[100000];
        nbitems=0;
    }
    
    public GroupSet getbest(char op)
    {
        Objectives ob = new Objectives();
        double best_val=ob.initialize(op);
        int best_id=0;
        for (int i=0; i<this.getsize(); i++)
        {
            double current_value=this.getitem(i).getval(op);
            if (ob.is_better(current_value, best_val, op))
            {
                best_val = current_value;
                best_id = i;
            }
        }
        return this.getitem(best_id).getgroups();
    }
    
    public void add(GroupSet gs, double er, double cov, double dvs)
    {
        items[nbitems]=new CandidateItem();
        items[nbitems++].set(gs, er, cov, dvs);
    }
    
        public void add(GroupSet gs, double val, char op)
    {
        items[nbitems]=new CandidateItem();
        switch(op)
        {
            case 'e':
                items[nbitems++].set(gs, val, 0, 0);
                break;
            case 'd':
                items[nbitems++].set(gs, 0, 0, val);
                break;
            case 'c':
                items[nbitems++].set(gs, 0, val, 0);
                break;
        }
    }
    
    public int getsize()
    {
        return nbitems;
    }
    
    public CandidateItem getitem(int i)
    {
        return this.items[i];
    }
}
