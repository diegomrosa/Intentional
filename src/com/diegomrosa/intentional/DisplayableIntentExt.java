package com.diegomrosa.intentional;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Set;

class DisplayableIntentExt {
    private Context context;
    private IntentExt intentExt;

    public DisplayableIntentExt(Context context, IntentExt intentExt) {
        this.context = context;
        this.intentExt = intentExt;
    }

    public String getUriString() {
        Uri uri = intentExt.getData();

        return uri == null ? getEmptySummary() : uri.toString();
    }

    public String getMimeTypeString() {
        String mimeType = intentExt.getType();

        return mimeType == null ? getEmptySummary() : mimeType;
    }

    public String getActionString() {
        String action = intentExt.getAction();

        return action == null ? getEmptySummary() : action;
    }

    public String getCategoriesString() {
        Set<String> categories = intentExt.getCategories();

        if ((categories == null) || categories.isEmpty()) {
            return getEmptySummary();
        }
        StringBuilder builder = new StringBuilder(categories.iterator().next());

        if (categories.size() > 1) {
            appendMore(builder, Integer.valueOf(categories.size() - 1));
        }
        return builder.toString();
    }

    public String getExtrasString() {
        Bundle extras = intentExt.getExtras();

        if ((extras == null) || extras.isEmpty()) {
            return getEmptySummary();
        }
        Iterator<String> keyIterator = extras.keySet().iterator();
        StringBuilder builder = new StringBuilder(keyIterator.next());

        if (keyIterator.hasNext()) {
            appendMore(builder, Integer.valueOf(extras.size() - 1));
        }
        return builder.toString();
    }

    public String getFlagsString() {
        int flags = intentExt.getFlags();

        if (flags == 0) {
            return getEmptySummary();
        }
        Iterator<Integer> flagIterator = Flags.iterator(flags);
        StringBuilder builder = new StringBuilder(Flags.toString(flagIterator.next().intValue()));

        if (flagIterator.hasNext()) {
            appendMore(builder);
        }
        return builder.toString();
    }

    public String getComponentString() {
        ComponentName compName = intentExt.getComponent();

        if (compName == null) {
            return getEmptySummary();
        }
        return compName.getClassName();
    }

    private String getEmptySummary() {
        return context.getString(R.string.intent_summary_empty);
    }

    private void appendMore(StringBuilder builder) {
        builder.append(' ').append(context.getString(R.string.intent_summary_more));
    }

    private void appendMore(StringBuilder builder, Integer count) {
        builder.append(' ').append(context.getString(R.string.intent_summary_x_more, count));
    }
}
