/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moqo.mri;

import java.io.IOException;
import java.util.HashSet;

/**
 *
 * @author omidvarb
 */
public class Objectives {

    //// INI MODIFIED ////
    public double rating_deviation(GroupSet gs){
        double n;
        double value;

        n = gs.getsize();
        value = (gs.average_rating() - n + 1) / gs.average_rating();

        return value;
    }
    //// END MODIFIED ////

    public double coverage(GroupSet gs, int nbr)
    {
        int cnt = gs.countunion()-1;
        double cvg = cnt / (float) nbr;
        //System.out.println(Integer.toString(cnt)+" covered out of "+Integer.toString(nbr));
        return cvg;
    }

    public double diversity(GroupSet gs)
    {
        int sum = 0;
        for (int i=0; i<gs.getsize(); i++)
            for(int j=i+1; j<gs.getsize();j++)
            {
                int ints = gs.pairwiseintersectsize(i, j);
                sum += ints;
            }
        //System.out.println("***"+sum);
        float divs = 1 / (1+ (float)sum);
        return divs;
    }

    public double error(GroupSet gs)
    {
        int nbgroups = gs.getsize();
        double max = 0;
        for(int i=0; i<nbgroups; i++)
        {
            double nstdev = gs.getnormalizedstdev(i);
            if(nstdev>max)
                max = nstdev;
        }
        return max;
    }

    public boolean is_better(double v1, double v2, char op)
    {
        switch(op)
        {
            case 'e':
                if (v1<v2)
                    return true;
                else
                    return false;
            default:
                if (v1>v2)
                    return true;
                else
                    return false;
        }
    }

    public boolean is_best(double v, char op)
    {
        switch(op)
        {
            case 'e':
                if (v==0)
                    return true;
                else
                    return false;
            default:
                if (v==1)
                    return true;
                else
                    return false;
        }
    }

    public double initialize(char op)
    {
        if (op=='e')
            return 1;
        return 0;
    }

    public double compute(char op, GroupSet gs)
    {
        double val;
        switch(op)
        {
            case 'e':
                val = this.error(gs);
                break;
            case 'd':
                val = this.diversity(gs);
                break;
            case 'c':
                val = this.coverage(gs, MOMRI.nbratings);
                break;
            //// INI MODIFIED ////
            case 'r':
                val = this.rating_deviation(gs);
                break;
            //// END MODIFIED ////
            default:
                val=0;
        }
        return val;
    }


    public void compute_and_report(GroupSet gs, int nbr)
    {
        Stylizer style = new Stylizer();
        double er, cvg, dvs;
        er = this.error(gs);
        dvs = this.diversity(gs);
        cvg = this.coverage(gs, nbr);
        System.out.println("Diameter: "+style.to_nice_double(er)+" Diversity: "+style.to_nice_double(dvs)+" Coverage:"+style.to_nice_double(cvg));
    }

    public GroupSet single_optimize(char op, GroupSet gs, int sigma, int nbratings, Rating [] ratings) throws IOException
    {
        boolean to_continue=true;
        Stylizer style = new Stylizer();
        GroupSet current_gs = new GroupSet();
        for (int p=0; p<gs.getsize(); p++)
            current_gs.addgroup(gs.getgroup(p));
        double initial_val = this.compute(op, current_gs);
        //System.out.println("Initial objective value: "+style.to_percentage(initial_val));

        while (to_continue)
        {
            CandidateList CL = new CandidateList();
            double val = this.compute(op, current_gs);
            if (this.is_best(val, op))
            {
                //System.out.println("Achieved to the best score!");
                return current_gs;
            }
            for (int i=0; i<current_gs.getsize(); i++)
            {
                Group grp = current_gs.getgroup(i);
                HashSet neighbors = grp.getpossibleneighbors();
                //current_gs.show();
                //System.out.println(current_gs.getgroup(i).getlabel());
                //System.out.println("NN:" + neighbors.size());
                if (neighbors==null || neighbors.isEmpty())
                {
                    //System.out.println("No neighbors found for group "+grp.getlabel());
                    continue;
                }
                for (Object neigh:neighbors)
                {
                    GroupSet groupsetghost = current_gs;
                    String description=neigh.toString();
                    description = description.trim();
                    Group group_example2 = new Group();
                    group_example2.build_group_given_desc(description, ratings, nbratings);
                    if(group_example2.getnbratings()>sigma && grp.getlabel().equals(description)==false && !description.equals("-;-;-;-;"))
                    {
                        //System.out.println(grp.getlabel() + "** "+description);
                        //System.out.print("BEFORE: ");
                        //groupsetghost.show();
                        //System.out.println("WITH: "+initial_val);
                        groupsetghost.addgroupbyindex(group_example2, i);
                        double val_candidate = this.compute(op,groupsetghost);
                        //System.out.print("AFTER: ");
                        //groupsetghost.show();
                        //System.out.println("WITH: "+val_candidate);
                        CL.add(groupsetghost, val_candidate, op);
                    }
                } // end loop on all neighbors
            } // end loop on all groups in group set
            //System.out.println("Found "+CL.getsize()+" candidates.");
            if (CL.getsize()<=1)
            {
                //System.out.println("No more neighbors!");
                if (this.is_better(val, initial_val, op))
                {
                    //System.out.println("Optimized value: "+style.to_percentage(val));
                    return current_gs;
                }
                else
                {
                    return gs;
                }
            }
            double best_val = this.initialize(op);
            int best_id= 0;
            for (int i=0; i<CL.getsize(); i++)
            {
                if (this.is_better(CL.getitem(i).getval(op), best_val, op))
                {
                    best_val = CL.getitem(i).getval(op);
                    best_id = i;
                }
            }
            if (best_val > val)
            {
                System.out.println("Optimized value: "+style.to_percentage(best_val));
                to_continue = false;
                return CL.getitem(best_id).getgroups();
            }
            else
            {
                current_gs=CL.getitem(best_id).getgroups();
            }
        }
    return null;
    }

