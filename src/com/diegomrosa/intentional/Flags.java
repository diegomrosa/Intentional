package com.diegomrosa.intentional;

import java.util.Iterator;

class Flags {
    private static final String[] FLAG_NAMES = {
        "GRANT_READ_URI_PERMISSION",
        "GRANT_WRITE_URI_PERMISSION",
        "FROM_BACKGROUND",
        "DEBUG_LOG_RESOLUTION",
        "EXCLUDE_STOPPED_PACKAGES",
        "INCLUDE_STOPPED_PACKAGES",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "ACTIVITY_TASK_ON_HOME",
        "ACTIVITY_CLEAR_TASK",
        "ACTIVITY_NO_ANIMATION",
        "ACTIVITY_REORDER_TO_FRONT",
        "ACTIVITY_NO_USER_ACTION",
        "ACTIVITY_CLEAR_WHEN_TASK_RESET",
        "ACTIVITY_LAUNCHED_FROM_HISTORY",
        "ACTIVITY_RESET_TASK_IF_NEEDED",
        "ACTIVITY_BROUGHT_TO_FRONT",
        "ACTIVITY_EXCLUDE_FROM_RECENTS",
        "ACTIVITY_PREVIOUS_IS_TOP",
        "ACTIVITY_FORWARD_RESULT",
        "ACTIVITY_CLEAR_TOP",
        "ACTIVITY_MULTIPLE_TASK",
        "ACTIVITY_NEW_TASK",
        "ACTIVITY_SINGLE_TOP/RECEIVER_REPLACE_PENDING",
        "ACTIVITY_NO_HISTORY/RECEIVER_REGISTERED_ONLY",
        null
    };
    private static final String FLAG_HEX_FORMAT = "0x%08x";

    public static String toString(int flag) {
        if (flag <= 0) {
            throw new IllegalArgumentException("Zero and negatives are not valid flags: " + flag);
        }
        String flagName = FLAG_NAMES[indexOf(flag)];

        if (flagName == null) {
            return String.format(FLAG_HEX_FORMAT, Integer.valueOf(flag));
        }
        return flagName;
    }

    private static int indexOf(int flag) {
        return msb(flag);
    }

    public static Iterator<Integer> iterator(int flags) {
        return new FlagIterator(flags);
    }

    private static int msb(int valArg) {
        int val = valArg;
        int msb = 0;

        while ((val >>= 1) > 0) {
            msb++;
        }
        return msb;
    }
}
