package idv.mibudin.cwbApp.core.data;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;


public class TopoJson
{
    private static final String DEFAULT_ENCODING = "UTF-8";

    private TopologyObject topologyObject;


    public TopoJson(String filePath, String objectsKey) throws IOException
    {
        this(filePath, DEFAULT_ENCODING, objectsKey);
    }

    public TopoJson(String filePath, String encoding, String objectsKey) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        while((temp = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(temp);
        }
        bufferedReader.close();

        topologyObject = new TopologyObject(new JSONObject(stringBuilder.toString()), objectsKey);
    }

    public TopoJson(JSONObject topoJson, String objectsKey)
    {
        topologyObject = new TopologyObject(topoJson, objectsKey);
    }

    public TopologyObject getTopologyObject()
    {
        return topologyObject;
    }


    public static enum ObjectType
    {
        TOPOLOGY,
        POINT,
        MULTI_POINT,
        LINE_STRING,
        MULTI_LINE_STRING,
        POLYGON,
        MULTI_POLYGON,
        GEOMETRY_COLLECTION
    }


    public static class TopologyObject
    {
        private ObjectType type;

        private Vector2D transform_scale;
        private Vector2D transform_translate;

        private Vector<Vector<Vector2I>> arcs;

        /**
         * @deprecated Useless temporary. <p>
         * The boundary box of these geometry objects.
         */
        // private Vector<Double> bbox;

        private GeometryObject objects;


        public TopologyObject(JSONObject topoJson, String objectsKey)
        {
            parseTopologyObject(topoJson, objectsKey);
        }

        private void parseTopologyObject(JSONObject topoJson, String objectsKey)
        {
            type = ObjectType.TOPOLOGY;

            if(topoJson.has("transform"))
            {
                JSONArray scaleJsonArray = topoJson.getJSONObject("transform").getJSONArray("scale");
                transform_scale = new Vector2D(scaleJsonArray.getDouble(0), scaleJsonArray.getDouble(1));
                JSONArray translateJsonArray = topoJson.getJSONObject("transform").getJSONArray("translate");
                transform_translate = new Vector2D(translateJsonArray.getDouble(0), translateJsonArray.getDouble(1));
            }
            else
            {
                transform_scale = new Vector2D(1.0, 1.0);
                transform_translate = new Vector2D(0.0, 0.0);
            }

            /**
             * Processes for `bbox`.
             */

            JSONArray arcsJsonArray = topoJson.getJSONArray("arcs");
            arcs = new Vector<Vector<Vector2I>>(arcsJsonArray.length());
            for(int i = 0; i < arcsJsonArray.length(); i++)
            {
                JSONArray arcJsonArray = arcsJsonArray.getJSONArray(i);
                Vector<Vector2I> arc = new Vector<Vector2I>(arcJsonArray.length());
                for(int j = 0; j < arcJsonArray.length(); j++)
                {
                    JSONArray pointJsonArray = arcJsonArray.getJSONArray(j);
                    arc.add(j, new Vector2I(pointJsonArray.getInt(0), pointJsonArray.getInt(1)));
                }
                arcs.add(i, arc);
            }

            objects = GeometryObject.createGeometryObject(topoJson.getJSONObject("objects").getJSONObject(objectsKey));
        }

        public Vector<Vector<Vector2D>> getDecodedArcs()
        {
            Vector<Vector<Vector2D>> decodedArcsCache = new Vector<Vector<Vector2D>>(arcs.size() * 2);
            for(int i = 0; i < arcs.size(); i++)
            {
                decodedArcsCache.add(i, decodeArc(i));
            }
            for(int i = arcs.size(); i < arcs.size() * 2; i++)
            {
                decodedArcsCache.add(i, decodeArc(-(i - arcs.size()) - 1));
            }

            return decodedArcsCache;
        }

        private Vector2D transformPoint(Vector2I point)
        {
            return new Vector2D(point).zoom(transform_scale).add(transform_translate)
                /**
                 * TODO: For Test
                 */
                // ;
                .add(new Vector2D(-118, -21.5))
                // ;
                .zoom(new Vector2D(150, 150));
        }

        private Vector<Vector2D> decodeArc(int arcIndex)
        {
            boolean isReversed = arcIndex < 0;
            int realArcIndex = isReversed ? -arcIndex - 1 : arcIndex;

            Vector<Vector2I> targetArc = arcs.get(realArcIndex);
            Vector<Vector2D> decodedArc = new Vector<Vector2D>(targetArc.size());

            Vector2I cumulatePoint = new Vector2I(0, 0);
            for(int i = 0; i < targetArc.size(); i++)
            {
                cumulatePoint = cumulatePoint.add(targetArc.get(i));
                decodedArc.add(i, transformPoint(cumulatePoint));
            }

            if(isReversed)
            {
                Collections.reverse(decodedArc);
            }

            return decodedArc;
        }

        public GeometryObject getObjects()
        {
            return objects;
        }
    }


    public static abstract class GeometryObject
    {
        private ObjectType type;

        private JSONObject properties;


        private static GeometryObject createGeometryObject(JSONObject geometryObjectJson)
        {
            switch(getTypeByClassName(geometryObjectJson.getString("type")))
            {
                case GEOMETRY_COLLECTION:
                    return new GeometryCollectionObject(geometryObjectJson);

                case POINT:
                    return new PointObject(geometryObjectJson);

                case MULTI_POINT:
                    return new MultiPointObject(geometryObjectJson);

                case LINE_STRING:
                    return new LineStringObject(geometryObjectJson);

                case MULTI_LINE_STRING:
                    return new MultiLineStringObject(geometryObjectJson);

                case POLYGON:
                    return new PolygonObject(geometryObjectJson);

                case MULTI_POLYGON:
                    return new MultiPolygonObject(geometryObjectJson);

                case TOPOLOGY:
                default:
                    return null;
            }
        }

