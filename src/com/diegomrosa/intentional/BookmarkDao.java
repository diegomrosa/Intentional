package com.diegomrosa.intentional;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;
import com.diegomrosa.intentional.Database.TBookmark;

class BookmarkDao {
    private static final String TAG = BookmarkDao.class.getSimpleName();

    private static final String ITEM_SEPARATOR = "; ";
    private static final String EXTRA_SEPARATOR = ", ";

    private enum ExtraType {
        STRING,
        URI,
        BOOLEAN,
        BYTE,
        SHORT,
        CHAR,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE};

    private Context context;

    public BookmarkDao(Context context) {
        this.context = context;
    }

    public void create(Bookmark bookmark) throws SQLException {
        Database helper = new Database(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = toContentValues(bookmark);
        long newId = db.insertOrThrow(Database.TBookmark.TABLE_NAME, null, values);

        if (newId == -1) {
            throw new SQLException("Error while inserting bookmark.");
        }
        bookmark.setId(newId);
    }

    public List<Bookmark> readAll() throws SQLException {
        Database helper = new Database(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Bookmark> all = new ArrayList<Bookmark>();
        Cursor cursor = db.query(TBookmark.TABLE_NAME, TBookmark.ALL_COLUMNS, null, null, null, null, null);

        if (cursor == null) {
            throw new SQLException("Error reading all bookmarks.");
        }
        try {
            while (cursor.moveToNext()) {
                all.add(toBookmark(cursor));
            }
        } finally {
            cursor.close();
        }
        return all;
    }

    private static ContentValues toContentValues(Bookmark bookmark) {
        ContentValues values = new ContentValues();
        Intent intent = bookmark.getIntent();
        Long now = System.currentTimeMillis();
        String packageName = null;
        String className = null;
        ComponentName component = intent.getComponent();

        if (component != null) {
            packageName = component.getPackageName();
            className = component.getClassName();
        }
        values.put(TBookmark.NAME_COLUMN, bookmark.getName());
        values.put(TBookmark.CREATION_DATE_COLUMN, now);
        values.put(TBookmark.DATA_COLUMN, intent.getDataString());
        values.put(TBookmark.TYPE_COLUMN, intent.getType());
        values.put(TBookmark.ACTION_COLUMN, intent.getAction());
        values.put(TBookmark.CATEGORIES_COLUMN, toDbString(intent.getCategories()));
        values.put(TBookmark.EXTRAS_COLUMN, toDbString(intent.getExtras()));
        values.put(TBookmark.FLAGS_COLUMN, intent.getFlags());
        values.put(TBookmark.COMPONENT_PACKAGE_COLUMN, packageName);
        values.put(TBookmark.COMPONENT_CLASS_COLUMN, className);
        return values;
    }

    private static Bookmark toBookmark(Cursor cursor) {
        Long id = getLong(cursor, TBookmark.ID_COLUMN);
        String name = getString(cursor, TBookmark.NAME_COLUMN);
        Date creationDate = getDate(cursor, TBookmark.CREATION_DATE_COLUMN);
        Intent intent = new Intent();
        String data = getString(cursor, TBookmark.DATA_COLUMN);
        String type = getString(cursor, TBookmark.TYPE_COLUMN);
        String action = getString(cursor, TBookmark.ACTION_COLUMN);
        String[] categories = getCategories(cursor, TBookmark.CATEGORIES_COLUMN);
        Bundle extras = getExtras(cursor, TBookmark.EXTRAS_COLUMN);
        int flags = getInt(cursor, TBookmark.FLAGS_COLUMN);
        String packageName = getString(cursor, TBookmark.COMPONENT_PACKAGE_COLUMN);
        String className = getString(cursor, TBookmark.COMPONENT_CLASS_COLUMN);

        if (data != null) {
            intent.setData(Uri.parse(data));
        }
        if (type != null) {
            intent.setType(type);
        }
        if (action != null) {
            intent.setAction(action);
        }
        for (String category : categories) {
            intent.addCategory(category);
        }
        intent.putExtras(extras);
        intent.addFlags(flags);
        if ((packageName != null) && (className != null)) {
            intent.setComponent(new ComponentName(packageName, className));
        }
        return new Bookmark(id, name, creationDate, intent);
    }

    private static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static Date getDate(Cursor cursor, String columnName) {
        return new Date(cursor.getLong(cursor.getColumnIndex(columnName)));
    }

    private static String[] getCategories(Cursor cursor, String columnName) {
        String categoriesString = getString(cursor, columnName);

        if ((categoriesString == null) || (categoriesString.length() == 0)) {
            return new String[0];
        }
        return categoriesString.split(ITEM_SEPARATOR);
    }

    private static String toDbString(Set<String> categories) {
        if ((categories == null) || categories.isEmpty()) {
            return null;
        }
        Iterator<String> categoryIterator = categories.iterator();
        StringBuilder builder = new StringBuilder(categoryIterator.next());

        while (categoryIterator.hasNext()) {
            builder.append(ITEM_SEPARATOR).append(categoryIterator.next());
        }
        return builder.toString();
    }

    private static Bundle getExtras(Cursor cursor, String columnName) {
        String extrasString = getString(cursor, columnName);
        Bundle bundle = new Bundle();

        if ((extrasString == null) || (extrasString.length() == 0)) {
            return bundle;
        }
        Log.d(TAG, extrasString);
        String[] extraItems = extrasString.split(ITEM_SEPARATOR);
        for (String extraItem : extraItems) {
            String[] extraTriple = extraItem.split(EXTRA_SEPARATOR);
            Log.d(TAG, "Triple: " + extraTriple[0] + ", " + extraTriple[1] + ", " + extraTriple[2]);
            String key = extraTriple[0];
            String typeString = extraTriple[1];
            ExtraType type = ExtraType.valueOf(typeString);
            String valueString = extraTriple[2];

            switch (type) {
            case STRING:
                bundle.putString(key, valueString);
                break;
            case URI:
                bundle.putParcelable(key, Uri.parse(valueString));
                break;
            case BOOLEAN:
                bundle.putBoolean(key, Boolean.valueOf(valueString));
                break;
            case BYTE:
                bundle.putByte(key, Byte.valueOf(valueString));
                break;
            case SHORT:
                bundle.putShort(key, Short.valueOf(valueString));
                break;
            case CHAR:
                bundle.putChar(key, valueString.charAt(0));
                break;
            case INTEGER:
                bundle.putInt(key, Integer.valueOf(valueString));
                break;
            case LONG:
                bundle.putLong(key, Long.valueOf(valueString));
                break;
            case FLOAT:
                bundle.putFloat(key, Float.valueOf(valueString));
                break;
            case DOUBLE:
                bundle.putDouble(key, Double.valueOf(valueString));
                break;
            default:
                Log.e(TAG, "Unexpected type: " + typeString);
                break;
            }
        }
        return bundle;
    }

    private static String toDbString(Bundle extras) {
        if ((extras == null) || extras.isEmpty()) {
            return null;
        }
        Iterator<String> keyIterator = extras.keySet().iterator();
        StringBuilder builder = new StringBuilder();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = extras.get(key);

            if ((value instanceof String) || (value instanceof Uri)
                    || (value instanceof Boolean) || (value instanceof Byte)
                    || (value instanceof Short) || (value instanceof Character)
                    || (value instanceof Integer) || (value instanceof Long)
                    || (value instanceof Float) || (value instanceof Double)) {
                if (builder.length() > 0) {
                    builder.append(ITEM_SEPARATOR);
                }
                builder.append(key);
                if (value instanceof String) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.STRING);
                    builder.append(EXTRA_SEPARATOR).append(value);
                } else if (value instanceof Uri) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.URI);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                } else if (value instanceof Boolean) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.BOOLEAN);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                } else if (value instanceof Byte) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.BYTE);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                } else if (value instanceof Short) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.SHORT);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                } else if (value instanceof Character) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.CHAR);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                } else if (value instanceof Integer) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.INTEGER);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                } else if (value instanceof Long) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.LONG);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                } else if (value instanceof Float) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.FLOAT);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                } else if (value instanceof Double) {
                    builder.append(EXTRA_SEPARATOR).append(ExtraType.DOUBLE);
                    builder.append(EXTRA_SEPARATOR).append(value.toString());
                }
            } else {
                Log.d(TAG, "This type is not supported and will be skipped.");
            }
        }
        return builder.toString();
    }
}
