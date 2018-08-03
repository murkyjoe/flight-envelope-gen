
package preprojectfx;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.control.ToolBar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.io.*;

/***********************************************
 * Class: PreProjectfx
 * Creates the main GUI and handles the events for all elements.
 * 
 * @author: Joe
 * @version: 1.0
 * 
 ***********************************************/
public class mainGUI extends Application {

    /****************************
     * Builds GUI, and handles events for all elements.
     * @param primaryStage The base componenent for a JavaFX scene
     * 
     ****************************/

    @Override
    public void start(Stage primaryStage) {
        //Builds the main GUI.
        ToolBar toolbar = new ToolBar();
        Button btnAbout = new Button("About");
        Button btnHelp = new Button("Help");

        final ComboBox load = loads.loadDropDown();
        //load = loads.test();

        toolbar.getItems().add(load);
        toolbar.getItems().add(btnAbout);
        toolbar.getItems().add(btnHelp);

        load.setPromptText("Load");

        Label wlab = new Label(" Enter weight(kg) ");
        Label alab = new Label(" Enter wing area(m^2) ");
        Label cllab = new Label(" Enter stall speed(Knots)");
        Label vnelab = new Label(" Enter Vne(Knots)");

        TextField wfld = new TextField();
        TextField afld = new TextField();
        TextField clfld = new TextField();
        TextField vnefld = new TextField();

        Image iconWeight = new Image("GFX/planeWeight.png", 100, 150, false, false);
        ImageView icW = new ImageView(iconWeight);
        icW.setFitWidth(60);
        icW.setFitHeight(60);
        Tooltip wTip = new Tooltip("Aircraft weight with typical number of passengers");
        Tooltip.install(icW, wTip);

        Image iconArea = new Image("GFX/planeArea.png", 100, 150, false, false);
        ImageView icA = new ImageView(iconArea);
        icA.setFitWidth(60);
        icA.setFitHeight(60);
        Tooltip aTip = new Tooltip("Aircraft wing area, excluding non-wing surfaces, and in normal flight config");
        Tooltip.install(icA, aTip);

        Image iconStall = new Image("GFX/planeStall.png", 100, 150, false, false);
        ImageView icS = new ImageView(iconStall);
        icS.setFitWidth(60);
        icS.setFitHeight(60);
        Tooltip sTip = new Tooltip("Aircraft unaccelerated stall speed in knots, normal flight config");
        Tooltip.install(icS, sTip);

        Image iconVne = new Image("GFX/planeVne.png", 100, 150, false, false);
        ImageView icV = new ImageView(iconVne);
        icV.setFitWidth(60);
        icV.setFitHeight(60);
        Tooltip vTip = new Tooltip("Aircraft never exceed speed at 1g normal flight config");
        Tooltip.install(icV, vTip);

        Text inputTitle = new Text("Aircraft details: ");
        inputTitle.setFont(Font.font("Bauhaus", 20));

        Text errorMessage = new Text("");

        Button calc = new Button("Calculate");
        Button save = new Button("Save");

        //Event handler for the calculate button.
        EventHandler calcHandler = new EventHandler<ActionEvent>()
            {

                @Override
                public void handle(ActionEvent event) {
                    errorMessage.setText("");
                    try{
                        String name = null;
                        int weight = Integer.parseInt(wfld.getText());
                        double area = Double.parseDouble(afld.getText());
                        int vs1 = Integer.parseInt(clfld.getText());
                        int Vne = Integer.parseInt(vnefld.getText());

                        //Create instance of AreaChartSample object to produce results.
                        AreaChartSample.chartRun graph = new AreaChartSample.chartRun();
                        graph.startCalc(name, weight, area, vs1, Vne);
                    }
                    catch(NumberFormatException e)
                    {
                        errorMessage.setText("Invalid data! Please enter valid inputs.");
                        errorMessage.setFill(Color.RED);
                    }

                }
            };
        //Event handler for the about button.
        EventHandler aboutHandler = new EventHandler<ActionEvent>()
            {

                @Override
                public void handle(ActionEvent event) {
                    Help.aboutGen();
                }
            };

        //Event handler for the help button.
        EventHandler helpHandler = new EventHandler<ActionEvent>()
            {

                @Override
                public void handle(ActionEvent event) {
                    Help.helpGen();
                }
            };

        EventHandler saveHandler = new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event){
                    errorMessage.setText("");
                    try{
                        int weight = Integer.parseInt(wfld.getText());
                        double area = Double.parseDouble(afld.getText());
                        int vs1 = Integer.parseInt(clfld.getText());
                        int Vne = Integer.parseInt(vnefld.getText());
                        Help.name(weight,area,vs1,Vne);

                    }
                    catch(NumberFormatException e)
                    {
                        errorMessage.setText("Invalid data! Please enter valid inputs before saving.");
                        errorMessage.setFill(Color.RED);
                    }

                }
            };
        load.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String selection, String t)
                {
                    System.out.println("CHANGE");
                    loads.loader(t);
                    load.getSelectionModel().clearSelection();
                }
            });

        calc.setOnAction(calcHandler);
        btnAbout.setOnAction(aboutHandler);
        btnHelp.setOnAction(helpHandler);
        save.setOnAction(saveHandler);

        primaryStage.setTitle("ProjTest");
        BorderPane root = new BorderPane();
        GridPane data = new GridPane();
        data.setAlignment(Pos.CENTER_RIGHT);
        data.setHgap(10);
        data.setVgap(15);
        data.setPadding(new Insets(50, 25, 50, 25));

        GridPane sp = new GridPane();
        sp.setAlignment(Pos.TOP_LEFT);
        sp.setHgap(10);
        sp.setVgap(5);
        sp.setHgap(10);
        sp.setVgap(5);
        sp.setPadding(new Insets(50, 25, 50, 25));

        HBox wInput = new HBox(10.0);
        wInput.getChildren().add(icW);
        wInput.getChildren().add(wfld);

        HBox aInput = new HBox(10.0);
        aInput.getChildren().add(icA);
        aInput.getChildren().add(afld);

        HBox clInput = new HBox(10.0);
        clInput.getChildren().add(icS);
        clInput.getChildren().add(clfld);

        HBox vneInput = new HBox(10.0);
        vneInput.getChildren().add(icV);
        vneInput.getChildren().add(vnefld);

        sp.add(wlab, 1, 1);
        sp.add(wInput, 1, 2);
        sp.add(alab, 1, 6);
        sp.add(aInput, 1, 7);
        sp.add(cllab, 1, 10);
        sp.add(clInput, 1, 11);
        sp.add(vnelab, 1, 14);
        sp.add(vneInput, 1, 15);

        HBox hbent = new HBox(20);
        hbent.setAlignment(Pos.BOTTOM_RIGHT);
        hbent.getChildren().add(save);
        hbent.getChildren().add(calc);
        hbent.getChildren().add(errorMessage);

        sp.add(hbent, 1, 18);
        sp.add(inputTitle, 1, 0, 2, 1);

        root.setCenter(sp);
        root.setRight(data);
        root.setTop(toolbar);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(mainGUI.class.getResource("preprojectfx.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /****************************************************************************
     * Accepts loaded values for an aircraft profile from the 'load' class,
    and creates an instance of AreaChartSample to produce the results for it.
     * 
     * @param na name of aircraft
     * @param Weight weight
     * @param wa wing area
     * @param vs1 stall speed
     * @param vne never exceed speed
     ****************************************************************************/
    public static void loadProf(String na, int Weight, double wa, int vs1, int vne)
    {
        String name = na;
        int weight = Weight;
        double area = wa;
        int vs = vs1;
        int Vne = vne;
        System.out.println("Loaded");

        AreaChartSample.chartRun graph = new AreaChartSample.chartRun();
        graph.startCalc(name, weight, area, vs, Vne);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
