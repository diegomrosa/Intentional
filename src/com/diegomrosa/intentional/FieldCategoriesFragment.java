package com.diegomrosa.intentional;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.Set;

public class FieldCategoriesFragment extends ListFieldFragment implements View.OnClickListener {
    private View view;

    @Override
    public int getIndex() {
        return Constants.CATEGORIES_IDX;
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
        view = inflater.inflate(R.layout.field_categories_fragment, null);
        Button addButton = (Button) view.findViewById(R.id.categoryAddButton);
        IntentExt intentExt = getIntentExt();
        Set<String> categories = (intentExt == null) ? null : intentExt.getCategories();

        if ((categories != null) && !categories.isEmpty()) {
            String[] categoriesArray = new String[categories.size()];
            int index = 0;

            for (String category : categories) {
                categoriesArray[index++] = category;
            }
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    R.layout.single_line_row, R.id.rowText, categoriesArray));
        }
        addButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        final AutoCompleteTextView input = new AutoCompleteTextView(getActivity());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        input.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Intents.CATEGORIES));
        alert.setTitle(getActivity().getString(R.string.categories_add_dialog_title));
        alert.setMessage(getActivity().getString(R.string.categories_add_dialog_title));
        alert.setView(input);
        alert.setPositiveButton(getActivity().getString(R.string.categories_add_dialog_ok),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                if ((value != null) && (value.length() > 0)) {
                    getIntentExt().addCategory(value);
                }
            }
        });
        alert.setNegativeButton(getActivity().getString(R.string.categories_add_dialog_ok),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }
}
