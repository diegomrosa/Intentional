package com.diegomrosa.intentional;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class FieldListFragment extends ListFragment {
    private static final String CURRENT_SELECTED_FIELD_KEY = "currentSelectedField";

    private boolean mDualPane;
    private int mCurrentSelectedField = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewIntentActivity activity = (ViewIntentActivity) getActivity();

        // Populate list with our static array of titles.
        setListAdapter(activity.getAdapter());

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = activity.findViewById(R.id.fieldDetails);

        mDualPane = ((detailsFrame != null) && (detailsFrame.getVisibility() == View.VISIBLE));
        if (savedInstanceState != null) {

            // Restore last state for checked position.
            mCurrentSelectedField = savedInstanceState.getInt(CURRENT_SELECTED_FIELD_KEY, 0);
        }
        if (mDualPane) {

            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Make sure our UI is in the correct state.
            showDetails(mCurrentSelectedField);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_SELECTED_FIELD_KEY, mCurrentSelectedField);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    /**
     * Display the details of a selected field, either by displaying a fragment
     * in-place in the current UI, or starting a whole new activity in which it
     * is displayed.
     */
    void showDetails(int index) {
        mCurrentSelectedField = index;
        if (mDualPane) {

            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            Fragment fieldFragment = (Fragment) getFragmentManager().findFragmentById(R.id.fieldDetails);

            if ((fieldFragment == null) || (((FieldFragmentFace) fieldFragment).getIndex() != index)) {
                fieldFragment = FieldFragment.getFragment(index, getIntentExt());

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fieldDetails, fieldFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {

            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();

            intent.setClass(getActivity(), FieldDetailsActivity.class);
            intent.putExtra(Constants.INTENT_EXT_EXTRA, getIntentExt());
            intent.putExtra(Constants.FIELD_INDEX_EXTRA, Integer.valueOf(index));
            startActivity(intent);
        }
    }

    private ViewIntentActivity getViewIntentActivity() {
        return (ViewIntentActivity) getActivity();
    }

    private IntentAdapter getIntentAdapter() {
        return getViewIntentActivity().getAdapter();
    }

    private IntentExt getIntentExt() {
        return getViewIntentActivity().getIntentExt();
    }
}
