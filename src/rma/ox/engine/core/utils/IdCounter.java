package rma.ox.engine.core.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IdCounter {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static int getNextNumber(){
        return counter.incrementAndGet();
    }
}
