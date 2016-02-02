package com.andriyantonov.lockpatternt.lock.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by pro100svitlo on 1/24/16.
 */
public class LPV_SharedPreferences {

    public static final String LPV_MAIN_SAVE_PASS_KEY = "LPV_MAIN_SAVE_PASS_KEY";
    public static final String LPV_SECOND_SAVE_PASS_KEY = "LPV_SECOND_SAVE_PASS_KEY";

    private Context mContext;
    private SharedPreferences mShp;
    private SharedPreferences.Editor mShpEditor;

    public LPV_SharedPreferences(Context c){
        mContext = c;
        mShp = PreferenceManager.getDefaultSharedPreferences(mContext);
        mShpEditor = mShp.edit();
    }

    public void saveMainPass(String pass){
        mShpEditor.putString(LPV_MAIN_SAVE_PASS_KEY, pass);
        mShpEditor.apply();
    }

    public String getMainSavedPass(){
        return mShp.getString(LPV_MAIN_SAVE_PASS_KEY, "");
    }

    public void saveSecondPass(String pass){
        mShpEditor.putString(LPV_SECOND_SAVE_PASS_KEY, pass);
        mShpEditor.apply();
    }

    public String getSecondSavedPass(){
        return mShp.getString(LPV_SECOND_SAVE_PASS_KEY, "");
    }

    public void clearSharedPreferences(){
        mShpEditor.putString(LPV_MAIN_SAVE_PASS_KEY, "");
        mShpEditor.putString(LPV_SECOND_SAVE_PASS_KEY, "");
        mShpEditor.apply();
    }

}
