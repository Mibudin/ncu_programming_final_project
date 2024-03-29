package idv.mibudin.cwbApp.core.gui.controller;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import idv.mibudin.cwbApp.core.gui.InformationMapPane_Rev;
import idv.mibudin.cwbApp.core.gui.TopoJsonRenderer;
import idv.mibudin.cwbApp.core.tool.ResourceLoader;
import idv.mibudin.cwbApp.core.tool.VectorTools.Vector2DTransformer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class ObserveDataController implements Initializable
{
    private static ObserveDataController instance;

    private InformationMapPane_Rev informationMapPane;
    private TopoJsonRenderer topoJsonRenderer;
    private JSONObject informationData1;
    private JSONObject informationData2;
    private JSONObject informationData3;
    private JSONArray locations;

    private InformationType currentType;

    private static final Vector2DTransformer MAP_TRANSFORMER = 
        (Vector2D vector2D) ->
        {
            return vector2D
                .zoom(1, -1)
                .add(-117.5, 27.0)
                .zoom(1, 110751.0 / 101751.0)
                .zoom(200, 200);
        }
    ;
    private double userScale = 1.0;
    private double lastUserScale = userScale;


    @FXML private Pane informationMapRoom;

    @FXML private VBox informationPanelVBox;

    @FXML private Label locationNameLabel;
    @FXML private Label regionNameLabel;

    @FXML private Label observeTimeLabel;

    @FXML private Label  elevLabel;
    @FXML private Label  wdirLabel;
    @FXML private Label  wdsdLabel;
    @FXML private Label  tempLabel;
    @FXML private Label  humdLabel;
    @FXML private Label  presLabel;
    @FXML private Label h_24rLabel;
    @FXML private Label  h_fxLabel;
    @FXML private Label  h_xdLabel;
    @FXML private Label h_fxtLabel;
    @FXML private Label  d_txLabel;
    @FXML private Label d_txtLabel;
    @FXML private Label  d_tnLabel;
    @FXML private Label d_tntLabel;

    @FXML private ChoiceBox<String> typeChoiceBox;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        instance = this;

        loadInformationMap();
        loadInformations();
        
        currentType = InformationType.TEMP;
        informationMapRoom.getChildren().setAll(renderInformationMapPane());

        informationPanelVBox.setVisible(false);

        typeChoiceBox.getItems().addAll(
            "海拔高度",
            "風向",
            "風速",
            "溫度",
            "相對溼度",
            "氣壓",
            "日累積雨量",
            "小時最大陣風風速",
            "小時最大陣風風向",
            "本日最高溫",
            "本日最低溫"
        );
        typeChoiceBox.setValue("溫度");

        ScrollPane mapScrollPane = (ScrollPane)informationMapPane.getChildren().get(0);
        mapScrollPane.setHvalue(0.45);
        mapScrollPane.setVvalue(0.07);
    }

    @FXML
    private void onMouseClickedTypeButton(MouseEvent mouseEvent)
    {

        String typeString = (String)typeChoiceBox.getValue();
        switch(typeString)
        {
            case        "海拔高度": currentType = InformationType.ELEV; break;
            case           "風向": currentType = InformationType.WDIR; break;
            case           "風速": currentType = InformationType.WDSD; break;
            case           "溫度": currentType = InformationType.TEMP; break;
            case        "相對溼度": currentType = InformationType.HUMD; break;
            case           "氣壓": currentType = InformationType.PRES; break;
            case      "日累積雨量": currentType = InformationType.H_24R; break;
            case "小時最大陣風風速": currentType = InformationType.H_FX; break;
            case "小時最大陣風風向": currentType = InformationType.H_XD; break;
            case      "本日最高溫": currentType = InformationType.D_TX; break;
            case      "本日最低溫": currentType = InformationType.D_TN; break;
            default:              currentType = InformationType.ELEV;
        }

        renderInformationMapPaneInformationOnly();
        // informationMapPane.setInformationElements(informationDataToInformations(locations, currentType));
        // informationMapPane.renderInformations(currentType, locations);
    }

    @FXML
    private void onMouseClickZoomInButton(MouseEvent mouseEvent)
    {
        if(userScale < 4.0)
        {
            lastUserScale = userScale;
            if(userScale < 1.0)
            {
                userScale += 0.1;
            }
            else if(userScale < 2.0)
            {
                userScale += 0.2;
            }
            else
            {
                userScale += 0.5;
            }
            informationMapPane.setOuterTransformer(getUserTransformer());

            informationMapPane.renderMap();
            informationMapPane.renderInformations(currentType, locations);

            scaleMapScrollPane();
        }
    }

    @FXML
    private void onMouseClickZoomOutButton(MouseEvent mouseEvent)
    {
        if(userScale >= 0.5)
        {
            lastUserScale = userScale;
            if(userScale < 1.0)
            {
                userScale -= 0.1;
            }
            else if(userScale < 2.0)
            {
                userScale -= 0.2;
            }
            else
            {
                userScale -= 0.5;
            }
            informationMapPane.setOuterTransformer(getUserTransformer());
            
            informationMapPane.renderMap();
            informationMapPane.renderInformations(currentType, locations);

            scaleMapScrollPane();
        }
    }

    private Vector2DTransformer getUserTransformer()
    {
        Vector2DTransformer userTransformer = 
            (Vector2D vector2D) ->
            {
                return MAP_TRANSFORMER.transform(vector2D).zoom(userScale, userScale);
            }
        ;

        return userTransformer;
    }

    private void scaleMapScrollPane()
    {
        ScrollPane mapScrollPane = (ScrollPane)informationMapPane.getChildren().get(0);
        double clipWidth = ((Rectangle)mapScrollPane.getClip()).getWidth();
        double clipHeight = ((Rectangle)mapScrollPane.getClip()).getHeight();
        double contentWidth = ((Pane)mapScrollPane.getContent()).getWidth();
        double contentHeight = ((Pane)mapScrollPane.getContent()).getHeight();
        mapScrollPane.setHvalue((mapScrollPane.getHvalue()) / lastUserScale * userScale + (clipWidth  / 2 * (userScale / lastUserScale - 1)) / contentWidth );
        mapScrollPane.setVvalue((mapScrollPane.getVvalue()) / lastUserScale * userScale + (clipHeight / 2 * (userScale / lastUserScale - 1)) / contentHeight);
    }

    private void loadInformationMap()
    {
        JSONObject mapData = new JSONObject(ResourceLoader.LoadStream("../res/informationMap/COUNTY_MOI_1081121 (2).json"));
        topoJsonRenderer = new TopoJsonRenderer(new TopoJson(mapData));
    }

    private void loadInformations()
    {
        CwbApi ca = new CwbApi(CwbApi.DEFAULT_AUTHORIZATION_KEY);
        ca.setShouldReturnJson(true);

        String data1 = ca.requestDatastore(
            // CwbApiDataID.AUTO_RAIN_STA__RAIN_OBS
            CwbApiDataID.AUTO_WX_STA__WX_OBS
        ).getResponseContent();

        informationData1 = new JSONObject(data1);

        String data2 = ca.requestDatastore(
            CwbApiDataID.BUR_WX_STA__WX_OBS,
            "elementName", "TIME,ELEV,WDIR,WDSD,TEMP,HUMD,PRES,24R,H_FX,H_XD,H_FXT,D_TX,D_TXT,D_TN,D_TNT"
        ).getResponseContent();

        informationData2 = new JSONObject(data2);

        String data3 = ca.requestDatastore(
            CwbApiDataID.AUTO_RAIN_STA__RAIN_OBS,
            "elementName", "ELEV,RAIN,MIN_10,HOUR_3,HOUR_6,HOUR_12,NOW"
        ).getResponseContent();

        informationData3 = new JSONObject(data3);
    }

    private InformationMapPane_Rev renderInformationMapPane()
    {
        JSONArray locations1 = informationData1.getJSONObject("records").getJSONArray("location");
        JSONArray locations2 = informationData2.getJSONObject("records").getJSONArray("location");
        List<Object> locationsList =  locations1.toList();
        locationsList.addAll(locations2.toList());
        locations = new JSONArray(locationsList);

        Vector<InformationElement> informations = informationDataToInformations(locations, currentType);

        informationMapPane = new InformationMapPane_Rev(520, 520, getUserTransformer(), topoJsonRenderer, informations);
        informationMapPane.setMinInnerWidth(520);
        informationMapPane.setMinInnerHeight(520);
        informationMapPane.render();
        informationMapPane.renderMap();
        informationMapPane.renderInformations(currentType, locations);

        return informationMapPane;
    }

    private void renderInformationMapPaneInformationOnly()
    {
        informationMapPane.setInformationElements(informationDataToInformations(locations, currentType));
        informationMapPane.renderInformations(currentType, locations);
    }

    public void showInformationPanel(JSONObject location)
    {
        locationNameLabel.setText(location.getString("locationName"));
        regionNameLabel.setText(
            location.getJSONArray("parameter").getJSONObject(0).getString("parameterValue")
            + "・" +
            location.getJSONArray("parameter").getJSONObject(2).getString("parameterValue")
        );

        setLabelData(observeTimeLabel, location.getJSONObject("time").getString("obsTime"));

        setLabelData( elevLabel, location.getJSONArray("weatherElement").getJSONObject( 0).getString("elementValue"));
        setLabelData( wdirLabel, location.getJSONArray("weatherElement").getJSONObject( 1).getString("elementValue"));
        setLabelData( wdsdLabel, location.getJSONArray("weatherElement").getJSONObject( 2).getString("elementValue"));
        setLabelData( tempLabel, location.getJSONArray("weatherElement").getJSONObject( 3).getString("elementValue"));
        setLabelData( humdLabel, location.getJSONArray("weatherElement").getJSONObject( 4).getString("elementValue"));
        setLabelData( presLabel, location.getJSONArray("weatherElement").getJSONObject( 5).getString("elementValue"));
        setLabelData(h_24rLabel, location.getJSONArray("weatherElement").getJSONObject( 6).getString("elementValue"));
        setLabelData( h_fxLabel, location.getJSONArray("weatherElement").getJSONObject( 7).getString("elementValue"));
        setLabelData( h_xdLabel, location.getJSONArray("weatherElement").getJSONObject( 8).getString("elementValue"));
        setLabelData(h_fxtLabel, location.getJSONArray("weatherElement").getJSONObject( 9).getString("elementValue"));
        setLabelData( d_txLabel, location.getJSONArray("weatherElement").getJSONObject(10).getString("elementValue"));
        setLabelData(d_txtLabel, location.getJSONArray("weatherElement").getJSONObject(11).getString("elementValue"));
        setLabelData( d_tnLabel, location.getJSONArray("weatherElement").getJSONObject(12).getString("elementValue"));
        setLabelData(d_tntLabel, location.getJSONArray("weatherElement").getJSONObject(13).getString("elementValue"));

        informationPanelVBox.setVisible(true);
    }

    private static Vector<InformationElement> informationDataToInformations(JSONArray locations, InformationType type)
    {
        Vector<InformationElement> informations = new Vector<InformationElement>();
        for(int i = 0; i < locations.length(); i++)
        {
            JSONObject location = locations.getJSONObject(i);

            String name = location.getString("locationName");
            Vector2D loc = new Vector2D(location.getDouble("lon"), location.getDouble("lat"));
            double value = location.getJSONArray("weatherElement").getJSONObject(Information.InformationType.getInformationIndex(type)).getDouble("elementValue");
    
            InformationGroup ig = new InformationGroup();
            ig.addInformation(new Information(type, value));

            informations.add(i, new InformationElement(name, loc, ig));
        }

        return informations;
    }

    private static void setLabelData(Label label, String value)
    {
        try
        {
            double doubleValue = Double.parseDouble(value);
            if(doubleValue != -99 && doubleValue != -999)
            {
                label.setText(value);
            }
            else
            {
                label.setText("×");
            }
        }
        catch(NumberFormatException e)
        {
            label.setText(value);
        }
    }

    public static ObserveDataController getInstance()
    {
        return instance;
    }
}
