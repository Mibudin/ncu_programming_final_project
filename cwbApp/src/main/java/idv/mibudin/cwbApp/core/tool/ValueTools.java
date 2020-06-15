package idv.mibudin.cwbApp.core.tool;


import javafx.scene.paint.Color;


public class ValueTools
{
    public static double celsiusToFahrenheit(double celsius)
    {
        return celsius * 1.8 + 32;
    }

    public static double fahrenheitToCelsius(double fahrenheit)
    {
        return (fahrenheit - 32) / 1.8;
    }


    public static class Cwb
    {
        public static boolean isValidRainfallValue(double rainfall)
        {
            if(rainfall == -99.0)
            {
                return false;
            }

            if(rainfall < 0.0 && rainfall != -998.0)
            {
                return false;
            }

            return true;
        }

        public static boolean isValidTemperatureValue(double temperature)
        {
            if(temperature == -99.0)
            {
                return false;
            }

            return true;
        }

        public static Color getDefaultInvalidColor()
        {
            return Color.rgb(0, 0, 0);
        }

        public static Color getRainfallColorSmallSpacing(double rainfall)
        {
                 if(rainfall == -99) return Color.rgb(  0,   0,   0);  // Invalid value.
            else if(rainfall >= 300) return Color.rgb(255, 214, 254);
            else if(rainfall >= 200) return Color.rgb(255,  56, 251);
            else if(rainfall >= 150) return Color.rgb(219,  44, 209);
            else if(rainfall >= 130) return Color.rgb(171,  31, 162);
            else if(rainfall >= 110) return Color.rgb(170,  24,   0);
            else if(rainfall >=  90) return Color.rgb(218,  35,   4);
            else if(rainfall >=  70) return Color.rgb(255,  43,   6);
            else if(rainfall >=  50) return Color.rgb(255, 167,  31);
            else if(rainfall >=  40) return Color.rgb(255, 211,  40);
            else if(rainfall >=  30) return Color.rgb(254, 253,  39);
            else if(rainfall >=  20) return Color.rgb(  0, 250,  47);
            else if(rainfall >=  15) return Color.rgb( 38, 163,  27);
            else if(rainfall >=  10) return Color.rgb(  1, 119, 253);
            else if(rainfall >=   6) return Color.rgb(  0, 165, 254);
            else if(rainfall >=   2) return Color.rgb(  1, 210, 253);
            else if(rainfall >=   1) return Color.rgb(158, 253, 255);
            else if(rainfall >    0) return Color.rgb(202, 202, 202);
            else                     return Color.rgb(255, 255, 255);
        }

        public static Color getRainfallColorLargeSpacing(double rainfall)
        {
                 if(rainfall ==  -99) return Color.rgb(  0,   0,   0);  // Invalid value.
            else if(rainfall >= 1500) return Color.rgb(255, 214, 254);
            else if(rainfall >= 1200) return Color.rgb(255,  56, 251);
            else if(rainfall >= 1000) return Color.rgb(219,  44, 209);
            else if(rainfall >=  900) return Color.rgb(171,  31, 162);
            else if(rainfall >=  800) return Color.rgb(170,  24,   0);
            else if(rainfall >=  700) return Color.rgb(218,  35,   4);
            else if(rainfall >=  600) return Color.rgb(255,  43,   6);
            else if(rainfall >=  500) return Color.rgb(255, 167,  31);
            else if(rainfall >=  400) return Color.rgb(255, 211,  40);
            else if(rainfall >=  300) return Color.rgb(254, 253,  39);
            else if(rainfall >=  200) return Color.rgb(  0, 250,  47);
            else if(rainfall >=  150) return Color.rgb( 38, 163,  27);
            else if(rainfall >=  100) return Color.rgb(  1, 119, 253);
            else if(rainfall >=   60) return Color.rgb(  0, 165, 254);
            else if(rainfall >=   20) return Color.rgb(  1, 210, 253);
            else if(rainfall >=   10) return Color.rgb(158, 253, 255);
            else if(rainfall >     0) return Color.rgb(202, 202, 202);
            else                      return Color.rgb(255, 255, 255);
        }

        public static Color getTempatureColor(double temprature)
        {
                 if(temprature == -99) return Color.rgb(  0,   0,   0);  // Invalid value.
            else if(temprature >=  38) return Color.rgb(127,  38, 158);
            else if(temprature >=  37) return Color.rgb(133,  80, 152);
            else if(temprature >=  36) return Color.rgb(156, 104,  70);
            else if(temprature >=  35) return Color.rgb(118,   3,   6);
            else if(temprature >=  34) return Color.rgb(172,   5,  57);
            else if(temprature >=  33) return Color.rgb(239,  22,  91);
            else if(temprature >=  32) return Color.rgb(240,  83,  52);
            else if(temprature >=  31) return Color.rgb(221, 124,   7);
            else if(temprature >=  30) return Color.rgb(229, 141,  41);
            else if(temprature >=  29) return Color.rgb(233, 158,  57);
            else if(temprature >=  28) return Color.rgb(237, 178,  76);
            else if(temprature >=  27) return Color.rgb(241, 195,  99);
            else if(temprature >=  26) return Color.rgb(233, 158,  57);
            else if(temprature >=  25) return Color.rgb(246, 231, 138);
            else if(temprature >=  24) return Color.rgb(244, 243, 195);
            else if(temprature >=  23) return Color.rgb(217, 241, 145);
            else if(temprature >=  22) return Color.rgb(202, 230, 143);
            else if(temprature >=  21) return Color.rgb(187, 223, 136);
            else if(temprature >=  20) return Color.rgb(167, 217, 132);
            else if(temprature >=  19) return Color.rgb(150, 208, 124);
            else if(temprature >=  18) return Color.rgb(133, 201, 118);
            else if(temprature >=  17) return Color.rgb(118, 193, 111);
            else if(temprature >=  16) return Color.rgb( 98, 187, 107);
            else if(temprature >=  15) return Color.rgb( 81, 178,  99);
            else if(temprature >=  14) return Color.rgb( 63, 169,  94);
            else if(temprature >=  13) return Color.rgb( 47, 162,  87);
            else if(temprature >=  12) return Color.rgb( 28, 154,  83);
            else if(temprature >=  11) return Color.rgb( 12, 146,  77);
            else if(temprature >=  10) return Color.rgb(180, 233, 247);
            else if(temprature >=   9) return Color.rgb(166, 224, 236);
            else if(temprature >=   8) return Color.rgb(149, 212, 227);
            else if(temprature >=   7) return Color.rgb(135, 203, 216);
            else if(temprature >=   6) return Color.rgb(119, 191, 205);
            else if(temprature >=   5) return Color.rgb(103, 180, 198);
            else if(temprature >=   4) return Color.rgb( 93, 170, 190);
            else if(temprature >=   3) return Color.rgb( 77, 158, 177);
            else if(temprature >=   2) return Color.rgb( 59, 147, 167);
            else if(temprature >=   1) return Color.rgb( 46, 137, 156);
            else if(temprature >=   0) return Color.rgb( 31, 126, 148);
            else                       return Color.rgb( 16, 115, 136);
        }
    }
}
