package com.andriyantonov.lockpatternt;

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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andriyantonov.lockpatternt.lock.view.LPV_ForgotPassDialog;
import com.andriyantonov.lockpatternt.lock.view.LPV_Interface;
import com.andriyantonov.lockpatternt.lock.view.LPV_SecondPassDialog;
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

    private LockPatternView mLPV;
    private LPV_Interface mInterface;
    private LPV_SharedPreferences mSharedPreferences;
    private BottomButtonsLayout mBottomButtonsLayout;
    private Context mContext;
    private Button mBtnCancel, mBtnConfirm;
    private Bitmap mPatternBitmap;
    private Canvas mPatternCanvas;
    private Paint mLinePaint;
    private Paint mPatternPaint;
    private Path mPatternPAth;
    private Vibrator mVibrator;
    private Bitmap mItemBitmapNormal;
    private Bitmap mItemBitmapTouched;
    private Bitmap mItemBitmapError;
    private ArrayList<ImageView> mAllItems = new ArrayList<>();
    private ArrayList<ImageView> mItemTouched = new ArrayList<>();
    private int mStatusTitleColor = R.color.lpv_white_100;
    private int mItemColorNormal = R.color.item_bg_def;
    private int mItemColorTouched = R.color.item_bg_enter;
    private int mItemColorError = R.color.lpv_bg_error;
    private int mLpvBgDefault = R.drawable.lpv_bg_default;
    private int mLpvBgError = R.drawable.lpv_bg_error;
    private int mPathColor = R.color.item_bg_enter;
    private int mForgotPassColor = R.color.lpv_white_100;
    private int mItemAnimDur = 100;
    private int mItemCountMin = 4;
    private int mVibrateShort = 10;
    private int mVibrateLong = 200;
    private int mHorizontalItemsCount = 3;
    private int mVerticalItemsCount = 3;
    private int mItemRadius = 15;
    private int mCurrentLockStatus;
    private int mStatusBarHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mLastPatternElement;
    private int mItemStartPosition;
    private int mMainPatternViewSize;
    private int mMatrixSize;
    private float[] mItemsCoordX;
    private float[] mItemsCoordY;
    private float mItemMaxScale = 4f;
    private float mItemNormalScale = 1f;
    private float mPatternItemWidth;
    private float mPatternItemHeight;
    private float mDisplayDensity;
    private float mItemTouchLock_prevX;
    private float mItemTouchLock_prevY;
    private float mItemTouchLock_currentY;
    private float mItemTouchLock_currentX;
    private String mPassSetStr;
    private String mPassConfirmStr;
    private String mPatternToShort;
    private String mForgotPassFailedStr;
    private String mForgotPassSuccessStr;
    private String mStatus_SetNewPatter;
    private String mStatus_PatterError;
    private String mStatus_ConfirmPattern;
    private String mStatus_PatternConfirmed;
    private String mForgotPassStr;

    public LockPatternView(Context context) {
        super(context);
        mContext = context;
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getItemsFromTheme(attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getItemsFromTheme(attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        getItemsFromTheme(attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()){
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
                mPatternPAth.reset();
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


        if (w>h){
            Toast.makeText(mContext, "розверни телефон!", Toast.LENGTH_LONG).show();
        } else {
            onCreateLockPatternView();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mItemTouchLock_prevX != 0 && mItemTouchLock_prevY != 0){
            canvas.drawBitmap(mPatternBitmap, 0, 0, mPatternPaint);
            canvas.drawLine(mItemTouchLock_prevX, mItemTouchLock_prevY,
                    mItemTouchLock_currentX, mItemTouchLock_currentY, mLinePaint);
        }
    }

    public void setActivityAndInterface(Context c, LPV_Interface interf){
        mInterface = interf;
        mContext = c;
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
        return mSharedPreferences;
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
            mPatternBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
            mPatternCanvas = new Canvas(mPatternBitmap);
        }
    }

    public void forgotPassSuccessful(){
        mCurrentLockStatus = SET_PATTERN;
        mStatusTitle.setText(mStatus_SetNewPatter);
        mForgotPassTitle.setVisibility(GONE);
        showSnackBar(mForgotPassSuccessStr);
    }

    public void forgotPassFailed(){
        showSnackBar(mForgotPassFailedStr);
    }

    private void getItemsFromTheme(AttributeSet attrs){
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv, 0, 0);
        try {
            int i = a.getInteger(R.styleable.lpv_itemColorDefault, 0);
            if (i != 0){
                mItemColorNormal = i;
            } else {
                mItemColorNormal = ContextCompat.getColor(mContext, mItemColorNormal);
            }
            String f = a.getString(R.styleable.lpv_forgotPassStr);
            if (f != null){
                mForgotPassStr = f;
            }
        } finally {
            a.recycle();
        }
    }

    private void onCreateLockPatternView() {
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

        mLinePaint = new Paint();
        mPatternPAth = new Path();
        mLinePaint.setColor(ContextCompat.getColor(mContext, mPathColor));
        mLinePaint.setStrokeWidth(10);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mPatternPaint = new Paint(Paint.DITHER_FLAG);
        setWillNotDraw(false);
        clearPathBitmap();


        mPatternToShort = String.format(mContext.getString(R.string.lpv_tv_shortPattern), mItemCountMin);
        mForgotPassFailedStr = mContext.getString(R.string.lpv_snack_forgotPassFailed);
        mForgotPassSuccessStr = mContext.getString(R.string.lpv_snack_forgotPassSuccess);

        mDisplayDensity = mContext.getResources().getDisplayMetrics().density;
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
            mForgotPassTitle.setVisibility(VISIBLE);
        } else {
            mStatusTitle.setText(mStatus_SetNewPatter);
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
        addView(mMainPatternView);
    }

    private void createAndAddTitleStatusTextView(){
        mStatusTitle = new StatusTextView(mContext);
        addView(mStatusTitle);
    }

    private void createAndAddBottomButtons(){
        mBottomButtonsLayout = new BottomButtonsLayout(mContext);
        addView(mBottomButtonsLayout);
    }

    private void createForgotPassView(){
        mForgotPassTitle = new ForgotPassTextView(mContext);
        addView(mForgotPassTitle);
    }

    public void secondPassConfirmed(String secondPass){
        String confirmPass = mLPV.getPassConfirm();
        mInterface.patternConfirmedSuccess(true, confirmPass, secondPass);
        savePass(confirmPass, secondPass);
        mLPV.setCurrentLockStatus(mLPV.CHECK_PATTERN);
        mBottomButtonsLayout.setBottomButtonsVisibility(false);
    }

    public void secondPassDismissed(){
        mBottomButtonsLayout.onCancelPatternListener.onClick(mBtnCancel);
    }

    private void savePass(String confirmPass, String secondPass){
        if (mSharedPreferences == null){
            mSharedPreferences = mLPV.getLPV_ShPrefs();
        }
        mSharedPreferences.saveMainPass(confirmPass);
        mSharedPreferences.saveSecondPass(secondPass);
        mLPV.clearPassStrings();
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
                        mPatternPAth.moveTo(xx, yy - mStatusBarHeight);
                    } else {
                        mPatternPAth.lineTo(xx, yy - mStatusBarHeight);
                    }
                    mPatternCanvas.drawPath(mPatternPAth, mLinePaint);

                    mItemTouchLock_prevX = xx;
                    mItemTouchLock_prevY = yy - mStatusBarHeight;

                    mVibrator.vibrate(mVibrateShort);
                    setItemIsActive(i);
                }
            }
        }
    }

    private void setItemIsActive(int position){
        ImageView iv = mAllItems.get(position);
        makeItemBig(iv);
        iv.setEnabled(false);
        iv.setImageBitmap(mItemBitmapTouched);
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
        int size = mItemTouched.size();
        if (size == 1){
            setViewDefault();
        } else if (size > 1 && size < mItemCountMin){
            patternToShort();
        } else if (size >= mItemCountMin){
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
                    iv.setImageBitmap(mItemBitmapNormal);
                }
                mItemTouched.clear();
                mMainPatternView.setBackgroundResource(mLpvBgDefault);
            }
        }, mVibrateLong);
    }

    private void patternToShort(){
        showSnackBar(mPatternToShort);
        patternError();
        clearPathBitmap();
        setViewDefault();
    }

    private void patternError(){
        mMainPatternView.setBackgroundResource(mLpvBgError);
        mVibrator.vibrate(mVibrateLong);
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
                mStatusTitle.setText(mStatus_SetNewPatter);
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

        return mSharedPreferences.getMainSavedPass();
    }

    private void getLPV_SharedPreferences(){
        if (mSharedPreferences == null){
            mSharedPreferences = new LPV_SharedPreferences(mContext);
        }
    }

    // TODO: 1/25/16   END - onTouchEvent action

    private class MainPatternView extends LinearLayout {

        private LinearLayout.LayoutParams mPatternRowParams, mPatternItemParams;

        private Paint mItemPaint;
        private Bitmap.Config mItemBitmapConfig;
        private int mPatternItemMargin = 16;
        private int mPatternItemPadding = 30;
        private float mPatternItemWidth;
        private float mPatternItemHeight;

        private boolean mAllItemsIsEnable = true;

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

        public int getMainPatternViewSize(){
            return mMainPatternViewSize;
        }

        public void setAllItemsEnable(final boolean enable){
            mAllItemsIsEnable = enable;
            if (!enable){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mAllItems.size(); i++) {
                            mAllItems.get(i).setEnabled(enable);
                        }
                    }
                }, mLPV.mVibrateLong *2);
            } else {
                for (int i = 0; i < mAllItems.size(); i++) {
                    mAllItems.get(i).setEnabled(enable);
                }
            }
        }

        public boolean areItemsEnabled(){
            return mAllItemsIsEnable;
        }

        private void onCreateMainPatternView(){
            calculateItemsData();



            setId((int) System.currentTimeMillis());
            setOrientation(LinearLayout.VERTICAL);
            setBackgroundResource(mLpvBgDefault);

            for (int i = 0; i < mHorizontalItemsCount; i++) {
                addView(addNewPatternRow());
            }

            setMainPatternViewLayoutParams();

            calculateItemsCoordinates();
        }

        private LinearLayout addNewPatternRow(){
            LinearLayout row = new LinearLayout(mContext);
            row.setOrientation(LinearLayout.HORIZONTAL);
            if (mPatternRowParams == null){
                mPatternRowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            }
            mPatternRowParams.weight = 1f;
            row.setLayoutParams(mPatternRowParams);
            for (int i = 0; i < mVerticalItemsCount; i++) {
                row.addView(addPatternNewItem());
            }
            return row;
        }

        private ImageView addPatternNewItem(){
            ImageView item = new ImageView(mContext);
            item.setImageBitmap(mItemBitmapNormal);
            int scale = 2;
            if (mPatternItemParams == null){
                mPatternItemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mPatternItemParams.gravity = Gravity.CENTER_VERTICAL;
                mPatternItemMargin = Math.round(mPatternItemMargin * mDisplayDensity);
                mPatternItemParams.setMargins(mPatternItemMargin, mPatternItemMargin, mPatternItemMargin, mPatternItemMargin);
            }

            mPatternItemParams.weight = 1f;

            item.setPadding(mPatternItemPadding, mPatternItemPadding * scale,
                    mPatternItemPadding, mPatternItemPadding * scale);
            item.setLayoutParams(mPatternItemParams);

            mAllItems.add(item);
            item.setTag(String.valueOf(mItemStartPosition));
            mItemStartPosition++;

            return item;
        }

        private void setMainPatternViewLayoutParams(){
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(mMainPatternViewSize, mMainPatternViewSize);
            rlp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            setLayoutParams(rlp);
        }

        private void calculateItemsData(){
            mItemPaint = new Paint();
            mItemPaint.setAntiAlias(true);
            mItemBitmapConfig = Bitmap.Config.ARGB_8888;
            int size = mItemRadius * 2;
            mItemBitmapNormal = Bitmap.createBitmap(size, size, mItemBitmapConfig);
            mItemBitmapTouched = Bitmap.createBitmap(size, size, mItemBitmapConfig);
            mItemBitmapError = Bitmap.createBitmap(size, size, mItemBitmapConfig);

            mItemColorTouched = ContextCompat.getColor(mContext, mItemColorTouched);

            drawItemNormal();
            drawItemTouched();
            drawItemError();
            //////////////

            mMatrixSize = mHorizontalItemsCount * mVerticalItemsCount;
            mItemsCoordX = new float[mMatrixSize];
            mItemsCoordY = new float[mMatrixSize];
            /////////////
            mDisplayDensity = mContext.getResources().getDisplayMetrics().density;
            /////////////
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            mMainPatternViewSize = width - width/5;
            /////////////
        }

        private void drawItemNormal(){
            mItemPaint.setColor(mItemColorNormal);
            Canvas canvas = new Canvas(mItemBitmapNormal);
            canvas.drawCircle(mItemRadius, mItemRadius, mItemRadius, mItemPaint);
        }

        private void drawItemTouched(){
            mItemPaint.setColor(mItemColorTouched);
            Canvas canvas = new Canvas(mItemBitmapTouched);
            canvas.drawCircle(mItemRadius, mItemRadius, mItemRadius, mItemPaint);
        }

        private void drawItemError(){
            mItemPaint.setColor(mItemColorError);
            Canvas canvas = new Canvas(mItemBitmapError);
            canvas.drawCircle(mItemRadius, mItemRadius, mItemRadius, mItemPaint);
        }

        private void calculateItemsCoordinates(){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculateItemSize();
                    String text = "";
                    for (int i = 0; i < mMatrixSize; i++) {
                        int[] loc = new int[2];
                        mAllItems.get(i).getLocationInWindow(loc);

                        mItemsCoordX[i] = loc[0] + mPatternItemWidth / 2;
                        mItemsCoordY[i] = loc[1] + mPatternItemHeight / 2;

                        text = text + "x = " + String.valueOf(mItemsCoordX[i]) + "\ny = " + String.valueOf(mItemsCoordY[i]) + "\n=========\n";
                    }

                    if (mItemsCoordX[0] == 0) {
                        calculateItemsCoordinates();
                    } else {
                        sendMainPatternViewData();
                        MainActivityFragment.setText(text);
                    }
                }
            }, 50);
        }

        private void calculateItemSize(){
            if (mPatternItemWidth == 0 || mPatternItemHeight == 0){
                mPatternItemWidth = mAllItems.get(0).getWidth();
                mPatternItemHeight = mAllItems.get(0).getHeight();
            }
        }

        private void sendMainPatternViewData(){
            mLPV.setMainPatternViewData(mAllItems, mItemsCoordX, mItemsCoordY, mPatternItemWidth,
                    mPatternItemHeight, mDisplayDensity);
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
            initTitleStrings();

            float textSize = mContext.getResources().getDimension(R.dimen.lpv_tv_titleSize)/mDisplayDensity;
            setGravity(Gravity.CENTER_HORIZONTAL);
            setTextSize(textSize);
            setTextColor(ContextCompat.getColor(mContext, mStatusTitleColor));

            setLayoutParams();
        }

        private void initTitleStrings(){
            mStatus_SetNewPatter = mContext.getString(R.string.lpv_tv_statusTitle_setNewPattern);
            mStatus_ConfirmPattern = mContext.getString(R.string.lpv_tv_statusTitle_confirmPattern);
            mStatus_PatternConfirmed = mContext.getString(R.string.lpv_tv_statusTitle_patternConfirmed);
            mStatus_PatterError = mContext.getString(R.string.lpv_tv_statusTitle_patternError);
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

    private class ForgotPassTextView extends TextView implements View.OnClickListener {

        private LPV_ForgotPassDialog mForgotDialog;

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
            initTitleStrings();

            mDisplayDensity = getResources().getDisplayMetrics().density;

            float textSize = mContext.getResources().getDimension(R.dimen.lpv_tv_forgotPassSize)/mDisplayDensity;
            setGravity(Gravity.CENTER_HORIZONTAL);
            setTextSize(textSize);
            setTextColor(ContextCompat.getColor(mContext, mForgotPassColor));
            setText(mForgotPassStr);
            setVisibility(GONE);
            setOnClickListener(this);

            setLayoutParams();
        }

        private void initTitleStrings(){
            if (mForgotPassStr == null){
                mForgotPassStr = mContext.getString(R.string.lpv_tv_forgotPass_title);
            }
        }

        private void setLayoutParams(){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int m = (int) mContext.getResources().getDimension(R.dimen.activity_vertical_margin);

            lp.setMargins(m, m, m, m);
            lp.addRule(CENTER_HORIZONTAL, TRUE);
            lp.addRule(BELOW, mMainPatternView.getId());
            setLayoutParams(lp);
            setPadding(m, m, m, m);
        }

        @Override
        public void onClick(View v) {
            if (mForgotDialog == null){
                mForgotDialog = new LPV_ForgotPassDialog(mContext, mLPV);
            }
            mForgotDialog.show();
        }
    }

    private class BottomButtonsLayout extends LinearLayout {

        private LPV_SecondPassDialog mSecondPassDialog;
        private String mStrCancel, mStrConfirm, mStrRepeat;
        private int mMargin =  16;

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
                mLPV.mMainPatternView.setAllItemsEnable(false);
            } else {
                mBtnCancel.setVisibility(GONE);
                mBtnConfirm.setVisibility(GONE);
                mLPV.mMainPatternView.setAllItemsEnable(true);
            }
        }

        private void onCreateBottomButtonsLayout(){
            initButtonsText();
            setLayoutParams();
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
                mLPV.mStatusTitle.setText(mStatus_SetNewPatter);
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
                    mLPV.mStatusTitle.setText(mStatus_ConfirmPattern);
                } else if (currentStatus == mLPV.CONFIRM_PATTERN){
                    mLPV.mStatusTitle.setText(mStatus_PatternConfirmed);
                    if (mSecondPassDialog == null){
                        mSecondPassDialog = new LPV_SecondPassDialog(mContext,  mLPV);
                    }
                    mSecondPassDialog.show();
                }
                setBottomButtonsVisibility(false);
                mLPV.clearPathBitmap();
            }
        };

    }

}
