/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package moqo.mri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author omidvarb
 */
public class InputTools {
    public void GetRatingsOfMovie(String moviename) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("database.txt"));
        String line;
        int cnt=0;
        while ((line = br.readLine()) != null)
        {
            line = line.trim();
            String [] parts = line.split("\t");
            if (moviename.equals(parts[0]))
            {
                cnt++;
                //System.out.println(line);
            }
        }
        br.close();
        System.out.println(cnt+" records found!");
    }
}
