package com.pro100svitlo.lockpattern;

/**
 * Created by pro100svitlo on 1/24/16.
 */
public interface LPV_Interface {
    void patternExist(boolean patternExist);
    void patternSet(String setPass);
    void patternConfirmedSuccess(boolean isPassSaving, String mainPass, String secondPass);
    void patternConfirmFailed(String wrongPass);
    void patternSubmitCanceled();
}
