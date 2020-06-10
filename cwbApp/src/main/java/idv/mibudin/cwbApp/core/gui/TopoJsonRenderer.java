package idv.mibudin.cwbApp.core.gui;


import java.util.Vector;

import idv.mibudin.cwbApp.core.data.TopoJson;
import javafx.scene.shape.Shape;


public class TopoJsonRenderer
{
    private TopoJson topoJson;


    public TopoJsonRenderer(TopoJson topoJson)
    {
        this.topoJson = topoJson;
    }

    public Vector<Shape> renderToShapes()
    {
        
    }


    private static class TopologyObjectRenderer
    {

    }


    private static abstract class GeometryObjectRenderer
    {

    }


    private static class GeometryCollectionObjectRenderer extends GeometryObjectRenderer
    {

    }


    private static class PointObjectRenderer extends GeometryObjectRenderer
    {

    }
}
