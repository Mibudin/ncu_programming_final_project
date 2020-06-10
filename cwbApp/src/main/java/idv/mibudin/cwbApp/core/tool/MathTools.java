package idv.mibudin.cwbApp.core.tool;


public class MathTools
{
    public static Number addNumbers(Number a, Number b)
    {
        if(a instanceof Double || b instanceof Double)
        {
            return a.doubleValue() + b.doubleValue();
        }
        else if(a instanceof Float || b instanceof Float)
        {
            return a.floatValue() + b.floatValue();
        }
        else if(a instanceof Long || b instanceof Long)
        {
            return a.longValue() + b.longValue();
        }
        else if(a instanceof Short || b instanceof Short)
        {
            return a.shortValue() + b.shortValue();
        }
        else if(a instanceof Byte || b instanceof Byte)
        {
            return a.byteValue() + b.byteValue();
        }
        else
        {
            return a.intValue() + b.intValue();
        }
    }
}
