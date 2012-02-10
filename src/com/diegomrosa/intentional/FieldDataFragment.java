package com.diegomrosa.intentional;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FieldDataFragment extends FieldFragment {
    private static final String TAG = FieldDataFragment.class.getSimpleName();

    private View view;

    @Override
    public int getIndex() {
        return Constants.DATA_IDX;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {

            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        view = inflater.inflate(R.layout.field_data_fragment, null);
        EditText valueEdit = (EditText) view.findViewById(R.id.dataFieldValue);
        IntentExt intentExt = getIntentExt();
        Uri data = (intentExt == null) ? null : intentExt.getData();
        String dataString = (data == null) ? null : data.toString();

        valueEdit.setText(dataString);
        valueEdit.addTextChangedListener(new DataWatcher());
        return view;
    }


    private class DataWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence text, int a, int b, int c) {}

        @Override
        public void onTextChanged(CharSequence text, int a, int b, int c) {
            Uri newData = Uri.parse(text.toString());

            if (newData != null) {
                getIntentExt().setData(newData);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