    public boolean inbounds(double val, double from, double to)
    {
        return val >= from && val < to;
    }

    public GroupSet single_optimize_bounded(char op, GroupSet gs, int sigma, Rating [] ratings, double begin_with, double end_with, char bound_on) throws IOException
    {
        // This function returns null if it doesn't find a solution inside the bounds. This means that the function will be recalled with new random seeds.
        boolean to_continue=true;
        Stylizer style = new Stylizer();
        GroupSet current_gs = new GroupSet();
        for (int p=0; p<gs.getsize(); p++)
            current_gs.addgroup(gs.getgroup(p));
        double initial_val = this.compute(op, current_gs);
        //System.out.println("Initial objective value: "+style.to_percentage(initial_val));

        while (to_continue)
        {
            CandidateList CL = new CandidateList();
            double val = this.compute(op, current_gs);
            for (int i=0; i<current_gs.getsize(); i++)
            {
                Group grp = current_gs.getgroup(i);
                HashSet neighbors = grp.getpossibleneighbors();
                if (grp==null || neighbors==null)
                    return null;
                if (neighbors.isEmpty())
                {
                    System.out.println("No neighbors found for group "+grp.getlabel());
                    continue;
                }
                for (Object neigh:neighbors)
                {
                    GroupSet groupsetghost = current_gs;
                    String description=neigh.toString();
                    description = description.trim();
                    Group group_example2 = new Group();
                    group_example2.build_group_given_desc(description, ratings, MOMRI.nbratings);
                    if(group_example2.getnbratings()>sigma)
                    {
                        groupsetghost.addgroupbyindex(group_example2, i);
                        double val_candidate = this.compute(op,groupsetghost);
                        CL.add(groupsetghost, val_candidate, op);
                    }
                } // end loop on all neighbors
            } // end loop on all groups in group set
            //System.out.println("Found "+CL.getsize()+" candidates.");
            if (CL.getsize()==0)
            {
                //System.out.println("No more neighbors!");
                double value_on_bound = this.compute(bound_on, current_gs);
                if (this.is_better(val, initial_val, op) && this.inbounds(value_on_bound, begin_with, end_with))
                {
                    //System.out.println("Optimized value: "+style.to_percentage(val));
                    return current_gs;
                }
                else
                {
                    return null;
                }
            }
            double best_val = this.initialize(op);
            int best_id= 0;
            for (int i=0; i<CL.getsize(); i++)
            {
                if (this.is_better(CL.getitem(i).getval(op), best_val, op))
                {
                    best_val = CL.getitem(i).getval(op);
                    best_id = i;
                }
            }
            double value_on_bound = this.compute(bound_on, CL.getitem(best_id).getgroups());
            if (best_val > val && this.inbounds(value_on_bound, begin_with, end_with))
            {
                //System.out.println("Optimized value: "+style.to_percentage(best_val));
                to_continue = false;
                return CL.getitem(best_id).getgroups();
            }
            else
            {
                current_gs=CL.getitem(best_id).getgroups();
            }
        }
    return null;
    }
}
