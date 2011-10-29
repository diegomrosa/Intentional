package com.diegomrosa.intentional;

import android.support.v4.app.Fragment;

public abstract class FieldFragment extends Fragment {

    public abstract int getIndex();

    public IntentExt getIntentExt() {
        return getArguments().getParcelable(Constants.INTENT_EXT_EXTRA);
    }
}
