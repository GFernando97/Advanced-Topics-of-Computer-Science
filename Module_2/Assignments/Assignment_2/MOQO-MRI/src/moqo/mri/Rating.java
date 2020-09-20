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
public class Rating {
    private String movie;
    private int userid;
    private float rating;
    private char gender;
    private String agecat;
    private String job;
    private String location;
    
    public void setrating(String m, int u, float r, char g, String a, String j, String l)
    {
        this.movie=m;
        this.userid=u;
        this.rating=r;
        this.gender=g;
        this.agecat=a;
        this.job=j;
        this.location=l;
    }
    
    public float getRatingNote()
    {
        return this.rating;
    }
    
    public boolean is_attrib_equal(String str)
    {
        if(str.equals(this.agecat))
            return true;
        if(str.equals(this.job))
            return true;
        if(str.equals(this.location))
            return true;
        return str.length()==1 && str.charAt(0)==this.gender;
    }
    
    public int getuser()
    {
        return this.userid;
    }
    
    public void show()
    {
        System.out.println(Integer.toString(this.userid)+" "+this.movie+" "+Float.toString(rating)+" "+this.gender+" "+this.agecat+" "+this.job+" "+this.location);
    }
}
