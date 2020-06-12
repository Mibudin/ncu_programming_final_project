package idv.mibudin.cwbApp.core.gui;


import java.util.Vector;

import idv.mibudin.cwbApp.core.data.InformationElement;
import idv.mibudin.cwbApp.core.data.TopoJson;
import idv.mibudin.cwbApp.core.data.Vector2D;
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
    private TopoJsonRenderer mapTopoJsonRenderer;
    private Vector<InformationElement> informationElements;


    public InformationMapPane_Rev(TopoJsonRenderer mapTopoJsonRenderer, Vector<InformationElement> informationElements)
    {
        super();

        this.mapTopoJsonRenderer = mapTopoJsonRenderer;
        this.informationElements = informationElements;
    }

    public void render(double width, double height, Vector2DTransformer outerTransformer)
    {
        // getChildren().setAll(mapTopoJsonRenderer.renderToShapes());

        StackPane p = new StackPane();
        p.getChildren().setAll(
            renderMap(width, height, outerTransformer),
            renderInformations(width, height, outerTransformer)
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

    private Pane renderMap(double width, double height, Vector2DTransformer outerTransformer)
    {
        /**
         * TODO: For Test
         */

        Pane mapPane = new Pane();
        mapPane.setStyle("-fx-background-color: transparent;");
        mapPane.getChildren().addAll(mapTopoJsonRenderer.renderToShapes());

        return mapPane;
    }

    private Pane renderInformations(double width, double height, Vector2DTransformer outerTransformer)
    {
        Pane informationsPane = new Pane();
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
            Rectangle information = new Rectangle(realCoodinate.getX() - 4, realCoodinate.getY() - 4, 8, 8);
            information.setStroke(Color.TRANSPARENT);
            information.setStrokeWidth(2);

            double value = informationElement.getValues().get(0);
            if(value != -99.0)
            {
                information.setFill(getTempColor(informationElement.getValues().get(0)));
            }
            else
            {
                information.setFill(Color.RED);
            }

            information.addEventFilter(MouseEvent.MOUSE_PRESSED, 
                (MouseEvent mouseEvent) ->
                {
                    System.out.println("> " + informationElement.getName() + ", " + value);
                }
            );
            information.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET,
                (MouseEvent mouseEvent) ->
                {
                    information.setStroke(Color.WHITE);
                }
            );
            information.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET,
                (MouseEvent mouseEvent) ->
                {
                    information.setStroke(Color.TRANSPARENT);
                }
            );

            informationsPane.getChildren().add(information);
        }

        return informationsPane;
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

    /**
     * TODO: For Test
     */
    public static Color getTempColor(double temp)
    {
        if(temp >= 38)
        {
            return Color.rgb(127, 38, 158);
        }
        else if(temp >= 37)
        {
            return Color.rgb(133, 80, 152);
        }
        else if(temp >= 36)
        {
            return Color.rgb(156, 104, 70);
        }
        else if(temp >= 35)
        {
            return Color.rgb(118, 3, 6);
        }
        else if(temp >= 34)
        {
            return Color.rgb(172, 5, 57);
        }
        else if(temp >= 33)
        {
            return Color.rgb(239, 22, 91);
        }
        else if(temp >= 32)
        {
            return Color.rgb(240, 83, 52);
        }
        else if(temp >= 31)
        {
            return Color.rgb(221, 124, 7);
        }
        else if(temp >= 30)
        {
            return Color.rgb(229, 141, 41);
        }
        else if(temp >= 29)
        {
            return Color.rgb(233, 158, 57);
        }
        else if(temp >= 28)
        {
            return Color.rgb(237, 178, 76);
        }
        else if(temp >= 27)
        {
            return Color.rgb(241, 195, 99);
        }
        else if(temp >= 26)
        {
            return Color.rgb(233, 158, 57);
        }
        else if(temp >= 25)
        {
            return Color.rgb(246, 231, 138);
        }
        else if(temp >= 24)
        {
            return Color.rgb(244, 243, 195);
        }
        else if(temp >= 23)
        {
            return Color.rgb(217, 241, 145);
        }
        else if(temp >= 22)
        {
            return Color.rgb(202, 230, 143);
        }
        else if(temp >= 21)
        {
            return Color.rgb(187, 223, 136);
        }
        else if(temp >= 20)
        {
            return Color.rgb(167, 217, 132);
        }
        else if(temp >= 19)
        {
            return Color.rgb(150, 208, 124);
        }
        else if(temp >= 18)
        {
            return Color.rgb(133, 201, 118);
        }
        else if(temp >= 17)
        {
            return Color.rgb(118, 193, 111);
        }
        else if(temp >= 16)
        {
            return Color.rgb(98, 187, 107);
        }
        else if(temp >= 15)
        {
            return Color.rgb(81, 178, 99);
        }
        else if(temp >= 14)
        {
            return Color.rgb(63, 169, 94);
        }
        else if(temp >= 13)
        {
            return Color.rgb(47, 162, 87);
        }
        else if(temp >= 12)
        {
            return Color.rgb(28, 154, 83);
        }
        else if(temp >= 11)
        {
            return Color.rgb(12, 146, 77);
        }
        else if(temp >= 10)
        {
            return Color.rgb(180, 233, 247);
        }
        else if(temp >= 9)
        {
            return Color.rgb(166, 224, 236);
        }
        else if(temp >= 8)
        {
            return Color.rgb(149, 212, 227);
        }
        else if(temp >= 7)
        {
            return Color.rgb(135, 203, 216);
        }
        else if(temp >= 6)
        {
            return Color.rgb(119, 191, 205);
        }
        else if(temp >= 5)
        {
            return Color.rgb(103, 180, 198);
        }
        else if(temp >= 4)
        {
            return Color.rgb(93, 170, 190);
        }
        else if(temp >= 3)
        {
            return Color.rgb(77, 158, 177);
        }
        else if(temp >= 2)
        {
            return Color.rgb(59, 147, 167);
        }
        else if(temp >= 1)
        {
            return Color.rgb(46, 137, 156);
        }
        else if(temp >= 0)
        {
            return Color.rgb(31, 126, 148);
        }
        else
        {
            return Color.rgb(16, 115, 136);
        }
    }
}
