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
    void bOverA() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getBOverA());
        incrementBOverA(mwr);
        assertEquals(0.5f, mwr.getBOverA());
        for (int i = 0; i < 100000; i++) {
            incrementBOverA(mwr);
        }
        assertEquals(0.5f, mwr.getBOverA());
    }

    private void incrementBOverA(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
    }

    @Test
    void bOverC() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getBOverC());
        incrementBOverC(mwr);
        assertEquals(0.5f, mwr.getBOverC());
        for (int i = 0; i < 100000; i++) {
            incrementBOverC(mwr);
        }
        assertEquals(0.5f, mwr.getBOverC());
    }

    private void incrementBOverC(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
    }

    @Test
    void bOverD() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getBOverD());
        incrementBOverD(mwr);
        assertEquals(0.5f, mwr.getBOverD());
        for (int i = 0; i < 100000; i++) {
            incrementBOverD(mwr);
        }
        assertEquals(0.5f, mwr.getBOverD());
    }

    private void incrementBOverD(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
    }

    @Test
    void cOverA() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getCOverA());
        incrementCOverA(mwr);
        assertEquals(0.5f, mwr.getCOverA());
        for (int i = 0; i < 100000; i++) {
            incrementCOverA(mwr);
        }
        assertEquals(0.5f, mwr.getCOverA());
    }

    private void incrementCOverA(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
    }

    @Test
    void cOverB() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getCOverB());
        incrementCOverB(mwr);
        assertEquals(0.5f, mwr.getCOverB());
        for (int i = 0; i < 100000; i++) {
            incrementCOverB(mwr);
        }
        assertEquals(0.5f, mwr.getCOverB());
    }

    private void incrementCOverB(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
    }

    @Test
    void cOverD() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getCOverD());
        incrementCOverD(mwr);
        assertEquals(0.5f, mwr.getCOverD());
        for (int i = 0; i < 100000; i++) {
            incrementCOverD(mwr);
        }
        assertEquals(0.5f, mwr.getCOverD());
    }

    private void incrementCOverD(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterD();
    }

    @Test
    void dOverA() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getDOverA());
        incrementDOverA(mwr);
        assertEquals(0.5f, mwr.getDOverA());
        for (int i = 0; i < 100000; i++) {
            incrementDOverA(mwr);
        }
        assertEquals(0.5f, mwr.getDOverA());
    }

    private void incrementDOverA(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
        mwr.incrementCounterA();
    }


    @Test
    void dOverB() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getDOverB());
        incrementDOverB(mwr);
        assertEquals(0.5f, mwr.getDOverB());
        for (int i = 0; i < 100000; i++) {
            incrementDOverB(mwr);
        }
        assertEquals(0.5f, mwr.getDOverB());
    }

    private void incrementDOverB(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
        mwr.incrementCounterB();
    }

    @Test
    void dOverC() {
        MovingWeightedRatio4 mwr = new MovingWeightedRatio4();
        assertEquals(0.0f, mwr.getDOverC());
        incrementDOverC(mwr);
        assertEquals(0.5f, mwr.getDOverC());
        for (int i = 0; i < 100000; i++) {
            incrementDOverC(mwr);
        }
        assertEquals(0.5f, mwr.getDOverC());
    }

    private void incrementDOverC(final MovingWeightedRatio4 mwr) {
        mwr.incrementCounterD();
        mwr.incrementCounterD();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
        mwr.incrementCounterC();
    }
}