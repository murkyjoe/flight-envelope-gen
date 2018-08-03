
package preprojectfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class: Help
 * 
 * Handles the help dialog box and the save profile GUI
 * 
 * @author Joe
 * @version 1.0
 */

public class Help extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("ProjTest");

    }
    
    /****************************************************************************
         * Build the about dialog box created when user clicks "about" in the main GUI.
         *
         ****************************************************************************/
    public static void aboutGen()
    {
        System.out.println("test");
        Stage stage = new Stage();
        BorderPane root = new BorderPane();

        TextArea text = new TextArea();
        text.setPrefRowCount(20);
        text.setPrefSize(400, 400);
        text.setEditable(false);
        text.appendText(" Flight Envelope Generator V 0.10" +
            "\n --------------------------------------------------" +
            "\n - Minimum system requirements: " + 
            "\n      *1.33GHz quad core processor, 1GB RAM, 300Kb free disk space." +
            "\n      *Java version 8" + "\n" +

            "\n - Any data or information produced may be innacurate, consult your POH." + 
            "\n - Directly manipulating the saves txt file may result in crashes and or incorrct results.");

                        
        root.setCenter(text);
        Scene scene = new Scene(root, 600, 400); 
        stage.setScene(scene);
        stage.show();

    }
    
    /****************************************************************************
         * Build the help dialog box created when user clicks "help" in the main GUI.
         *
         ****************************************************************************/
    public static void helpGen()
    {
        System.out.println("test");
        Stage stage = new Stage();
        BorderPane root = new BorderPane();

        TextArea text = new TextArea();
        text.setPrefRowCount(20);
        text.setPrefSize(400, 400);
        text.setEditable(false);
        text.appendText(" Help" +
            "\n --------------------------------------------------" +
            "\n - Inputs: " + 
            "\n      1. Wing Area: The wing area of the aircraft in m^2, this icludes only the wings." +
            "\n      2. Weight: The weight of the aircraft with passangers in KG. The weight can be " +
            "\n         modified with the slider in the results panel." +
            "\n      3. Stall speed: The airspeed at which the aircraft will stall when unaccelerated" +
            "\n      4. Vne: The never exceed speed of the aircraft in Knots. This can be found in POH" +

            "\n - Upon generating a diagram, you can save it as an aircraft profile. Click the save button" + 
            "\n   next to calculate, and enter a name for the aircraft. Saved aircraft profiles can be selected" +
            "\n   by clicking Load.");

        root.setCenter(text);
        Scene scene = new Scene(root, 600, 400); 
        stage.setScene(scene);
        stage.show();

    }
    
    /****************************************************************************
         * Build the save box created when user clicks "save" in the main GUI.
         * 
         * @param weight Weight
         * @param area Wing area
         * @param vs Stall speed
         * @param vne Never exceed speed
         ****************************************************************************/
    public static void name(int weight, double area, int vs, int vne)
    {
        Stage stage = new Stage();
        BorderPane root = new BorderPane();

        Button btn = new Button("Enter");
        Label lab = new Label("Enter aircraft name");
        TextField text = new TextField();
        text.setText("Enter aircraft name");

        HBox box = new HBox(30);
        box.setPadding(new Insets(50, 25, 50, 25));
        box.getChildren().add(text);
        box.getChildren().add(btn);

        root.setCenter(box);
        

        Scene scene = new Scene(root, 300, 150); 
        stage.setScene(scene);
        stage.show();

        EventHandler btnHandler = new EventHandler<ActionEvent>()
            {

                @Override
                public void handle(ActionEvent event) {

                    String aircraftName = text.getText();
                    loads.save(aircraftName,weight,area, vs, vne);

                }
            };
        btn.setOnAction(btnHandler);

    }

    public static void main(String[] args) {
        launch(args);
    }

}