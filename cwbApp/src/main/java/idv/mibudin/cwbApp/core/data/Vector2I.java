package idv.mibudin.cwbApp.core.data;


public class Vector2I
{
    private int x;
    private int y;


    public Vector2I(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static Vector2I sum(Vector2I a, Vector2I b)
    {
        return new Vector2I(a.getX() + b.getX(), a.getY() + b.getY());
    }

    public static Vector2I scale(Vector2I a, Vector2I b)
    {
        return new Vector2I(a.getX() * b.getX(), a.getY() * b.getY());
    }

    public Vector2I add(Vector2I b)
    {
        return sum(this, b);
    }

    public Vector2I zoom(Vector2I b)
    {
        return scale(this, b);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }
}
