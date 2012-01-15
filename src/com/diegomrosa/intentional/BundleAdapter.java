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

class BundleAdapter extends BaseAdapter {
    private Context context;
    private Bundle bundle;
    private String[] keyArray;

    public BundleAdapter(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
        int index = 0;

        keyArray = new String[bundle.size()];
        for (String key : bundle.keySet()) {
            keyArray[index++] = key;
        }
    }

    @Override
    public int getCount() {
        return keyArray.length;
    }

    @Override
    public Object getItem(int i) {
        return bundle.get(keyArray[i]);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View extraView = view;

        if ((extraView == null) || (extraView.getId() != R.layout.double_line_row)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.double_line_row, null);
        }
        TextView titleView = (TextView) view.findViewById(R.id.rowTitle);
        TextView valueView = (TextView) view.findViewById(R.id.rowValue);

        titleView.setText(keyArray[i]);
        valueView.setText(getItem(i).toString());
        return view;
    }
}
