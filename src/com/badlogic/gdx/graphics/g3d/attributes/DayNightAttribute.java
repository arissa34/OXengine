package com.badlogic.gdx.graphics.g3d.attributes;

import com.badlogic.gdx.graphics.g3d.Attribute;

public class DayNightAttribute extends Attribute {

    public final static String DayNightAlias = "dayNight";
    public static final long DayNight = register(DayNightAlias);

    public final static boolean is (final long mask) {
        return (mask & DayNight) == mask;
    }

    public float timeRadian;

    public DayNightAttribute() {
        this(0f);
    }

    public DayNightAttribute(float timeRadian) {
        super(DayNight);
        this.timeRadian = timeRadian;
    }

    public void setTime(float timeRadian){
        this.timeRadian = timeRadian;
    }

    @Override
    public Attribute copy() {
        return new DayNightAttribute(timeRadian);
    }

    @Override
    public int compareTo(Attribute attribute) {
        return 0;
    }
}
