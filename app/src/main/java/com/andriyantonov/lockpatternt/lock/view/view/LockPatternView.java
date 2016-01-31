package com.andriyantonov.lockpatternt.lock.view.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andriyantonov.lockpatternt.MainActivityFragment;
import com.andriyantonov.lockpatternt.R;
import com.andriyantonov.lockpatternt.lock.view.LPV_Interface;
import com.andriyantonov.lockpatternt.lock.view.LPV_SharedPreferences;

import java.util.ArrayList;

/**
 * Created by pro100svitlo on 1/24/16.
 */
public class LockPatternView extends RelativeLayout{

    public final int SET_PATTERN = 100;
    public final int CONFIRM_PATTERN = 200;
    public final int CHECK_PATTERN = 300;

    public MainPatternView mMainPatternView;
    public StatusTextView mStatusTitle;
    public ForgotPassTextView mForgotPassTitle;
    public int mLongVibrate = 200;

    private LockPatternView mLPV;
    private LPV_Interface mInterface;
    private LPV_SharedPreferences mLPV_ShPrefs;
    private BottomButtonsLayout mBottomButtonsLayout;
    private Context mContext;
    private Activity mActivity;
    private Button mBtnCancel, mBtnConfirm;
    private Bitmap canvasBitmap;
    private Canvas drawCanvas;
    private Paint drawPaint, canvasPaint;
    private Path drawPath;
    private Vibrator mVibrator;
    private ArrayList<ImageView> mAllItems = new ArrayList<>();
    private ArrayList<ImageView> mItemTouched = new ArrayList<>();
    private float mItemMaxScale = 4f;
    private float mItemNormalScale = 1f;
    private float[] mItemsCoordX;
    private float[] mItemsCoordY;
    private float mPatternItemWidth, mPatternItemHeight;
    private float mDisplayDensity;
    private int mItemBgDefault = R.drawable.item_bg_default;
    private int mItemBgActive = R.drawable.item_bg_active;
    private int mLpvBgDefault = R.drawable.lpv_bg_default;
    private int mLpvBgError = R.drawable.lpv_bg_error;
    private int mPathColor = R.color.item_bg_enter;
    private int mItemAnimDur = 100;
    private int mItemCountMin = 4;
    private int mShortVibrate = 50;
    private int mCurrentLockStatus;
    private int mStatusBarHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mLastPatternElement;
    private int mItemColor;
    private float mItemTouchLock_prevX;
    private float mItemTouchLock_prevY;
    private float mItemTouchLock_currentY;
    private float mItemTouchLock_currentX;
    private String mPassSetStr;
    private String mPassConfirmStr;
    private String mPatternToShort;
    private String mForgotPassFailedStr;
    private String mForgotPassSuccessStr;



    public LockPatternView(Context context) {
        super(context);
        mContext = context;
        onCreate();
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        getItemsFromTheme(attrs);
        onCreate();
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getItemsFromTheme(attrs);
        onCreate();
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        getItemsFromTheme(attrs);
        onCreate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        String text = "x = " + String.valueOf(x) + "\ny = " + String.valueOf(y);
        MainActivityFragment.setText_2(text);
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (mMainPatternView.areItemsEnabled()){
                    checkItemInPosition(x, y);
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (mMainPatternView.areItemsEnabled()){
                    checkItemInPosition(x, y);
                    mItemTouchLock_currentX = x;
                    mItemTouchLock_currentY = y;
                    invalidate();
                    return true;
                }
            case MotionEvent.ACTION_UP:
                checkEnteredPattern();
                drawPath.reset();
                mItemTouchLock_currentX = mItemsCoordX[mLastPatternElement];
                mItemTouchLock_currentY = mItemsCoordY[mLastPatternElement] - mStatusBarHeight;
                invalidate();
                return true;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
            super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        mScreenHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mItemTouchLock_prevX != 0 && mItemTouchLock_prevY != 0){
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawLine(mItemTouchLock_prevX, mItemTouchLock_prevY,
                    mItemTouchLock_currentX, mItemTouchLock_currentY, drawPaint);
        }
    }

    public void setActivityAndInterface(Activity a, LPV_Interface interf){
        mInterface = interf;
        mActivity = a;
    }

    public void setMainPatternViewData(ArrayList<ImageView> al, float[] x, float[] y, float itemWidth,
                                       float itemHeight, float density){
        mAllItems = al;
        mItemsCoordX = x;
        mItemsCoordY = y;
        mPatternItemWidth = itemWidth;
        mPatternItemHeight = itemHeight;
        mDisplayDensity = density;
    }



