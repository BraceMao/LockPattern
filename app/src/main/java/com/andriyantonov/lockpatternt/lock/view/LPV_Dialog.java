package com.andriyantonov.lockpatternt.lock.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andriyantonov.lockpatternt.R;

/**
 * Created by pro100svitlo on 1/29/16.
 */
public class LPV_Dialog {

    public static class Builder{

        private Context mContext;
        private LockPatternView mLPV;
        private int mMarginLeftRight = 24;
        private int mMarginTopBottom = 16;
        private int mMinAnswerLength = 4;
        private int mMaxAnswerLength = 20;
        private int mPrimaryColor = R.color.colorPrimary;
        private int mAccentColor = R.color.colorAccent;
        private int mDialogType;
        private String[] mQuestionsArray;
        private String mTitleStr;
        private String mMessageStr;
        private String mQuestionStr;
        private String mPositiveStr;
        private String mNegativeStr;

        public Builder(Context c, LockPatternView lp, float density, int dialogType){
            mContext = c;
            mLPV = lp;
            mDialogType = dialogType;

            getDialogItems(density);
        }

        public Builder setTitle(String title){
            mTitleStr = title;
            return this;
        }

        public Builder setMessage(String message){
            mMessageStr = message;
            return this;
        }

        public Builder setPositive(String p){
            mPositiveStr = p;
            return this;
        }
        public Builder setNegative(String n){
            mNegativeStr = n;
            return this;
        }

        public Builder setMinimalAnswerLength(int length){
            mMinAnswerLength = length;
            return this;
        }

        public Builder setMaxAnswerLength(int length){
            mMaxAnswerLength = length;
            return this;
        }

        public LPV_Dialog build(){
            return new LPV_Dialog(this);
        }

        private void getDialogItems(float screenDensity){
            mPrimaryColor = ContextCompat.getColor(mContext, mPrimaryColor);
            mAccentColor = ContextCompat.getColor(mContext, mAccentColor);

            if (mDialogType == LPV_Dialog.DIALOG_RESTORE_PATTERN){
                mTitleStr = mContext.getString(R.string.lpv_ad_forgotPass_title);
                mMessageStr = mContext.getString(R.string.lpv_ad_forgotPass_message);
                mPositiveStr = mContext.getString(R.string.lpv_ad_forgotPass_pos);
                mNegativeStr = mContext.getString(R.string.lpv_ad_forgotPass_neg);
            } else if (mDialogType == LPV_Dialog.DIALOG_SET_SECOND_PASS){
                mTitleStr = mContext.getString(R.string.lpv_ad_secondPass_title);
                mMessageStr = mContext.getString(R.string.lpv_ad_secondPass_message);
                mPositiveStr = mContext.getString(R.string.lpv_ad_secondPass_neg);
                mNegativeStr = mContext.getString(R.string.lpv_ad_secondPass_pos);
                mQuestionsArray = mContext.getResources().getStringArray(R.array.secondPassQuestions);
            }

            mMarginLeftRight = Math.round(mMarginLeftRight * screenDensity);
            mMarginTopBottom = Math.round(mMarginTopBottom * screenDensity);
        }
    }

    public static final int DIALOG_SET_SECOND_PASS = 0;
    public static final int DIALOG_RESTORE_PATTERN = 1;

    private Context mContext;
    private LockPatternView mLPV;
    private RadioGroup mQuestionsGroup;
    private InputMethodManager mInputMethodManager;
    private AlertDialog mDialog;
    private LPV_SharedPreferences mShp;
    private LinearLayout mContainer;
    private TextView mQuestion;
    private EditText mAnswer;
    private Button mPositiveButton;
    private int mMarginLeftRight;
    private int mMarginTopBottom;
    private int mMinAnswerLength;
    private int mMaxAnswerLength;
    private float mTextSize = 16;
    private String[] mQuestionsArray;
    private String mCurrentAnswerStr = "";
    private String mCorrectAnswerStr;
    private String mTitleStr;
    private String mMessageStr;
    private String mQuestionStr;
    private String mPositiveStr;
    private String mNegativeStr;
    private int mPrimaryColor;
    private int mAccentColor;
    private int mDialogType;

