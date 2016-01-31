package com.andriyantonov.lockpatternt.lock.view.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andriyantonov.lockpatternt.R;
import com.andriyantonov.lockpatternt.lock.view.LPV_ForgotPassDialog;

/**
 * Created by pro100svitlo on 1/30/16.
 */
public class ForgotPassTextView extends TextView implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;
    private MainPatternView mMainPatternView;
    private LPV_ForgotPassDialog mForgotDialog;
    private LockPatternView mLPV;
    private int mStatusTitleColor = R.color.lpv_white_100;
    private float mDisplayDensity;
    private String mStrForgotPass;

    public ForgotPassTextView(Context context) {
        super(context);
        mContext = context;
        onCreate();
    }

    public ForgotPassTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        onCreate();
    }

    public ForgotPassTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        onCreate();
    }

    public ForgotPassTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        onCreate();
    }

    public void setAdditions(MainPatternView mpv, LockPatternView lpv, Activity a){
        mMainPatternView = mpv;
        mLPV = lpv;
        mActivity = a;
        setLayoutParams();
    }

    public void setTitleText(String title){
        setText(title);
    }

    private void onCreate(){
        initTitleStrings();

        mDisplayDensity = getResources().getDisplayMetrics().density;

        float textSize = mContext.getResources().getDimension(R.dimen.lpv_tv_forgotPassSize)/mDisplayDensity;
        setGravity(Gravity.CENTER_HORIZONTAL);
        setTextSize(textSize);
        setTextColor(ContextCompat.getColor(mContext, mStatusTitleColor));
        setText(mStrForgotPass);
        setVisibility(GONE);
        setOnClickListener(this);
    }

    private void initTitleStrings(){
        mStrForgotPass = mContext.getString(R.string.lpv_tv_forgotPass_title);
    }

    private void setLayoutParams(){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int m = (int) mContext.getResources().getDimension(R.dimen.activity_vertical_margin);

        lp.setMargins(m, m, m, m);
        lp.addRule(RelativeLayout.BELOW, mMainPatternView.getId());
        setLayoutParams(lp);
        setPadding(m, m, m, m);
    }

    @Override
    public void onClick(View v) {
        if (mForgotDialog == null){
            mForgotDialog = new LPV_ForgotPassDialog(mActivity, mLPV);
        }
        mForgotDialog.show();
    }
}
