package com.diegomrosa.intentional;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FieldComponentFragment extends FieldFragment {
    private View view;

    @Override
    public int getIndex() {
        return Constants.COMPONENT_IDX;
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
        view = inflater.inflate(R.layout.field_component_fragment, null);
        EditText packageEdit = (EditText) view.findViewById(R.id.componentPackageEdit);
        EditText classEdit = (EditText) view.findViewById(R.id.componentClassEdit);
        IntentExt intentExt = getIntentExt();
        ComponentName component = (intentExt == null) ? null : intentExt.getComponent();
        String packageString = (component == null) ? null : component.getPackageName();
        String classString = (component == null) ? null : component.getClassName();

        packageEdit.setText(packageString);
        classEdit.setText(classString);
        return view;
    }
}
