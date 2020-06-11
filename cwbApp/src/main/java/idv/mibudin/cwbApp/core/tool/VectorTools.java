package idv.mibudin.cwbApp.core.tool;


import java.util.Vector;

import idv.mibudin.cwbApp.core.data.Vector2D;


public class VectorTools
{
    public static Vector<Double> flatenVector2Ds(final Vector<Vector2D> vector2Ds)
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

    public static <T> Vector<T> removeLastPoint(final Vector<T> vector2Ds)
    {
        Vector<T> changedVector2Ds = new Vector<T>(vector2Ds);
        changedVector2Ds.remove(changedVector2Ds.size() - 1);

        return changedVector2Ds;
    }

    public static <T> Vector<T> removeLastTwoPoint(final Vector<T> vector2Ds)
    {
        Vector<T> changedVector2Ds = new Vector<T>(vector2Ds);
        changedVector2Ds.remove(changedVector2Ds.size() - 1);
        changedVector2Ds.remove(changedVector2Ds.size() - 1);

        return changedVector2Ds;
    }
}
