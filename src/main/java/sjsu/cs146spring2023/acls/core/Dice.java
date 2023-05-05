package sjsu.cs146spring2023.acls.core;

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
}

class Die {
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

    @Override
    public String toString() {
        return "S" + side + ":" + getValue();
    }
}