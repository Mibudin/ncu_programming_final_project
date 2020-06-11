package idv.mibudin.cwbApp.core.tool;


import java.util.Vector;

import idv.mibudin.cwbApp.core.data.Vector2D;


public class VectorTools
{
    public static Vector<Double> flatenVector2Ds(Vector<Vector2D> vector2Ds)
    {
        Vector<Double> flatenedVector2Ds = new Vector<Double>(vector2Ds.size() * 2);
        for(int i = 0; i < vector2Ds.size(); i++)
        {
            Vector2D vector2D = vector2Ds.get(i);
            flatenedVector2Ds.add(i * 2, vector2D.getX());
            flatenedVector2Ds.add(i * 2 + 1, vector2D.getY());
        }

        return flatenedVector2Ds;
    }
}
