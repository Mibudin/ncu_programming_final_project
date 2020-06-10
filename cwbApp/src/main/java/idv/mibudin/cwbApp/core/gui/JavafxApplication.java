package idv.mibudin.cwbApp.core.gui;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class JavafxApplication extends Application
{
    public static void launchJavafxApplication(String[] args)
    {
        System.setProperty("prism.allowhidpi", "false");

        Application.launch(JavafxApplication.class, args);
    }

	@Override
    public void start(Stage primaryStage) throws Exception
    {
        // FileInputStream fis = new FileInputStream("CwbApplet/res/informationMap/COUNTY_MOI_1081121 (2).json");
        // FileReader fr = new FileReader("D:/Workspaces/ncu_programming_final_project/res/informationMap/COUNTY_MOI_1081121 (2).json");
        InputStreamReader isr = new InputStreamReader(new FileInputStream("D:/Workspaces/ncu_programming_final_project/res/informationMap/COUNTY_MOI_1081121 (2).json"), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String temp;
        while((temp = br.readLine()) != null)
        {
            sb.append(temp);
        }
        br.close();
        JSONObject mapData = new JSONObject(sb.toString());
        // System.out.println(sb.toString());

        InformationMapPane imp = new InformationMapPane(mapData);

        // InformationMapCanvas imc = new InformationMapCanvas(640, 480);
        // imc.draw();

        Scene s = new Scene(imp, 1280, 720);
        s.addEventFilter(MouseEvent.MOUSE_PRESSED, 
            (MouseEvent mouseEvent) ->
            {
                System.out.println("> (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
            }
        );

        primaryStage.setTitle("JavaFX Test");
        primaryStage.setScene(s);
		primaryStage.show();
	}
}
