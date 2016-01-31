package com.andriyantonov.lockpatternt.lock.view.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andriyantonov.lockpatternt.MainActivityFragment;
import com.andriyantonov.lockpatternt.R;

import java.util.ArrayList;

/**
 * Created by pro100svitlo on 1/29/16.
 */
public class MainPatternView extends LinearLayout {

    private Context mContext;
    private LockPatternView mLPV;

    private ArrayList<ImageView> mAllItems = new ArrayList<>();

    private LinearLayout.LayoutParams mPatternRowParams, mPatternItemParams;

    private Bitmap mItemBitmap;
    private Paint mPaint;
    private Bitmap.Config mBitmapConfig;
    private int mItemColorDefault = R.color.item_bg_def;
    private int mItemBgDefault = R.drawable.item_bg_default;
    private int mLpvBgDefault = R.drawable.lpv_bg_default;
    private int mLpvBgError = R.drawable.lpv_bg_error;
    private int mPatternItemMargin = 16;
    private int mPatternItemPadding = 30;
    private int mHorizontalItemsCount = 3;
    private int mVerticalItemsCount = 3;
    private int mItemRadius = 15;
    private int mItemStartPosition;
    private int mMainPatternViewSize;
    private int mMatrixSize;

    private float mDisplayDensity;
    private float mPatternItemWidth;
    private float mPatternItemHeight;
    private float[] mItemsCoordX;
    private float[] mItemsCoordY;

    private boolean mAllItemsIsEnable = true;

    public MainPatternView(Context context) {
        super(context);
        mContext = context;
    }

