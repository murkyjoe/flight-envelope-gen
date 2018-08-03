package preprojectfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class: loads
 * Responsible for loading and saving aircraft profiles by reading and writing to the saves text file.
 * 
 * @author Joe
 * @version 1.0
 */


public class loads  {

    /****************************************************************************
     * Finds the names of all saved aircraft profiles and adds them to the main GUI drop down menu.
     * 
     * @return list Returns a JavaFX ComboBox list for the mainGUI
     ****************************************************************************/
    public static ComboBox loadDropDown() 
    { 
        ComboBox list = new ComboBox(); // 
        try{

            BufferedReader br = new BufferedReader(new FileReader("saves.txt"));
            String line;
            Pattern check = Pattern.compile("n(.+)n");
            Matcher match;

            while((line = br.readLine()) != null)
            {
                match = check.matcher(line);
                if(match.find())
                {

                    line = (String)match.group(1);
                    list.getItems().add(line);
                }
            }
            br.close();

        }
        catch(IOException ex)
        {
            System.out.println("You broke your saves. Haha!");
        }

        return list;
    }
    
    /****************************************************************************
     * Find an aircraft profile that matches a user search term, and loads the profile data for use.
     * 
     * @param search The search term selected from the drop down menu
     ****************************************************************************/
    public static void loader(String search)
    {
        String name = search;
        int weight = 0;
        double area = 0;
        int vs1 = 0;
        int vne = 0;
        String con;

        try{
            BufferedReader br = new BufferedReader(new FileReader("saves.txt"));
            String line;
            Pattern check = Pattern.compile(search);
            Matcher match;
            boolean found = false;

            while((line = br.readLine()) != null && found == false )

            {
                match = check.matcher(line);
                if(match.find())
                {
                    found = true;
                    check = Pattern.compile("'w(.+)w'");
                    match = check.matcher(line);
                    if(match.find())
                    {
                        con = match.group(1);
                        weight = Integer.parseInt(con);
                    }

                    check = Pattern.compile("'a(.+)a'");
                    match = check.matcher(line);
                    if(match.find())
                    {
                        con = match.group(1);
                        area = Double.parseDouble(con);
                    }

                    check = Pattern.compile("'c(.+)c'");
                    match = check.matcher(line);
                    if(match.find())
                    {
                        con = match.group(1);
                        vs1 = Integer.parseInt(con);
                    }

                    check = Pattern.compile("'s(.+)s'");
                    match = check.matcher(line);
                    if(match.find())
                    {
                        con = match.group(1);
                        vne = Integer.parseInt(con);
                    }

                }
            }

            br.close();
            mainGUI.loadProf(name, weight, area, vs1, vne);

        }
        catch(IOException ex)
        {
        }
    }

    /****************************************************************************
     * Find an aircraft profile that matches a user search term, and loads the profile data for use.
     * 
     * @param name The profile name entered by a user
     * @param weight Weight
     * @param area Wing area
     * @param vs Stall speed
     * @param vne Never exceed speed
     ****************************************************************************/
    public static void save(String name, int weight, double area, int vs, int vne)
    {
        try
        {
            String data = "n" + name + "n" + "  " + "'w" + weight +"w'" + "  " + "'a"+area+"a'" + "  " + "'c" + vs +"c'" + "  " +"'s"+vne+"s'";
            File file = new File("saves.txt");
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(data);
            pw.close();

        }
        catch(IOException ex)
        {
        }
    }

}
