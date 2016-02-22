package com.pro100svitlo.lockpattern;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pro100svitlo.lockpattern.interfaces.SecondPassDialogInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pro100svitlo on 1/29/16.
 */
public class LPV_Dialog {

    public static class Builder{

        private final Context mContext;
        private final LockPatternView mLPV;
        private final SecondPassDialogInterface mInterfaceSPD;
        private int mMarginLeftRight = 24;
        private int mMarginTopBottom = 16;
        private int mMinAnswerLength;
        private int mMaxAnswerLength;
        private int mTitleColor = R.color.colorPrimary;
        private int mMessageColor = R.color.colorPrimary;
        private int mTextColor = R.color.lpv_gray;
        private int mButtonsColor = R.color.colorAccent;
        private int mRadioBtnColor = R.color.colorAccent;
        private final int mDialogType;
        private String[] mQuestionsArray;
        private String mTitleStr;
        private String mMessageStr;
        private String mPositiveStr;
        private String mNegativeStr;

        public Builder(Context c, LockPatternView lp, float density, int dialogType,
                       SecondPassDialogInterface interf){
            mContext = c;
            mLPV = lp;
            mDialogType = dialogType;
            mInterfaceSPD = interf;

            getDialogItems(density);
        }

        public Builder setTitleColor(int c){
            if (c != 0){
                mTitleColor = c;
            }
            return this;
        }

        public Builder setMessageColor(int c){
            if (c != 0){
                mMessageColor = c;
            }
            return this;
        }

        public Builder setTextColor(int c){
            if (c != 0){
                mTextColor = c;
            }
            return this;
        }

        public Builder setButtonsColor(int c){
            if (c != 0){
                mButtonsColor = c;
            }
            return this;
        }

        public Builder setRadioButtonsColor(int c){
            if (c != 0){
                mRadioBtnColor = c;
            }
            return this;
        }

        public Builder setTitleStr(String title){
            if (title != null){
                mTitleStr = title;
            }
            return this;
        }

        public Builder setMessageStr(String message){
            if (message != null){
                mMessageStr = message;
            }
            return this;
        }

        public Builder setPositiveStr(String p){
            if (p != null){
                mPositiveStr = p;
            }
            return this;
        }
        public Builder setNegativeStr(String n){
            if (n != null){
                mNegativeStr = n;
            }
            return this;
        }

        public Builder setQuestionsArray(String[] q){
            if (q != null){
                mQuestionsArray = q;
            }
            return this;
        }

        public Builder setMinAnswerLength(int l){
            mMinAnswerLength = l;
            return this;
        }

        public Builder setMaxAnswerLength(int l){
            mMaxAnswerLength = l;
            return this;
        }

        public LPV_Dialog build(){
            return new LPV_Dialog(this);
        }

        private void getDialogItems(float screenDensity){
            mTitleColor = ContextCompat.getColor(mContext, mTitleColor);
            mMessageColor = ContextCompat.getColor(mContext, mMessageColor);
            mTextColor = ContextCompat.getColor(mContext, mTextColor);
            mButtonsColor = ContextCompat.getColor(mContext, mButtonsColor);
            mRadioBtnColor = ContextCompat.getColor(mContext, mRadioBtnColor);

            if (mDialogType == LPV_Dialog.DIALOG_RESTORE_PATTERN){
                mTitleStr = mContext.getString(R.string.lpv_ad_restorePass_title);
                mMessageStr = mContext.getString(R.string.lpv_ad_restorePass_message);
                mPositiveStr = mContext.getString(R.string.lpv_ad_restorePass_pos);
                mNegativeStr = mContext.getString(R.string.lpv_ad_restorePass_neg);
            } else if (mDialogType == LPV_Dialog.DIALOG_SET_SECOND_PASS){
                mTitleStr = mContext.getString(R.string.lpv_ad_secondPass_title);
                mMessageStr = mContext.getString(R.string.lpv_ad_secondPass_message);
                mPositiveStr = mContext.getString(R.string.lpv_ad_secondPass_pos);
                mNegativeStr = mContext.getString(R.string.lpv_ad_secondPass_neg);
                mQuestionsArray = mContext.getResources().getStringArray(R.array.secondPassQuestions);
            }

            mMarginLeftRight = Math.round(mMarginLeftRight * screenDensity);
            mMarginTopBottom = Math.round(mMarginTopBottom * screenDensity);
        }
    }

    public static final int DIALOG_SET_SECOND_PASS = 0;
    public static final int DIALOG_RESTORE_PATTERN = 1;

