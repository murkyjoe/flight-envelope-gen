
package preprojectfx;

import static java.lang.Math.sqrt;
import static java.lang.Math.round;

/**
 * Class: Maths
 * Object containing methods for the bulk of mathematical operations carried out by the program.
 * 
 * @author: Joe
 * @version: 1.0
 */


public class Maths {

    static double check;
    private final static double pressure = 1.225; //Standard air fluid pressure.
    
    /****************************************************************************
     * Calculates the maximum G force that an aircraft can safely operate at with the data provided.
     * 
     * @param vne Never exceed speed
     * @param weight Weight
     * @param clmax Max lift co-efficent
     * @param area Wing area
     * @param gmod Accuracy Modifier
     * 
     * @return Gmax*gmod Returns the calculated G Value
     ****************************************************************************/
    public static double glimits(int vne, int weight, double clmax, double area, double gmod) {
        //double conVne = ((vne * 1.1) / 1.55) / 1.943;
        double conVne = vne*0.51444;
        System.out.println("TEST" + conVne);
        System.out.println(gmod);
        double Gmax = (conVne * conVne) * pressure * area * clmax / (2 * weight);
        System.out.println(Gmax);
        //System.out.println("G = " + Gmax*gmod);
        return Gmax*gmod;

    }

    /****************************************************************************
     * Calculate an array of curve points for the V-n diagram with the data provided.
     * 
     * @param vne Never exceed speed
     * @param weight Weight
     * @param clmax Max lift co-efficent
     * @param area Wing area
     * @param maxGpos The currently calculated max g value
     * 
     * @return curvePoints Returns the array storing the top curve stall speed points
     ****************************************************************************/
    public static double[] curvCalc(int vne, int weight, double clmax, double area, double maxGpos) {
        double[] curvePoints = new double[51];
        double freq = 0;
        //Assigns stall values to the curvePoints array.
        for (int n = 0; n < curvePoints.length; n++) {
            curvePoints[n] = 2 * (weight * freq) / (pressure * area * clmax);
            curvePoints[n] = sqrt(curvePoints[n]) * 1.945;
            freq = freq + (maxGpos / 50);

            System.out.println(curvePoints[n]);
        }
        return (curvePoints);

    }
    
    /****************************************************************************
     * Calculate the aircrafts cornering speed with the data provided
     * 
     * @param vne Never exceed speed
     * @param weight Weight
     * @param clmax Max lift co-efficent
     * @param area Wing area
     * @param maxGpos The currently calculated max g value
     * 
     * @return vc Returns cornering speed
     ****************************************************************************/
    public static int corneringSpeed(int vne, int weight, double clmax, double area, double maxGpos) {
        double vs1 = 2 * (weight) / (pressure * area * clmax);
        double vc = sqrt(maxGpos * vs1);
        vc = vc*1.945;
        System.out.println("WHAT " + area*clmax);
        //double vc = sqrt((double)weight/((double)weight+150));
        //vc = vc*100;
        System.out.println("CORN" + vc);
        return (int) vc;
    }

}
