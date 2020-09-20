package moqo.mri;

import fr.liglab.jlcm.util.MemoryPeakWatcherThread;
import java.io.IOException;
import java.util.Arrays;


public class MOMRI {
    
    public static int nbgroups=3;
    public static int nbratings=100000;
    public static int buffersize= 100;
    public static Group [] B = new Group[nbgroups];
    public static Rating [] ratings = new Rating[nbratings];
    public static Plan [] paretoset = new Plan[buffersize];
    public static GroupSet groupset = new GroupSet();
    public static Initializer init = new Initializer();
    public static Stylizer style = new Stylizer();
    public static int sigma = 10; // minimum number of ratings in a group
    public static double alpha = 1; // precision ratio for approximation
    public static String ds = "ml1m";
    public static int nbinterval = 10;
    public static int nbrepeat = 200;

    public static void main(String[] args) throws IOException {
                long startTime = System.currentTimeMillis();
                MemoryPeakWatcherThread watch = new MemoryPeakWatcherThread();
                watch.start();
                String query = "American Beauty";
                init.readratings(query, ds);
                
//              HeuristicSolver HS = new HeuristicSolver();
//              HS.solve(nbgroups, ratings, nbratings, sigma, nbinterval, nbrepeat);
                
                ApproximateSolver AS = new ApproximateSolver();
                AS.solve(nbgroups, ratings, nbratings, sigma, alpha);
                

    long stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    System.out.println("Execution time: "+elapsedTime+" ms");
    watch.interrupt();
    long mem=watch.getMaxUsedMemory();
    System.out.println("Memory: "+mem+" bytes");
    } // end main 
    
}
