package rma.ox.engine.skeleton;

import rma.ox.engine.utils.RandomUtils;

public enum Direction {
    LEFT, RIGHT;

    public static Direction generate(){
        return RandomUtils.getRandomBoolean() ? LEFT : RIGHT;
    }
}
