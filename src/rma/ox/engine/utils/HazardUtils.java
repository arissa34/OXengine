package rma.ox.engine.utils;

public class HazardUtils {

    public static boolean hasChance(int chancePercent){
        int randomNbr = RandomUtils.getRandomInt(1, 100);
        if(chancePercent>0 && randomNbr<chancePercent){
            return true;
        }
        return false;
    }
}
