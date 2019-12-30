package com.badlogic.gdx.graphics.g3d.attributes;

import com.badlogic.gdx.graphics.g3d.Attribute;

public class NoLightAttribute extends Attribute {

    public final static String NoLightAlias = "noLight";
    public static final long NoLight = register(NoLightAlias);

    public final static boolean is (final long mask) {
        return (mask & NoLight) == mask;
    }

    public NoLightAttribute() {
        super(NoLight);
    }

    @Override
    public Attribute copy() {
        return new NoLightAttribute();
    }

    @Override
    public int compareTo(Attribute attribute) {
        return 0;
    }
}
