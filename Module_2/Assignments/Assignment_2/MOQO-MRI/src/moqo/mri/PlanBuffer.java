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
public class PlanBuffer {
    private Plan [] plans = new Plan[1000000];
    private int counter=0;
    
    public int add_plan(Plan p, double alpha)
    {
        int nb_reduct=0;
        plans[counter]=p;
        for (int i=0; i<counter; i++)
        {
            if (this.is_worse(plans[i],p,alpha)==true)
            {
                nb_reduct++;
                for(int j=i; j<=counter; j++)
                    plans[j]=plans[j+1];
                counter--;
            }
        }
        counter++;
    return nb_reduct;
    }
    
    public Plan get_plan(int id)
    {
        return this.plans[id];
    }
    
    public int get_counter()
    {
        return this.counter;
    }
    
    public boolean is_better_on_obj(double val1, double val2, char op, double alpha)
    {
        double approx_val2 = val2 * alpha;
        if (op=='e')
            return (val1*alpha)<val2;
        return val1>approx_val2;
    }
    
    public boolean is_worse(Plan p1, Plan p2, double alpha)
    {
        boolean result = true;
        char [] cs = {'c','d','e'};
        for(char c:cs)
        {
            if (is_better_on_obj(p1.get_values(c),p2.get_values(c),c, alpha))
                    result=false;
        }
        return result;
    }
    
    public boolean is_better(Plan p, double alpha)
    {
        char [] cs = {'c','d','e'};
        boolean pareto_better = true;
        for (int i=0; i<this.counter; i++)
            for(char c:cs)
                if (is_better_on_obj(p.get_values(c),this.plans[i].get_values(c),c, alpha))
                    pareto_better=false;
    return pareto_better==false;
    }
}
