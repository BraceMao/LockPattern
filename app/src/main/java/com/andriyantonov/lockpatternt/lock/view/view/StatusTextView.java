package com.andriyantonov.lockpatternt.lock.view.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andriyantonov.lockpatternt.R;

/**
 * Created by pro100svitlo on 1/29/16.
 */
public class StatusTextView extends TextView {

    public String mStatus_SetNewPatter;
    public String mStatus_EnterPattern;
    public String mStatus_ConfirmPattern;
    public String mStatus_PatternConfirmed;

    private Context mContext;
    private MainPatternView mMainPatternView;
    private int mStatusTitleColor = R.color.lpv_white_100;
    private float mDisplayDensity;


    public StatusTextView(Context context) {
        super(context);
        mContext = context;
        onCreate();
    }

    public StatusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        onCreate();
    }

    public StatusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        onCreate();
    }

    public StatusTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        onCreate();
    }

    public void setAdditions(MainPatternView mpv){
        mMainPatternView = mpv;
        setLayoutParams();
    }

    public void setTitleText(String title){
        setText(title);
    }

    private void onCreate(){
        initTitleStrings();

        mDisplayDensity = getResources().getDisplayMetrics().density;

        float textSize = mContext.getResources().getDimension(R.dimen.lpv_tv_titleSize)/mDisplayDensity;
        setGravity(Gravity.CENTER_HORIZONTAL);
        setTextSize(textSize);
        setTextColor(ContextCompat.getColor(mContext, mStatusTitleColor));
    }

    private void initTitleStrings(){
        mStatus_SetNewPatter = mContext.getString(R.string.lpv_tv_statusTitle_setNewPattern);
        mStatus_EnterPattern = mContext.getString(R.string.lpv_tv_statusTitle_enterPattern);
        mStatus_ConfirmPattern = mContext.getString(R.string.lpv_tv_statusTitle_confirmPattern);
        mStatus_PatternConfirmed = mContext.getString(R.string.lpv_tv_statusTitle_patternConfirmed);
    }

    private void setLayoutParams(){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int m = (int) mContext.getResources().getDimension(R.dimen.activity_vertical_margin);

        lp.setMargins(m, m, m, m);
        lp.addRule(RelativeLayout.ABOVE, mMainPatternView.getId());
        setLayoutParams(lp);
    }


}
