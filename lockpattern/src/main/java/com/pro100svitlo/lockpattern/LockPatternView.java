package com.pro100svitlo.lockpattern;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro100svitlo.lockpattern.interfaces.DialogLPVInterface;
import com.pro100svitlo.lockpattern.interfaces.MainLPVInterface;

import java.util.ArrayList;

/**
 * Created by pro100svitlo on 1/24/16.
 */
public class LockPatternView extends LinearLayout{

    private final int SET_PATTERN = 100;
    private final int CONFIRM_PATTERN = 200;
    private final int CHECK_PATTERN = 300;

    private Context mContext;
    private MainLPVInterface mInterfaceLPV;
    private DialogLPVInterface mInterfaceSPD;

    private LockPatternView mLPV;
    private MainPatternView mMainPatternView;
    private StatusTextView mStatusTitle;
    private ForgotPassTextView mForgotPassTitle;
    private SharedPreferencesLPV mSharedPreferences;
    private BottomButtonsLayout mBottomButtonsLayout;
    private ImageView mLogoView;
    private Button mBtnCancel;
    private Button mBtnConfirm;

    //mainPatternView
    private Canvas mPatternCanvas;
    private Drawable mBgNormal;
    private Drawable mBgError;
    private Paint mDotLinePaint;
    private Paint mBgLinePaint;
    private Paint mPatternPaint;
    private Path mPatternPath;
    private Bitmap mPatternBitmap;
    private Bitmap mDotBitmapNormal;
    private Bitmap mDotBitmapTouched;
    private Bitmap mDotBitmapError;
    private ArrayList<ImageView> mAllDots;
    private final ArrayList<ImageView> mDotsTouched = new ArrayList<>();
    private int mDotColorNormal;
    private int mDotColorTouched;
    private int mDotColorError;
    private int mBgColorNormal;
    private int mBgColorError;
    private int mDotAnimationDuration;
    private int mDotCountMin;
    private int mDotRadius;
    private int mDotLineWidth;
    private int mBgRadius;
    private int mBgLineWidth;
    private int mDotVibrateDuration;
    private int mErrorVibrateDuration;
    private int mErrorTimeOut;
    private final int mHorizontalDotsCount = 3;
    private final int mVerticalDotsCount = 3;
    private float mDotWidth;
    private float mDotHeight;
    private float mDotTouchLock_prevX;
    private float mDotTouchLock_prevY;
    private float mDotTouchLock_currentY;
    private float mDotTouchLock_currentX;
    private boolean mDotVibrateEnable;
    private boolean mNeedToShowPass;
    private float[] mDotsCoordinatesX;
    private float[] mDotsCoordinatesY;
    private float mDotAnimationScaleMax;
    private float mDotAnimationScaleNormal;
    private int mMainPatternViewSize;
    private int mMatrixSize;
    private int mTouchedDotLast;
    private int mTouchedDotFirst;
    ////


    //ButtonsView
    private int mButtonTextColor;
    private Drawable mButtonBgResource;
    private String mBtnCancelStr;
    private String mBtnConfirmStr;
    private String mBtnRepeatStr;
    ////

    //StatusView
    private int mStatusColorNormal;
    private int mStatusColorError;
    private int mCurrentLockStatus;
    private String mTitleSetNewPatterStr;
    private String mTitlePatterErrorStr;
    private String mTitleConfirmPatternStr;
    ////

    //DialogView
    private int mDialogMinAnswerLength;
    private int mDialogMaxAnswerLength;
    private int mDialogTitleColor;
    private int mDialogMessageColor;
    private int mDialogTextColor;
    private int mDialogButtonsColor;
    private int mSecondPassRadioBtnColor;
    private String[] mQuestionsArray;
    private String mDialogSecondPassTitleStr;
    private String mDialogSecondPassMessageStr;
    private String mDialogSecondPassPositiveStr;
    private String mDialogSecondPassNegativeStr;
    private String mPassRestoreTitleStr;
    private String mPassRestoreMessageStr;
    private String mPassRestorePositiveStr;
    private String mPassRestoreNegativeStr;
    private boolean mSecondPassDialogEnable;
    ////

    //ForgotPasswordView
    private String mForgotPassTitleStr;
    private int mForgotPassColor;
    ////

    //LogoView
    private Drawable mDrawableLogo;
    ////

    //SnackBarView
    private String mPatternToShortStr;
    private String mPassRestoreFailedStr;
    private String mPassRestoreSuccessStr;
    ////

    private LayoutTransition mLayoutTransition;
    private Vibrator mVibrator;
    private int mAnimationTimeOut;
    private final int mButtonsViewId = -111;
    private final int mForgetPassViewId = -222;
    private final int mMainPatternViewId = -333;
    private final int mStatusViewId = -444;
    private final int mLogoViewId = -555;
    private int mMargin16 = 16;
    private int mStatusBarHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private float mTextSize;
    private float mDisplayDensity;
    private String mPassSetStr;
    private String mPassConfirmStr;
    private boolean mErrorVibrateEnable;
    private boolean mPatternEditEnable;
    private boolean mNeedSetDefItems;
    private boolean mSecretModeEnable;
    private boolean mIsPatternBgEnable;
    private boolean mIsStartAnimationEnable;

