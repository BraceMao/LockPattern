# LockPattern

This library provides developer with possibility to easily integrate lock pattern protection into his android application. A lot of items are available for customization.

<!--![alt text](screenshots/111222.gif "Description goes here")-->

1. [Usage](#usage)
2. [Callbacks](#callbacks)
    1. [Main Interface](#main-lpv-interface)
    2. [Dialog Interface](#dialog-interface)
3. [Customization](Customization.md)
4. [Password Managing](#password-managing)

# Usage
##### Add the dependencies to your gradle file:
```
    dependencies {
        compile 'com.github.pro100svitlo:lockpattern:0.9.2'
    }
```

##### Also you need to add few lines to your manifest file:
```
  <uses-permission android:name="android.permission.VIBRATE" />
  <application
        ...
        tools:replace="label"
        ...
        >
```
##### In your layout xml you need to add following code:

**_Note: your activity must be without toolbar, etc.! Only LockPatternView should be used as main and single layout!_**
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
    //second argument - main interface callback
    //second argument - dialogs interface callback
```
That's pretty much all what you need to start the work!

---

### Callbacks:
 
###### Main LPV Interface
 (This is the main callback for managing LockPatternView)
 
```
    @Override
    public void isPatternExist(boolean isExist) {
        //this will be called with every activity/fragment start
        
        if (isExist){
            //do something here if necessary...
        } else{
            //do something else here if necessary...
        }
    }
    
    @Override
    public void patternConfirmed(boolean isFirstEnter, String patternPass) {
        //this will be called after user successfully creates new pattern
        //or when he successfully confirms existing pattern
        
        //here you can make some intent into inner activity of your app...
        
        if (isFirstEnter){
            //do something here if necessary...
            //it means that user has just set up a pattern and used it for the first time
        }
        
        //patternPass - by default return null
        //if you need to know which password was created, then add flag "showPassStr = true" to your custom style
    }
    
    @Override
    public void patternFailed() {
        //do something here if necessary...
        
        //it will be called if set password and confirmed password don't match
        //or saved password and just entered password don't match
    }
    
    @Override
    public void setPatternCanceled() {
         //do something here if necessary...
         
         //it will be called if user cancels setting process of new pattern
    }
```

###### Dialog Interface
 (This callback is set as a default, but can be cancelled. It provides possibility to set the second password in case user forgets his pattern and further pattern recovery is needed. In order to cancel the second password and password recovery dialogs add *secondPassDialogEnable = false* to your style).
```
    @Override
    public void setSecondPassCanceled() {
        //it will be called, if user dismisses the second password dialog
        //after this user will be asked to set up new pattern from the first step
    }

    @Override
    public void secondPassCreated(String secondPass) {
        //it will be called, if user successfully creates the second recovery password
        
         //secondPass - by default return null
        //if you need to know which answer is used for the secret question by user, then add flag "showPassStr = true" into your style
    }

    @Override
    public void secondPassResetConfirmed() {
        //it will be called, if user gives correct answer on secret question
        //after this a new pattern creation view will be opened
    }

    @Override
    public void secondPassResetFailed() {
        //it will be called, if user gives incorrect answer on secret question
    }
```

### Password Managing:
Manually clean/get user passwords:
    
```
    SharedPreferencesLPV shp = new SharedPreferencesLPV(context);
    shp.clearSharedPreferences();
    shp.getMainSavedPass();
    shp.getSecondPassQuestion();
    shp.getSecondSavedPass();
```
