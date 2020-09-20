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
public class ObjectiveValues {
        private double cvg;
    private double dvs;
    private double err;
    //// INI MODIFIED ////
    private double dev;
    //// END MODIFIED ////

    //// INI MODIFIED ////
    public ObjectiveValues()
    {
        this.cvg=0;
        this.dvs=0;
        this.err=0;
        this.dev=0;
    }
    //// END MODIFIED ////

    //// INI MODIFIED ////
    public ObjectiveValues(double c, double d, double e, double r)
    {
        this.cvg=c;
        this.dvs=d;
        this.err=e;
        this.dev=r;
    }
    //// END MODIFIED ////

    /**
    * Returns the value of objective.
    * The input parameter is a character.
    * argument is a specifier that is relative to the url argument.
    * @param  op  hello
    * @return      well it returns what has to be returned
    */
    //// INI MODIFIED ////
    public double get(char op)
    {
        switch(op)
        {
            case 'c':
                return this.cvg;
            case 'd':
                return this.dvs;
            case 'e':
                return this.err;
            case 'r':
                return this.dev;
        }
    return 0;
    }
    //// END MODIFIED ///
}
