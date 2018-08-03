
package preprojectfx;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

/**
 * Class: AreaChartSample
 * 
 * This class is responsible for calculating the data sets of the diagram, and rendering it in a GUI seperate from the input GUI.
It does three main things: Calculates the datasets, renders the diagram, and creates an information panel.
 * 
 * The chartRun class is nested inside a parent class in order to extend both Application (JavaFX environment) and Maths.
 * 
 * Below are several headernotes, necessary due to the technacalities of the stacked area chart, and the complications of calculating G tolerances.
1. incRate determines how much the value of G should change per array position of curveArrayTop, this is inorder to maintain a curve resolution
of 50 no matter what the maximum G value is. Because the top and bottom curves stack over each other (necessary to create the correct shaded
areas), the value of incRate has to account for a changing "0" position (the G value of the bottom curve). 
2. vc = maximum maneuvering speed, marks where yellow area begins, | vne = never exceed speed, marks where red area begins.
3. Calculating G using guess work and hope, seems to work the majority of the time.

 *@author Joe
 *@version 1.0
 */

public class AreaChartSample extends Maths {

    public static class chartRun extends Application {

        private double[] curveArrayTop = new double[50]; //Array for storing graph curve points
        private double maxGpos; //Maximum G value for aircraft
        private int vc; //Maximum manuevering speed
        private double vfail; //Speed of certain structural failure
        private double incRate; //Determines the rate at which G should increase per data point in order to have 50 points within the G tolerances
        private double maxGneg;
        private double Gmod = 0.52;
        private double clmax;
        
        @Override
        public void start(Stage stage) {
            stage.setTitle("Area Chart Sample");
            //Stage stage = new Stage();

        }

        /****************************************************************************
         * Run inherited method calls to calculate the required results, and pass them onto diagram.
         * 
         * @param name Aircraft name
         * @param weight Weight
         * @param area Wing area
         * @param vs1 Stall speed
         * @param vne Never exceed speed
         ****************************************************************************/
        public void startCalc(String name, int weight, double area, double vs1, int vne) {
            /*
            curveArrayTop = new double[50]; //Array for storing graph curve points
            maxGpos; //Maximum G value for aircraft
            vc; //Maximum manuevering speed
            vfail; //Speed of certain structural failure
            incRate; //Determines the rate at which G should increase per data point in order to have 50 points within the G tolerances
            maxGneg;
            Gmod = 0.52; //Initial G value modifier, see point 3
            */
           
            clmax = 2*weight/(area*1.225*((vs1*0.5144)*(vs1*0.5144)));
            
            //Method calls below are inhereted from Maths, calculated all data needed for the diagram
            maxGpos = AreaChartSample.glimits(vne, weight, clmax, area, Gmod);
            vc = AreaChartSample.corneringSpeed(vne, weight, clmax, area, maxGpos);
            Gmod = (vne - vc);
            Gmod = (vne/Gmod)/10;
            maxGpos = AreaChartSample.glimits(vne, weight, clmax, area, Gmod);
            maxGpos = Math.round(maxGpos);
            maxGneg = (maxGpos * 0.4); //Aircraft have lower -G tolerances
            curveArrayTop = AreaChartSample.curvCalc(vne, weight, clmax, area, maxGpos);
            vc = (int)curveArrayTop[50];

            System.out.println(vc);
            vfail = vne * 1.15;
            incRate = maxGpos / 50;

            //Calls method to construct and display diagram
            diagram(name, vc, vne, curveArrayTop, maxGpos, maxGneg, incRate);

        }

