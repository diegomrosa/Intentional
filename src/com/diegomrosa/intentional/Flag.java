package com.diegomrosa.intentional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

enum Flag {
    GRANT_READ_URI_PERMISSION,
    GRANT_WRITE_URI_PERMISSION,
    FROM_BACKGROUND,
    DEBUG_LOG_RESOLUTION,
    EXCLUDE_STOPPED_PACKAGES,
    INCLUDE_STOPPED_PACKAGES,
    Ox00000040,
    Ox00000080,
    Ox00000100,
    Ox00000200,
    Ox00000400,
    Ox00000800,
    Ox00001000,
    Ox00002000,
    ACTIVITY_TASK_ON_HOME,
    ACTIVITY_CLEAR_TASK,
    ACTIVITY_NO_ANIMATION,
    ACTIVITY_REORDER_TO_FRONT,
    ACTIVITY_NO_USER_ACTION,
    ACTIVITY_CLEAR_WHEN_TASK_RESET,
    ACTIVITY_LAUNCHED_FROM_HISTORY,
    ACTIVITY_RESET_TASK_IF_NEEDED,
    ACTIVITY_BROUGHT_TO_FRONT,
    ACTIVITY_EXCLUDE_FROM_RECENTS,
    ACTIVITY_PREVIOUS_IS_TOP,
    ACTIVITY_FORWARD_RESULT,
    ACTIVITY_CLEAR_TOP,
    ACTIVITY_MULTIPLE_TASK,
    ACTIVITY_NEW_TASK,
    ACTIVITY_SINGLE_TOP_or_RECEIVER_REPLACE_PENDING,
    ACTIVITY_NO_HISTORY_or_RECEIVER_REGISTERED_ONLY,
    Ox80000000;

    private static final String FLAG_HEX_FORMAT = "0x%08x";

    private int mFlag;

    private Flag() {
        mFlag = 1 << ordinal();
    }

    public String toHex() {
        return String.format(FLAG_HEX_FORMAT, Integer.valueOf(mFlag));
    }

    public static Flag[] toArray(int intentFlags) {
        Flag[] flagArray = new Flag[countFlags(intentFlags)];
        int flagIndex = 0;

        for (int i = 0; i < 32; i++) {
            int mask = 1 << i;

            if ((intentFlags & mask) != 0) {
                flagArray[flagIndex++] = Flag.values()[i];
            }
        }
        return flagArray;
    }

    private static int countFlags(int flags) {
        int flagCount = 0;

        for (int i = 0; i < 32; i++) {
            int mask = 1 << i;

            flagCount += ((flags & mask) == 0) ? 0 : 1;
        }
        return flagCount;
    }
}