    private final Context mContext;
    private final LockPatternView mLPV;
    private final SecondPassDialogInterface mInterfaceSPD;
    private RadioGroup mQuestionsGroup;
    private InputMethodManager mInputMethodManager;
    private AlertDialog mDialog;
    private LPV_SharedPreferences mShp;
    private LinearLayout mContainer;
    private TextView mQuestion;
    private EditText mAnswer;
    private Button mPositiveButton;
    private final List <RadioButton> mQuestionRBtnsList = new ArrayList<>();
    private final int mMarginLeftRight;
    private final int mMarginTopBottom;
    private final int mMinAnswerLength;
    private final int mMaxAnswerLength;
    private final float mTextSize = 16;
    private final String[] mQuestionsArray;
    private String mCurrentAnswerStr = "";
    private String mCorrectAnswerStr;
    private final String mTitleStr;
    private final String mMessageStr;
    private final String mPositiveStr;
    private final String mNegativeStr;
    private int mSelectedQuestionPosition = -1;
    private final int mTitleColor;
    private final int mMessageColor;
    private int mTextColor;
    private final int mButtonsColor;
    private final int mRadioBtnColor;

    private final int mDialogType;
    private boolean mIsQuestionChosen;

    private LPV_Dialog(Builder b){
        mContext = b.mContext;
        mLPV = b.mLPV;
        mInterfaceSPD = b.mInterfaceSPD;
        mDialogType = b.mDialogType;

        mMarginLeftRight = b.mMarginLeftRight;
        mMarginTopBottom = b.mMarginTopBottom;

        mTitleColor = b.mTitleColor;
        mMessageColor = b.mMessageColor;
        mTextColor = b.mTextColor;
        mButtonsColor = b.mButtonsColor;
        mRadioBtnColor = b.mRadioBtnColor;

        mTextColor = b.mTextColor;
        mMaxAnswerLength = b.mMaxAnswerLength;
        mMinAnswerLength = b.mMinAnswerLength;

        mQuestionsArray = b.mQuestionsArray;
        mTitleStr = b.mTitleStr;
        mMessageStr = b.mMessageStr;
        mPositiveStr = b.mPositiveStr;
        mNegativeStr = b.mNegativeStr;

        mIsQuestionChosen = false;

        createDialog();
    }

    public void show(){
        if (mDialogType == DIALOG_RESTORE_PATTERN){
            getCorrectAnswer();
        }
        mDialog.show();
        if (mPositiveButton == null){
            mPositiveButton = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negative = mDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            TextView title = (TextView)mDialog.findViewById(R.id.alertTitle);
            TextView message = (TextView)mDialog.findViewById(android.R.id.message);

            title.setTextColor(mTitleColor);
            message.setTextColor(mMessageColor);
            mPositiveButton.setTextColor(mButtonsColor);
            negative.setTextColor(mButtonsColor);

            showSoftKeyboard();
            mAnswer.requestFocus();
        }
        checkPositiveBtnEnable();
    }

    public void hide(){
        mDialog.hide();
    }

    private void setMaxLengthLimit(int limit){
        mAnswer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
    }

    private void getCorrectAnswer(){
        mShp = new LPV_SharedPreferences(mContext);
        mCorrectAnswerStr = mShp.getSecondSavedPass();
        String question = mShp.getSecondPassQuestion();

        mQuestion.setText(question);
    }

    private void createDialog(){
        initView();

        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
        b.setTitle(mTitleStr);
        b.setMessage(mMessageStr);
        b.setView(mContainer);
        b.setPositiveButton(mPositiveStr, onPositiveBtnListener);
        b.setNegativeButton(mNegativeStr, onNegativeBtnListener);
        b.setOnCancelListener(onCancelListener);
        mDialog = b.create();
    }

    private void initView(){
        mContainer = new LinearLayout(mContext);
        setContainerView();

        mAnswer = new EditText(mContext);
        setAnswerView();

        if (mDialogType == DIALOG_RESTORE_PATTERN){
            mQuestion = new TextView(mContext);
            setQuestionView();
            mContainer.addView(mQuestion);
        } else if (mDialogType == DIALOG_SET_SECOND_PASS){
            mQuestionsGroup = new RadioGroup(mContext);
            setQuestionsGroupView();
            mContainer.addView(mQuestionsGroup);
            mAnswer.setVisibility(View.GONE);
        }

        mContainer.addView(mAnswer);

        setMaxLengthLimit(mMaxAnswerLength);
        setPassLengthListener();
    }

    private void setContainerView(){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainer.setPadding(mMarginLeftRight, mMarginTopBottom, mMarginLeftRight, 0);
        mContainer.setLayoutParams(lp);
        mContainer.setOrientation(LinearLayout.VERTICAL);
    }

    private void setQuestionView(){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mQuestion.setLayoutParams(lp);

        mQuestion.setTextColor(mTextColor);
        mQuestion.setTextSize(mTextSize);
    }

