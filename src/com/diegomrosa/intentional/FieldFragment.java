package com.diegomrosa.intentional;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class FieldFragment extends Fragment implements FieldFragmentFace {

    @Override
    public IntentExt getIntentExt() {
        return getArguments().getParcelable(Constants.INTENT_EXT_EXTRA);
    }

    public static Fragment getFragment(int index, IntentExt intentExt) {
        Bundle arguments = new Bundle();

        arguments.putParcelable(Constants.INTENT_EXT_EXTRA, intentExt);
        return getFragment(index, arguments);
    }

    public static Fragment getFragment(int index, Bundle arguments) {
        Fragment fragment = null;

        switch (index) {
        case Constants.DATA_IDX:
            fragment = new FieldDataFragment();
            break;
        case Constants.TYPE_IDX:
            fragment = new FieldTypeFragment();
            break;
        case Constants.ACTION_IDX:
            fragment = new FieldActionFragment();
            break;
        case Constants.CATEGORIES_IDX:
            fragment = new FieldCategoriesFragment();
            break;
        case Constants.EXTRAS_IDX:
            fragment = new FieldExtrasFragment();
            break;
        case Constants.FLAGS_IDX:
            fragment = new FieldFlagsFragment();
            break;
        case Constants.COMPONENT_IDX:
            fragment = new FieldComponentFragment();
            break;
        default:
            throw new IllegalArgumentException("Illegal field index: " + index);
        }
        fragment.setArguments(arguments);
        return fragment;
    }
}
