package com.diegomrosa.intentional;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;

public class ViewIntentActivity extends PreferenceActivity {
    private static final String TAG = ViewIntentActivity.class.getSimpleName();

    private static final int BUFFER_SIZE = 8192;

    private static final String PREF_KEY_DATA = "data";
    private static final String PREF_KEY_MIME_TYPE = "mimeType";
    private static final String PREF_KEY_ACTION = "action";
    private static final String PREF_KEY_CATEGORIES = "categories";
    private static final String PREF_KEY_EXTRAS = "extras";
    private static final String PREF_KEY_FLAGS = "flags";
    private static final String PREF_KEY_COMPONENT = "component";

    private static final int DIALOG_ID_SAVE = 0;

    private static final int EDIT_DATA_REQUEST = 0;

    private IntentExt intentExt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addPreferencesFromResource(R.xml.intent_preferences);
        Intent originalIntent = getIntent();

        Log.d(TAG, "act created.");
        if (originalIntent != null) {
            intentExt = new IntentExt(originalIntent);
            populateFields();
        }
    }

    private void populateFields() {
        DisplayableIntentExt die = new DisplayableIntentExt(this, intentExt);
        PreferenceScreen screen = getPreferenceScreen();

        screen.findPreference(PREF_KEY_DATA).setSummary(die.getUriString());
        screen.findPreference(PREF_KEY_MIME_TYPE).setSummary(die.getMimeTypeString());
        screen.findPreference(PREF_KEY_ACTION).setSummary(die.getActionString());
        screen.findPreference(PREF_KEY_CATEGORIES).setSummary(die.getCategoriesString());
        screen.findPreference(PREF_KEY_EXTRAS).setSummary(die.getExtrasString());
        screen.findPreference(PREF_KEY_FLAGS).setSummary(die.getFlagsString());
        screen.findPreference(PREF_KEY_COMPONENT).setSummary(die.getComponentString());
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference pref) {
        Log.d(TAG, "List item clicked.");
        if (PREF_KEY_DATA.equals(pref.getKey())) {
            startActivityForResult(new Intent(
                    ViewIntentActivity.this, ViewDataActivity.class), EDIT_DATA_REQUEST);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.intent_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem forwardItem = menu.findItem(R.id.menu_item_forward);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        MenuItem saveItem = menu.findItem(R.id.menu_item_save);

        if (intentExt != null) {
            forwardItem.setEnabled(true);
            if (intentExt.getDataStream() != null) {
                shareItem.setEnabled(true);
                saveItem.setEnabled(true);
            } else {
                shareItem.setEnabled(false);
                saveItem.setEnabled(false);
            }
        } else {
            forwardItem.setEnabled(false);
            shareItem.setEnabled(false);
            saveItem.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_forward:
            startActivity(intentExt.getUpdatedIntent());
            break;
        case R.id.menu_item_share:
            startActivity(intentExt.getShareIntent());
            break;
        case R.id.menu_item_save:
            showDialog(DIALOG_ID_SAVE);
            break;
        default:
            Log.e(TAG, "Unexpected option item id: " + item.getItemId());
            break;
        }
        return true;
    }

    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_ID_SAVE:
            return createSaveDialog();
        default:
            Log.e(TAG, "Unexpected dialog id: " + id);
            return null;
        }
    }

    private Dialog createSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText edit = new EditText(this);
        String storageState = Environment.getExternalStorageState();
        File initialDir = Environment.getRootDirectory();

        builder.setTitle(getString(R.string.dialog_save_title));
        builder.setMessage(getString(R.string.dialog_save_message));
        if (Environment.MEDIA_MOUNTED.equals(storageState)) {
            initialDir = Environment.getExternalStorageDirectory();
        }
        edit.setText(initialDir.getAbsolutePath());
        builder.setView(edit);
        builder.setNegativeButton(R.string.dialog_save_cancel_button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogFace, int whichButton) {
                dialogFace.dismiss();
            }
        });
        builder.setPositiveButton(R.string.dialog_save_save_button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogFace, int whichButton) {
                String path = edit.getText().toString();

                if (TextUtils.isEmpty(path)) {
                    showLongToast(R.string.dialog_save_empty_path_message);
                } else {
                    try {
                        saveDataTo(path);
                        showLongToast(R.string.dialog_save_success_message);
                    } catch (FileNotFoundException exc) {
                        showLongToast(R.string.dialog_save_not_found_message);
                    } catch (IOException exc) {
                        showLongToast(R.string.dialog_save_io_error_message);
                    }
                    dialogFace.dismiss();
                }
            }
        });
        return builder.create();
    }

    private void saveDataTo(String path) throws FileNotFoundException, IOException {
        ContentResolver cr = getContentResolver();
        Uri data = intentExt.getDataStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        InputStream is = cr.openInputStream(data);
        OutputStream os = null;

        try {
            os = new FileOutputStream(path);
            bytesRead = is.read(buffer);
            while (bytesRead > 0) {
                os.write(buffer, 0, bytesRead);
                bytesRead = is.read(buffer);
            }
            os.flush();
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    private void showLongToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}
