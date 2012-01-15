package com.diegomrosa.intentional;

import android.support.v4.app.ListFragment;

public abstract class ListFieldFragment extends ListFragment implements FieldFragmentFace {

    @Override
    public IntentExt getIntentExt() {
        return getArguments().getParcelable(Constants.INTENT_EXT_EXTRA);
    }
}
