package com.diegomrosa.intentional;

import java.util.Iterator;

class FlagIterator implements Iterator<Integer> {
    private int flags;
    private int index;

    public FlagIterator(int flags) {
        if (flags <= 0) {
            throw new IllegalArgumentException("Flags not valid: " + flags);
        }
        this.flags = flags;
        this.index = -1;
    }

    @Override
    public boolean hasNext() {
        if (index == -1) {
            return true;
        }
        int currentFlag = 1 >> index;

        return (flags > currentFlag);
    }

    @Override
    public Integer next() {
        while (index <= 32) {
            int mask = 1 << index;
            int flag = flags & mask;

            if (flag != 0) {
                return Integer.valueOf(flag);
            }
            index++;
        }
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not implemented for the flag iterator.");
    }
}
