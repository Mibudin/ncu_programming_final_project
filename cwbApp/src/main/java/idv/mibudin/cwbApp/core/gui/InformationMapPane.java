package idv.mibudin.cwbApp.core.gui;


import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;


public class InformationMapPane extends Pane
{
    private Vector<Vector<Shape>> multiShapes;


    public InformationMapPane(JSONObject topojson)
    {
        super();

        // this.addEventFilter(KeyEvent.KEY_PRESSED, 
        //     (KeyEvent keyEvent) ->
        //     {
        //         if(keyEvent.getCode() == KeyCode.UP)
        //         {
        //             setTranslateY(getTranslateY() + 10);
        //         }
        //         if(keyEvent.getCode() == KeyCode.DOWN)
        //         {
        //             setTranslateY(getTranslateY() - 10);
        //         }
        //         if(keyEvent.getCode() == KeyCode.LEFT)
        //         {
        //             setTranslateX(getTranslateX() + 10);
        //         }
        //         if(keyEvent.getCode() == KeyCode.RIGHT)
        //         {
        //             setTranslateX(getTranslateX() - 10);
        //         }
        //     }
        // );

        constructByTopojson(topojson);
    }

    // TODO: Rewrite this.
    public void constructByTopojson(JSONObject topojson)
    {
        Vector<Vector<Vector<Double>>> arcs = new Vector<Vector<Vector<Double>>>();
        // Vector<Vector<Double>> arcs = new Vector<Vector<Double>>();
        for(int i = 0; i < topojson.getJSONArray("arcs").length(); i++)
        {
            JSONArray points = topojson.getJSONArray("arcs").getJSONArray(i);
            JSONArray scale = topojson.getJSONObject("transform").getJSONArray("scale");
            JSONArray translate = topojson.getJSONObject("transform").getJSONArray("translate");
            arcs.add(i, arcPointsToCoordinates(points, scale, translate));
            // arcs.add(i, arcPointsToCoordinatesFlat(points, scale, translate));
        }
        
        multiShapes = new Vector<Vector<Shape>>();
        JSONArray polygonDatas = topojson.getJSONObject("objects").getJSONObject("COUNTY_MOI_1081121").getJSONArray("geometries");
        for(int i = 0; i < polygonDatas.length(); i++)
        {
            JSONObject polygonData = polygonDatas.getJSONObject(i);
            Vector<Vector<Vector<Integer>>> arcIndices = new Vector<Vector<Vector<Integer>>>();
            if(polygonData.getString("type").equals("Polygon"))
            {
                JSONArray polyArcs = polygonData.getJSONArray("arcs");
                arcIndices.add(0, new Vector<Vector<Integer>>());
                for(int j = 0; j < polyArcs.length(); j++)
                {
                    JSONArray polyLine = polyArcs.getJSONArray(j);
                    Vector<Integer> polyArcIndices = new Vector<Integer>();
                    for(int k = 0; k < polyLine.length(); k++)
                    {
                        polyArcIndices.add(k, polyLine.getInt(k));
                    }
                    arcIndices.get(0).add(j, polyArcIndices);
                }
            }
            else if(polygonData.getString("type").equals("MultiPolygon"))
            {
                JSONArray polyArcs = polygonData.getJSONArray("arcs");
                for(int j = 0; j < polyArcs.length(); j++)
                {
                    JSONArray polys = polyArcs.getJSONArray(j);
                    Vector<Vector<Integer>> polysIndices = new Vector<Vector<Integer>>();
                    for(int k = 0; k < polys.length(); k++)
                    {
                        JSONArray polyLine = polys.getJSONArray(k);
                        Vector<Integer> polyArcIndices = new Vector<Integer>();
                        for(int l = 0; l < polyLine.length(); l++)
                        {
                            polyArcIndices.add(l, polyLine.getInt(l));
                        }
                        polysIndices.add(k, polyArcIndices);
                    }
                    arcIndices.add(j, polysIndices);
                }
            }

            multiShapes.add(i, new Vector<Shape>());
            for(int j = 0; j < arcIndices.size(); j++)
            {
                Vector<Vector<Integer>> polygon = arcIndices.get(j);
                // Each polygon
                Vector<Polygon> proPolys = new Vector<Polygon>();
                for(int k = 0; k < polygon.size(); k++)
                {
                    Vector<Integer> arcGroup = polygon.get(k);
                    // Each arc group
                    proPolys.add(k, new Polygon());
                    for(int l = 0; l < arcGroup.size(); l++) // 2
                    {
                        // System.out.println("> " + i + ", " + j + ", " + k + ", " + l);
                        // each arc
                        int arcIndex = arcGroup.get(l);
                        // System.out.println("> " + arcIndex);
                        
                        if(arcIndex >= 0)
                        {
                            proPolys.get(k).getPoints().addAll(flaten2DVector(arcs.get(arcIndex)));
                            // System.out.println("> " + i + ", " + j + ", " + k + ", " + l + " - " + arcs.get(arcIndex).size());
                        }
                        else
                        {
                            Vector<Vector<Double>> reversedArc = (Vector<Vector<Double>>)(arcs.get(-arcIndex - 1).clone());
                            Collections.reverse(reversedArc);
                            proPolys.get(k).getPoints().addAll(flaten2DVector(reversedArc));
                            // System.out.println("> " + i + ", " + j + ", " + k + ", " + l + " - " + arcs.get(-arcIndex - 1).size());
                        }
                        
                    }
                }
                Shape shape;
                if(proPolys.size() == 2)
                {
                    shape = Shape.subtract(proPolys.get(0), proPolys.get(1));
                }
                else
                {
                    shape = proPolys.get(0);
                }
                shape.setFill(Color.gray(i / 21.0));
                shape.setStroke(Color.RED);
                shape.addEventFilter(MouseEvent.MOUSE_PRESSED, 
                    (MouseEvent mouseEvent) ->
                    {
                        JSONObject properties = polygonData.getJSONObject("properties");
                        System.out.println("> " + properties.getString("COUNTYNAME") + ", " + properties.getString("COUNTYENG"));
                    }
                );

                multiShapes.get(i).add(j, shape);
                // System.out.println("> " + i + ", " + j);
            }
            getChildren().addAll(multiShapes.get(i));
        }
    }

