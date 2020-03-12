package com.markgrand.moving_ratio;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a class that can be used to track the ratio of some subset of events to the total number of events. For
 * example, the number of requests that produced an error result to the total number of requests. The ratio is
 * maintained as a weighted moving average. More recent measurements have more weight than older measurements.
 *
 * <p>The way that it works is that there are two counters. These are referred to as counterA and counterB. Each can be
 * incremented independently of the other.</p>
 *
 * <p> The counters are kept as 15 bit quantities. When any of the counters overflow, all the counters are
 * right-shifted. These operations are thread-safe and do not block.</p>
 */
public class MovingWeightedRatio extends AbstractMovingWeightedRatio {
    private static final int A_INCREMENT = 0x10000;
    private static final int A_OVERFLOW_MASK = 0x80000000;

    private static final int B_INCREMENT = 1;
    private static final int B_OVERFLOW_MASK = 0x8000;

    private static final int CLEAR_OVERFLOW_BITS_MASK = 0x7fff7fff;
    private static final int LOW_ORDER_15_BITS_MASK = 0x7fff;

    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Increment counter A
     */
    public void incrementCounterA() {
        counter.updateAndGet(i -> {
            int newValue = i + A_INCREMENT;
            if ((newValue & A_OVERFLOW_MASK) != 0) {
                newValue = (newValue >> 1) & CLEAR_OVERFLOW_BITS_MASK;
            }
            return newValue;
        });
    }

    /**
     * increment counter B
     */
    public void incrementCounterB() {
        counter.updateAndGet(i -> {
            int newValue = i + B_INCREMENT;
            if ((newValue & B_OVERFLOW_MASK) != 0) {
                newValue = (newValue >> 1) & CLEAR_OVERFLOW_BITS_MASK;
            }
            return newValue;
        });
    }

    /**
     * compute A / B
     *
     * @return A / B unless B == 0. If B == 0 then return 0.
     */
    public float getAOverB() {
        int i = counter.get();
        int b = i & LOW_ORDER_15_BITS_MASK;
        int a = (i >> 16) & LOW_ORDER_15_BITS_MASK;
        return ratio(a, b);
    }

    /**
     * compute B / A
     *
     * @return B / A unless A == 0. If A == 0 then return 0.
     */
    public float getBOverA() {
        int i = counter.get();
        int b = i & LOW_ORDER_15_BITS_MASK;
        int a = (i >> 16) & LOW_ORDER_15_BITS_MASK;
        return ratio(b, a);
    }

}
