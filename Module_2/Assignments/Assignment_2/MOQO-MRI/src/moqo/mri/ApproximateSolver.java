/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moqo.mri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author omidvarb
 */
public class ApproximateSolver {
    
    LabelManagement LM;
    GroupSet AllGroups = new GroupSet();
    int nb_singletons = 0;
    int nb_pruning = 0;
    
    public void solve(int nbgroups, Rating [] ratings, int nbratings, int sigma, double alpha) throws IOException
    {
        try
        {
            LM = new LabelManagement();
        }
        catch(FileNotFoundException fnfe)
        { 
            System.out.println(fnfe.getMessage());
        } 
        PlanBuffer pareto = new PlanBuffer();
        String current_group = "M;Teen;writer;MN"; // first depth-first group
        while (current_group!=null)
        {
            String next = LM.next_group(current_group);
            current_group = next;
            if (current_group == null || current_group == "-;-;-;-;")
                break;
            Group g = new Group();
            g.build_group_given_desc(next, ratings, nbratings);
            //if (g.getnbratings() >= 1)
              //  System.out.println(current_group+" ("+g.getnbratings()+")");
            if (g.getnbratings()<sigma)
                continue;
            AllGroups.addgroup(g);
            GroupSet gs = new GroupSet();
            gs.addgroup(g);
            Plan p = new Plan(gs);
            //gs.show();
            if (pareto.is_better(p, alpha)==true || pareto.get_counter()==0)
            {
                nb_pruning += pareto.add_plan(p,alpha);
                //System.out.print("Added to pareto: ");
                //p.report_values();
            }
            else
            {
                nb_pruning++;
            }
        }
        System.out.println(pareto.get_counter()+" singleton pareto plans added!");
        nb_singletons = pareto.get_counter();
        int[] arr = new int[nb_singletons];
        for (int y=0; y<nb_singletons; y++)
            arr[y]=y;
        
        for (int n=2; n<nbgroups; n++)
        {
            int enum_cnt=0;
            Stylizer st = new Stylizer();
            int nbcomb = st.factorial(nb_singletons) / (st.factorial(n) * st.factorial(nb_singletons - n));
            Combination cb = new Combination(nbcomb);
            cb.make_combination(arr, n, 0, new int[n]);
            System.out.println("n is now "+n+" pareto size "+pareto.get_counter());
            String [] next_enumeration;
            while (enum_cnt != cb.count())
            {
                next_enumeration = cb.get_comb(enum_cnt++);
                GroupSet new_gs = new GroupSet();
                for (int y=0; y<n; y++)
                {
                    new_gs.addgroup(AllGroups.getgroup(Integer.parseInt(next_enumeration[y])));
                }
                Plan p = new Plan(new_gs);
                System.out.print(Arrays.toString(next_enumeration)+": ");
                p.report_values();
                if (pareto.is_better(p, alpha)==true || pareto.get_counter()==0)
                {
                    nb_pruning += pareto.add_plan(p,alpha);
                    System.out.print("Added to pareto: ");
                    p.report_values();
                    p.get_groupset().show();
                    System.out.println("Pareto size: "+pareto.get_counter()+" prunned:"+nb_pruning);
                }
                else
                {
                    nb_pruning++;
                }
            }
        } 
    System.out.println("Alpha: "+alpha+" Pareto size: "+pareto.get_counter()+" prunned:"+nb_pruning);
    }
    
}
