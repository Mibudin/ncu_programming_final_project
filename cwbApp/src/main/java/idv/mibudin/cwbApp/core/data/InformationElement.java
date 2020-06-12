package idv.mibudin.cwbApp.core.data;


import java.util.Vector;


public class InformationElement
{
    private String name;

    /**
     * TODO: Lon, lat.
     */
    private Vector2D location;

    private Vector<Double> values;

    
    public InformationElement(String name, Vector2D location, Vector<Double> values)
    {
        this.name = name;
        this.location = location;
        this.values = values;
    }

    public String getName()
    {
        return name;
    }

    public Vector2D getLocation()
    {
        return location;
    }

    public Vector<Double> getValues()
    {
        return values;
    }
}