        private GeometryObject(JSONObject geometryObjectJson)
        {
            parseObjectJson(geometryObjectJson);
        }

        protected void parseObjectJson(JSONObject geometryObjectJson)
        {
            type = getTypeByClassName(this.getClass().getSimpleName());

            if(geometryObjectJson.has("properties"))
            {
                properties = geometryObjectJson.getJSONObject("properties");
            }
        }

        private static ObjectType getTypeByClassName(String className)
        {
            return ObjectType.valueOf(Pattern.compile("(?<=.)(?=[A-Z][a-z])").matcher(className.replaceAll("Object", "")).replaceAll("_").toUpperCase());
        }

        public ObjectType getType()
        {
            return type;
        }

        public JSONObject getProperties()
        {
            return properties;
        }
    }


    public static class GeometryCollectionObject extends GeometryObject
    {
        private Vector<GeometryObject> geometries;


        private GeometryCollectionObject(JSONObject geometryObjectJson)
        {
            super(geometryObjectJson);
        }

        @Override
        protected void parseObjectJson(JSONObject geometryObjectJson)
        {
            super.parseObjectJson(geometryObjectJson);
            
            JSONArray geometriesArray = geometryObjectJson.getJSONArray("geometries");
            geometries = new Vector<GeometryObject>(geometriesArray.length());
            for(int i = 0; i < geometriesArray.length(); i++)
            {
                geometries.add(i, GeometryObject.createGeometryObject(geometriesArray.getJSONObject(i)));
            }
        }

        public Vector<GeometryObject> getGeometries()
        {
            return geometries;
        }
    }


    public static class PointObject extends GeometryObject
    {
        private PointObject(JSONObject geometryObjectJson)
        {
            super(geometryObjectJson);
        }

        @Override
        protected void parseObjectJson(JSONObject geometryObjectJson)
        {
            super.parseObjectJson(geometryObjectJson);

            // TODO Auto-generated method stub
        }
    }


    public static class MultiPointObject extends GeometryObject
    {
        private MultiPointObject(JSONObject geometryObjectJson)
        {
            super(geometryObjectJson);
        }

        @Override
        protected void parseObjectJson(JSONObject geometryObjectJson)
        {
            super.parseObjectJson(geometryObjectJson);

            // TODO Auto-generated method stub
        }
    }


    public static class LineStringObject extends GeometryObject
    {
        private LineStringObject(JSONObject geometryObjectJson)
        {
            super(geometryObjectJson);
        }

        @Override
        protected void parseObjectJson(JSONObject geometryObjectJson)
        {
            super.parseObjectJson(geometryObjectJson);

            // TODO Auto-generated method stub
        }
    }


    public static class MultiLineStringObject extends GeometryObject
    {
        private MultiLineStringObject(JSONObject geometryObjectJson)
        {
            super(geometryObjectJson);
        }

        @Override
        protected void parseObjectJson(JSONObject geometryObjectJson)
        {
            super.parseObjectJson(geometryObjectJson);

            // TODO Auto-generated method stub
        }
    }


    public static class PolygonObject extends GeometryObject
    {
        private Vector<Vector<Integer>> arcs;


        private PolygonObject(JSONObject geometryObjectJson)
        {
            super(geometryObjectJson);
        }

        @Override
        protected void parseObjectJson(JSONObject geometryObjectJson)
        {
            super.parseObjectJson(geometryObjectJson);

            JSONArray arcRingsJsonArray = geometryObjectJson.getJSONArray("arcs");
            arcs = new Vector<Vector<Integer>>(arcRingsJsonArray.length());
            for(int i = 0; i < arcRingsJsonArray.length(); i++)
            {
                JSONArray arcIndicesJsonArray = arcRingsJsonArray.getJSONArray(i);
                arcs.add(i, new Vector<Integer>(arcIndicesJsonArray.length()));
                for(int j = 0; j < arcIndicesJsonArray.length(); j++)
                {
                    arcs.get(i).add(j, arcIndicesJsonArray.getInt(j));
                }
            }
        }

        public Vector<Vector<Integer>> getArcs()
        {
            return arcs;
        }
    }


    public static class MultiPolygonObject extends GeometryObject
    {
        private Vector<Vector<Vector<Integer>>> arcs;

        private MultiPolygonObject(JSONObject geometryObjectJson)
        {
            super(geometryObjectJson);
        }

        @Override
        protected void parseObjectJson(JSONObject geometryObjectJson)
        {
            super.parseObjectJson(geometryObjectJson);

            JSONArray polygonArcsJsonArray = geometryObjectJson.getJSONArray("arcs");
            arcs = new Vector<Vector<Vector<Integer>>>(polygonArcsJsonArray.length());
            for(int i = 0; i < polygonArcsJsonArray.length(); i++)
            {
                JSONArray arcRingsJsonArray = polygonArcsJsonArray.getJSONArray(i);
                arcs.add(i, new Vector<Vector<Integer>>(arcRingsJsonArray.length()));
                for(int j = 0; j < arcRingsJsonArray.length(); j++)
                {
                    JSONArray arcIndicesJsonArray = arcRingsJsonArray.getJSONArray(j);
                    arcs.get(i).add(j, new Vector<Integer>(arcIndicesJsonArray.length()));
                    for(int k = 0; k < arcIndicesJsonArray.length(); k++)
                    {
                        arcs.get(i).get(j).add(k, arcIndicesJsonArray.getInt(k));
                    }
                }
            }
        }

        public Vector<Vector<Vector<Integer>>> getArcs()
        {
            return arcs;
        }
    }
}
