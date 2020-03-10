package com.markgrand.moving_ratio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovingWeightedRatioTest {
    @Test
    void aOverB() {
        MovingWeightedRatio mwr = new MovingWeightedRatio();
        assertEquals(0.0f, mwr.getAOverB());
        incrementAOverB(mwr);
        assertEquals(0.5f, mwr.getAOverB());
        for (int i = 0; i < 100000; i++) {
            incrementAOverB(mwr);
        }
        assertEquals(0.5f, mwr.getAOverB());
    }

    private void incrementAOverB(final MovingWeightedRatio mwr) {
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
    }

    @Test
    void bOverA() {
        MovingWeightedRatio mwr = new MovingWeightedRatio();
        assertEquals(0.0f, mwr.getBOverA());
        incrementBOverA(mwr);
        assertEquals(0.5f, mwr.getBOverA());
        for (int i = 0; i < 100000; i++) {
            incrementBOverA(mwr);
        }
        assertEquals(0.5f, mwr.getBOverA());
    }

    private void incrementBOverA(final MovingWeightedRatio mwr) {
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
    }
}