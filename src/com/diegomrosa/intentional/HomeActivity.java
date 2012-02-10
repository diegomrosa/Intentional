package com.diegomrosa.intentional;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends Activity implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ((TextView) findViewById(R.id.bookmarkText)).setOnClickListener(this);
        ((TextView) findViewById(R.id.aboutText)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.bookmarkText:
            startActivity(new Intent(this, BookmarksActivity.class));
            break;
        case R.id.aboutText:
            Mime.getInstance(this);
            break;
        default:
            Log.e(TAG, "Unexpected view on click: " + view.getId());
            break;
        }
    }
}
