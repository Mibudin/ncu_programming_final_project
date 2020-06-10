package idv.mibudin.cwbApp.core.data;


import idv.mibudin.cwbApp.core.tool.MathTools;


/**
 * @deprecated <p>
 * The 2-dimension vector with Number.
 * @param <T> A class extending Number.
 */
public class Vector2<T extends Number>
{
    private T x;
    private T y;


    public Vector2(T x, T y)
    {
        this.x = x;
        this.y = y;
    }

    public static <T extends Number> Vector2<T> sum(Vector2<T> vector2A, Vector2<T> vector2B)
    {
        return new Vector2<T>((T)MathTools.addNumbers(vector2A.getX(), vector2B.getX()), (T)MathTools.addNumbers(vector2A.getY(), vector2B.getY()));
    }

    public T getX()
    {
        return x;
    }

    public T getY()
    {
        return y;
    }

    public void setX(T x)
    {
        this.x = x;
    }

    public void setY(T y)
    {
        this.y = y;
    }
}
