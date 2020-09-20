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
public class Plan {
    private GroupSet groups = new GroupSet();
    private ObjectiveValues values;


    //// INI MODIFIED ////
    public Plan(GroupSet gs)
    {
        Objectives o = new Objectives();
        this.groups=gs;
        double c = o.compute('c', gs);
        double d = o.compute('d', gs);
        double e = o.compute('e', gs);
        double r = o.compute('r', gs);

        values = new ObjectiveValues(c,d,e,r);
    }
    //// END MODIFIED ////

    public Plan()
    {

    }

    //// INI MODIFIED ////
    public void set_groupset(GroupSet gs)
    {
        Objectives o = new Objectives();
        this.groups=gs;
        double c = o.compute('c', gs);
        double d = o.compute('d', gs);
        double e = o.compute('e', gs);
        double r = o.compute('r', gs);
        values = new ObjectiveValues(c,d,e,r);
    }
    //// END MODIFIED ////

    public GroupSet get_groupset()
    {
        return this.groups;
    }

    //// INI MODIFIED ////
    public void report_values()
    {
        System.out.println(values.get('c')+" "+values.get('d')+" "+values.get('e')+" "+"\nRating Deviation:"+values.get('r')+"\n ");
    }
    //// END MODIFIED ////

    //// INI MODIFIED ////
    public double get_values(char c)
    {
        switch(c)
        {
            case 'c':
                return values.get('c');
            case 'd':
                return values.get('d');
            case 'e':
                return values.get('e');
            case 'r':
                return values.get('r');
        }
        return 0;
    }
    //// END MODIFIED ////
}
