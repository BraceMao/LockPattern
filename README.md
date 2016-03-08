# LockPattern

This library provides developer with possibility of easy integration of lock pattern protection to his android application. A lot of items are available for customization.

<!--![alt text](screenshots/111222.gif "Description goes here")-->

1. [Usage](#usage)
2. [Callbaks](#callbacks)
3. [Customization](Customization.md)

# Usage
##### Add the dependencies to your gradle file:
```
    dependencies {
        compile 'com.github.pro100svitlo:lockpattern:0.9.2'
    }
```

##### Also you need few line in your manifest file:
```
  <uses-permission android:name="android.permission.VIBRATE" />
  <application
        ...
        tools:replace="label"
        ...
        >
```
##### In your layout xml you need add next code:

**Note: your activity must be without toolbar or etc! Just LockPatternView as main and single layout!**
```
  <com.pro100svitlo.lockpattern.LockPatternView
        android:id="@+id/lpv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
```
##### Inside your activity or fragment:
```sh
    LockPatternView lockPatternView = (LockPatternView) findViewById(R.id.lpv);
    lockPatternView.initLockPatternView(getActivity(), this, this);
```
thats preaty mutch all what you need for start work!

---

### Callbacks:
 
 ###### Main LPV Interface
 (This is tha main callback for managing LockPatternView)
 
```
    @Override
    public void isPatternExist(boolean isExist) {
        //this will called with every activity/fragment start
        
        if (isExist){
            //do some staff here if need it...
        } else{
            //do some other staff here if need it...
        }
    }
    
    @Override
    public void patternConfirmed(boolean isFirstEnter, String patternPass) {
        //this will called when user successfully create new pattern
        //or when successfully entered with existed pattern
        
        //here you can make some intent into inner activity of your app...
        
        if (isFirstEnter){
            //do some staff here if need it...
            //it mean that user just setup pattern and enter first time
        }
        
        //patternPass - by default retirn null
        //if you need to know which password user create, than add style flag "showPassStr = true"
    }
    
    @Override
    public void patternFailed() {
        //do some staff here if need it...
        
        //it will be called if set password and confirm password didnn't match
        //or saved password and current entered password didn't match
    }
    
    @Override
    public void setPatternCanceled() {
         //do some staff here if need it...
         
         //it will called if user cancel setting new pattern
    }
```

 ###### Dialog Interface
 (This is optional callback - needed by default. It provide managing setting second password in case user will forgot his pattern and for future pattern recovery. For switch off secon password and password recovery dialogs add *secondPassDialogEnable = false* to your style)
```
    @Override
    public void setSecondPassCanceled() {
        //it will be called in case user dismiss second password dialog
        //after this user will be needed to setup pattern from the first step
    }

    @Override
    public void secondPassCreated(String secondPass) {
        //it will be called in case user succesfully create second password for recovery
        //after this user will be needed to setup pattern from the first step
        
         //secondPass - by default retirn null
        //if you need to know which answer user give for secret question, than add style flag "showPassStr = true"
    }

    @Override
    public void secondPassResetConfirmed() {
        //it will be called in case user give correct answer for secret qustion
        //after this will open first step of creation of new pattern and new secret question dialog
    }

    @Override
    public void secondPassResetFailed() {
        //it will be called in case user give uncorrect answer for secret qustion
    }
```
