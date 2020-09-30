package rma.ox.engine.core.utils;

public class MathUtils {

    final static double EPSILON = 1e-12;

    public static int addPercentage(int value, int percentToAdd){
        return value + ((value * percentToAdd)/100);
    }

    public static boolean approximatelyEqual(float desiredValue, float actualValue, float tolerancePercentage) {
        float diff = Math.abs(desiredValue - actualValue);
        float tolerance = tolerancePercentage/100 * desiredValue;
        return diff < tolerance;
    }

    public static float map(float valueCoord1,
                            float startCoord1, float endCoord1,
                            float startCoord2, float endCoord2) {

        if (Math.abs(endCoord1 - startCoord1) < EPSILON) {
            throw new ArithmeticException("/ 0");
        }

        float offset = startCoord2;
        float ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
        return ratio * (valueCoord1 - startCoord1) + offset;
    }

}
