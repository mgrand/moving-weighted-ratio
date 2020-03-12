package com.markgrand.moving_ratio;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This is a class that can be used to track the ratio of some subset of events to the total number of events. For
 * example, the number of requests that produced an error result to the total number of requests. The ratio is
 * maintained as a weighted moving average. More recent measurements have more weight than older measurements.
 *
 * <p>The way that it works is that there are four counters. These are referred to as counterA, counterB, counterC and
 * CounterD. Each can be incremented independently of the other.</p>
 *
 * <p> The counters are kept as 15 bit quantities. When any of the counters overflow, all the counters are
 * right-shifted. These operations are thread-safe and do not block.</p>
 */
public class MovingWeightedRatio4 extends AbstractMovingWeightedRatio {
    private static final long A_INCREMENT = 0x1000000000000L;
    private static final int A_RIGHT_SHIFT = 48;

    private static final long B_INCREMENT = 0x100000000L;
    private static final int B_RIGHT_SHIFT = 32;

    private static final long C_INCREMENT = 0x10000;
    private static final int C_RIGHT_SHIFT = 16;

    private static final long D_INCREMENT = 1;
    private static final int D_RIGHT_SHIFT = 0;

    private static final long OVERFLOW_DETECTION_MASK =0x8000800080008000L;
    private static final long CLEAR_OVERFLOW_BITS_MASK = 0x7fff7fff7fff7fffL;
    private static final int LOW_ORDER_15_BITS_MASK = 0x7fff;

    private final AtomicLong counter = new AtomicLong(0);

    private static long increment(final long value, final long increment) {
        long newValue = increment + value;
        if ((newValue & OVERFLOW_DETECTION_MASK) != 0) {
            newValue = (newValue >> 1) & CLEAR_OVERFLOW_BITS_MASK;
        }
        return newValue;
    }

    /**
     * Increment counter A
     */
    public void incrementCounterA() {
        counter.updateAndGet(value -> increment(value, A_INCREMENT));
    }

    /**
     * increment counter B
     */
    public void incrementCounterB() {
        counter.updateAndGet(value -> increment(value, B_INCREMENT));
    }

    /**
     * increment counter C
     */
    public void incrementCounterC() {
        counter.updateAndGet(value -> increment(value, C_INCREMENT));
    }

    /**
     * increment counter D
     */
    public void incrementCounterD() {
        counter.updateAndGet(value -> increment(value, D_INCREMENT));
    }

    private float get(int numeratorRightShift, int denominatorRightShift) {
        long i = counter.get();
        int numerator = (int)(i >> numeratorRightShift) & LOW_ORDER_15_BITS_MASK;
        int denominator = (int)(i >> denominatorRightShift) & LOW_ORDER_15_BITS_MASK;
        return ratio(numerator, denominator);
    }

    /**
     * compute A / B
     *
     * @return A / B unless B == 0. If B == 0 then return 0.
     */
    public float getAOverB() {
        return get(A_RIGHT_SHIFT, B_RIGHT_SHIFT);
    }

    /**
     * compute A / C
     *
     * @return A / C unless C == 0. If C == 0 then return 0.
     */
    public float getAOverC() {
        return get(A_RIGHT_SHIFT, C_RIGHT_SHIFT);
    }

    /**
     * compute A / D
     *
     * @return A / D unless D == 0. If D == 0 then return 0.
     */
    public float getAOverD() {
        return get(A_RIGHT_SHIFT, D_RIGHT_SHIFT);
    }

    /**
     * compute B / A
     *
     * @return B / A unless A == 0. If A == 0 then return 0.
     */
    public float getBOverA() {
        return get(B_RIGHT_SHIFT, A_RIGHT_SHIFT);
    }

    /**
     * compute B / C
     *
     * @return B / C unless C == 0. If C == 0 then return 0.
     */
    public float getBOverC() {
        return get(B_RIGHT_SHIFT, C_RIGHT_SHIFT);
    }

    /**
     * compute B / D
     *
     * @return B / D unless D == 0. If B == 0 then return 0.
     */
    public float getBOverD() {
        return get(B_RIGHT_SHIFT, D_RIGHT_SHIFT);
    }

    /**
     * compute C / A
     *
     * @return C / A unless A == 0. If A == 0 then return 0.
     */
    public float getCOverA() {
        return get(C_RIGHT_SHIFT, A_RIGHT_SHIFT);
    }

    /**
     * compute C / B
     *
     * @return C / B unless B == 0. If B == 0 then return 0.
     */
    public float getCOverB() {
        return get(C_RIGHT_SHIFT, B_RIGHT_SHIFT);
    }

    /**
     * compute C / D
     *
     * @return C / D unless D == 0. If D == 0 then return 0.
     */
    public float getCOverD() {
        return get(C_RIGHT_SHIFT, D_RIGHT_SHIFT);
    }

    /**
     * compute D / A
     *
     * @return D / A unless A == 0. If A == 0 then return 0.
     */
    public float getDOverA() {
        return get(D_RIGHT_SHIFT, A_RIGHT_SHIFT);
    }

    /**
     * compute D / B
     *
     * @return D / B unless B == 0. If B == 0 then return 0.
     */
    public float getDOverB() {
        return get(D_RIGHT_SHIFT, B_RIGHT_SHIFT);
    }

    /**
     * compute D / C
     *
     * @return D / C unless C == 0. If C == 0 then return 0.
     */
    public float getDOverC() {
        return get(D_RIGHT_SHIFT, C_RIGHT_SHIFT);
    }
}