    public void setCurrentLockStatus(int currentLockStatus){
        mCurrentLockStatus = currentLockStatus;
    }

    public int getCurrentLockStatus(){
        return mCurrentLockStatus;
    }

    public void clearPassStrings(){
        mPassSetStr = mPassConfirmStr = "";
    }

    public LPV_SharedPreferences getLPV_ShPrefs(){
        return mLPV_ShPrefs;
    }

    public String getPassConfirm(){
        return mPassConfirmStr;
    }

    public String getSetPass(){
        String pass = "";
        for (int i = 0; i < mItemTouched.size(); i++) {
            pass += mItemTouched.get(i).getTag();
        }
        return pass;
    }

    public void clearPathBitmap(){
        if (mScreenWidth == 0 || mScreenHeight == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearPathBitmap();
                }
            }, 50);
        }else {
            setViewDefault();
            canvasBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }
    }

    public void forgotPassSuccessful(){
        mCurrentLockStatus = SET_PATTERN;
        mStatusTitle.setTitleText(mStatusTitle.mStatus_SetNewPatter);
        mForgotPassTitle.setVisibility(GONE);
        showSnackBar(mForgotPassSuccessStr);
    }

    public void forgotPassFailed(){
        showSnackBar(mForgotPassFailedStr);
    }

    private void getItemsFromTheme(AttributeSet attrs){
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LockPatternView, 0, 0);
        try {
            mItemColor = a.getInteger(R.styleable.LockPatternView_itemColor_default, 0);
        } finally {
            a.recycle();
        }
    }

    private void onCreate() {
        initItems();

        createAndAddMainPatternView();
        createAndAddTitleStatusTextView();
        createAndAddBottomButtons();
        createForgotPassView();

        checkSavedPass();
    }

    private void initItems(){
        mLPV = this;

        mVibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);

        getStatusBarHeight();

        mCurrentLockStatus = SET_PATTERN;

        drawPaint = new Paint();
        drawPath = new Path();

        drawPaint.setColor(ContextCompat.getColor(mContext, mPathColor));
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        setWillNotDraw(false);
        clearPathBitmap();
        mPatternToShort = String.format(mContext.getString(R.string.lpv_tv_shortPattern), mItemCountMin);
        mForgotPassFailedStr = mContext.getString(R.string.lpv_snack_forgotPassFailed);
        mForgotPassSuccessStr = mContext.getString(R.string.lpv_snack_forgotPassSuccess);
    }

    private void getStatusBarHeight(){
        mStatusBarHeight = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0){
            mStatusBarHeight = getResources().getDimensionPixelSize(resId);
        }
    }

    private void checkSavedPass(){
        mPassSetStr = getSavedPass();
        boolean patternExist = false;
        if (mPassSetStr.length() >= mItemCountMin){
            mCurrentLockStatus = CHECK_PATTERN;
            patternExist = true;
            mStatusTitle.setTitleText(mStatusTitle.mStatus_EnterPattern);
            mForgotPassTitle.setVisibility(VISIBLE);
        } else {
            mStatusTitle.setTitleText(mStatusTitle.mStatus_SetNewPatter);
        }
        sendInterfacePassExist(patternExist);
    }

    private void sendInterfacePassExist(final boolean patternExist) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterface != null) {
                    mInterface.patternExist(patternExist);
                } else {
                    sendInterfacePassExist(patternExist);
                }
            }
        }, 50);
    }

    private void createAndAddMainPatternView(){
        mMainPatternView = new MainPatternView(mContext);
        mMainPatternView.setAdditions(mLPV);
        addView(mMainPatternView);
    }

    private void createAndAddTitleStatusTextView(){
        mStatusTitle = new StatusTextView(mContext);
        mStatusTitle.setAdditions(mMainPatternView);
        addView(mStatusTitle);
    }

    private void createAndAddBottomButtons(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity == null) {
                    createAndAddBottomButtons();
                } else {
                    mBottomButtonsLayout = new BottomButtonsLayout(mContext);
                    mBottomButtonsLayout.setAdditions(mActivity, mLPV, mInterface);

                    addView(mBottomButtonsLayout);
                }
            }
        }, 50);
    }

    private void createForgotPassView(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity == null) {
                    createForgotPassView();
                } else {
                    mForgotPassTitle = new ForgotPassTextView(mContext);
                    mForgotPassTitle.setAdditions(mMainPatternView, mLPV, mActivity);
                    addView(mForgotPassTitle);
                }
            }
        }, 5);
    }



    // TODO: 1/25/16   START - onTouchEven action
    private void checkItemInPosition( float x, float y){
        for (int i = 0; i < mAllItems.size(); i++) {
            if (mAllItems.get(i).isEnabled()){
                float currentX_min = mItemsCoordX[i] - mPatternItemWidth/2;
                float currentX_max = mItemsCoordX[i] + mPatternItemWidth/2;
                float currentY_min = mItemsCoordY[i] - mPatternItemHeight/2;
                float currentY_max = mItemsCoordY[i] + mPatternItemHeight/2;

                if (x > currentX_min && x < currentX_max &&
                        y > currentY_min && y < currentY_max){

                    mLastPatternElement = i;
                    int xx = (int) mItemsCoordX[i];
                    int yy = (int) mItemsCoordY[i];

                    if (mItemTouched.size() == 0){
                        drawPath.moveTo(xx, yy - mStatusBarHeight);
                    } else {
                        drawPath.lineTo(xx, yy - mStatusBarHeight);
                    }
                    drawCanvas.drawPath(drawPath, drawPaint);

                    mItemTouchLock_prevX = xx;
                    mItemTouchLock_prevY = yy - mStatusBarHeight;

                    mVibrator.vibrate(mShortVibrate);
                    setItemIsActive(i);
                }
            }
        }
    }

    private void setItemIsActive(int position){
        ImageView iv = mAllItems.get(position);
        makeItemBig(iv);
        iv.setEnabled(false);
        iv.setImageResource(mItemBgActive);
        mItemTouched.add(iv);
    }

    private void makeItemBig(final ImageView v){
        v.animate()
                .scaleX(mItemMaxScale).scaleY(mItemMaxScale)
                .setDuration(mItemAnimDur)
                .start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.animate()
                        .scaleX(mItemNormalScale).scaleY(mItemNormalScale)
                        .setDuration(mItemAnimDur)
                        .start();
            }
        }, mItemAnimDur);
    }

    private void checkEnteredPattern(){
        if (mItemTouched.size() > 1 && mItemTouched.size() < mItemCountMin){
            patternToShort();
        } else if (mItemTouched.size() >= mItemCountMin){
            patternIsSet();
        }
    }

    private void showSnackBar(String mess){
        Snackbar.make(mMainPatternView, mess, Snackbar.LENGTH_LONG).show();
    }

    private void setViewDefault() {
        // TODO: 1/25/16 добавить флаг дефолтВьюРеади
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mItemTouched.size(); i++) {
                    ImageView iv = mItemTouched.get(i);
                    iv.setEnabled(true);
                    iv.animate()
                            .scaleX(mItemNormalScale).scaleY(mItemNormalScale)
                            .setDuration(mItemAnimDur)
                            .start();
                    iv.setImageResource(mItemBgDefault);
                }
                mItemTouched.clear();
                mMainPatternView.setBackgroundResource(mLpvBgDefault);
            }
        }, mLongVibrate);
    }

    private void patternToShort(){
        showSnackBar(mPatternToShort);
        patternError();
        clearPathBitmap();
    }

    private void patternError(){
        mMainPatternView.setBackgroundResource(mLpvBgError);
        mVibrator.vibrate(mLongVibrate);
    }

    private void patternIsSet(){
        if (mCurrentLockStatus == SET_PATTERN){
            mPassSetStr = getSetPass();
            mBottomButtonsLayout.setBottomButtonsVisibility(true);
        } else if (mCurrentLockStatus == CONFIRM_PATTERN){
            mPassConfirmStr = getSetPass();
            if (mPassConfirmStr.equals(mPassSetStr)){
                mBottomButtonsLayout.setBottomButtonsVisibility(true);
            } else {
                mInterface.patternConfirmFailed(mPassConfirmStr);
                patternError();
                mCurrentLockStatus = SET_PATTERN;
                mStatusTitle.setTitleText(mStatusTitle.mStatus_SetNewPatter);
                clearPassStrings();
                clearPathBitmap();
            }
        } else if(mCurrentLockStatus == CHECK_PATTERN){
            mPassConfirmStr = getSetPass();
            if (mPassConfirmStr.equals(mPassSetStr)){
                mInterface.patternConfirmedSuccess(false, mPassConfirmStr, "");
            }  else {
                mInterface.patternConfirmFailed(mPassConfirmStr);
                patternError();
            }
            mPassConfirmStr = "";
            clearPathBitmap();
        }
    }

    private String getSavedPass(){
        getLPV_SharedPreferences();

        return mLPV_ShPrefs.getMainSavedPass();
    }

    private void getLPV_SharedPreferences(){
        if (mLPV_ShPrefs == null){
            mLPV_ShPrefs = new LPV_SharedPreferences(mContext);
        }
    }

    // TODO: 1/25/16   END - onTouchEvent action
}
