package idv.mibudin.cwbApp.core.data;


public class Vector2D
{
    private double x;
    private double y;


    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2I vector2I)
    {
        this(vector2I.getX(), vector2I.getY());
    }

    public static Vector2D sum(Vector2D a, Vector2D b)
    {
        return new Vector2D(a.getX() + b.getX(), a.getY() + b.getY());
    }

    public static Vector2D scale(Vector2D a, Vector2D b)
    {
        return new Vector2D(a.getX() * b.getX(), a.getY() * b.getY());
    }

    public Vector2D add(Vector2D b)
    {
        return sum(this, b);
    }

    public Vector2D zoom(Vector2D b)
    {
        return scale(this, b);
    }

    @Override
    public String toString()
    {
        return String.format("[%d, %d]", x, y);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}
