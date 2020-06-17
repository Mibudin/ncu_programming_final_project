package idv.mibudin.cwbApp.core.gui;


import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import idv.mibudin.cwbApp.core.data.Information;
import idv.mibudin.cwbApp.core.data.InformationElement;
import idv.mibudin.cwbApp.core.data.TopoJson;
import idv.mibudin.cwbApp.core.data.Vector2D;
import idv.mibudin.cwbApp.core.data.Information.InformationType;
import idv.mibudin.cwbApp.core.gui.controller.ObserveDataController;
import idv.mibudin.cwbApp.core.tool.ValueTools;
import idv.mibudin.cwbApp.core.tool.VectorTools.Vector2DTransformer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;


public class InformationMapPane_Rev extends StackPane
{
    private static final String STYLE_CLASS_NAME = "information-map-pane-rev";
    private static final String MAP_PANE_STYLE_CLASS_NAME = "map-pane";

    private double width;
    private double height;
    private Vector2DTransformer outerTransformer;

    private double minInnerWidth;
    private double minInnerHeight;

    private TopoJsonRenderer mapTopoJsonRenderer;
    private Vector<InformationElement> informationElements;

    private Pane mapPane;
    private Pane informationsPane;


    public InformationMapPane_Rev(double width, double height, Vector2DTransformer outerTransformer, TopoJsonRenderer mapTopoJsonRenderer, Vector<InformationElement> informationElements)
    {
        super();

        this.width = width;
        this.height = height;
        this.outerTransformer = outerTransformer;

        this.minInnerWidth = this.width;
        this.minInnerHeight = this.height;

        this.mapTopoJsonRenderer = mapTopoJsonRenderer;
        this.informationElements = informationElements;

        this.getStyleClass().add(STYLE_CLASS_NAME);

        mapPane = new Pane();
        informationsPane = new Pane();

        mapPane.getStyleClass().add(MAP_PANE_STYLE_CLASS_NAME);
    }

    public void render()
    {
        // getChildren().setAll(mapTopoJsonRenderer.renderToShapes());

        StackPane p = new StackPane();
        p.getChildren().setAll(
            // renderMap(width, height, outerTransformer),
            // renderInformations(width, height, outerTransformer)
            mapPane,
            informationsPane
        );

        // double width = mapPane.getBoundsInLocal().getWidth();
        // double height = mapPane.getBoundsInLocal().getHeight();
        ScrollPane mapScrollPane = createScrollPane(p, width, height);
        // mapScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        mapScrollPane.setClip(new Rectangle(0, 0, width, height));


        // setStyle("-fx-background-color: rgba(32, 32, 32, 0.85);" +
        //         //    "-fx-effect: dropshadow(gaussian, white, 50, 0, 0, 0);" +
        //            "-fx-background-insets: 0;"
        // );
        getChildren().setAll(
            // renderMap(width, height),
            // renderInformations(width, height)
            mapScrollPane
        );
    }

    public void renderMap()
    {
        /**
         * TODO: For Test
         */
        mapPane.getChildren().clear();

        mapPane.setMinWidth(minInnerWidth);
        mapPane.setMinHeight(minInnerHeight);

        mapTopoJsonRenderer.getTopoJson().getTopologyObject().setVector2DTransformer(outerTransformer);
        mapTopoJsonRenderer.cacheArcs();

        // Pane mapPane = new Pane();
        // mapPane.setStyle("-fx-background-color: transparent;");
        mapPane.getChildren().addAll(mapTopoJsonRenderer.renderToShapes());

        // return mapPane;
    }

