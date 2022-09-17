package meteor.rgb.util;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Maths {

    public static int intToTicks(int i) {
        return i * 20;
    }
    public static int ticksToInt(int i) {
        return i / 20;
    }

    public static double roundDouble(double d) {return Math.ceil(d);}

    public static long msPassed(Long start) {
        return System.currentTimeMillis() - start;
    }
    public static long now() {return System.currentTimeMillis();}

    public static String millisElapsed(Long start) {return msPassed(start) + "ms";}

    public static long secondsToMS(int seconds) {return TimeUnit.SECONDS.toMillis(seconds);}


    public static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getBlue(), color.getGreen());
    }

}
