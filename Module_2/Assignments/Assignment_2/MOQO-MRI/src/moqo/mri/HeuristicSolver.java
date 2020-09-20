/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moqo.mri;

import java.io.IOException;

/**
 *
 * @author omidvarb
 */
public class HeuristicSolver {
    
    public void solve(int nbgroups, Rating [] ratings, int nbratings, int sigma, int nbinterval, int nbrepeat) throws IOException
    {
        Objectives objectives = new Objectives();
        LabelManagement LM = new LabelManagement();
        GroupSet groupset = new GroupSet();
        PlanBuffer InitBF = new PlanBuffer();
        double max_divs = 0;
        for(int i=0; i<nbrepeat; i++)
        {
            //System.out.println("I make the solution "+i);
            GroupSet opti_gs=null;
            groupset = LM.make_k_random_groups(nbgroups, ratings, nbratings, sigma);
            //System.out.println("Here is the random group set in iteration "+i+ " :");
            //groupset.show();
            //objectives.compute_and_report(groupset, nbratings);
            opti_gs=objectives.single_optimize('d', groupset, sigma, nbratings, ratings);
            //System.out.println("Here is the optimized group set in iteration "+i+ " :");
            //opti_gs.show();
            double covg = objectives.coverage(opti_gs, nbratings);
            double divs = objectives.diversity(opti_gs);
            double err = objectives.error(opti_gs);
            Plan p = new Plan();
            p.set_groupset(opti_gs);
            InitBF.add_plan(p, 1);
            if (divs > max_divs)
                max_divs = divs;
        }
        double period_lenght = max_divs / nbinterval;
        int [] intv_cnt = new int[nbinterval];
        System.out.println("Max Diversity: "+max_divs+ " Interval Lenght: "+period_lenght);
        PlanBuffer [] intervalBF = new PlanBuffer[nbinterval];
        for (int i=0; i<nbinterval; i++)
            intervalBF[i] = new PlanBuffer();
        for (int i=0; i<InitBF.get_counter(); i++)
        {
            int which_period = (int) (InitBF.get_plan(i).get_values('d')/period_lenght);
            if (which_period==nbinterval)
                which_period--;
            Plan p = InitBF.get_plan(i);
            if (intervalBF[which_period].is_better(p, 1)==true || intervalBF[which_period].get_counter()==0)
                intervalBF[which_period].add_plan(p, 1);
        }
        int sum=0;
        for (int i=0; i<nbinterval; i++)
        {
            //System.out.println("*** INTERVAL "+i+" ***");
            sum += intervalBF[i].get_counter();
            double min = 100;
            int min_plan = 0;
            for (int j=0; j<intervalBF[i].get_counter(); j++)
            {
                //System.out.println("-----");
                //intervalBF[i].get_plan(j).get_groupset().show();
                if (objectives.compute('e', intervalBF[i].get_plan(j).get_groupset())>min)
                {
                    min = objectives.compute('e', intervalBF[i].get_plan(j).get_groupset());
                    min_plan = j;
                }
            }
            if (intervalBF[i].get_counter()!=0)
            {
                intervalBF[i].get_plan(min_plan).get_groupset().show();
                objectives.compute_and_report(intervalBF[i].get_plan(min_plan).get_groupset(), nbratings);
            }
        }
        System.out.println(sum+" solutions");
    }
}
