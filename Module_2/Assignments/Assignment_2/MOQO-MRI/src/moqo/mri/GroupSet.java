/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moqo.mri;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author omidvarb
 */
public class GroupSet {
    private int maxgroups=100000;
    private Group [] groups=new Group[maxgroups];
    private int counter=0;
    Stylizer style = new Stylizer();

    //// INI MODIFIED ////
    public double average_rating(){
        double av_rating = 0;

        for(int i = 0; i < counter; i++){
            av_rating = av_rating + groups[i].average_rating();
        }

        return av_rating / counter;
    }
    //// END MODIFIED ////

    public void show()
    {
        for(int i=0; i<counter; i++)
        {
            System.out.println((i+1)+". ["+this.groups[i].getlabel()+"] ("+this.groups[i].getnbratings()+" ratings with average "+style.to_nice_double(this.groups[i].average_rating())+")");
        }
    }

    public void addgroup(Group g)
    {
        groups[counter]=new Group();
        groups[counter++]=g;
    }

    public void addgroupbyindex(Group G, int i)
    {
        this.groups[i]=G;
    }

    public Group getgroup(int i)
    {
        return this.groups[i];
    }

    public int getsize()
    {
        return counter;
    }

    public int countunion()
    {
        HashSet allratings = new HashSet();
        for(int i=0; i<counter; i++)
        {
            Rating [] rr = this.groups[i].getRatingsAsSet();
            for(int j=0; j<rr.length; j++)
            {
//                boolean found = false;
//                for (int u=0; u<MOQOMRI.nbratings; u++)
//                    if (MOQOMRI.ratings[u].equals(rr[j]))
//                    {
//                        System.out.println("yes");
//                        found=true;
//                        break;
//                    }
//                if (found==true)
                    allratings.add(rr[j]);
            }
        }
        return allratings.size();
    }

    public int pairwiseunionsize(Group g1, Group g2)
    {
        HashSet allusers = new HashSet();
        String us1_str=g1.getusers();
        String us2_str=g2.getusers();
        String [] us1_arr = us1_str.split(",");
        String [] us2_arr = us2_str.split(",");
        for (String u1: us1_arr)
            allusers.add(Integer.parseInt(u1));
        for (String u2: us2_arr)
            allusers.add(Integer.parseInt(u2));
        return allusers.size();
    }

    public int pairwiseintersectsize(int i, int j)
    {
        Group g1 = new Group();
        Group g2 = new Group();
        g1 = this.groups[i];
        g2 = this.groups[j];
        HashSet allrating1 = new HashSet();
        HashSet allrating2 = new HashSet();
        HashSet allallratings= new HashSet();
        Rating [] us1_arr = g1.getRatingsAsSet();
        Rating [] us2_arr = g2.getRatingsAsSet();
        for (Rating rt:us1_arr)
            allrating1.add(rt);
        for (Rating rt:us2_arr)
            allrating2.add(rt);
        for (int p=0; p<MOMRI.nbratings; p++)
            allallratings.add(MOMRI.ratings[p]);
        allrating1.retainAll(allrating2);
        allrating1.retainAll(allallratings);
        return allrating1.size();
    }

    public double getMean(float [] hs)
    {
        double sum = 0.0;
        for (int i=0; i<hs.length; i++)
            sum += (double) hs[i];
        return sum/(double)hs.length;
    }

    double getVariance(float [] hs)
    {
        double mean = getMean(hs);
        double temp = 0;
        for (int i=0; i<hs.length; i++)
        {
            double a = (double) hs[i];
            temp += (mean-a)*(mean-a);
        }
        return temp/(double)hs.length;
    }

    double getStdDev(float [] hs)
    {
        return Math.sqrt(getVariance(hs));
    }

    public double getnormalizedstdev(int n)
    {
        float [] rs = new float[000];
        rs = this.groups[n].getRatingValuesAsSet();
        double stdev = this.getStdDev(rs) / 5;
        return stdev;
    }
}
