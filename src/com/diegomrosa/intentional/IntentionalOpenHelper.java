package com.diegomrosa.intentional;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IntentionalOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = IntentionalOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "intentional";

    private static final String BOOKMARK_TABLE_NAME = "bookmark";
    private static final String ID_COLUMN = "id";
    private static final String DATA_COLUMN = "data";
    private static final String TYPE_COLUMN = "type";
    private static final String ACTION_COLUMN = "action";
    private static final String CATEGORIES_COLUMN = "categories";
    private static final String EXTRAS_COLUMN = "extras";
    private static final String FLAGS_COLUMN = "flags";
    private static final String COMPONENT_PACKAGE_COLUMN = "component_package";
    private static final String COMPONENT_CLASS_COLUMN = "component_class";

    private static final String BOOKMARK_TABLE_CREATE =
        "CREATE TABLE " + BOOKMARK_TABLE_NAME + " (\n"
        + "    " + ID_COLUMN + " INTEGER AUTOINCREMENT PRIMARY KEY, \n"
        + "    " + DATA_COLUMN + " TEXT,\n"
        + "    " + TYPE_COLUMN + " TEXT,\n"
        + "    " + ACTION_COLUMN + " TEXT,\n"
        + "    " + CATEGORIES_COLUMN + " TEXT\n"
        + "    " + EXTRAS_COLUMN + " TEXT\n"
        + "    " + FLAGS_COLUMN + " INTEGER\n"
        + "    " + COMPONENT_PACKAGE_COLUMN + " TEXT\n"
        + "    " + COMPONENT_CLASS_COLUMN + " TEXT);";

    public IntentionalOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(BOOKMARK_TABLE_CREATE);
        } catch (SQLException exc) {
            Log.e(TAG, "Error creating Intentional database.", exc);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // No upgrades for the moment.
    }
}
