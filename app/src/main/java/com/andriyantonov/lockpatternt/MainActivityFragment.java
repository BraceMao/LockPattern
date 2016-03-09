package com.andriyantonov.lockpatternt;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pro100svitlo.lockpattern.LockPatternView;
import com.pro100svitlo.lockpattern.interfaces.MainLPVInterface;
import com.pro100svitlo.lockpattern.interfaces.DialogLPVInterface;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainLPVInterface, DialogLPVInterface {

    public MainActivityFragment() {
    }

    private String mMessage;

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
        if (isExist){
            mMessage = "pattern exist";
        } else {
            mMessage = "pattern doesn't exist";
        }
        showToast(mMessage);
    }

    @Override
    public void patternConfirmed(boolean isFirstEnter, String patternPass) {
        showToast("pattern confirmed");
        Intent i = new Intent(getActivity(), ActivityNext.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        getActivity().startActivity(i);
    }

    @Override
    public void patternFailed() {
        showToast("pattern failed");
    }

    @Override
    public void setPatternCanceled() {
        showToast("set pattern canceled");
    }

    @Override
    public void setSecondPassCanceled() {
        showToast("set second pass canceled");
    }

    @Override
    public void secondPassCreated(String secondPass) {
        showToast("second pass created");
    }

    @Override
    public void passRestoreConfirmed() {
        showToast("pass restore confirmed");
    }

    @Override
    public void passRestoreFailed() {
        showToast("pass restore failed");
    }
}
