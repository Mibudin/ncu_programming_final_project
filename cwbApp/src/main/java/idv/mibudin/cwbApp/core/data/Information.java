package idv.mibudin.cwbApp.core.data;


import idv.mibudin.cwbApp.core.tool.ValueTools;
import javafx.scene.paint.Color;


public class Information
{
    private InformationType type;

    private double doubleValue;
    private String stringValue;


    public Information(InformationType type)
    {
        this(type, 0.0, "");
    }

    public Information(InformationType type, double doubleValue)
    {
        this(type, doubleValue, "");
    }

    public Information(InformationType type, String stringValue)
    {
        this(type, 0.0, stringValue);
    }

    public Information(InformationType type, double doubleValue, String stringValue)
    {
        this.type = type;
        this.doubleValue = doubleValue;
        this.stringValue = stringValue;
    }

    public InformationType getType()
    {
        return type;
    }

    public double getDoubleValue()
    {
        return doubleValue;
    }

    public String getStringValue()
    {
        return stringValue;
    }

    public void setDoubleValue(double doubleValue)
    {
        this.doubleValue = doubleValue;
    }

    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }

    public boolean isValidDoubleValue()
    {
        switch(type)
        {
            case RAINFALL:
                return ValueTools.Cwb.isValidRainfallValue(doubleValue);

            case TEMPERATURE:
                return ValueTools.Cwb.isValidTemperatureValue(doubleValue);                

            default:
                return false;
        }
    }

    public Color getColorDoubleValue()
    {
        switch(type)
        {
            case RAINFALL:
                return ValueTools.Cwb.getRainfallColorSmallSpacing(doubleValue);

            case TEMPERATURE:
                return ValueTools.Cwb.getTempatureColor(doubleValue);

            default:
                return ValueTools.Cwb.getDefaultInvalidColor();
        }
    }

    
    public static enum InformationType
    {
        TEMPERATURE,
        RAINFALL;
    }
}
