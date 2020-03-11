package com.markgrand.moving_ratio;

public class AbstractMovingWeightedRatio {
    protected AbstractMovingWeightedRatio() {}

    protected static float ratio(int numerator, int denominator) {
        if (denominator == 0) {
            return 0.0f;
        }
        return (float) numerator / (float) denominator;
    }
}
