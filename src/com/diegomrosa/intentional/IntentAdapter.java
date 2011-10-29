package com.diegomrosa.intentional;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Set;

class IntentAdapter extends BaseAdapter {
    private static final int COUNT = 7;

    public static final int DATA_IDX = 0;
    public static final int TYPE_IDX = 1;
    public static final int ACTION_IDX = 2;
    public static final int CATEGORIES_IDX = 3;
    public static final int EXTRAS_IDX = 4;
    public static final int FLAGS_IDX = 5;
    public static final int COMPONENT_IDX = 6;

    private static final int[] TITLES = {
            R.string.intent_title_data,
            R.string.intent_title_type,
            R.string.intent_title_action,
            R.string.intent_title_categories,
            R.string.intent_title_extras,
            R.string.intent_title_flags,
            R.string.intent_title_component};

    private Context context;
    private IntentExt intentExt;

    public IntentAdapter(Context context, IntentExt intentExt) {
        this.context = context;
        this.intentExt = intentExt;
    }

    public IntentExt getIntentExt() {
        return intentExt;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public Object getItem(int i) {
        switch (i) {
        case DATA_IDX:
            return getDataString();
        case TYPE_IDX:
            return getTypeString();
        case ACTION_IDX:
            return getActionString();
        case CATEGORIES_IDX:
            return getCategoriesString();
        case EXTRAS_IDX:
            return getExtrasString();
        case FLAGS_IDX:
            return getFlagsString();
        case COMPONENT_IDX:
            return getComponentString();
        default:
            throw new IllegalArgumentException("Invalid intent field: " + i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View fieldView = view;

        if ((fieldView == null) || (fieldView.getId() != R.layout.field_row)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            fieldView = inflater.inflate(R.layout.field_row, null);
        }
        TextView titleView = (TextView) fieldView.findViewById(R.id.intentRowTitle);
        TextView valueView = (TextView) fieldView.findViewById(R.id.intentRowValue);

        titleView.setText(context.getString(TITLES[i]));
        valueView.setText((String) getItem(i));
        return fieldView;
    }

    public String getDataString() {
        Uri uri = intentExt.getData();

        return uri == null ? getEmptySummary() : uri.toString();
    }

    public String getTypeString() {
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
