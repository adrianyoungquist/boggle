package com.adrianyoungquist.model;

import java.util.Arrays;
import java.util.Random;

public class Dice {
    public static final int NUM_DICE = 16;
    private static final Die[] CLASSIC_DICE;
    private static final Die[] MODERN_DICE;

    static {
        String[] classicList = new String[]{"AACIOT", "ABILTY", "ABJMOQ", "ACDEMP", "ACELRS", "ADENVZ", "AHMORS", "BIFORX", "DENOSW", "DKNOTU", "EEFHIY", "EGKLUY", "EGINTV", "EHINPS", "ELPSTU", "GILRUW"};
        String[] modernList = new String[]{"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW", "CIMOTU", "DEILRX", "DELRVY", "DISTTY", "EEGHNW", "EEINSU", "EHRTVW", "EIOSST", "ELRTTY", "HIMNUQ", "HLNNRZ"};

        CLASSIC_DICE = new Die[NUM_DICE];
        MODERN_DICE = new Die[NUM_DICE];

        for (int i = 0; i < classicList.length; i++) {
            CLASSIC_DICE[i] = new Die(classicList[i]);
            MODERN_DICE[i] = new Die(modernList[i]);
        }
    }


    protected boolean classic;
    protected Die[] dice;

    public Dice() {
        classic = true;
        dice = CLASSIC_DICE;
    }

    public Dice(boolean classic) {
        this.classic = classic;
        if (classic) {
            dice = CLASSIC_DICE;
        } else {
            dice = MODERN_DICE;
        }
    }

    public Dice(char[] values) { // all 6 sides of each die will have the same values
        char[] tmp_sides = new char[Die.SIDES];
        dice = new Die[values.length];
        for (int i = 0; i < values.length; i++) {
            Arrays.fill(tmp_sides, values[i]);
            dice[i] = new Die(tmp_sides);
        }
    }

    public Die get(int index) {
        if (index > NUM_DICE) {
            throw new IllegalArgumentException("Die number of " + index + " out of range.");
        }
        return dice[index];
    }

    public void set(int index, int side_no) {
        if (index > NUM_DICE) {
            throw new IllegalArgumentException("Die number of " + index + " out of range.");
        }
        dice[index].setSide(side_no);
    }

    public void randomizeDiceSides() {
        Random rand = new Random();
        for (Die d : dice) {
            d.setRandom(rand);
        }
    }

    public void randomizeDiceSides(Random rand) {
        for (Die d : dice) {
            d.setRandom(rand);
        }
    }

    @Override
    public String toString() {
        return "Dice{" +
                "dice=" + Arrays.toString(dice) +
                '}';
    }
}

