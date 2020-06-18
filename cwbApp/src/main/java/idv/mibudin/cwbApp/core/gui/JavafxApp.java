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

import idv.mibudin.cwbApp.App;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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


public class JavafxApp extends Application
{
    public static final double WINDOW_WIDTH  = 1020;  // 960, Shadow: + 30 * 2
    public static final double WINDOW_HEIGHT =  690;  // 600, Shadow: + 30 * 2, Title Bar: + 30

    public static final double SCREEN_WIDTH  = 960;
    public static final double SCREEN_HEIGHT = 600;

    public static final double TAB_WIDTH  = 960;
    public static final double TAB_HEIGHT = 560;

    public static final String TITLE = "CWB API DEMO";

    public static final boolean IS_HI_DPI = true;
    public static final boolean IS_RESIZABLE = false;


    private Stage mainStage;


    public static void launchJavafxApplication(String[] args)
    {
        System.setProperty("prism.allowhidpi", IS_HI_DPI ? "true" : "false");

        Application.launch(JavafxApp.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        App.setJavafxApp(this);

        mainStage = primaryStage;

        // test();
        test2();
    }

    public Pane test() throws Exception
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

        // lon / lat ~ 101751 / 110751 (in Nantou)
        Vector2DTransformer demoMapTransformer = 
            (Vector2D vector2D) ->
            {
                return vector2D
                    .zoom(1, -1)
                    .add(-117.5, 26.7)
                    .zoom(1, 110751.0 / 101751.0)
                    .zoom(200, 200);
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
            // CwbApiDataID.AUTO_RAIN_STA__RAIN_OBS
            CwbApiDataID.AUTO_WX_STA__WX_OBS
            // "elementName", "HOUR_24"
            // "elementName", "TEMP"
            // "parameterName", "CITY"
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
            ig.addInformation(new Information(InformationType.H_24R, value));

            informations1.add(i, new InformationElement(name, loc, ig));
        }

        InformationMapPane_Rev imp_r = new InformationMapPane_Rev(520, 520, demoMapTransformer, tjr, informations1);
        imp_r.setMinInnerWidth(1000);
        imp_r.setMinInnerHeight(1500);
        imp_r.render();
        imp_r.renderMap();
        imp_r.renderInformations(InformationType.H_24R, locations);

        Button bt = new Button("Test Button");
        Pane pTest = new Pane();
        pTest.setStyle("-fx-background-color: white;");
        pTest.getChildren().add(bt);

        HBox h = new HBox();
        h.setStyle("-fx-background-color: transparent;");
        h.getChildren().addAll(imp_r, pTest);

        // return h;
        return imp_r;


        // Scene s = new Scene(h, WINDOW_WIDTH, WINDOW_HEIGHT);
        // s.setFill(Color.TRANSPARENT);
        // s.addEventFilter(MouseEvent.MOUSE_PRESSED, 
        //     (MouseEvent mouseEvent) ->
        //     {
        //         System.out.println("> (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
        //     }
        // );

        // /**
        //  * TODO: For Test
        //  */
        // mainStage.initStyle(StageStyle.TRANSPARENT);

        // mainStage.setTitle(TITLE);
        // mainStage.setResizable(IS_RESIZABLE);
        // mainStage.centerOnScreen();
        // mainStage.setScene(s);
        // mainStage.show();
    }

    private void test2() throws Exception
    {
        Scene s = new Scene(new FXMLLoader(App.class.getResource("fxml/MainScene.fxml")).load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        s.setFill(Color.TRANSPARENT);
        s.getStylesheets().add(App.class.getResource("css/defaultStyle.css").toExternalForm());
        // s.addEventFilter(MouseEvent.MOUSE_PRESSED, 
        //     (MouseEvent mouseEvent) ->
        //     {
        //         System.out.println("> (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
        //     }
        // );

        /**
         * TODO: For Test
         */
        mainStage.initStyle(StageStyle.TRANSPARENT);

        mainStage.setTitle(TITLE);
        mainStage.setResizable(IS_RESIZABLE);
        mainStage.centerOnScreen();
        mainStage.setScene(s);
        mainStage.show();
    }

    public Stage getMainStage()
    {
        return mainStage;
    }
}