    private Vector<Vector<Double>> arcPointsToCoordinates(JSONArray points, JSONArray scale, JSONArray translate)
    {
        // Vector<Integer> origin = new Vector<Integer>(2);
        // origin.add(0, (points.getJSONArray(0).getInt(0)));
        // origin.add(1, (points.getJSONArray(0).getInt(1)));
        int originX = 0;
        int originY = 0;
        Vector<Vector<Double>> coordinates = new Vector<Vector<Double>>(points.length() - 1);
        for(int i = 0; i < points.length(); i++)
        {
            JSONArray point = points.getJSONArray(i);
            Vector<Double> coordinate = new Vector<Double>(2);
            coordinate.add(0, ((double)(originX += point.getInt(0)) * scale.getDouble(0) + translate.getDouble(0) - 118) * 150);
            coordinate.add(1, ((double)(originY += point.getInt(1)) * scale.getDouble(1) + translate.getDouble(1) - 21) * 150);
            coordinates.add(i, coordinate);
        }

        return coordinates;
    }

    private Vector<Double> flaten2DVector(Vector<Vector<Double>> vector)
    {
        Vector<Double> result = new Vector<Double>();
        for(int i = 0; i < vector.size(); i++)
        {
            result.addAll(vector.get(i));
        }

        return result;
    }

    private Vector<Double> arcPointsToCoordinatesFlat(JSONArray points, JSONArray scale, JSONArray translate)
    {
        // Vector<Integer> origin = new Vector<Integer>(2);
        // origin.add(0, (points.getJSONArray(0).getInt(0)));
        // origin.add(1, (points.getJSONArray(0).getInt(1)));
        int originX = 0;
        int originY = 0;
        Vector<Double> coordinates = new Vector<Double>((points.length() - 1) * 2);
        for(int i = 0; i < points.length(); i++)
        {
            JSONArray point = points.getJSONArray(i);
            coordinates.add(i * 2 - 2, (double)(originX += point.getInt(0)) * scale.getDouble(0) + translate.getDouble(0));
            coordinates.add(i * 2 - 1, (double)(originY += point.getInt(1)) * scale.getDouble(1) + translate.getDouble(1));
            // System.out.println("> " + originX + ", " + originY);
            // System.out.println("> " + coordinates.get(i * 2 - 2) + ", " + coordinates.get(i * 2 - 1));
            // coordinates.add(i * 2 - 2, (double)(point.getInt(0) + 500));
            // coordinates.add(i * 2 - 1, (double)(point.getInt(1) + 300));
        }

        return coordinates;
    }

    public void constructByGeojson(JSONObject geojson)
    {

    }
}
