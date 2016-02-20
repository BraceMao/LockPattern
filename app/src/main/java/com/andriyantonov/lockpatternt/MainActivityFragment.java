package com.andriyantonov.lockpatternt;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pro100svitlo.lockpattern.LPV_Interface;
import com.pro100svitlo.lockpattern.LockPatternView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LPV_Interface {

    private static LockPatternView mLockPatternView;
    public static TextView mText;
    public static TextView mText_2;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mText = (TextView)rootView.findViewById(R.id.text);
        mText_2 = (TextView)rootView.findViewById(R.id.text_2);

        mLockPatternView = (LockPatternView)rootView.findViewById(R.id.lpv);

        mLockPatternView.initLockPatternView(getActivity(), this);

        return rootView;
    }

    @Override
    public void patternExist(boolean patternExist) {
        if (patternExist){
            showToast("patternExist");
        } else {
            showToast("please set pass");
        }
    }

    @Override
    public void patternSet(String pass) {
        showToast("now Confirm PASS");
    }

    @Override
    public void patternConfirmedSuccess(boolean isPassSaving, String mainPass, String secondPass) {
        if (isPassSaving){
            showToast("first enter");
        } else {
            showToast("regular enter with pattern");
        }
        Intent i = new Intent(getActivity(), ActivityNext.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        getActivity().startActivity(i);
    }

    @Override
    public void patternConfirmFailed(String pass) {
        showToast("Confirm Failed");
    }

    @Override
    public void patternSubmitCanceled() {
        showToast("Submit Canceled");
    }


    private void showToast(String mess){
//        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }


    public static void setText(String text){
//        mText.setText(text);
    }

    public static void setText_2(String text){
//        mText_2.setText(text);
    }
}