        /****************************************************************************
         * Create the Diagram and GUI from the results calculated by the test method.
         * 
         * @param name Aircraft name
         * @param vc Cornering speed
         * @param vne Never exceed speed
         * @param curveArrayTop Array containing the top curve stall speeds
         * @param maxGpos Maximum G Force
         * @param macGneg -above
         * @param incRate plot increase rate
         ****************************************************************************/
        public void diagram(String name, int vc, int vne, double curveArrayTop[], double maxGpos, double maxGneg, double incRate)
        {
            Stage stage = new Stage();

            BorderPane root = new BorderPane();
            GridPane info = new GridPane();

            final NumberAxis xAxis = new NumberAxis(0, vne + 30, 5);
            final NumberAxis yAxis = new NumberAxis (-maxGpos, maxGpos + 1, 1); //Sets axis to appropiate values based on G tolerances
            final StackedAreaChart<Number, Number> ac //Initialises stacked area chart
            = new StackedAreaChart<>(xAxis, yAxis);
            ac.setTitle("V-n Diagram");

            XYChart.Series<Number, Number> bottomCurve = new XYChart.Series<>();
            bottomCurve.setName("");
            bottomCurve.getData().add(new XYChart.Data(0, 0));
            int y = 0;
            double n;
            for (n = 0; n > -maxGneg; n = n - incRate * 0.6) { //incRate is less as aircraft -G tolerances are lower
                bottomCurve.getData().add(new XYChart.Data(curveArrayTop[y], n)); //assigns airspeeds to the coordinate set of bottom curve
                y++;
            }
            bottomCurve.getData().add(new XYChart.Data(vc, -maxGneg)); //Draws line to meet vc 
            n = 0;

            XYChart.Series<Number, Number> topCurve = new XYChart.Series<>();
            topCurve.setName("Airspeed (KEAS)");
            topCurve.getData().add(new XYChart.Data(0, 0));
            int x = 0;
            for (n = 0; n < maxGpos; n = n + incRate) {
                if(curveArrayTop[x] < curveArrayTop[y])// see point 1
                {
                    topCurve.getData().add(new XYChart.Data(curveArrayTop[x], n*1.6));//see point 1
                }
                else if(curveArrayTop[x] > vc) //Stops topcurve overlapping vc and pushing yellow line up
                {
                    n = maxGpos;   
                }
                else 
                {
                    topCurve.getData().add(new XYChart.Data(curveArrayTop[x], n+maxGneg));  
                }
                x++;
            }

            //Yellow area of diagram
            XYChart.Series<Number, Number> southLine = new XYChart.Series<>();
            southLine.getData().add(new XYChart.Data(vc, 0));
            southLine.getData().add(new XYChart.Data(vne, -maxGneg));

            XYChart.Series<Number, Number> northLine = new XYChart.Series<>();
            northLine.getData().add(new XYChart.Data(vc, maxGpos+maxGneg));
            northLine.getData().add(new XYChart.Data(vne, maxGpos+maxGneg));

            //Red area of diagram
            XYChart.Series<Number, Number> southDeath = new XYChart.Series<>();
            southDeath.getData().add(new XYChart.Data(vne, -(maxGneg+maxGpos)));
            southDeath.getData().add(new XYChart.Data(vne*1.10, -maxGneg));

            XYChart.Series<Number, Number> northDeath = new XYChart.Series<>();
            northDeath.getData().add(new XYChart.Data(vne, maxGpos+maxGneg));
            northDeath.getData().add(new XYChart.Data(vne*1.10, maxGpos+maxGneg));

            ac.getData().addAll(bottomCurve);
            ac.getData().addAll(topCurve, southLine, northLine, southDeath, northDeath);

            //Information Panel
            TextArea stats = new TextArea();
            stats = statSet(name, stats, incRate, maxGpos,curveArrayTop, vne);

            info.add(stats, 10, 3, 2, 2);

            root.setCenter(ac);
            root.setLeft(info);

            Scene scene = new Scene(root, 1100, 900);
            scene.getStylesheets().add("preprojectfx/chart.css"); 

            stage.setScene(scene);
            stage.show();

        }

        /****************************************************************************
         * Called by Diagram to produce the information box as part of the results.
         * 
         * @param name Aircraft name
         * @param statf Input text area
         * @param increase Stores rate at which speeds increase in curve array
         * @param maxGpos Maximum G Force
         * @param curveArrayTop Array containing the top curve stall speeds
         * @param vne Never exceed speed
         * 
         * @return statf Returns the text area
         ****************************************************************************/
        public TextArea statSet(String name, TextArea statf, double increase, double maxGpos, double curveArrayTop[], int vne)
        {
            statf.setPrefRowCount(30);
            statf.setPrefSize(200, 800);
            //Adds text to textArea.
            statf.appendText("\n Type: " + name + "\n" +
                "\n Maximum +G: " + maxGpos +
                "\n Maximum -G: " + maxGpos*0.4 +
                "\n" +
                "\n    Stall speeds" + "\n");
            for(int n = 1; n < maxGpos + 1; n++) //Prints Stall speeds
            {
                double arrayPos = Math.round(n/increase);
                statf.appendText(n + "G stall speed: " + Math.round(curveArrayTop[(int)arrayPos]) + "\n");

            }

            statf.setEditable(false);
            return(statf);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
