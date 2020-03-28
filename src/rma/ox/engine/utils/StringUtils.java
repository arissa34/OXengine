package rma.ox.engine.utils;

public class StringUtils {
    public static String capitalize(String str)
    {
        if(str == null) return str;
        str = str.toLowerCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }
}
