package com.diegomrosa.intentional;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.Set;

public class FieldCategoriesFragment extends ListFieldFragment implements View.OnClickListener {

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
        View view = inflater.inflate(R.layout.field_categories_fragment, null);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        Button addButton = (Button) view.findViewById(R.id.categoryAddButton);

        refreshCategoriesAdapter();
        addButton.setOnClickListener(this);
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onClick(View view) {
        final AutoCompleteTextView input = new AutoCompleteTextView(getActivity());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        input.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Intents.CATEGORIES));
        alert.setTitle(getActivity().getString(R.string.categories_add_dialog_title));
        alert.setMessage(null);
        alert.setView(input);
        alert.setPositiveButton(getActivity().getString(R.string.categories_add_dialog_ok),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                if ((value != null) && (value.length() > 0)) {
                    getIntentExt().addCategory(value);
                    refreshCategoriesAdapter();
                    notifyIntentChanged();
                }
            }
        });
        alert.setNegativeButton(getActivity().getString(R.string.categories_add_dialog_cancel),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

            menu.setHeaderTitle((String) getListAdapter().getItem(info.position));
            menu.add(R.string.categories_delete_item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String category = (String) getListAdapter().getItem(info.position);

        getIntentExt().getCategories().remove(category);
        refreshCategoriesAdapter();
        notifyIntentChanged();
        return true;
    }

    private void refreshCategoriesAdapter() {
        IntentExt intentExt = getIntentExt();
        Set<String> categories = (intentExt == null) ? null : intentExt.getCategories();
        String[] categoriesArray = null;

        if ((categories == null) || categories.isEmpty()) {
            categoriesArray = new String[0];
        } else {
            int index = 0;

            categoriesArray = new String[categories.size()];
            for (String category : categories) {
                categoriesArray[index++] = category;
            }
        }
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                    R.layout.single_line_row, R.id.rowText, categoriesArray));
    }
}
