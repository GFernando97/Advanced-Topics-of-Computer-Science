/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moqo.mri;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author omidvarb
 */
public class Group {
    private int nbratings;
    private int nbusers;
    Set users = new HashSet();
    private Rating [] ratings = new Rating[5000];
    private String label;
    
    public Group()
    {
        nbratings = 0;
        nbusers = 0;
    }
    
    public double average_rating()
    {
        double sum = 0;
        for (int i=0; i<this.nbratings; i++)
            sum += this.ratings[i].getRatingNote();
        return sum/nbratings;
    }
    
    public void adduser(int uid)
    {
        users.add(Integer.toString(uid));
    }
    
    public void addrating(Rating rating)
    {
        try
        {
           this.ratings[this.nbratings]=rating;
           this.nbratings++; 
        }
        catch(Exception e)
        {
           System.out.println("!!! ["+this.label+"]");
           System.exit(0);
        }
          
    }
    
    public void setlabel(String str)
    {
        this.label=str;
    }
    
    public String getlabel()
    {
        return this.label;
    }
        
    public String getusers()
    {
        String str="";
        Iterator it = users.iterator();
        while (it.hasNext())
            {
            // Get element
            Object element = it.next();
            str += element.toString()+" ";
            }
        return str;
    }
    
    public void build_group_given_desc(String desc, Rating [] ratings, int nbr)
    {
        if (this.nbratings>0)
            System.out.println("This group has already "+this.nbratings+ "ratings.");
        this.label=desc;
        //System.out.println(desc);
        String [] parts = desc.split(";");
        for(int i=0; i<nbr; i++)
        {
            boolean found=true;
            for(String part:parts)
            {
                if(part.equals("-"))
                        continue;
                if(ratings[i].is_attrib_equal(part)==false)
                    found = false;
            }
            if(found==true)
            {
                //ratings[i].show();
                this.addrating(ratings[i]);
                this.adduser(ratings[i].getuser());
            }
        }
    }
    
    public String getstatistics()
    {
        String str = Integer.toString(this.nbratings)+" ratings and "+Integer.toString(users.size())+" users.";
        return str;
    }
    
    public Rating [] getRatingsAsSet()
    {
        Rating [] hs = new Rating[10000];
        for(int i=0; i<this.nbratings; i++)
        {
            hs[i] = ratings[i];
        }
        return hs;
    }
    
    public int getnbratings()
    {
        return this.nbratings;
    }
    
     public float [] getRatingValuesAsSet()
    {
        float [] hs = new float[10000];
        for(int i=0; i<this.nbratings; i++)
        {
            hs[i] = ratings[i].getRatingNote();
        }
        return hs;
    }
     
    public HashSet getpossibleneighbors() throws IOException
    {
        LabelManagement LM=new LabelManagement();
        HashSet hs = LM.possbileneighbors(this);
        return hs;
    }
}
