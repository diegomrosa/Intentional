package com.diegomrosa.intentional;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.Set;

public class BookmarksActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO This should be into an AsyncTask.
        BookmarkDao dao = new BookmarkDao(this);
        List<Bookmark> bookmarkList = dao.readAll();

        setListAdapter(new BookmarkAdapter(this, bookmarkList));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bookmark bookmark = (Bookmark) getListAdapter().getItem(position);
        Intent intent = bookmark.getIntent();

        intent.setClass(this, ViewIntentActivity.class);
        startActivity(intent);
    }
}