    private void setQuestionsGroupView(){
        LinearLayout.LayoutParams rglp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams rblp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mQuestionsGroup.setLayoutParams(rglp);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{mRadioBtnColor},
                        new int[]{mRadioBtnColor}
                },
                new int[]{mRadioBtnColor, mRadioBtnColor}
        );
        for (int i = 0; i < mQuestionsArray.length; i++) {
            AppCompatRadioButton rb = new AppCompatRadioButton(mContext);

            setQuestionItem(i, rb, rblp, colorStateList);
            mQuestionsGroup.addView(rb);
        }
    }

    private void setQuestionItem(int pos, AppCompatRadioButton rb, ViewGroup.LayoutParams lp,
                                 ColorStateList csl){
        rb.setLayoutParams(lp);
        rb.setTag(pos);
        rb.setTextColor(mTextColor);
        rb.setTextSize(mTextSize);
        rb.setText(mQuestionsArray[pos]);
        rb.setOnClickListener(onQuestionItemListener);
        rb.setSupportButtonTintList(csl);

        mQuestionRBtnsList.add(rb);
    }

    private void setAnswerView(){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mAnswer.setLayoutParams(lp);

        mAnswer.setTextColor(mTextColor);
        mAnswer.setTextSize(mTextSize);
        mAnswer.setSingleLine(true);
        mAnswer.getBackground().setColorFilter(mButtonsColor, PorterDuff.Mode.SRC_ATOP);
    }

    private void setPassLengthListener(){
        mAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mCurrentAnswerStr = s.toString();
                checkPositiveBtnEnable();
            }
        });
    }

    private void checkPositiveBtnEnable(){
        int l = mCurrentAnswerStr.length();
        if (l < mMinAnswerLength){
            mPositiveButton.setClickable(false);
            mPositiveButton.setAlpha(0.3f);
        } else if (l>mMaxAnswerLength){
            mCurrentAnswerStr = mCurrentAnswerStr.substring(0, mMaxAnswerLength);
            mAnswer.setText(mCurrentAnswerStr);
        } else {
            mPositiveButton.setClickable(true);
            mPositiveButton.setAlpha(1f);
        }
    }

    private void showSoftKeyboard(){
        if (mInputMethodManager == null){
            mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mInputMethodManager.showSoftInput(mAnswer, 0);
            }
        }, 10);
    }

    private void hideSoftKeyboard(){
        if (mInputMethodManager == null){
            mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        mInputMethodManager.hideSoftInputFromWindow(mAnswer.getWindowToken(), 0);
    }

    private final DialogInterface.OnClickListener onPositiveBtnListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mAnswer.getText().clear();
            if (mDialogType == DIALOG_RESTORE_PATTERN){
                if (mCurrentAnswerStr.equals(mCorrectAnswerStr)) {
                    mShp.clearSharedPreferences();
                    mLPV.resetPatternSuccessful();
                    mInterfaceSPD.secondPassResetConfirmed();
                } else {
                    mLPV.resetPatternFailed();
                    mInterfaceSPD.secondPassResetFailed();
                }
            } else if (mDialogType == DIALOG_SET_SECOND_PASS){
                mLPV.patternSetSuccessful(mCurrentAnswerStr, mQuestionsArray[mSelectedQuestionPosition]);
                mInterfaceSPD.secondPassCreated(mCurrentAnswerStr);
            }
        }
    };

    private final DialogInterface.OnClickListener onNegativeBtnListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    private final DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mDialogType == DIALOG_SET_SECOND_PASS){
                mLPV.secondPassDismissed();
                mInterfaceSPD.setSecondPassCanceled();
                if (mAnswer.getVisibility() == View.VISIBLE){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onQuestionItemListener.onClick(mQuestionRBtnsList.get(mSelectedQuestionPosition));
                        }
                    }, 500);
                }
            }
            mAnswer.getText().clear();
        }
    };

    private final View.OnClickListener onQuestionItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int size = mQuestionRBtnsList.size();
            for (int i = 0; i < size; i++) {
                RadioButton rb = mQuestionRBtnsList.get(i);
                if (!rb.equals(v)){
                    if (!mIsQuestionChosen){
                        rb.setVisibility(View.GONE);
                    } else {
                        rb.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (!mIsQuestionChosen){
                mAnswer.setVisibility(View.VISIBLE);
                mAnswer.requestFocus();
                mSelectedQuestionPosition = (int)v.getTag();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showSoftKeyboard();
                    }
                }, 200);
            } else {
                mQuestionsGroup.clearCheck();
                mAnswer.setVisibility(View.GONE);
                hideSoftKeyboard();
            }

            mIsQuestionChosen = !mIsQuestionChosen;
        }
    };
}
