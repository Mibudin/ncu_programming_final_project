package idv.mibudin.cwbApp.core.gui;


import java.util.Vector;

import org.json.JSONObject;

import idv.mibudin.cwbApp.core.data.TopoJson;
import idv.mibudin.cwbApp.core.data.Vector2D;
import idv.mibudin.cwbApp.core.data.TopoJson.GeometryObject;
import idv.mibudin.cwbApp.core.data.TopoJson.MultiPolygonObject;
import idv.mibudin.cwbApp.core.data.TopoJson.GeometryCollectionObject;
import idv.mibudin.cwbApp.core.data.TopoJson.ObjectType;
import idv.mibudin.cwbApp.core.data.TopoJson.PolygonObject;
import idv.mibudin.cwbApp.core.data.TopoJson.TopologyObject;
import idv.mibudin.cwbApp.core.tool.VectorTools;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;


public class TopoJsonRenderer
{
    private TopoJson topoJson;

    private TopologyObjectRenderer topologyObjectRenderer;


    public TopoJsonRenderer(TopoJson topoJson)
    {
        this.topoJson = topoJson;
        topologyObjectRenderer = new TopologyObjectRenderer(topoJson.getTopologyObject());

        cacheDecodedArcs();
        cacheFlatenedDecodedArcs();
    }

    private void cacheDecodedArcs()
    {
        topologyObjectRenderer.cacheDecodedArcs();
    }

    private void cacheFlatenedDecodedArcs()
    {
        topologyObjectRenderer.cacheFlatenedDecodedArcs();
    }

    public Vector<Shape> renderToShapes()
    {
        return topologyObjectRenderer.renderToShapes();
    }


    private static class TopologyObjectRenderer
    {
        private TopologyObject topologyObject;

        private Vector<Vector<Vector2D>> decodedArcsCache;
        private Vector<Vector<Double>> flatenedDecodedArcsCache;

        private GeometryObjectRenderer geometryObjectRenderer;


        private TopologyObjectRenderer(TopologyObject topologyObject)
        {
            this.topologyObject = topologyObject;
            parseTopologyObjectRenderer();
        }

        private void parseTopologyObjectRenderer()
        {
            geometryObjectRenderer = new GeometryCollectionObjectRenderer(topologyObject.getObjects(), this);
        }

        private Vector<Shape> renderToShapes()
        {
            return geometryObjectRenderer.renderToShapes();
        }

        private void cacheDecodedArcs()
        {
            this.decodedArcsCache = topologyObject.getDecodedArcs();
        }

        public Vector<Vector2D> accessDecodedArc(int index)
        {
            if(index < 0)
            {
                return decodedArcsCache.get(decodedArcsCache.size() / 2 - index - 1);
            }
            else
            {
                return decodedArcsCache.get(index);
            }
        }

        private void cacheFlatenedDecodedArcs()
        {
            this.flatenedDecodedArcsCache = new Vector<Vector<Double>>(decodedArcsCache.size());
            for(int i = 0; i < decodedArcsCache.size(); i++)
            {
                flatenedDecodedArcsCache.add(i, VectorTools.flatenVector2Ds(decodedArcsCache.get(i)));
            }
        }

        public Vector<Double> accessFlatenedDecodedArc(int index)
        {
            if(index < 0)
            {
                return flatenedDecodedArcsCache.get(flatenedDecodedArcsCache.size() / 2 - index - 1);
            }
            else
            {
                return flatenedDecodedArcsCache.get(index);
            }
        }

        private TopologyObject getTopologyObject()
        {
            return topologyObject;
        }
    }


    private static abstract class GeometryObjectRenderer
    {
        private GeometryObject geometryObject;

        private TopologyObjectRenderer topologyObjectRenderer;


        private static GeometryObjectRenderer createGeometryObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            switch(geometryObject.getType())
            {
                case GEOMETRY_COLLECTION:
                    return new GeometryCollectionObjectRenderer(geometryObject, topologyObjectRenderer);

                case POINT:
                    return new PointObjectRenderer(geometryObject, topologyObjectRenderer);

                case MULTI_POINT:
                    return new MultiPointObjectRenderer(geometryObject, topologyObjectRenderer);

                case LINE_STRING:
                    return new LineStringObjectRenderer(geometryObject, topologyObjectRenderer);

                case MULTI_LINE_STRING:
                    return new MultiLineStringObjectRenderer(geometryObject, topologyObjectRenderer);

                case POLYGON:
                    return new PolygonObjectRenderer(geometryObject, topologyObjectRenderer);

                case MULTI_POLYGON:
                    return new MultipolygonObjectRenderer(geometryObject, topologyObjectRenderer);
                    
                case TOPOLOGY:
                default:
                    return null;
            }
        }

