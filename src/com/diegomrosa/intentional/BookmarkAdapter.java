package com.diegomrosa.intentional;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class BookmarkAdapter extends ArrayAdapter<Bookmark> {

    public BookmarkAdapter(Context context, List<Bookmark> bookmarkList) {
        super(context, R.layout.single_line_row, R.id.rowTitle, bookmarkList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View extraView = view;

        if ((extraView == null) || (extraView.getId() != R.layout.double_line_row)) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.triple_line_row, null);
        }
        Bookmark bookmark = getItem(i);
        TextView upperView = (TextView) view.findViewById(R.id.rowUpperValue);
        TextView titleView = (TextView) view.findViewById(R.id.rowTitle);
        TextView lowerView = (TextView) view.findViewById(R.id.rowLowerValue);

        upperView.setText(bookmark.getDateString());
        titleView.setText(bookmark.toString());
        lowerView.setText(bookmark.getDataString());
        return view;
    }
}
