package com.diegomrosa.intentional;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class Database extends SQLiteOpenHelper {
    private static final String TAG = Database.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "intentional";

    public static final class TBookmark {
        public static final String TABLE_NAME = "bookmark";
        public static final String ID_COLUMN = "id";
        public static final String NAME_COLUMN = "name";
        public static final String CREATION_DATE_COLUMN = "creation_date";
        public static final String DATA_COLUMN = "data";
        public static final String TYPE_COLUMN = "type";
        public static final String ACTION_COLUMN = "action";
        public static final String CATEGORIES_COLUMN = "categories";
        public static final String EXTRAS_COLUMN = "extras";
        public static final String FLAGS_COLUMN = "flags";
        public static final String COMPONENT_PACKAGE_COLUMN = "component_package";
        public static final String COMPONENT_CLASS_COLUMN = "component_class";

        public static final String[] ALL_COLUMNS = {
            ID_COLUMN,
            NAME_COLUMN,
            CREATION_DATE_COLUMN,
            DATA_COLUMN,
            TYPE_COLUMN,
            ACTION_COLUMN,
            CATEGORIES_COLUMN,
            EXTRAS_COLUMN,
            FLAGS_COLUMN,
            COMPONENT_PACKAGE_COLUMN,
            COMPONENT_CLASS_COLUMN
        };

        private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (\n"
            + "    " + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, \n"
            + "    " + NAME_COLUMN + " TEXT,\n"
            + "    " + CREATION_DATE_COLUMN + " INTEGER,\n"
            + "    " + DATA_COLUMN + " TEXT,\n"
            + "    " + TYPE_COLUMN + " TEXT,\n"
            + "    " + ACTION_COLUMN + " TEXT,\n"
            + "    " + CATEGORIES_COLUMN + " TEXT,\n"
            + "    " + EXTRAS_COLUMN + " TEXT,\n"
            + "    " + FLAGS_COLUMN + " INTEGER,\n"
            + "    " + COMPONENT_PACKAGE_COLUMN + " TEXT,\n"
            + "    " + COMPONENT_CLASS_COLUMN + " TEXT);";
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Will create bookmark database: " + TBookmark.TABLE_CREATE);
        try {
            db.execSQL(TBookmark.TABLE_CREATE);
            Log.d(TAG, "Bookmark table created successfully.");
        } catch (SQLException exc) {
            Log.e(TAG, "Error creating Intentional database.", exc);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // No upgrades for the moment.
    }
}
