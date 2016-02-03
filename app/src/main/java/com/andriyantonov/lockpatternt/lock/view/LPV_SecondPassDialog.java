package com.andriyantonov.lockpatternt.lock.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.andriyantonov.lockpatternt.R;

/**
 * Created by pro100svitlo on 1/29/16.
 */
public class LPV_SecondPassDialog {

    private Context mContext;
    private LockPatternView mLPV;
    private InputMethodManager mInputMethodManager;
    private AlertDialog mDialog;
    private EditText mSecondPass;
    private Button mPositiveButton;
    private int mMinPassLimit = 4;
    private int mMaxPassLimit = 20;
    private String mSecondPassStr = "";

    public LPV_SecondPassDialog(Context c, LockPatternView lp){
        mContext = c;
        mLPV = lp;

        createDialog();
    }

    public void show(){
        mDialog.show();
        if (mPositiveButton == null){
            mPositiveButton = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        }
        checkPositiveBtnEnable();
        mSecondPass.requestFocus();
        showSoftKeyboard();
    }

    public void hide(){
        mDialog.hide();
    }

    public void setMaxLengthLimit(int limit){
        mSecondPass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
    }

    private void createDialog(){
        String title = mContext.getString(R.string.lpv_ad_secondPass_title);
        String message = mContext.getString(R.string.lpv_ad_secondPass_message);
        String pos = mContext.getString(R.string.lpv_ad_secondPass_pos);
        String neg = mContext.getString(R.string.lpv_ad_secondPass_neg);

        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_second_pass, null);
        initSecondPass(v);

        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
        b.setTitle(title);
        b.setMessage(message);
        b.setView(v);
        b.setPositiveButton(pos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLPV.patternSetSuccessful(mSecondPassStr);
            }
        });
        b.setNegativeButton(neg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mDialog = b.create();

        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mLPV.secondPassDismissed();
            }
        });
    }

    private void initSecondPass(View v){
        mSecondPass = (EditText)v.findViewById(R.id.secondPass_et);
        int textColor = ContextCompat.getColor(mContext, android.R.color.primary_text_light_nodisable);
        mSecondPass.setTextColor(textColor);
        setMaxLengthLimit(mMaxPassLimit);
        setPassLengthListener();
    }

    private void setPassLengthListener(){
        mSecondPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mSecondPassStr = s.toString();
                checkPositiveBtnEnable();
            }
        });
    };

    private void checkPositiveBtnEnable(){
        int l = mSecondPassStr.length();
        if (l < mMinPassLimit){
            mPositiveButton.setEnabled(false);
        } else {
            mPositiveButton.setEnabled(true);
        }
    }

    private void showSoftKeyboard(){
        if (mInputMethodManager == null){
            mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mInputMethodManager.showSoftInput(mSecondPass, 0);
            }
        }, 10);
    }
}
