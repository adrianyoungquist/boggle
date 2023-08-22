package com.adrianyoungquist.model;

import java.util.Random;

public class Die {
    static final int SIDES = 6;

    private final char[] values;
    private int side;

    Die(String values) {
        if (values.length() != SIDES) {
            throw new IllegalArgumentException("Expected " + SIDES + " sides, found " + values.length());
        }
        this.values = values.toLowerCase().toCharArray();
        side = 0;
    }

    Die(char[] values) {
        if (values.length != SIDES) {
            throw new IllegalArgumentException("Expected " + SIDES + " sides, found " + values.length);
        }

        this.values = new char[SIDES];
        for (int i = 0; i < SIDES; i++) {
            this.values[i] = Character.toLowerCase(values[i]);
        }
        side = 0;
    }

    public char[] getValues() {
        return values;
    }

    public char getValue() {
        return values[side];
    }

    public int getSide() {
        return side;
    }

    public char setSide(int side_no) {
        if (side_no >= SIDES) {
            throw new IllegalArgumentException("Maximum " + SIDES + " sides, received index " + side_no);
        }
        side = side_no;
        return values[side];
    }

    public int setRandom() {
        Random rand = new Random();
        side = rand.nextInt(SIDES);
        return side;
    }

    public int setRandom(Random rand) {
        side = rand.nextInt(SIDES);
        return side;
    }

    public boolean contains(char c) {
        for (char ch : values) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "S" + side + ":" + getValue();
    }
}