    public LockPatternView(Context context) {
        super(context);
        mContext = context;
        initDefaultItems();
    }
    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initDefaultItems();
        getItemsFromTheme(attrs);
    }
    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initDefaultItems();
        getItemsFromTheme(attrs);
    }

    private void initDefaultItems(){
        mDisplayDensity = getResources().getDisplayMetrics().density;

        initDefaultFloats();
        initDefaultInteger();
        initDefaultColors();
        initDefaultStrings();
        initDefaultBooleans();
    }
    private void initDefaultFloats(){
        TypedValue tv = new TypedValue();
        getResources().getValue(R.dimen.dotAnimScaleMax, tv, true);
        mDotAnimationScaleMax = tv.getFloat();

        getResources().getValue(R.dimen.dotAnimScaleNormal, tv, true);
        mDotAnimationScaleNormal = tv.getFloat();
    }
    private void initDefaultInteger(){
        mTextSize = mMargin16;
        mMargin16 = Math.round(mMargin16 * mDisplayDensity);
        mDotRadius = (int)getResources().getDimension(R.dimen.dotRadius);
        mDotLineWidth = (int)getResources().getDimension(R.dimen.dotLineWidth);
        mBgRadius = (int)getResources().getDimension(R.dimen.mainBgRadius);
        mBgLineWidth = (int)getResources().getDimension(R.dimen.mainBgLineWidth);
        mDotCountMin = getResources().getInteger(R.integer.dotMinCount);
        mDotVibrateDuration = getResources().getInteger(R.integer.dotVibrateDur);
        mErrorVibrateDuration = getResources().getInteger(R.integer.errorVibrateDur);
        mErrorTimeOut = getResources().getInteger(R.integer.errorTimeOut);
        mDialogMinAnswerLength  = getResources().getInteger(R.integer.dialogMinAnswerLength);
        mDialogMaxAnswerLength  = getResources().getInteger(R.integer.dialogMaxAnswerLength);

        mDotAnimationDuration = getResources().getInteger(R.integer.dotAnimationDuration);
        mAnimationTimeOut = getResources().getInteger(R.integer.animTimeOut);
    }
    private void initDefaultColors(){
        mStatusColorNormal = ContextCompat.getColor(mContext, R.color.lpv_white);
        mStatusColorError = ContextCompat.getColor(mContext, R.color.lpv_white);
        mForgotPassColor = ContextCompat.getColor(mContext, R.color.lpv_white);

        mDotColorNormal = ContextCompat.getColor(mContext, R.color.lpv_white);
        mDotColorTouched = ContextCompat.getColor(mContext, R.color.lpv_green);
        mDotColorError = ContextCompat.getColor(mContext, R.color.lpv_red);

        mBgColorNormal = ContextCompat.getColor(mContext, R.color.lpv_white);
        mBgColorError = ContextCompat.getColor(mContext, R.color.lpv_red);

        mButtonTextColor = ContextCompat.getColor(mContext, R.color.lpv_gray);
    }
    private void initDefaultStrings(){
        mForgotPassTitleStr = mContext.getString(R.string.lpv_tv_forgotPass_title);
        mPassRestoreFailedStr = mContext.getString(R.string.lpv_snack_forgotPassFailed);
        mPassRestoreSuccessStr = mContext.getString(R.string.lpv_snack_forgotPassSuccess);
        mPatternToShortStr = mContext.getString(R.string.lpv_snackbar_shortPattern);

        mTitleSetNewPatterStr = mContext.getString(R.string.lpv_tv_statusTitle_setNewPattern);
        mTitleConfirmPatternStr = mContext.getString(R.string.lpv_tv_statusTitle_confirmPattern);
        mTitlePatterErrorStr = mContext.getString(R.string.lpv_tv_statusTitle_patternError);

        mBtnCancelStr = mContext.getString(R.string.lpv_btn_actionCancel);
        mBtnConfirmStr = mContext.getString(R.string.lpv_btn_actionConfirm);
        mBtnRepeatStr = mContext.getString(R.string.lpv_btn_actionRepeat);
    }
    private void initDefaultBooleans(){
        mDotVibrateEnable = true;
        mNeedToShowPass = false;
        mErrorVibrateEnable = true;
        mPatternEditEnable = true;
        mSecretModeEnable = false;
        mSecondPassDialogEnable = true;
        mIsPatternBgEnable = false;
        mIsStartAnimationEnable = true;
    }

    private void getItemsFromTheme(AttributeSet attrs){
        setCustomGeneralStyle(attrs);
        setCustomStatusStyle(attrs);
        setCustomMainPatternStyle(attrs);
        setCustomForgotPassStyle(attrs);
        setCustomButtonsStyle(attrs);
        setCustomDialogStyle(attrs);
        setCustomLogoStyle(attrs);
    }
    private void setCustomGeneralStyle(AttributeSet attrs){
        TypedArray lpv = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_general, 0, 0);
        try {
            mErrorTimeOut = lpv.getInteger(R.styleable.lpv_general_errorTimeOutDur, mErrorTimeOut);
            mSecretModeEnable = lpv.getBoolean(R.styleable.lpv_general_secretModeEnable, mSecretModeEnable);
            mSecondPassDialogEnable = lpv.getBoolean(R.styleable.lpv_general_secondPassDialogEnable, mSecondPassDialogEnable);
            mNeedToShowPass = lpv.getBoolean(R.styleable.lpv_general_showPassStr, mNeedToShowPass);
        } finally {
            lpv.recycle();
        }
    }
    private void setCustomButtonsStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_btn, 0, 0);
        try {
            mButtonBgResource = ta.getDrawable(R.styleable.lpv_btn_btnBgResource);
            mButtonTextColor = ta.getInteger(R.styleable.lpv_btn_btnTextColor, mButtonTextColor);

            String cancelStr = ta.getString(R.styleable.lpv_btn_btnCancelStr);
            String confirmStr = ta.getString(R.styleable.lpv_btn_btnConfirmStr);
            String repeatStr = ta.getString(R.styleable.lpv_btn_btnRepeatStr);
            mBtnCancelStr = cancelStr != null ? cancelStr : mBtnCancelStr;
            mBtnConfirmStr = confirmStr != null ? confirmStr : mBtnConfirmStr;
            mBtnRepeatStr = repeatStr != null ? repeatStr : mBtnRepeatStr;
        } finally {
            ta.recycle();
        }
    }
    private void setCustomForgotPassStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_forgotPass, 0, 0);
        try {
            mForgotPassColor = ta.getInteger(R.styleable.lpv_forgotPass_forgotPassColor, mForgotPassColor);
            String forgotPass = ta.getString(R.styleable.lpv_forgotPass_forgotPassTitleStr);
            String forgotPassFailed = ta.getString(R.styleable.lpv_forgotPass_forgotPassFailedStr);
            String forgotPassSuccess = ta.getString(R.styleable.lpv_forgotPass_forgotPassSuccessStr);
            mForgotPassTitleStr = forgotPass != null ? forgotPass : mForgotPassTitleStr;
            mPassRestoreFailedStr = forgotPassFailed != null ? forgotPassFailed : mPassRestoreFailedStr;
            mPassRestoreSuccessStr = forgotPassSuccess != null ? forgotPassSuccess : mPassRestoreSuccessStr;
        } finally {
            ta.recycle();
        }
    }
    private void setCustomMainPatternStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_main, 0, 0);
        try {
            mDotAnimationScaleMax = ta.getFloat(R.styleable.lpv_main_dotAnimScaleMax, mDotAnimationScaleMax);
            mDotRadius = ta.getInteger(R.styleable.lpv_main_dotRadius, mDotRadius);
            mDotLineWidth = ta.getInteger(R.styleable.lpv_main_dotLineWidth, mDotLineWidth);
            mBgRadius = ta.getInteger(R.styleable.lpv_main_mainBgRadius, mBgRadius);
            mBgLineWidth = ta.getInteger(R.styleable.lpv_main_mainBgLineWidth, mBgLineWidth);
            int minDotsCount = ta.getInteger(R.styleable.lpv_main_dotMinCount, mDotCountMin);
            if (minDotsCount > mDotCountMin){
                mDotCountMin = minDotsCount;
            } else if (minDotsCount > 9){
                mDotCountMin = 9;
            }
            mDotVibrateDuration = ta.getInteger(R.styleable.lpv_main_dotVibrateDur, mDotVibrateDuration);
            mErrorVibrateDuration = ta.getInteger(R.styleable.lpv_main_errorVibrateDur, mErrorVibrateDuration);

            mDotColorNormal = ta.getInteger(R.styleable.lpv_main_dotNormalColor, mDotColorNormal);
            mDotColorTouched = ta.getInteger(R.styleable.lpv_main_dotTouchedColor, mDotColorTouched);
            mDotColorError = ta.getInteger(R.styleable.lpv_main_dotErrorColor, mDotColorError);
            mBgColorNormal = ta.getInteger(R.styleable.lpv_main_dotErrorColor, mBgColorNormal);
            mBgColorError = ta.getInteger(R.styleable.lpv_main_dotErrorColor, mBgColorError);

            String patternToShortStr = ta.getString(R.styleable.lpv_main_toShortPatternStr);
            if (patternToShortStr != null){
                mPatternToShortStr = patternToShortStr;
            }
            mPatternToShortStr = String.format(mPatternToShortStr, mDotCountMin);

            mIsPatternBgEnable = ta.getBoolean(R.styleable.lpv_main_mainIsBgEnable, mIsPatternBgEnable);
            mDotVibrateEnable = ta.getBoolean(R.styleable.lpv_main_dotVibrateEnable, mDotVibrateEnable);
            mErrorVibrateEnable = ta.getBoolean(R.styleable.lpv_main_errorVibrateEnable, mErrorVibrateEnable);
        } finally {
            ta.recycle();
        }
    }
    private void setCustomStatusStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_status, 0, 0);
        try {
            mStatusColorNormal = ta.getInteger(R.styleable.lpv_status_statusNormalColor, mStatusColorNormal);
            mStatusColorError = ta.getInteger(R.styleable.lpv_status_statusErrorColor, mStatusColorError);

            String titleSetNewPattern = ta.getString(R.styleable.lpv_status_statusSetNewPatternStr);
            String titleConfirmPatternStr = ta.getString(R.styleable.lpv_status_statusConfirmPatternStr);
            String titleErrorPatternStr = ta.getString(R.styleable.lpv_status_statusErrorPatternStr);
            mTitleSetNewPatterStr = titleSetNewPattern != null ? titleSetNewPattern : mTitleSetNewPatterStr;
            mTitleConfirmPatternStr = titleConfirmPatternStr != null ? titleConfirmPatternStr : mTitleConfirmPatternStr;
            mTitlePatterErrorStr = titleErrorPatternStr != null ? titleErrorPatternStr : mTitlePatterErrorStr;
        } finally {
            ta.recycle();
        }
    }
    private void setCustomDialogStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_dialog, 0, 0);
        try {
            //dialog general
            mDialogTitleColor = ta.getInteger(R.styleable.lpv_dialog_dialogTitleColor, 0);
            mDialogMessageColor = ta.getInteger(R.styleable.lpv_dialog_dialogMessageColor, 0);
            mDialogTextColor = ta.getInteger(R.styleable.lpv_dialog_dialogTextColor, 0);
            mDialogButtonsColor = ta.getInteger(R.styleable.lpv_dialog_dialogButtonsColor, 0);
            mDialogMinAnswerLength = ta.getInteger(R.styleable.lpv_dialog_dialogMinAnswerLength, mDialogMinAnswerLength);
            mDialogMaxAnswerLength = ta.getInteger(R.styleable.lpv_dialog_dialogMaxAnswerLength, mDialogMaxAnswerLength);

            //dialog setSecondPass
            mSecondPassRadioBtnColor = ta.getInteger(R.styleable.lpv_dialog_dialogRadioBtnColor, 0);
            mDialogSecondPassTitleStr = ta.getString(R.styleable.lpv_dialog_dialogSecondPassTitleStr);
            mDialogSecondPassMessageStr = ta.getString(R.styleable.lpv_dialog_dialogSecondPassMessageStr);
            mDialogSecondPassPositiveStr = ta.getString(R.styleable.lpv_dialog_dialogSecondPassPositiveStr);
            mDialogSecondPassNegativeStr = ta.getString(R.styleable.lpv_dialog_dialogSecondPassNegativeStr);
            //string-arrays
            CharSequence[] questions = ta.getTextArray(R.styleable.lpv_dialog_dialogQuestionsArray);
            if (questions != null){
                mQuestionsArray = new String[questions.length];
                for (int i = 0; i < questions.length; i++) {
                    mQuestionsArray[i] = questions[i].toString();
                }
            }

            //dialog passRestore
            mPassRestoreTitleStr = ta.getString(R.styleable.lpv_dialog_dialogPassRestoreTitleStr);
            mPassRestoreMessageStr = ta.getString(R.styleable.lpv_dialog_dialogPassRestoreMessageStr);
            mPassRestorePositiveStr = ta.getString(R.styleable.lpv_dialog_dialogPassRestorePositiveStr);
            mPassRestoreNegativeStr = ta.getString(R.styleable.lpv_dialog_dialogPassRestoreNegativeStr);
        } finally {
            ta.recycle();
        }
    }
    private void setCustomLogoStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_logo, 0, 0);
        try {
            //dialog general
            mDrawableLogo = ta.getDrawable(R.styleable.lpv_logo_lpv_logo);
        } finally {
            ta.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        if (mPatternEditEnable){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Log.d("TAG", "ACTION_DOWN x = " + String.valueOf(x)+"\ny = " +String.valueOf(y));
                    mNeedSetDefItems = true;
                    prepareDefaultView(0);
                    mDotTouchLock_currentX = x;
                    mDotTouchLock_currentY = y;
                    checkItemInPosition(x, y);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    checkItemInPosition(x, y);
                    mDotTouchLock_currentX = x;
                    mDotTouchLock_currentY = y;
                    invalidate();
                    return true;
                case MotionEvent.ACTION_UP:
                    checkEnteredPattern();
                    mPatternPath.reset();
                    setItemCoordinatesToDefault();
                    invalidate();
                    return true;
            }
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        mScreenHeight = h;
        calculateDotsCoordinates();
        playStartAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isSecretModeEnable() && mDotTouchLock_prevX != 0 && mDotTouchLock_prevY != 0){
            canvas.drawBitmap(mPatternBitmap, 0, 0, mPatternPaint);
            canvas.drawLine(mDotTouchLock_prevX, mDotTouchLock_prevY,
                    mDotTouchLock_currentX, mDotTouchLock_currentY, mDotLinePaint);
        }
    }


    public void initLockPatternView(Activity a, MainLPVInterface mainLpv_interface,
                                    DialogLPVInterface spd_interface){
        a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mInterfaceLPV = mainLpv_interface;
        mInterfaceSPD = spd_interface;
        mContext = a;

        onCreateLockPatternView();
    }

    public void resetPatternSuccessful(){
        mCurrentLockStatus = SET_PATTERN;
        mStatusTitle.setText(mTitleSetNewPatterStr);
        mForgotPassTitle.setVisibility(INVISIBLE);
        showSnackBar(mPassRestoreSuccessStr);
    }

    public void resetPatternFailed(){
        showSnackBar(mPassRestoreFailedStr);
    }

    public void patternSetSuccessful(String secondPass, String question){
        mInterfaceLPV.patternConfirmed(true, getShowedPass());

        savePass(mPassConfirmStr, secondPass, question);
        setCurrentLockStatus(CHECK_PATTERN);
        mBottomButtonsLayout.setBottomButtonsVisibility(false);
        setDefaultView();
    }

    public void secondPassDismissed(){
        mBottomButtonsLayout.onCancelPatternListener.onClick(mBtnCancel);
    }

    private void onCreateLockPatternView() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        initItems();

        createAndAddLogoView();
        createAndAddTitleStatusTextView();
        createAndAddMainPatternView();
        createForgotPassView();
        createAndAddBottomButtons();

        checkSavedPass();
    }

    private void playStartAnimation(){
        if (!mIsStartAnimationEnable){
            mBottomButtonsLayout.setVisibility(VISIBLE);
            mForgotPassTitle.setVisibility(VISIBLE);
            mMainPatternView.setVisibility(VISIBLE);
            mStatusTitle.setVisibility(VISIBLE);
            mLogoView.setVisibility(VISIBLE);
        } else {
            if (mCurrentLockStatus == CHECK_PATTERN){
                setItemDelayedVisibility(true, mForgotPassTitle, mAnimationTimeOut);
            }
            setItemDelayedVisibility(true, mMainPatternView, 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int s = mAllDots.size();
                    for (int i = 0; i < s; i++) {
                        setItemDelayedVisibility(true, mAllDots.get(i), mAnimationTimeOut + mAnimationTimeOut * i / 4);
                    }
                }
            }, mAnimationTimeOut / 2);
            setItemDelayedVisibility(true, mStatusTitle, mAnimationTimeOut + mAnimationTimeOut /2);
            if (mDrawableLogo != null){
                setItemDelayedVisibility(true, mLogoView, mAnimationTimeOut + mAnimationTimeOut /2);
            }
        }
    }

    private void setItemDelayedVisibility(final boolean visible, final View v, final int timeOut){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (visible){
                    v.setVisibility(VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.dot_start_anim);
                    animation.setDuration(timeOut);
                    v.startAnimation(animation);
                } else {
                    v.setVisibility(INVISIBLE);
                }
            }
        }, timeOut);
    }

    private void initItems(){
        mLPV = this;

        mVibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);

        getStatusBarHeight();

        mCurrentLockStatus = SET_PATTERN;

        mDotLinePaint = new Paint();
        mDotLinePaint.setColor(mDotColorTouched);
        mDotLinePaint.setAntiAlias(true);
        mDotLinePaint.setStrokeWidth(mDotLineWidth);
        mDotLinePaint.setStyle(Paint.Style.STROKE);
        mDotLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mPatternPath = new Path();
        mPatternPaint = new Paint(Paint.DITHER_FLAG);
        if (mIsPatternBgEnable){
            mBgLinePaint = new Paint();
            mBgLinePaint.setAntiAlias(true);
            mBgLinePaint.setStrokeWidth(mBgLineWidth);
            mBgLinePaint.setStyle(Paint.Style.STROKE);
        }

        setWillNotDraw(false);
        clearPathBitmap();

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        mMainPatternViewSize = width - width/5;

        setViewLayoutTransition(mLPV);
    }

    private void getStatusBarHeight(){
        mStatusBarHeight = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0){
            mStatusBarHeight = getResources().getDimensionPixelSize(resId);
        }
    }

    private void setViewLayoutTransition(ViewGroup v){
        if (mLayoutTransition == null){
            mLayoutTransition = new LayoutTransition();
            mLayoutTransition.setDuration(mAnimationTimeOut);
            mLayoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }
        v.setLayoutTransition(mLayoutTransition);
    }

    private void calculateItemSize(){
        if (mDotWidth == 0 || mDotHeight == 0){
            mDotWidth = mAllDots.get(0).getWidth();
            mDotHeight = mAllDots.get(0).getHeight();
        }
    }

    private void calculateDotsCoordinates(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calculateItemSize();
                for (int i = 0; i < mMatrixSize; i++) {
                    int[] loc = new int[2];
                    mAllDots.get(i).getLocationOnScreen(loc);

                    mDotsCoordinatesX[i] = loc[0] + mDotWidth / 2;
                    mDotsCoordinatesY[i] = loc[1] + mDotHeight / 2;
                    Log.d("TAG", "x = " + String.valueOf(mDotsCoordinatesX[i])+"\ny = " +String.valueOf(mDotsCoordinatesY[i]));
                }

                if (mDotsCoordinatesX[0] == 0) {
                    calculateDotsCoordinates();
                } else {
                    setDefaultView();
                }
            }
        }, 500);
    }

    private void checkSavedPass(){
        mPassSetStr = getSavedPass();
        boolean patternExist = false;
        if (mPassSetStr.length() >= mDotCountMin){
            mCurrentLockStatus = CHECK_PATTERN;
            patternExist = true;
            mForgotPassTitle.setVisibility(VISIBLE);
        } else {
            mStatusTitle.setText(mTitleSetNewPatterStr);
        }
        sendInterfacePassExist(patternExist);
    }

    private void sendInterfacePassExist(final boolean patternExist) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterfaceLPV != null) {
                    mInterfaceLPV.isPatternExist(patternExist);
                } else {
                    sendInterfacePassExist(patternExist);
                }
            }
        }, 50);
    }

    private void createAndAddBottomButtons(){
        mBottomButtonsLayout = new BottomButtonsLayout(mContext);
        addView(mBottomButtonsLayout);
        setViewLayoutTransition(mBottomButtonsLayout);
        setViewLayoutTransition(mBottomButtonsLayout);
    }
    private void createForgotPassView(){
        mForgotPassTitle = new ForgotPassTextView(mContext);
        if (mSecondPassDialogEnable) {
            addView(mForgotPassTitle);
            mForgotPassTitle.setVisibility(INVISIBLE);
        }
    }
    private void createAndAddMainPatternView(){
        mMainPatternView = new MainPatternView(mContext);
        addView(mMainPatternView);
        mMainPatternView.setVisibility(INVISIBLE);
    }
    private void createAndAddTitleStatusTextView(){
        mStatusTitle = new StatusTextView(mContext);
        addView(mStatusTitle);
        mStatusTitle.setVisibility(INVISIBLE);
    }
    private void createAndAddLogoView(){
        mLogoView = new CustomLogo(mContext);
        addView(mLogoView);
        mLogoView.setVisibility(INVISIBLE);
    }

    private void savePass(String confirmPass, String secondPass, String question){
        getLPV_SharedPreferences();
        mSharedPreferences.saveMainPass(confirmPass);
        mSharedPreferences.saveSecondPass(secondPass);
        mSharedPreferences.saveSecondQuestion(question);
        clearPassStrings();
    }

    private boolean isSecretModeEnable(){
        return mCurrentLockStatus == CHECK_PATTERN && mSecretModeEnable;
    }

    private void setCurrentLockStatus(int currentLockStatus){
        mCurrentLockStatus = currentLockStatus;
    }

    private  void clearPassStrings(){
        mPassSetStr = mPassConfirmStr = "";
    }

    private String getSetPass(){
        String pass = "";
        for (int i = 0; i < mDotsTouched.size(); i++) {
            pass += mDotsTouched.get(i).getTag();
        }
        return pass;
    }

    private void clearPathBitmap(){
        if (mScreenWidth == 0 || mScreenHeight == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearPathBitmap();
                }
            },20);
        } else {
            mPatternBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
            mPatternCanvas = new Canvas(mPatternBitmap);
        }
    }

    // TODO: 1/25/16   START - onTouchEven action
    private void checkItemInPosition( float x, float y){
        for (int i = 0; i < mAllDots.size(); i++) {
            if (mAllDots.get(i).isEnabled()){
                float currentX_min = mDotsCoordinatesX[i] - mDotWidth /2;
                float currentX_max = mDotsCoordinatesX[i] + mDotWidth /2;
                float currentY_min = mDotsCoordinatesY[i] - mDotHeight /2;
                float currentY_max = mDotsCoordinatesY[i] + mDotHeight /2;

                if (x > currentX_min && x < currentX_max &&
                        y > currentY_min && y < currentY_max){

                    mTouchedDotLast = i;
                    int xx = (int) mDotsCoordinatesX[i];
                    int yy = (int) mDotsCoordinatesY[i];

                    if (mDotsTouched.size() == 0){
                        mPatternPath.moveTo(xx, yy - mStatusBarHeight);
//                        mPatternPath.moveTo(xx, yy);
                    } else {
                        mPatternPath.lineTo(xx, yy - mStatusBarHeight);
//                        mPatternPath.lineTo(xx, yy);
                    }

                    mPatternCanvas.drawPath(mPatternPath, mDotLinePaint);

                    mDotTouchLock_prevX = xx;
                    mDotTouchLock_prevY = yy - mStatusBarHeight;

                    vibrate(mDotVibrateEnable, mDotVibrateDuration);
                    setItemIsActive(i);
                }
            }
        }
    }

    private void setItemIsActive(int position){
        ImageView iv = mAllDots.get(position);
        iv.setEnabled(false);
        if (!isSecretModeEnable()){
            makeItemBig(iv);
            iv.setImageBitmap(mDotBitmapTouched);
        }
        mDotsTouched.add(iv);
    }

    private void makeItemBig(final ImageView v){
        v.animate()
                .scaleX(mDotAnimationScaleMax).scaleY(mDotAnimationScaleMax)
                .setDuration(mDotAnimationDuration)
                .start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.animate()
                        .scaleX(mDotAnimationScaleNormal).scaleY(mDotAnimationScaleNormal)
                        .setDuration(mDotAnimationDuration)
                        .start();
            }
        }, mDotAnimationDuration);
    }

    private void checkEnteredPattern(){
        int size = mDotsTouched.size();
        if (size == 1){
            mNeedSetDefItems = true;
            prepareDefaultView(mErrorTimeOut);
        } else if (size > 1 && size < mDotCountMin){
            patternToShort();
        } else if (size >= mDotCountMin){
            patternIsSet();
        }
    }

    private void showSnackBar(String mess){
        Snackbar.make(mMainPatternView, mess, Snackbar.LENGTH_LONG).show();
    }

    private void prepareDefaultView(int timeOut) {
        for (int i = 0; i < mDotsTouched.size(); i++) {
            ImageView iv = mDotsTouched.get(i);
            iv.setEnabled(true);
        }

        if (mNeedSetDefItems && timeOut == 0){
            setDefaultView();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mNeedSetDefItems){
                        setDefaultView();
                    }
                }
            }, timeOut);
        }
        mDotsTouched.clear();
    }

    private void setDefaultView(){
        mStatusTitle.setTextColor(mStatusColorNormal);
        mDotLinePaint.setColor(mDotColorTouched);
        mNeedSetDefItems = false;
        for (int i = 0; i < mAllDots.size(); i++) {
            ImageView iv = mAllDots.get(i);
            iv.animate()
                    .scaleX(mDotAnimationScaleNormal).scaleY(mDotAnimationScaleNormal)
                    .setDuration(mDotAnimationDuration)
                    .start();
            iv.setImageBitmap(mDotBitmapNormal);
        }
        if (mIsPatternBgEnable){
            mMainPatternView.setBackground(mBgNormal);
        }
        clearPathBitmap();
        setDefaultTitle();
        invalidate();
    }

    private void setDefaultTitle(){
        if (mCurrentLockStatus == SET_PATTERN){
            mStatusTitle.setText(mTitleSetNewPatterStr);
        } else if (mCurrentLockStatus == CONFIRM_PATTERN){
            mStatusTitle.setText(mTitleConfirmPatternStr);
        } else if (mCurrentLockStatus == CHECK_PATTERN){
            mStatusTitle.setText("");
        }
    }

    private void patternToShort(){
        showSnackBar(mPatternToShortStr);
        patternError();
        if (mCurrentLockStatus != CHECK_PATTERN){
            mBtnConfirm.setText(mBtnRepeatStr);
        }
    }

    private void patternError(){
        mStatusTitle.setTextColor(mStatusColorError);
        mStatusTitle.setText(mTitlePatterErrorStr);
        if (mIsPatternBgEnable){
            mMainPatternView.setBackground(mBgError);
        }
        vibrate(mErrorVibrateEnable, mErrorVibrateDuration);
        mNeedSetDefItems = true;
        if (!isSecretModeEnable()){
            for (int i = 0; i < mDotsTouched.size(); i++) {
                ImageView iv = mDotsTouched.get(i);
                iv.setImageBitmap(mDotBitmapError);
            }
        }
        prepareDefaultView(mErrorTimeOut);
    }

    private void vibrate(boolean needToVibrate, int duration){
        if (needToVibrate){
            mVibrator.vibrate(duration);
        }
    }

    private void patternIsSet(){
        if (mCurrentLockStatus == SET_PATTERN){
            doIfStatusPatternSet();
        } else if (mCurrentLockStatus == CONFIRM_PATTERN){
            doIfStatusPatternConfirm();
        } else if(mCurrentLockStatus == CHECK_PATTERN){
            doIfStatusPatternCheck();
        }
    }

    private void doIfStatusPatternSet(){
        mPassSetStr = getSetPass();
        mBottomButtonsLayout.setBottomButtonsVisibility(true);
    }

    private void doIfStatusPatternConfirm(){
        mPassConfirmStr = getSetPass();
        if (mPassConfirmStr.equals(mPassSetStr)){
            mBottomButtonsLayout.setBottomButtonsVisibility(true);
        } else {
            mInterfaceLPV.patternFailed();
            patternError();
            mCurrentLockStatus = SET_PATTERN;
            mStatusTitle.setText(mTitleSetNewPatterStr);
            clearPassStrings();
        }
    }

    private void doIfStatusPatternCheck(){
        mPassConfirmStr = getSetPass();
        if (mPassConfirmStr.equals(mPassSetStr)){
            mInterfaceLPV.patternConfirmed(false, getShowedPass());
            setDefaultView();
        }  else {
            mInterfaceLPV.patternFailed();
            patternError();
        }
        mPassConfirmStr = "";
    }

    private String getShowedPass(){
        String showedPass;
        if (mNeedToShowPass){
            showedPass = mPassConfirmStr;
        } else {
            showedPass = "";
        }
        return showedPass;
    }

    private String getSavedPass(){
        getLPV_SharedPreferences();

        return mSharedPreferences.getMainSavedPass();
    }

    private void getLPV_SharedPreferences(){
        if (mSharedPreferences == null){
            mSharedPreferences = new SharedPreferencesLPV(mContext);
        }
    }

    private void setItemCoordinatesToDefault(){
        mDotTouchLock_currentX = mDotsCoordinatesX[mTouchedDotLast];
        mDotTouchLock_currentY = mDotsCoordinatesY[mTouchedDotLast] - mStatusBarHeight;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDotTouchLock_prevX = 0;
                mDotTouchLock_prevY = 0;
            }
        }, 100);
    }

    // TODO: 1/25/16   END - onTouchEvent action

    private class MainPatternView extends LinearLayout {

        private LayoutParams mPatternRowParams, mPatternItemParams;

        private Paint mItemPaint;
        private Bitmap.Config mItemBitmapConfig;
        private int mPatternItemMargin = 16;
        private final int mPatternItemPadding = 30;

        public MainPatternView(Context context) {
            super(context);
            onCreateMainPatternView();
        }

        public MainPatternView(Context context, AttributeSet attrs) {
            super(context, attrs);
            onCreateMainPatternView();
        }

        public MainPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            onCreateMainPatternView();
        }

        public void setPatternEditEnable(boolean enable){
            mPatternEditEnable = enable;
            for (int i = 0; i < mAllDots.size(); i++) {
                mAllDots.get(i).setEnabled(enable);
            }
        }

        private void onCreateMainPatternView(){
            calculateItemsData();

            setId(mMainPatternViewId);
            setOrientation(LinearLayout.VERTICAL);

            mAllDots = new ArrayList<>();
            for (int i = 0; i < mHorizontalDotsCount; i++) {
                addView(addNewPatternRow());
            }
            setMainPatternViewLayoutParams();

            if (mIsPatternBgEnable){
                createPatternBg();
            }
        }

        private void createPatternBg(){
            RectF r = new RectF();
            float div = Math.round(mMainPatternViewSize * 0.5 /100);
            r.set(div, div, mMainPatternViewSize - div, mMainPatternViewSize - div);

            Bitmap normal = Bitmap.createBitmap(mMainPatternViewSize, mMainPatternViewSize, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(normal);
            mBgLinePaint.setColor(mBgColorNormal);
            c.drawRoundRect(r, mBgRadius, mBgRadius, mBgLinePaint);
            mBgNormal = new BitmapDrawable(mContext.getResources(), normal);

            Bitmap error = Bitmap.createBitmap(mMainPatternViewSize, mMainPatternViewSize, Bitmap.Config.ARGB_8888);
            c = new Canvas(error);
            mBgLinePaint.setColor(mBgColorError);
            c.drawRoundRect(r, mBgRadius, mBgRadius, mBgLinePaint);
            mBgError = new BitmapDrawable(mContext.getResources(), error);

            setBackground(mBgNormal);
        }

        private LinearLayout addNewPatternRow(){
            LinearLayout row = new LinearLayout(mContext);
            row.setOrientation(LinearLayout.HORIZONTAL);
            if (mPatternRowParams == null){
                mPatternRowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            }
            mPatternRowParams.weight = 1f;
            row.setLayoutParams(mPatternRowParams);
            for (int i = 0; i < mVerticalDotsCount; i++) {
                row.addView(addPatternNewItem());
            }
            setViewLayoutTransition(row);
            return row;
        }

        private ImageView addPatternNewItem(){
            ImageView item = new ImageView(mContext);
            item.setImageBitmap(mDotBitmapNormal);
            int scale = 2;
            if (mPatternItemParams == null){
                mPatternItemParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mPatternItemParams.gravity = Gravity.CENTER_VERTICAL;
                mPatternItemMargin = Math.round(mPatternItemMargin * mDisplayDensity);
                mPatternItemParams.setMargins(mPatternItemMargin, mPatternItemMargin, mPatternItemMargin, mPatternItemMargin);
            }

            mPatternItemParams.weight = 1f;

            item.setPadding(mPatternItemPadding, mPatternItemPadding * scale,
                    mPatternItemPadding, mPatternItemPadding * scale);
            item.setLayoutParams(mPatternItemParams);

            mAllDots.add(item);
            item.setTag(String.valueOf(mTouchedDotFirst));
            mTouchedDotFirst++;

            item.setVisibility(INVISIBLE);

            return item;
        }

        private void setMainPatternViewLayoutParams(){
            RelativeLayout.LayoutParams lp =
                    new RelativeLayout.LayoutParams(mMainPatternViewSize, mMainPatternViewSize);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            setLayoutParams(lp);
        }

        private void calculateItemsData(){
            mItemPaint = new Paint();
            mItemPaint.setAntiAlias(true);
            mItemBitmapConfig = Bitmap.Config.ARGB_8888;
            int size = mDotRadius * 2;
            mDotBitmapNormal = Bitmap.createBitmap(size, size, mItemBitmapConfig);
            mDotBitmapTouched = Bitmap.createBitmap(size, size, mItemBitmapConfig);
            mDotBitmapError = Bitmap.createBitmap(size, size, mItemBitmapConfig);

            drawItemNormal();
            drawItemTouched();
            drawItemError();
            //////////////

            mMatrixSize = mHorizontalDotsCount * mVerticalDotsCount;
            mDotsCoordinatesX = new float[mMatrixSize];
            mDotsCoordinatesY = new float[mMatrixSize];
        }

        private void drawItemNormal(){
            mItemPaint.setColor(mDotColorNormal);
            Canvas canvas = new Canvas(mDotBitmapNormal);
            canvas.drawCircle(mDotRadius, mDotRadius, mDotRadius, mItemPaint);
        }

        private void drawItemTouched(){
            mItemPaint.setColor(mDotColorTouched);
            Canvas canvas = new Canvas(mDotBitmapTouched);
            canvas.drawCircle(mDotRadius, mDotRadius, mDotRadius, mItemPaint);
        }

        private void drawItemError(){
            mItemPaint.setColor(mDotColorError);
            Canvas canvas = new Canvas(mDotBitmapError);
            canvas.drawCircle(mDotRadius, mDotRadius, mDotRadius, mItemPaint);
        }


    }

    private class StatusTextView extends TextView {

        public StatusTextView(Context context) {
            super(context);
            onCreateStatusTextView();
        }

        public StatusTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            onCreateStatusTextView();
        }

        public StatusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            onCreateStatusTextView();
        }

        private void onCreateStatusTextView(){
            setId(mStatusViewId);
            setGravity(Gravity.CENTER_HORIZONTAL);
            setTextSize(mTextSize);
            setTextColor(mStatusColorNormal);

            setLayoutParams();
        }

        private void setLayoutParams(){
            LayoutParams lp = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            int m = mMargin16;

            lp.setMargins(m, m / 2, m, m / 2);
            setLayoutParams(lp);
        }
    }

    private class ForgotPassTextView extends TextView implements OnClickListener {

        private DialogLPV mPassResetDialog;

        public ForgotPassTextView(Context context) {
            super(context);
            onCreateForgotPassTextView();
        }

        public ForgotPassTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            onCreateForgotPassTextView();
        }

        public ForgotPassTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            onCreateForgotPassTextView();
        }

        private void onCreateForgotPassTextView(){
            setId(mForgetPassViewId);
            setGravity(Gravity.CENTER_HORIZONTAL);
            setTextSize(mTextSize);
            setTextColor(mForgotPassColor);
            setText(mForgotPassTitleStr);
            setVisibility(INVISIBLE);
            setOnClickListener(this);

            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray ta = mContext.obtainStyledAttributes(attrs);
            int bg = ta.getResourceId(0,0);
            setBackgroundResource(bg);
            ta.recycle();

            setLayoutParams();
        }

        private void setLayoutParams(){
            LayoutParams lp = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            int m = mMargin16;

            lp.setMargins(m, m / 2, m, m / 2);
            setLayoutParams(lp);
            setPadding(m, m/2, m, 0);
        }

        @Override
        public void onClick(View v) {
            if (mPassResetDialog == null){
                mPassResetDialog = new DialogLPV
                        .Builder(mContext, mLPV, mDisplayDensity, DialogLPV.DIALOG_RESTORE_PATTERN,
                        mInterfaceSPD)
                        .setTitleColor(mDialogTitleColor)
                        .setMessageColor(mDialogMessageColor)
                        .setTextColor(mDialogTextColor)
                        .setButtonsColor(mDialogButtonsColor)
                        .setTitleStr(mPassRestoreTitleStr)
                        .setMessageStr(mPassRestoreMessageStr)
                        .setPositiveStr(mPassRestorePositiveStr)
                        .setNegativeStr(mPassRestoreNegativeStr)
                        .setMinAnswerLength(mDialogMinAnswerLength)
                        .setMaxAnswerLength(mDialogMaxAnswerLength)
                        .build();
            }
            mPassResetDialog.show();
        }
    }

    private class BottomButtonsLayout extends LinearLayout {

        private DialogLPV mSecondPassDialog;
        private int mMargin16 = 16;

        public BottomButtonsLayout(Context context) {
            super(context);
            onCreateBottomButtonsLayout();
        }

        public BottomButtonsLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            onCreateBottomButtonsLayout();
        }

        public BottomButtonsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            onCreateBottomButtonsLayout();
        }

        public void setBottomButtonsVisibility(boolean visible){
            if (visible){
                mBtnCancel.setVisibility(VISIBLE);
                mBtnConfirm.setVisibility(VISIBLE);
                mMainPatternView.setPatternEditEnable(false);
            } else {
                mBtnCancel.setVisibility(INVISIBLE);
                mBtnConfirm.setVisibility(INVISIBLE);
                mMainPatternView.setPatternEditEnable(true);
            }
        }

        private void onCreateBottomButtonsLayout(){
            setId(mButtonsViewId);
            setLayoutParams();
            setOrientation(HORIZONTAL);
            addView(addCancelButton());
            addView(addConfirmButton());
        }

        private void setLayoutParams(){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mMainPatternViewSize,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            lp.setMargins(mMargin16 *2, mMargin16, mMargin16 *2, mMargin16);
            setLayoutParams(lp);
        }

        private Button addCancelButton(){
            mBtnCancel = new Button(mContext);
            LayoutParams lp = new LayoutParams(0,
                    LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.setMargins(0, 0, mMargin16, 0);
            mBtnCancel.setLayoutParams(lp);

            mBtnCancel.setText(mBtnCancelStr);
            mBtnCancel.setTextColor(mButtonTextColor);
            mBtnCancel.setOnClickListener(onCancelPatternListener);
            if (mButtonBgResource != null){
                mBtnCancel.setBackground(mButtonBgResource);
            }
            mBtnCancel.setVisibility(INVISIBLE);
            return mBtnCancel;
        }

        private Button addConfirmButton(){
            mBtnConfirm = new Button(mContext);
            LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.setMargins(mMargin16, 0, 0, 0);
            mBtnConfirm.setLayoutParams(lp);
            mBtnConfirm.setText(mBtnRepeatStr);
            mBtnConfirm.setTextColor(mButtonTextColor);
            mBtnConfirm.setOnClickListener(onConfirmPatternListener);
            if (mButtonBgResource != null){
                mBtnConfirm.setBackground(mButtonBgResource);
            }
            mBtnConfirm.setVisibility(INVISIBLE);
            return mBtnConfirm;
        }

        private final OnClickListener onCancelPatternListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterfaceLPV.setPatternCanceled();
                setCurrentLockStatus(mLPV.SET_PATTERN);
                mStatusTitle.setText(mTitleSetNewPatterStr);
                setBottomButtonsVisibility(false);
                mNeedSetDefItems = true;
                prepareDefaultView(0);
                mBtnConfirm.setText(mBtnRepeatStr);
            }
        };

        private final OnClickListener onConfirmPatternListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLockStatus == SET_PATTERN){
                    setCurrentLockStatus(CONFIRM_PATTERN);
                    mPassSetStr = mLPV.getSetPass();
                    mPassSetStr = mLPV.getSetPass();
                    mBtnConfirm.setText(mBtnConfirmStr);
                    mStatusTitle.setText(mTitleConfirmPatternStr);
                } else if (mCurrentLockStatus == CONFIRM_PATTERN){
                    if (mSecondPassDialogEnable){
                        if (mSecondPassDialog == null){
                            mSecondPassDialog = new DialogLPV
                                    .Builder(mContext, mLPV, mDisplayDensity,
                                    DialogLPV.DIALOG_SET_SECOND_PASS, mInterfaceSPD)
                                    .setTitleColor(mDialogTitleColor)
                                    .setMessageColor(mDialogMessageColor)
                                    .setTextColor(mDialogTextColor)
                                    .setButtonsColor(mDialogButtonsColor)
                                    .setRadioButtonsColor(mSecondPassRadioBtnColor)
                                    .setTitleStr(mDialogSecondPassTitleStr)
                                    .setMessageStr(mDialogSecondPassMessageStr)
                                    .setPositiveStr(mDialogSecondPassPositiveStr)
                                    .setNegativeStr(mDialogSecondPassNegativeStr)
                                    .setQuestionsArray(mQuestionsArray)
                                    .setMinAnswerLength(mDialogMinAnswerLength)
                                    .setMaxAnswerLength(mDialogMaxAnswerLength)
                                    .setShowAnswerBoolean(mNeedToShowPass)
                                    .build();
                        }
                        mSecondPassDialog.show();
                    } else {
                        patternSetSuccessful("", "");
                    }
                }
                setBottomButtonsVisibility(false);
                mNeedSetDefItems = true;
                prepareDefaultView(0);
            }
        };

    }

    private class CustomLogo extends ImageView{

        public CustomLogo(Context context) {
            super(context);
            createLogoView();
        }

        public CustomLogo(Context context, AttributeSet attrs) {
            super(context, attrs);
            createLogoView();
        }

        public CustomLogo(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            createLogoView();
        }

        private void createLogoView(){
            setId(mLogoViewId);
            setImageDrawable(mDrawableLogo);
            setLayoutParams();
        }

        private void setLayoutParams(){
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.weight = 1f;
            lp.setMargins(mMargin16*2, mMargin16*2, mMargin16*2, mMargin16);
            setLayoutParams(lp);
        }
    }
}