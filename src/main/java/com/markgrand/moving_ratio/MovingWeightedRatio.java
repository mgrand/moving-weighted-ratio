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
public class MovingWeightedRatio {
    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Increment counter A
     */
    public void incrementCounterA() {
        counter.updateAndGet(i -> {
            int newValue = i + 0x10000;
            if ((newValue & 0x80000000) != 0) {
                newValue = (newValue >> 1) & 0x7fff7fff;
            }
            return newValue;
        });
    }

    /**
     * increment counter B
     */
    public void incrementCounterB() {
        counter.updateAndGet(i -> {
            int newValue = i + 1;
            if ((newValue & 0x00008000) != 0) {
                newValue = (newValue >> 1) & 0x7fff7fff;
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
        int b = i & 0x7fff;
        int a = (i >> 16) & 0x7fff;
        return ratio(a, b);
    }

    /**
     * compute B / A
     *
     * @return B / A unless A == 0. If B == 0 then return 0.
     */
    public float getBOverA() {
        int i = counter.get();
        int b = i & 0x7fff;
        int a = (i >> 16) & 0x7fff;
        return ratio(b, a);
    }

    private static float ratio(int numerator, int denominator) {
        if (denominator == 0) {
            return 0.0f;
        }
        return (float) numerator / (float) denominator;
    }
}
