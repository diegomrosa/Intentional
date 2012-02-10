package com.diegomrosa.intentional;

import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;

final class Intents {
    public static final String[] ACTIONS = {
            "android.intent.action.ANSWER",
            "android.intent.action.ATTACH_DATA",
            "android.intent.action.CALL",
            "android.intent.action.CHOOSER",
            "android.intent.action.DELETE",
            "android.intent.action.DIAL",
            "android.intent.action.EDIT",
            "android.intent.action.FACTORY_TEST",
            "android.intent.action.GET_CONTENT",
            "android.intent.action.INSERT",
            "android.intent.action.MAIN",
            "android.intent.action.PICK",
            "android.intent.action.PICK_ACTIVITY",
            "android.intent.action.RUN",
            "android.intent.action.SEARCH",
            "android.intent.action.SEND",
            "android.intent.action.SENDTO",
            "android.intent.action.SYNC",
            "android.intent.action.VIEW",
            "android.intent.action.WEB_SEARCH"};

    public static final String[] CATEGORIES = {
            "android.intent.category.DEFAULT",
            "android.intent.category.BROWSABLE",
            "android.intent.category.TAB",
            "android.intent.category.ALTERNATIVE",
            "android.intent.category.SELECTED_ALTERNATIVE",
            "android.intent.category.LAUNCHER",
            "android.intent.category.INFO",
            "android.intent.category.HOME",
            "android.intent.category.PREFERENCE",
            "android.intent.category.TEST",
            "android.intent.category.CAR_DOCK",
            "android.intent.category.DESK_DOCK",
            "android.intent.category.LE_DESK_DOCK",
            "android.intent.category.HE_DESK_DOCK",
            "android.intent.category.CAR_MODE",
            "android.intent.category.APP_MARKET"};

    private static final String TXT_TYPE = "text/plain";

    public static Uri getDataStream(Intent intent) {
        Uri data = intent.getData();

        if (data == null) {
            data = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        }
        return data;
    }

    public static Uri getDataStreamOrText(Intent intent) {
        Uri data = getDataStream(intent);

        if (data == null) {
            String type = intent.getType();
            String extraText = intent.getStringExtra(Intent.EXTRA_TEXT);

            if (TXT_TYPE.equals(type) && URLUtil.isValidUrl(extraText)) {
                data = Uri.parse(extraText);
            }
        }
        return data;
    }
}