    private LPV_Dialog(Builder b){
        mContext = b.mContext;
        mLPV = b.mLPV;
        mDialogType = b.mDialogType;

        mMarginLeftRight = b.mMarginLeftRight;
        mMarginTopBottom = b.mMarginTopBottom;
        mPrimaryColor = b.mPrimaryColor;
        mAccentColor = b.mAccentColor;
        mMaxAnswerLength = b.mMaxAnswerLength;
        mMinAnswerLength = b.mMinAnswerLength;

        mQuestionsArray = b.mQuestionsArray;
        mTitleStr = b.mTitleStr;
        mMessageStr = b.mMessageStr;
        mPositiveStr = b.mPositiveStr;
        mNegativeStr = b.mNegativeStr;


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

            negative.setTextColor(mAccentColor);
            mPositiveButton.setTextColor(mAccentColor);
            title.setTextColor(mPrimaryColor);
            message.setTextColor(mPrimaryColor);
        }
        checkPositiveBtnEnable();
        mAnswer.requestFocus();
        showSoftKeyboard();
    }

    public void hide(){
        mDialog.hide();
    }

    public void setMaxLengthLimit(int limit){
        mAnswer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
    }

    private void getCorrectAnswer(){
        mShp = new LPV_SharedPreferences(mContext);
        mCorrectAnswerStr = mShp.getSecondSavedPass();
        mQuestionStr = mShp.getSecondPassQuestion();

        mQuestion.setText(mQuestionStr);
    }

    private void createDialog(){
        initView();

        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
        b.setTitle(mTitleStr);
        b.setMessage(mMessageStr);
        b.setView(mContainer);
        b.setPositiveButton(mPositiveStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCurrentAnswerStr.equals(mCorrectAnswerStr)) {
                    answerIsCorrect();
                } else {
                    answerIsWrong();
                }
            }
        });
        b.setNegativeButton(mNegativeStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAnswer.getText().clear();
                dialog.dismiss();
            }
        });
        mDialog = b.create();
    }

    private void initView(){
        mContainer = new LinearLayout(mContext);
        setContainerView();

        mAnswer = new EditText(mContext);

        if (mDialogType == DIALOG_RESTORE_PATTERN){
            mQuestion = new TextView(mContext);
            setQuestionView();
            mContainer.addView(mQuestion);
        } else if (mDialogType == DIALOG_SET_SECOND_PASS){
            mQuestionsGroup = new RadioGroup(mContext);
            setQuestionsGroupView();
            mContainer.addView(mQuestionsGroup);
        }

        setAnswerView();
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

        mQuestion.setTextColor(mPrimaryColor);
        mQuestion.setTextSize(mTextSize);
    }

    private void setQuestionsGroupView(){
        LinearLayout.LayoutParams rglp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams rblp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mQuestionsGroup.setLayoutParams(rglp);

        for (int i = 0; i < mQuestionsArray.length; i++) {
            RadioButton rb = new RadioButton(mContext);
            setQuestionItem(i, rb, rblp);
            mQuestionsGroup.addView(rb);
        }
    }

    private void setQuestionItem(int pos, RadioButton rb, ViewGroup.LayoutParams lp){
        rb.setLayoutParams(lp);
        rb.setTag(pos);
        rb.setTextSize(mTextSize);
        rb.setText(mQuestionsArray[pos]);
        rb.setTextColor(mPrimaryColor);
        rb.setButtonTintList(ColorStateList.valueOf(mAccentColor));
    }

    private void setAnswerView(){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mAnswer.setLayoutParams(lp);

        mAnswer.setTextColor(mPrimaryColor);
        mAnswer.setTextSize(mTextSize);
        mAnswer.setSingleLine(true);
        mAnswer.getBackground().setColorFilter(mAccentColor, PorterDuff.Mode.SRC_ATOP);
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
    };

    private void checkPositiveBtnEnable(){
        int l = mCurrentAnswerStr.length();
        if (l < mMinAnswerLength){
            mPositiveButton.setClickable(false);
            mPositiveButton.setAlpha(0.3f);
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

    private void answerIsCorrect(){
        mShp.clearSharedPreferences();
        mLPV.resetPatternSuccessful();
    }

    private void answerIsWrong(){
        mLPV.resetPatternFailed();
    }
}
