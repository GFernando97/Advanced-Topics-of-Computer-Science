/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moqo.mri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
/**
 *
 * @author omidvarb
 */
public class LabelManagement {
    private String [][] attrib_values;
    int nb_attribs=4;
    
    public LabelManagement() throws FileNotFoundException, IOException
    {
        this.initialize();
    }
    //{{"M","F" ,"-"}, {"Teen", "Young", "Middle", "Old", "-"}, {"writer", "executive", "technician", "educator", "engineer", "librarian", "programmer", "administrator", "student", "retired", "other", "doctor", "marketing", "artist", "lawyer", "salesman", "homemaker", "healthcare", "none", "entertainment", "scientist", "-"}, {"MN","FOREIGN","KY","CO","MA","PA","VA","CA","WA","OR","NH","OH","NY","KS","MO","WI","GA","TX","IA","ND","LA","MD","NC","UT","MI","AZ","NV","NJ","OK","SC","CT","IN","IL","DC","ID","VT","TN","AK","MS","FL","NE","AL","AR","MT","NM","RI","HI","WV","ME","DE","WY","AP","SD","AE","-"}};
    
    private void initialize() throws FileNotFoundException, IOException
    {
        String ds = MOMRI.ds;
        BufferedReader br = new BufferedReader(new FileReader("data/"+ds+"/ds.info"));
        nb_attribs = Integer.parseInt(br.readLine());
        int max_val = Integer.parseInt(br.readLine());
        //System.out.println(nb_attribs+" "+max_val);
        attrib_values = new String[nb_attribs][max_val];
        String line = "";
        for (int cnt_attrib=0; cnt_attrib<nb_attribs; cnt_attrib++)
        {
            line = br.readLine();
            //System.out.println(cnt_attrib+" "+line);
            String [] parts = line.trim().split(";");
            attrib_values [cnt_attrib] = parts;
        }
    }
    
    public String random_group_description()
    {
        //System.out.println("I'm going to make a random description.");
        boolean any=false;
        String str="";
        Random randomGenerator = new Random();
        for (int i=0; i<this.nb_attribs; i++)
        {
            int limit=2;
            if (i==3 && any==false)
                limit=1;
            int existence = randomGenerator.nextInt(limit);
            if (existence==1)
                str+="-;";
            else
            {
                any=true;
                int whichattrib = randomGenerator.nextInt(attrib_values[i].length);
                str+=attrib_values[i][whichattrib]+";";
            }
        }
        //System.out.println("**"+str);
        return str;
    }
    
    public HashSet possbileneighbors(Group g)
    {
        String desc=g.getlabel();
        String [] parts = desc.split(";");
        HashSet out = new HashSet();
        boolean possible = false;
        for (int i=0; i<parts.length; i++)
        {
            if (parts[i].equals("-"))
            {
                possible=true;
                for (int j=0; j<attrib_values[i].length; j++)
                {
                    String [] temp = new String[4];
                    for (int u=0; u<parts.length; u++)
                        temp[u] = parts[u];
                    temp[i]=attrib_values[i][j];
                    String b="";
                    for(int u=0; u<parts.length; u++)
                    {
                        b += ";"+temp[u];
                    }
                    out.add(b);
                }
            }
        }
        
        if (possible==false)
            return null;
        return out;
        
    }
    
    public GroupSet make_k_random_groups(int k, Rating [] ratings, int nbratings, int sigma)
    {
        GroupSet gs = new GroupSet();
        //System.out.println("Random initial groups: (k="+k+")");
        //System.out.println("I'm going to make "+k+ " random groups.");
        for (int i=0; i<k; i++)
        {
            int nb = 0;
            String s="";
            Group g = new Group();
            while (nb < sigma)
            {
                //System.out.println("I have already found "+ nb + "random groups.");
                s = this.random_group_description();
                Group gp = new Group();
                gp.build_group_given_desc(s, ratings, nbratings);
                nb = gp.getnbratings();
                for (int j=0; j<i; j++)
                    if(gs.getgroup(j).getlabel().equals(s) || gs.getgroup(j).getlabel().equals("-;-;"))
                        nb = 0;
                g = gp;
            }
            //System.out.println("*"+s+"*");
            
            gs.addgroup(g);
            //System.out.println((i+1)+". "+s+" ("+nb+" ratings)");
        }
        return gs;
    }
    
    public String next_value(String value, int attid)
    {
        int id=0;
        //System.out.println(value+" "+attid);
        //System.out.println(attrib_values[attid].length);
        for(int i=0; i<attrib_values[attid].length; i++)
        {
            if (attrib_values[attid][i].equals(value))
            {
                id = i;
                break;
            }
        }
        int my_id = id + 1 ;
        //System.out.println(my_id);
        if (my_id == attrib_values[attid].length)
            return "";
        else
        {
            //System.out.println(attrib_values[attid][my_id]);
            return attrib_values[attid][my_id];
        }
    }
    
    public String next_group(String current_group)
    {
        //System.out.println("**"+current_group);
        int attrib_available = this.nb_attribs-1;
        String parts [] = current_group.split(";");
        String next_att = "";
        while(next_att.equals(""))
        {
            next_att = next_value(parts[attrib_available], attrib_available);
            if (next_att.equals(""))
            {
                attrib_available--;
                if (attrib_available==-1)
                    return null;
                for(int u=attrib_available+1; u<parts.length; u++)
                    parts[u]=attrib_values[u][0];
            }
        }
    parts[attrib_available]=next_att;
    String out="";
    for (int i=0; i<parts.length; i++)
        out += parts[i]+";";
    //System.out.println("**"+out);
    return out;
    }
}
