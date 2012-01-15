package com.diegomrosa.intentional;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class FieldDetailsActivity extends FragmentActivity {
    private static final String TAG = FieldDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {

            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }
        if (savedInstanceState == null) {

            // During initial setup, plug in the corresponding fragment.
            Bundle extras = getIntent().getExtras();
            int index = extras.getInt(Constants.FIELD_INDEX_EXTRA, -1);
            Fragment fieldFragment = FieldFragment.getFragment(index, extras);

            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fieldFragment).commit();
        }
    }
}
