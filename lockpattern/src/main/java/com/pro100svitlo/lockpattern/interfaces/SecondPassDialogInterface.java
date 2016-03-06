package com.pro100svitlo.lockpattern.interfaces;

/**
 * Created by pro100svitlo on 2/21/16.
 */
public interface SecondPassDialogInterface {
    void setSecondPassCanceled();
    void secondPassCreated(String secondPass);
    void secondPassResetConfirmed();
    void secondPassResetFailed();
}
