package com.pro100svitlo.lockpattern.interfaces;

/**
 * Created by pro100svitlo on 2/21/16.
 */
public interface DialogLPVInterface {
    void setSecondPassCanceled();
    void secondPassCreated(String secondPass);
    void passRestoreConfirmed();
    void passRestoreFailed();
}
