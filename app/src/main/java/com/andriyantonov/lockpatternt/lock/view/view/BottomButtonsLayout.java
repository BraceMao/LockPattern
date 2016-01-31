package com.andriyantonov.lockpatternt.lock.view.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andriyantonov.lockpatternt.R;
import com.andriyantonov.lockpatternt.lock.view.LPV_Interface;
import com.andriyantonov.lockpatternt.lock.view.LPV_SecondPassDialog;
import com.andriyantonov.lockpatternt.lock.view.LPV_SharedPreferences;

/**
 * Created by pro100svitlo on 1/29/16.
 */
public class BottomButtonsLayout extends LinearLayout {

    private Context mContext;
    private Activity mActivity;
    private BottomButtonsLayout mBottomButtonsLayout;
    private LockPatternView mLPV;
    private LPV_Interface mInterface;
    private LPV_SecondPassDialog mSecondPassDialog;
    private LPV_SharedPreferences mLPV_ShPrefs;
    private Button mBtnCancel, mBtnConfirm;
    private String mStrCancel, mStrConfirm, mStrRepeat;
    private int mMargin =  16;

    public BottomButtonsLayout(Context context) {
        super(context);
        mContext = context;
        onCreate();
    }

    public BottomButtonsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        onCreate();
    }

    public BottomButtonsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        onCreate();
    }

    public BottomButtonsLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        onCreate();
    }

    public void setAdditions(Activity a, LockPatternView lpv, LPV_Interface interf){
        mActivity = a;
        mLPV = lpv;
        mInterface = interf;
        setLayoutParams();
    }

    public void secondPassConfirmed(String secondPass){
        String confirmPass = mLPV.getPassConfirm();
        mInterface.patternConfirmedSuccess(true, confirmPass, secondPass);
        savePass(confirmPass, secondPass);
        mLPV.setCurrentLockStatus(mLPV.CHECK_PATTERN);
        setBottomButtonsVisibility(false);
    }

    public void secondPassDismissed(){
        onCancelPatternListener.onClick(mBtnCancel);
    }

    public void setBottomButtonsVisibility(boolean visible){
        if (visible){
            mBtnCancel.setVisibility(VISIBLE);
            mBtnConfirm.setVisibility(VISIBLE);
            mLPV.mMainPatternView.setAllItemsEnable(false);
        } else {
            mBtnCancel.setVisibility(GONE);
            mBtnConfirm.setVisibility(GONE);
            mLPV.mMainPatternView.setAllItemsEnable(true);
        }
    }

    private void onCreate(){
        initButtonsText();

        mBottomButtonsLayout = this;

        setOrientation(HORIZONTAL);

        addView(addCancelButton());
        addView(addConfirmButton());
    }

    private void initButtonsText(){
        mStrCancel = mContext.getString(R.string.lpv_btn_actionCancel);
        mStrConfirm = mContext.getString(R.string.lpv_btn_actionConfirm);
        mStrRepeat = mContext.getString(R.string.lpv_btn_actionRepeat);
    }

    private void setLayoutParams(){
        int width = mLPV.mMainPatternView.getMainPatternViewSize();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.setMargins(mMargin, mMargin, mMargin, mMargin * 2);
        setLayoutParams(lp);
    }

    private Button addCancelButton(){
        mBtnCancel = new Button(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.setMargins(0, 0, mMargin, 0);
        mBtnCancel.setLayoutParams(lp);

        mBtnCancel.setText(mStrCancel);

        mBtnCancel.setOnClickListener(onCancelPatternListener);

        mBtnCancel.setVisibility(GONE);
        return mBtnCancel;
    }

    private Button addConfirmButton(){
        mBtnConfirm = new Button(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.setMargins(mMargin, 0, 0, 0);
        mBtnConfirm.setLayoutParams(lp);
        mBtnConfirm.setText(mStrRepeat);

        mBtnConfirm.setOnClickListener(onConfirmPatternListener);

        mBtnConfirm.setVisibility(GONE);
        return mBtnConfirm;
    }

    private OnClickListener onCancelPatternListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mInterface.patternSubmitCanceled();
            mLPV.setCurrentLockStatus(mLPV.SET_PATTERN);
            mLPV.mStatusTitle.setTitleText(mLPV.mStatusTitle.mStatus_SetNewPatter);
            setBottomButtonsVisibility(false);
            mLPV.mMainPatternView.setAllItemsEnable(true);
            setBottomButtonsVisibility(false);
            mLPV.clearPathBitmap();
            mBtnConfirm.setText(mStrRepeat);
        }
    };

    private OnClickListener onConfirmPatternListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int currentStatus = mLPV.getCurrentLockStatus();
            if (currentStatus == mLPV.SET_PATTERN){
                mLPV.setCurrentLockStatus(mLPV.CONFIRM_PATTERN);
                String pass = mLPV.getSetPass();
                mInterface.patternSet(pass);
                mBtnConfirm.setText(mStrConfirm);
                mLPV.mStatusTitle.setTitleText(mLPV.mStatusTitle.mStatus_ConfirmPattern);
            } else if (currentStatus == mLPV.CONFIRM_PATTERN){
                mLPV.mStatusTitle.setTitleText(mLPV.mStatusTitle.mStatus_PatternConfirmed);
                if (mSecondPassDialog == null){
                    mSecondPassDialog = new LPV_SecondPassDialog(mActivity, mBottomButtonsLayout);
                }
                mSecondPassDialog.show();
            }
            setBottomButtonsVisibility(false);
            mLPV.clearPathBitmap();
        }
    };

    private void savePass(String confirmPass, String secondPass){
        if (mLPV_ShPrefs == null){
            mLPV_ShPrefs = mLPV.getLPV_ShPrefs();
        }
        mLPV_ShPrefs.saveMainPass(confirmPass);
        mLPV_ShPrefs.saveSecondPass(secondPass);
        mLPV.clearPassStrings();
    }
}