    public MainPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        onCreate();
    }

    public MainPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        onCreate();
    }

    public MainPatternView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        onCreate();
    }

    public void setAdditions(LockPatternView lpv){
        mLPV = lpv;
        onCreate();
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
            }, mLPV.mLongVibrate *2);
        } else {
            for (int i = 0; i < mAllItems.size(); i++) {
                mAllItems.get(i).setEnabled(enable);
            }
        }
    }

    public boolean areItemsEnabled(){
        return mAllItemsIsEnable;
    }

    public void setItemColorDefault(int color){
        mItemColorDefault = color;
        invalidate();
    }

    private void onCreate(){
        calculateItemsData();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBitmapConfig = Bitmap.Config.ARGB_8888;
        mPaint.setColor(ContextCompat.getColor(mContext, mItemColorDefault));
        int size = mItemRadius * 2;
        mItemBitmap = Bitmap.createBitmap(size, size, mBitmapConfig);
        Canvas canvas = new Canvas(mItemBitmap);
        canvas.drawCircle(mItemRadius, mItemRadius, mItemRadius, mPaint);

        createMainPatternView();
        setMainPatternViewLayoutParams();

        calculateItemsCoordinates();
    }

    private void createMainPatternView(){
        mDisplayDensity = mContext.getResources().getDisplayMetrics().density;
        setId((int) System.currentTimeMillis());
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(mLpvBgDefault);

        for (int i = 0; i < mHorizontalItemsCount; i++) {
            addView(addNewPatternRow());
        }
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
//        item.setImageResource(mItemBgDefault);
        item.setImageBitmap(mItemBitmap);
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


//package com.andriyantonov.lockpatternt.lock.view.view;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.andriyantonov.lockpatternt.MainActivityFragment;
//import com.andriyantonov.lockpatternt.R;
//
//import java.util.ArrayList;
//
///**
// * Created by pro100svitlo on 1/29/16.
// */
//public class MainPatternView extends LinearLayout {
//
//    private Context mContext;
//    private LockPatternView mLPV;
//
//    private ArrayList<ImageView> mAllItems = new ArrayList<>();
//
//    private LinearLayout.LayoutParams mPatternRowParams, mPatternItemParams;
//
//    private int mItemBgDefault = R.drawable.item_bg_default;
//    private int mLpvBgDefault = R.drawable.lpv_bg_default;
//    private int mLpvBgError = R.drawable.lpv_bg_error;
//    private int mPatternItemMargin = 16;
//    private int mPatternItemPadding = 30;
//    private int mHorizontalItemsCount = 3;
//    private int mVerticalItemsCount = 3;
//    private int mItemStartPosition;
//    private int mMainPatternViewSize;
//    private int mStatusBarHeight;
//    private int mMatrixSize;
//
//    private float mDisplayDensity;
//    private float mPatternItemWidth;
//    private float mPatternItemHeight;
//    private float[] mItemsCoordX;
//    private float[] mItemsCoordY;
//
//    private boolean mAllItemsIsEnable = true;
//
//    public MainPatternView(Context context) {
//        super(context);
//        mContext = context;
//        onCreate();
//    }
//
//    public MainPatternView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mContext = context;
//        onCreate();
//    }
//
//    public MainPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mContext = context;
//        onCreate();
//    }
//
//    public MainPatternView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        mContext = context;
//        onCreate();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//    }
//
//    public void onCreate(LockPatternView lpv){
//        mLPV = lpv;
//    }
//
//    public int getMainPatternViewSize(){
//        return mMainPatternViewSize;
//    }
//
//    public void setAllItemsEnable(final boolean enable){
//        mAllItemsIsEnable = enable;
//        if (!enable){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < mAllItems.size(); i++) {
//                        mAllItems.get(i).setEnabled(enable);
//                    }
//                }
//            }, mLPV.mLongVibrate *2);
//        } else {
//            for (int i = 0; i < mAllItems.size(); i++) {
//                mAllItems.get(i).setEnabled(enable);
//            }
//        }
//    }
//
//    public boolean areItemsEnabled(){
//        return mAllItemsIsEnable;
//    }
//
//    private void onCreate(){
//        calculateItemsData();
//        createMainPatternView();
//        setMainPatternViewLayoutParams();
//
//        calculateItemsCoordinates();
//    }
//
//    private void createMainPatternView(){
//        mDisplayDensity = mContext.getResources().getDisplayMetrics().density;
//        setId((int) System.currentTimeMillis());
//        setOrientation(LinearLayout.VERTICAL);
//        setBackgroundResource(mLpvBgDefault);
//
//        for (int i = 0; i < mHorizontalItemsCount; i++) {
//            addView(addNewPatternRow());
//        }
//    }
//
//    private LinearLayout addNewPatternRow(){
//        LinearLayout row = new LinearLayout(mContext);
//        row.setOrientation(LinearLayout.HORIZONTAL);
//        if (mPatternRowParams == null){
//            mPatternRowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
//        }
//        mPatternRowParams.weight = 1f;
//        row.setLayoutParams(mPatternRowParams);
//        for (int i = 0; i < mVerticalItemsCount; i++) {
//            row.addView(addPatternNewItem());
//        }
//        return row;
//    }
//
//    private ImageView addPatternNewItem(){
//        ImageView item = new ImageView(mContext);
//        item.setImageResource(mItemBgDefault);
//        int scale = 2;
//        if (mPatternItemParams == null){
//            mPatternItemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            mPatternItemParams.gravity = Gravity.CENTER_VERTICAL;
//            mPatternItemMargin = Math.round(mPatternItemMargin * mDisplayDensity);
//            mPatternItemParams.setMargins(mPatternItemMargin, mPatternItemMargin, mPatternItemMargin, mPatternItemMargin);
//        }
//
//        mPatternItemParams.weight = 1f;
//
//        item.setPadding(mPatternItemPadding, mPatternItemPadding * scale,
//                mPatternItemPadding, mPatternItemPadding * scale);
//        item.setLayoutParams(mPatternItemParams);
//
//        mAllItems.add(item);
//        item.setTag(String.valueOf(mItemStartPosition));
//        mItemStartPosition++;
//        return item;
//    }
//
//    private void setMainPatternViewLayoutParams(){
//        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(mMainPatternViewSize, mMainPatternViewSize);
//        rlp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        setLayoutParams(rlp);
//    }
//
//    private void calculateItemsData(){
//        mMatrixSize = mHorizontalItemsCount * mVerticalItemsCount;
//        mItemsCoordX = new float[mMatrixSize];
//        mItemsCoordY = new float[mMatrixSize];
//        /////////////
//        mDisplayDensity = mContext.getResources().getDisplayMetrics().density;
//        /////////////
//        DisplayMetrics dm = new DisplayMetrics();
//        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        mMainPatternViewSize = width - width/5;
//        /////////////
//    }
//
//    private void calculateItemsCoordinates(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                calculateItemSize();
//                String text = "";
//                for (int i = 0; i < mMatrixSize; i++) {
//                    int[] loc = new int[2];
//                    mAllItems.get(i).getLocationInWindow(loc);
//
//                    mItemsCoordX[i] = loc[0] + mPatternItemWidth / 2;
//                    mItemsCoordY[i] = loc[1] + mPatternItemHeight / 2;
//
//                    text = text + "x = " + String.valueOf(mItemsCoordX[i]) + "\ny = " + String.valueOf(mItemsCoordY[i]) + "\n=========\n";
//                }
//
//                if (mItemsCoordX[0] == 0) {
//                    calculateItemsCoordinates();
//                } else {
//                    sendMainPatternViewData();
//                    MainActivityFragment.setText(text);
//                }
//            }
//        }, 50);
//    }
//
//    private void calculateItemSize(){
//        if (mPatternItemWidth == 0 || mPatternItemHeight == 0){
//            mPatternItemWidth = mAllItems.get(0).getWidth();
//            mPatternItemHeight = mAllItems.get(0).getHeight();
//        }
//    }
//
//    private void sendMainPatternViewData(){
//        mLPV.setMainPatternViewData(mAllItems, mItemsCoordX, mItemsCoordY, mPatternItemWidth,
//                mPatternItemHeight, mDisplayDensity);
//    }
//
//
//}
