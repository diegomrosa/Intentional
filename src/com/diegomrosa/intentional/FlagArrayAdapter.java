package com.diegomrosa.intentional;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class FlagArrayAdapter extends BaseAdapter {
    private Context mContext;
    private Flag[] mFlagArray;

    public FlagArrayAdapter(Context context, Flag[] flagArray) {
        mContext = context;
        mFlagArray = flagArray;
    }

    @Override
    public int getCount() {
        return mFlagArray.length;
    }

    @Override
    public Object getItem(int i) {
        return mFlagArray[i].toHex();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View extraView = view;

        if ((extraView == null) || (extraView.getId() != R.layout.double_line_row)) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.double_line_row, null);
        }
        TextView titleView = (TextView) view.findViewById(R.id.rowTitle);
        TextView valueView = (TextView) view.findViewById(R.id.rowValue);

        titleView.setText(mFlagArray[i].toString());
        valueView.setText(mFlagArray[i].toHex());
        return view;
    }
}
