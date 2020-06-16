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
            case  ELEV: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case  WDIR: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case  WDSD: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case  TEMP: return ValueTools.Cwb.isValidTemperatureValue(doubleValue);
            case  HUMD: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case  PRES: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case H_24R: return ValueTools.Cwb.isValidRainfallValue(doubleValue);
            case  H_FX: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case  H_XD: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case H_FXT: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case  D_TX: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case D_TXT: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case  D_TN: return ValueTools.Cwb.isValidTempValue(doubleValue);
            case D_TNT: return ValueTools.Cwb.isValidTempValue(doubleValue);
            default:    return false;
        }
    }

    public Color getColorDoubleValue()
    {
        switch(type)
        {
            case  ELEV: return ValueTools.Cwb.getTempColor();
            case  WDIR: return ValueTools.Cwb.getTempColor();
            case  WDSD: return ValueTools.Cwb.getTempColor();
            case  TEMP: return ValueTools.Cwb.getTempatureColor(doubleValue);
            case  HUMD: return ValueTools.Cwb.getTempColor();
            case  PRES: return ValueTools.Cwb.getTempColor();
            case H_24R: return ValueTools.Cwb.getRainfallColorSmallSpacing(doubleValue);
            case  H_FX: return ValueTools.Cwb.getTempColor();
            case  H_XD: return ValueTools.Cwb.getTempColor();
            case H_FXT: return ValueTools.Cwb.getTempColor();
            case  D_TX: return ValueTools.Cwb.getTempColor();
            case D_TXT: return ValueTools.Cwb.getTempColor();
            case  D_TN: return ValueTools.Cwb.getTempColor();
            case D_TNT: return ValueTools.Cwb.getTempColor();
            default:    return ValueTools.Cwb.getDefaultInvalidColor();
        }
    }

    
    public static enum InformationType
    {
        ELEV,
        WDIR,
        WDSD,
        TEMP,
        HUMD,
        PRES,
        H_24R,
        H_FX,
        H_XD,
        H_FXT,
        D_TX,
        D_TXT,
        D_TN,
        D_TNT;


        public static int getInformationIndex(InformationType type)
        {
            switch(type)
            {
                case  ELEV: return  0;
                case  WDIR: return  1;
                case  WDSD: return  2;
                case  TEMP: return  3;
                case  HUMD: return  4;
                case  PRES: return  5;
                case H_24R: return  6;
                case  H_FX: return  7;
                case  H_XD: return  8;
                case H_FXT: return  9;
                case  D_TX: return 10;
                case D_TXT: return 11;
                case  D_TN: return 12;
                case D_TNT: return 13;
                default:    return -1;
            }
        }
    }
}