    public void renderInformations(InformationType informationType, JSONArray locations)
    {
        // Pane informationsPane = new Pane();
        // informationsPane.setMouseTransparent(true);

        informationsPane.getChildren().clear();

        informationsPane.setMinWidth(minInnerWidth);
        informationsPane.setMinHeight(minInnerHeight);

        for(InformationElement informationElement : informationElements)
        {
            JSONObject location = locations.getJSONObject(informationElements.indexOf(informationElement));

            Vector2D realCoodinate = outerTransformer.transform(informationElement.getLocation());
                // .zoom(new Vector2D(1, -1))
                // .add(new Vector2D(-117.5, 26.7))
                // .zoom(new Vector2D(180, 180));
    
            /**
             * TODO: For Test
             */
            Shape informationIcon;

            if(informationType == InformationType.WDIR || informationType == InformationType.WDSD
            || informationType == InformationType.H_FX || informationType == InformationType.H_XD)
            {
                double[] points = {
                     0.0,  5.0,
                    -4.0, -5.0,
                     0.0, -3.0,
                     4.0, -5.0
                };
                for(int i = 0; i < points.length / 2; i++)
                {
                    points[2 * i    ] += realCoodinate.getX();
                    points[2 * i + 1] += realCoodinate.getY(); 
                }
                informationIcon = new Polygon(points);

                double dir = 0;
                if(informationType == InformationType.WDIR || informationType == InformationType.WDSD)
                {
                    dir = location.getJSONArray("weatherElement").getJSONObject(InformationType.getInformationIndex(InformationType.WDIR)).getDouble("elementValue");
                    
                }
                else if(informationType == InformationType.H_FX || informationType == InformationType.H_XD)
                {
                    dir = location.getJSONArray("weatherElement").getJSONObject(InformationType.getInformationIndex(InformationType.H_XD)).getDouble("elementValue");
                }
                informationIcon.getTransforms().add(Transform.rotate(dir, realCoodinate.getX(), realCoodinate.getY()));
            }
            else
            {
                double rectangleSize = 6;
                informationIcon = new Rectangle(realCoodinate.getX() - rectangleSize / 2, realCoodinate.getY() - rectangleSize / 2, rectangleSize, rectangleSize);
            }


            informationIcon.setStroke(Color.TRANSPARENT);
            informationIcon.setStrokeWidth(2);

            /**
             * TODO: For Test
             */
            Information information = informationElement.getInformation(informationType);
            if(information.isValidDoubleValue())
            {
                informationIcon.setFill(information.getColorDoubleValue());
            }
            else
            {
                System.out.println("> Invalid value: " + informationElement.getName() + ", " + information.getDoubleValue());
                informationIcon.setFill(ValueTools.Cwb.getDefaultInvalidColor());
            }

            /**
             * TODO: For Test
             */
            informationIcon.addEventFilter(MouseEvent.MOUSE_CLICKED, 
                (MouseEvent mouseEvent) ->
                {
                    System.out.println("> " + informationElement.getName() + ", " + information.getDoubleValue());
                    ObserveDataController.getInstance().showInformationPanel(location);
                }
            );
            informationIcon.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET,
                (MouseEvent mouseEvent) ->
                {
                    informationIcon.setStroke(Color.WHITE);
                }
            );
            informationIcon.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET,
                (MouseEvent mouseEvent) ->
                {
                    informationIcon.setStroke(Color.TRANSPARENT);
                }
            );

            informationsPane.getChildren().add(informationIcon);
        }

        // return informationsPane;
    }

    public void setInformationElements(Vector<InformationElement> informationElements)
    {
        this.informationElements = informationElements;
    }

    public void setOuterTransformer(Vector2DTransformer outerTransformer)
    {
        this.outerTransformer = outerTransformer;
    }

    public void setMinInnerWidth(double minInnerWidth)
    {
        this.minInnerWidth = minInnerWidth;
    }

    public void setMinInnerHeight(double minInnerHeight)
    {
        this.minInnerHeight = minInnerHeight;
    }

    private static ScrollPane createScrollPane(Node node, double width, double height)
    {
        ScrollPane scroll = new ScrollPane();
        // scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setPannable(true);
        scroll.setMinSize(ScrollPane.USE_PREF_SIZE, ScrollPane.USE_PREF_SIZE);
        scroll.setPrefSize(width, height);
        scroll.setMaxSize(ScrollPane.USE_PREF_SIZE, ScrollPane.USE_PREF_SIZE);
        scroll.setContent(node);

        return scroll;
    }
}
