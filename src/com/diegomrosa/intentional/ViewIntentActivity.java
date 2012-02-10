package com.diegomrosa.intentional;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.util.Date;

public class ViewIntentActivity extends FragmentActivity {
    private static final String TAG = ViewIntentActivity.class.getSimpleName();

    private static final String INTENT_EXT_KEY = "intentExt";
    private static final int BUFFER_SIZE = 8192;
    private static final int DIALOG_ID_SAVE = 0;

    private IntentAdapter intentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.view_intent_activity);
        if (savedInstanceState != null) {
            IntentExt savedIntent = savedInstanceState.getParcelable(INTENT_EXT_KEY);

            if (savedIntent != null) {
                intentAdapter = new IntentAdapter(this, savedIntent);
            }
        } else {
            Intent originalIntent = getIntent();

            if (originalIntent != null) {
                intentAdapter = new IntentAdapter(this, new IntentExt(originalIntent));
            }
        }
    }

    public IntentAdapter getAdapter() {
        return intentAdapter;
    }

    public IntentExt getIntentExt() {
        return (intentAdapter == null) ? null : intentAdapter.getIntentExt();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (getAdapter() != null) {
            savedInstanceState.putParcelable(INTENT_EXT_KEY, getIntentExt());
        }
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
        MenuItem bookmarkItem = menu.findItem(R.id.menu_item_bookmark);

        if (getAdapter() != null) {
            forwardItem.setEnabled(true);
            bookmarkItem.setEnabled(true);
            if (getIntentExt().getDataStream() != null) {
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
            bookmarkItem.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_forward:
            startActivity(getIntentExt().getUpdatedIntent());
            break;
        case R.id.menu_item_share:
            startActivity(getIntentExt().getShareIntent());
            break;
        case R.id.menu_item_save:
            showDialog(DIALOG_ID_SAVE);
            break;
        case R.id.menu_item_bookmark:
            bookmarkIntent();
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
        edit.setText(initialDir.getAbsolutePath() + "/");
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
        Uri data = getIntentExt().getDataStream();
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

    private void bookmarkIntent() {
        Intent updatedIntent = getIntentExt().getUpdatedIntent();

        try {
            BookmarkDao dao = new BookmarkDao(this);
            String action = updatedIntent.getAction();
            ComponentName compName = updatedIntent.getComponent();
            Date creationDate = new Date();

            dao.create(new Bookmark(null, creationDate, updatedIntent));
            showLongToast(R.string.intent_bookmark_succeeded);
        } catch (Exception exc) {
            Log.e(TAG, "Error while bookmarking intent.", exc);
            showLongToast(R.string.intent_bookmark_failed);
        }
    }

    private void showLongToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}
