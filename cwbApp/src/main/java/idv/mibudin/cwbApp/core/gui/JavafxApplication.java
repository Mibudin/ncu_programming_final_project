package idv.mibudin.cwbApp.core.gui;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import idv.mibudin.cwbApp.core.cwb.CwbApi;
import idv.mibudin.cwbApp.core.cwb.CwbApiDataID;
import idv.mibudin.cwbApp.core.data.Information;
import idv.mibudin.cwbApp.core.data.InformationElement;
import idv.mibudin.cwbApp.core.data.InformationGroup;
import idv.mibudin.cwbApp.core.data.TopoJson;
import idv.mibudin.cwbApp.core.data.Vector2D;
import idv.mibudin.cwbApp.core.data.Information.InformationType;
import idv.mibudin.cwbApp.core.tool.VectorTools.Vector2DTransformer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class JavafxApplication extends Application
{
    private static final int WIDTH  = 600;
    private static final int HEIGHT = 600;

    private static final String TITLE = "CWB API DEMO";
    private static final boolean IS_RESIZABLE = false;


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
        InputStreamReader isr = new InputStreamReader(new FileInputStream("../res/informationMap/COUNTY_MOI_1081121 (2).json"), "UTF-8");
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

        // InformationMapPane imp = new InformationMapPane(mapData);

        // // InformationMapCanvas imc = new InformationMapCanvas(640, 480);
        // // imc.draw();

        // Scene s = new Scene(imp, 1280, 720);
        // s.addEventFilter(MouseEvent.MOUSE_PRESSED, 
        //     (MouseEvent mouseEvent) ->
        //     {
        //         System.out.println("> (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
        //     }
        // );

        Vector2DTransformer demoTransformer = 
            (Vector2D vector2D) ->
            {
                return vector2D
                    .zoom(1, -1)
                    .add(-117.5, 26.7)
                    .zoom(300, 300);
            }
        ;

        TopoJsonRenderer tjr = new TopoJsonRenderer(new TopoJson(mapData));
        // tjr.getTopoJson().getTopologyObject().setVector2DTransformer(demoTransformer);
        // tjr.cacheArcs();

        // Pane p = new Pane();
        // p.setStyle("-fx-background-color: transparent;");
        // p.getChildren().addAll(tjr.renderToShapes());

        // ScrollPane sp = createScrollPane(p);
        // sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        // Rectangle clip = new Rectangle(0, 0, WIDTH, HEIGHT);
        // sp.setClip(clip);

        // StackPane stp = new StackPane();
        // stp.setStyle("-fx-background-color: rgba(32, 32, 32, 0.85);" +
        //         //    "-fx-effect: dropshadow(gaussian, white, 50, 0, 0, 0);" +
        //            "-fx-background-insets: 0;"
        // );
        // stp.getChildren().addAll(sp);

        // CwbApi ca = new CwbApi("CWB-D3AA9928-023B-4902-BBAB-55FB9A448508");
        // ca.setShouldReturnJson(true);
        // Map<String, String> parameters = new HashMap<String, String>();
        // parameters.put("elementName", "HOUR_24");
        // parameters.put("parameterName", "CITY");
        // String data = ca.requestDatastore("O-A0002-001", parameters).getResponseContent();

        CwbApi ca = new CwbApi("CWB-D3AA9928-023B-4902-BBAB-55FB9A448508");
        ca.setShouldReturnJson(true);
        String data = ca.requestDatastore(
            // CwbApiDataID.AUTO_RAIN_STA__RAIN_OBS,
            CwbApiDataID.AUTO_WX_STA__WX_OBS,
            // "elementName", "latest_3days",
            "elementName", "TEMP",
            "parameterName", "CITY"
        ).getResponseContent();


        JSONObject tempData = new JSONObject(data);
        JSONArray locations = tempData.getJSONObject("records").getJSONArray("location");
        Vector<InformationElement> informations1 = new Vector<InformationElement>();
        for(int i = 0; i < locations.length(); i++)
        {
            JSONObject location = locations.getJSONObject(i);

            String name = location.getString("locationName");
            Vector2D loc = new Vector2D(location.getDouble("lon"), location.getDouble("lat"));
            double value = location.getJSONArray("weatherElement").getJSONObject(0).getDouble("elementValue");
    
            InformationGroup ig = new InformationGroup();
            ig.addInformation(new Information(InformationType.TEMPERATURE, value));

            informations1.add(i, new InformationElement(name, loc, ig));
        }

        InformationMapPane_Rev imp_r = new InformationMapPane_Rev(WIDTH - 100, HEIGHT, demoTransformer, tjr, informations1);
        imp_r.render();
        imp_r.renderMap();
        imp_r.renderInformations(InformationType.TEMPERATURE);

        Button bt = new Button("Test Button");
        Pane pTest = new Pane();
        pTest.setStyle("-fx-background-color: white;");
        pTest.getChildren().add(bt);

        HBox h = new HBox();
        h.setStyle("-fx-background-color: transparent;");
        h.getChildren().addAll(imp_r, pTest);


        Scene s = new Scene(h, WIDTH, HEIGHT);
        s.setFill(Color.TRANSPARENT);
        s.addEventFilter(MouseEvent.MOUSE_PRESSED, 
            (MouseEvent mouseEvent) ->
            {
                System.out.println("> (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
            }
        );

        /**
         * TODO: For Test
         */
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(IS_RESIZABLE);
        primaryStage.centerOnScreen();
        primaryStage.setScene(s);
        primaryStage.show();
    }

    private ScrollPane createScrollPane(Node node) {
        ScrollPane scroll = new ScrollPane();

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        // scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scroll.setPannable(true);

        scroll.setMinSize(ScrollPane.USE_PREF_SIZE, ScrollPane.USE_PREF_SIZE);
        scroll.setPrefSize(WIDTH, HEIGHT);
        scroll.setMaxSize(ScrollPane.USE_PREF_SIZE, ScrollPane.USE_PREF_SIZE);

        scroll.setContent(node);

        return scroll;
    }

}
