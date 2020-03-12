package com.markgrand.moving_ratio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovingWeightedRatio4Test {
    @Test
    void aOverB() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getAOverB());
        incrementAOverB(mwr);
        assertEquals(0.5f, mwr.getAOverB());
        for (int i = 0; i < 100000; i++) {
            incrementAOverB(mwr);
        }
        assertEquals(0.5f, mwr.getAOverB());
    }

    private void incrementAOverB(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
    }

    @Test
    void aOverC() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getAOverC());
        incrementAOverC(mwr);
        assertEquals(0.5f, mwr.getAOverC());
        for (int i = 0; i < 100000; i++) {
            incrementAOverC(mwr);
        }
        assertEquals(0.5f, mwr.getAOverC());
    }

    private void incrementAOverC(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
    }

    @Test
    void aOverD() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getAOverD());
        incrementAOverD(mwr);
        assertEquals(0.5f, mwr.getAOverD());
        for (int i = 0; i < 100000; i++) {
            incrementAOverD(mwr);
        }
        assertEquals(0.5f, mwr.getAOverD());
    }

    private void incrementAOverD(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
    }

    @Test
    void getBOverA() {
    }

    @Test
    void getBOverC() {
    }

    @Test
    void getBOverD() {
    }

    @Test
    void getCOverA() {
    }

    @Test
    void getCOverB() {
    }

    @Test
    void getCOverD() {
    }

    @Test
    void getDOverA() {
    }

    @Test
    void getDOverB() {
    }

    @Test
    void getDOverC() {
    }
}