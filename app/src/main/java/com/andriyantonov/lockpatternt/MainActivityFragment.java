package com.andriyantonov.lockpatternt;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pro100svitlo.lockpattern.LockPatternView;
import com.pro100svitlo.lockpattern.interfaces.LPV_Interface;
import com.pro100svitlo.lockpattern.interfaces.SecondPassDialogInterface;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LPV_Interface, SecondPassDialogInterface {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        LockPatternView mLockPatternView = (LockPatternView) rootView.findViewById(R.id.lpv);

        mLockPatternView.initLockPatternView(getActivity(), this, this);

        return rootView;
    }


    private void showToast(String mess){
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void isPatternExist(boolean isExist) {
        showToast(String.valueOf(isExist));
    }

    @Override
    public void patternConfirmed(boolean isFirstEnter, String patternPass) {
        showToast(String.valueOf(isFirstEnter) + ",\n" + patternPass);
        Intent i = new Intent(getActivity(), ActivityNext.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        getActivity().startActivity(i);
    }

    @Override
    public void patternFailed() {
        showToast("patternFailed");
    }

    @Override
    public void setPatternCanceled() {
        showToast("setPatternCanceled");
    }

    @Override
    public void setSecondPassCanceled() {
        showToast("setSecondPassCanceled");
    }

    @Override
    public void secondPassCreated(String secondPass) {
        showToast("secondPassCreated");
    }

    @Override
    public void secondPassResetConfirmed() {
        showToast("secondPassResetConfirmed");
    }

    @Override
    public void secondPassResetFailed() {
        showToast("secondPassResetFailed");
    }
}
