package com.diegomrosa.intentional;

import android.app.Activity;
import android.support.v4.app.ListFragment;

public abstract class ListFieldFragment extends ListFragment implements FieldFragmentFace {

    @Override
    public IntentExt getIntentExt() {
        return getArguments().getParcelable(Constants.INTENT_EXT_EXTRA);
    }

    @Override
    public void notifyIntentChanged() {
        Activity activity = getActivity();

        /*
         * The fragment activity can be of two types: ViewIntentActivity
         * and FieldDetailsActivity. In case it is a ViewIntentActivity,
         * it means we are also showing the list of fields and need to
         * notify that the intent has changed.
         */
        if (activity instanceof ViewIntentActivity) {
            ViewIntentActivity viewActivity = (ViewIntentActivity) activity;

            viewActivity.getAdapter().notifyDataSetChanged();
        }
    }
}
