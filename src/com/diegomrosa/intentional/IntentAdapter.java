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
import java.util.List;
import java.util.Set;

class IntentAdapter extends BaseAdapter {
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
        return Constants.FIELD_COUNT;
    }

    @Override
    public Object getItem(int i) {
        switch (i) {
        case Constants.DATA_IDX:
            return getDataString();
        case Constants.TYPE_IDX:
            return getTypeString();
        case Constants.ACTION_IDX:
            return getActionString();
        case Constants.CATEGORIES_IDX:
            return getCategoriesString();
        case Constants.EXTRAS_IDX:
            return getExtrasString();
        case Constants.FLAGS_IDX:
            return getFlagsString();
        case Constants.COMPONENT_IDX:
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

        if ((fieldView == null) || (fieldView.getId() != R.layout.double_line_row)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            fieldView = inflater.inflate(R.layout.double_line_row, null);
        }
        TextView titleView = (TextView) fieldView.findViewById(R.id.rowTitle);
        TextView valueView = (TextView) fieldView.findViewById(R.id.rowValue);

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
        Flag[] flagArray = Flag.toArray(flags);
        StringBuilder builder = new StringBuilder(flagArray[0].toString());

        if (flagArray.length > 1) {
            appendMore(builder, flagArray.length - 1);
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

    private void appendMore(StringBuilder builder, Integer count) {
        builder.append(' ').append(context.getString(R.string.intent_summary_more, count));
    }
}
