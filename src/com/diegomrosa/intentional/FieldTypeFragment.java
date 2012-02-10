package com.diegomrosa.intentional;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.List;

public class FieldTypeFragment extends FieldFragment {
    private View view;

    @Override
    public int getIndex() {
        return Constants.TYPE_IDX;
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
        view = inflater.inflate(R.layout.field_type_fragment, null);
        List<Mime.Type> mimeTypes = Mime.getInstance(getActivity()).getTypes();
        ArrayAdapter<Mime.Type> adapter = new ArrayAdapter<Mime.Type>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, mimeTypes);
        AutoCompleteTextView valueView = (AutoCompleteTextView) view.findViewById(R.id.typeFieldValue);
        IntentExt intentExt = getIntentExt();
        String type = (intentExt == null) ? null : intentExt.getType();

        valueView.setAdapter(adapter);
        valueView.setText(type);
        valueView.addTextChangedListener(new TypeWatcher());
        return view;
    }

    private class TypeWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence text, int a, int b, int c) {}

        @Override
        public void onTextChanged(CharSequence text, int a, int b, int c) {
            getIntentExt().setType(text.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
