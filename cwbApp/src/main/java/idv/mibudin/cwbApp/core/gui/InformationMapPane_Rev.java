package idv.mibudin.cwbApp.core.gui;


import java.util.Vector;

import idv.mibudin.cwbApp.core.data.Information;
import idv.mibudin.cwbApp.core.data.InformationElement;
import idv.mibudin.cwbApp.core.data.TopoJson;
import idv.mibudin.cwbApp.core.data.Vector2D;
import idv.mibudin.cwbApp.core.data.Information.InformationType;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;


public class InformationMapPane_Rev extends StackPane
{
    private double width;
    private double height;
    private Vector2DTransformer outerTransformer;

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

        this.mapTopoJsonRenderer = mapTopoJsonRenderer;
        this.informationElements = informationElements;

        mapPane = new Pane();
        informationsPane = new Pane();
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
        mapScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        mapScrollPane.setClip(new Rectangle(0, 0, width, height));


        setStyle("-fx-background-color: rgba(32, 32, 32, 0.85);" +
                //    "-fx-effect: dropshadow(gaussian, white, 50, 0, 0, 0);" +
                   "-fx-background-insets: 0;"
        );
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

        mapTopoJsonRenderer.getTopoJson().getTopologyObject().setVector2DTransformer(outerTransformer);
        mapTopoJsonRenderer.cacheArcs();

        // Pane mapPane = new Pane();
        mapPane.setStyle("-fx-background-color: transparent;");
        mapPane.getChildren().addAll(mapTopoJsonRenderer.renderToShapes());

        // return mapPane;
    }

    public void renderInformations(InformationType informationType)
    {
        // Pane informationsPane = new Pane();
        // informationsPane.setMouseTransparent(true);
        for(InformationElement informationElement : informationElements)
        {
            Vector2D realCoodinate = outerTransformer.transform(informationElement.getLocation());
                // .zoom(new Vector2D(1, -1))
                // .add(new Vector2D(-117.5, 26.7))
                // .zoom(new Vector2D(180, 180));
    
            /**
             * TODO: For Test
             */
            Rectangle informationIcon = new Rectangle(realCoodinate.getX() - 4, realCoodinate.getY() - 4, 8, 8);
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
                informationIcon.setFill(Color.RED);
            }

            /**
             * TODO: For Test
             */
            informationIcon.addEventFilter(MouseEvent.MOUSE_PRESSED, 
                (MouseEvent mouseEvent) ->
                {
                    System.out.println("> " + informationElement.getName() + ", " + information.getDoubleValue());
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

    private static ScrollPane createScrollPane(Node node, double width, double height)
    {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        // scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setPannable(true);
        scroll.setMinSize(ScrollPane.USE_PREF_SIZE, ScrollPane.USE_PREF_SIZE);
        scroll.setPrefSize(width, height);
        scroll.setMaxSize(ScrollPane.USE_PREF_SIZE, ScrollPane.USE_PREF_SIZE);
        scroll.setContent(node);

        return scroll;
    }
}
