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
import static moqo.mri.MOMRI.nbratings;
import static moqo.mri.MOMRI.ratings;

/**
 *
 * @author omidvarb
 */
public class Initializer {
    
    public void readratings(String query, String ds) throws FileNotFoundException, IOException
    {
        Stylizer style=new Stylizer();
        //read ratings related to the query
        // database.txt contains ratings in this format: movie, rating, gender, age-group, job, location
        BufferedReader br = new BufferedReader(new FileReader("data/"+ds+"/database.txt"));
        String line;
        int cnt=0;
        double sum_ratings = 0;
        while ((line = br.readLine()) != null)
        {
            line = line.trim();
            String [] parts = line.split("\t");
            if (parts[1].contains(query))
            {
            String movie = parts[1];
            int uid = Integer.parseInt(parts[0]);
            float r = Float.parseFloat(parts[2]);
            sum_ratings += r;
            char gender = parts[3].charAt(0);
            //if (gender=='F')
              //  continue;
            String agecat = parts[4];
            //if (!agecat.equals("Young"))
              //  continue;
            String job = parts[5];
            //if (!job.equals("K-12 student"))
              //  continue;
            //System.out.println("**"+job+"**");
            String location = parts[6];
            //if (!location.equals("CA"))
              //  continue;
            ratings[cnt] = new Rating();
            ratings[cnt].setrating(movie, uid, r, gender, agecat, job, location);
            //ratings[cnt].show();
            cnt++;
            
            }
        }
        double average_ratings = sum_ratings / cnt;
        br.close();
        System.out.println("Query: "+query+" ("+cnt+" ratings with average "+style.to_nice_double(average_ratings)+")");
        nbratings = cnt;
    }
}