        private GeometryObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            this.geometryObject = geometryObject;
            this.topologyObjectRenderer = topologyObjectRenderer;
            parseGeometryObjectRenderer();
        }

        protected void parseGeometryObjectRenderer()
        {
            // TODO Auto-generated method stub
        }

        protected abstract Vector<Shape> renderToShapes();

        public ObjectType getType()
        {
            return geometryObject.getType();
        }

        protected GeometryObject getGeometryObject()
        {
            return geometryObject;
        }

        protected TopologyObjectRenderer getTopologyObjectRenderer()
        {
            return topologyObjectRenderer;
        }
    }


    private static class GeometryCollectionObjectRenderer extends GeometryObjectRenderer
    {
        private Vector<GeometryObjectRenderer> geometryObjectRenderers;


        private GeometryCollectionObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            super(geometryObject, topologyObjectRenderer);
        }

        @Override
        protected void parseGeometryObjectRenderer()
        {
            super.parseGeometryObjectRenderer();

            Vector<GeometryObject> geometries = ((GeometryCollectionObject)getGeometryObject()).getGeometries();
            geometryObjectRenderers = new Vector<GeometryObjectRenderer>(geometries.size());
            for(int i = 0; i < geometries.size(); i++)
            {
                geometryObjectRenderers.add(i, GeometryObjectRenderer.createGeometryObjectRenderer(geometries.get(i), getTopologyObjectRenderer()));
            }
        }

        @Override
        protected Vector<Shape> renderToShapes()
        {
            Vector<Shape> renderedShapes = new Vector<Shape>();
            for(GeometryObjectRenderer geometryObjectRenderer : geometryObjectRenderers)
            {
                renderedShapes.addAll(geometryObjectRenderer.renderToShapes());
            }

            return renderedShapes;
        }
    }


    private static class PointObjectRenderer extends GeometryObjectRenderer
    {
        private PointObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            super(geometryObject, topologyObjectRenderer);
        }

        @Override
        protected Vector<Shape> renderToShapes()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private static class MultiPointObjectRenderer extends GeometryObjectRenderer
    {
        private MultiPointObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            super(geometryObject, topologyObjectRenderer);
        }

        @Override
        protected Vector<Shape> renderToShapes()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private static class LineStringObjectRenderer extends GeometryObjectRenderer
    {
        private LineStringObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            super(geometryObject, topologyObjectRenderer);
        }

        @Override
        protected Vector<Shape> renderToShapes()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private static class MultiLineStringObjectRenderer extends GeometryObjectRenderer
    {
        private MultiLineStringObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            super(geometryObject, topologyObjectRenderer);
        }

        @Override
        protected Vector<Shape> renderToShapes()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private static class PolygonObjectRenderer extends GeometryObjectRenderer
    {
        private PolygonObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            super(geometryObject, topologyObjectRenderer);
        }

        @Override
        protected Vector<Shape> renderToShapes()
        {
            Shape shape = renderToAShape(((PolygonObject)getGeometryObject()).getArcs(), getTopologyObjectRenderer());
            /**
             * TODO: For Test
             */
            shape.setFill(Color.gray(0.8));
            shape.setStroke(Color.RED);
            shape.addEventFilter(MouseEvent.MOUSE_PRESSED, 
                (MouseEvent mouseEvent) ->
                {
                    JSONObject properties = getGeometryObject().getProperties();
                    System.out.println("> " + properties.getString("COUNTYNAME") + ", " + properties.getString("COUNTYENG"));
                }
            );

            Vector<Shape> shapes = new Vector<Shape>();
            shapes.add(shape);

            return shapes;
        }

        private static Shape renderToAShape(Vector<Vector<Integer>> arcs, TopologyObjectRenderer topologyObjectRenderer)
        {
            Vector<Polygon> polygons = new Vector<Polygon>(arcs.size());
            for(int i = 0; i < arcs.size(); i++)
            {
                Polygon polygon = new Polygon();
                Vector<Integer> arcIndices = arcs.get(i);
                for(int j = 0; j < arcIndices.size(); j++)
                {
                    polygon.getPoints().addAll(topologyObjectRenderer.accessFlatenedDecodedArc(arcIndices.get(j)));
                }
                polygons.add(i, polygon);
            }
            
            Shape shape = polygons.get(0);
            for(int i = 1; i < polygons.size(); i++)
            {
                shape = Shape.subtract(shape, polygons.get(i));
            }

            return shape;
        }
    }

    private static class MultipolygonObjectRenderer extends GeometryObjectRenderer
    {
        private MultipolygonObjectRenderer(GeometryObject geometryObject, TopologyObjectRenderer topologyObjectRenderer)
        {
            super(geometryObject, topologyObjectRenderer);
        }

        @Override
        protected Vector<Shape> renderToShapes()
        {
            MultiPolygonObject multiPolygonObject = (MultiPolygonObject)getGeometryObject();
            Vector<Shape> shapes = new Vector<Shape>();
            for(int i = 0; i < multiPolygonObject.getArcs().size(); i++)
            {
                shapes.add(i, PolygonObjectRenderer.renderToAShape(multiPolygonObject.getArcs().get(i), getTopologyObjectRenderer()));
            }

            Shape shape = shapes.get(0);
            for(int i = 1; i < shapes.size(); i++)
            {
                shape = Shape.union(shape, shapes.get(i));
            }

            /**
             * TODO: For Test
             */
            shape.setFill(Color.gray(0.8));
            shape.setStroke(Color.RED);
            shape.addEventFilter(MouseEvent.MOUSE_PRESSED, 
                (MouseEvent mouseEvent) ->
                {
                    JSONObject properties = getGeometryObject().getProperties();
                    System.out.println("> " + properties.getString("COUNTYNAME") + ", " + properties.getString("COUNTYENG"));
                }
            );

            Vector<Shape> returnShapes = new Vector<Shape>();
            returnShapes.add(shape);

            return returnShapes;
        }
    }
}
