package com.pro100svitlo.lockpattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by pro100svitlo on 1/24/16.
 */
public class LPV_SharedPreferences {

    private static final String LPV_MAIN_PASS_KEY = "LPV_MAIN_PASS_KEY";
    private static final String LPV_SECOND_PASS_ANSWER = "LPV_SECOND_PASS_ANSWER";
    private static final String LPV_SECOND_PASS_QUESTION = "LPV_SECOND_PASS_QUESTION";

    private final SharedPreferences mShp;
    private final SharedPreferences.Editor mShpEditor;

    public LPV_SharedPreferences(Context c){
        mShp = PreferenceManager.getDefaultSharedPreferences(c);
        mShpEditor = mShp.edit();
    }

    public void saveMainPass(String pass){
        mShpEditor.putString(LPV_MAIN_PASS_KEY, pass);
        mShpEditor.apply();
    }

    public String getMainSavedPass(){
        return mShp.getString(LPV_MAIN_PASS_KEY, "");
    }

    public void saveSecondPass(String pass){
        mShpEditor.putString(LPV_SECOND_PASS_ANSWER, pass);
        mShpEditor.apply();
    }

    public String getSecondSavedPass(){
        return mShp.getString(LPV_SECOND_PASS_ANSWER, "");
    }

    public void saveSecondQuestion(String q){
        mShpEditor.putString(LPV_SECOND_PASS_QUESTION, q);
        mShpEditor.apply();
    }

    public String getSecondPassQuestion(){
        return mShp.getString(LPV_SECOND_PASS_QUESTION, "ololol ???");
    }

    public void clearSharedPreferences(){
        mShpEditor.putString(LPV_MAIN_PASS_KEY, "");
        mShpEditor.putString(LPV_SECOND_PASS_ANSWER, "");
        mShpEditor.putString(LPV_SECOND_PASS_QUESTION, "");
        mShpEditor.apply();
    }

}
