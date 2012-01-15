package com.diegomrosa.intentional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FieldFlagsFragment extends ListFieldFragment {
    private View view;

    @Override
    public int getIndex() {
        return Constants.FLAGS_IDX;
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
        view = inflater.inflate(R.layout.field_flags_fragment, null);
        IntentExt intentExt = getIntentExt();
        int flags = (intentExt == null) ? null : intentExt.getFlags();

        if (flags != 0) {
            setListAdapter(new FlagArrayAdapter(getActivity(), Flag.toArray(flags)));
        }
        return view;
    }
}
