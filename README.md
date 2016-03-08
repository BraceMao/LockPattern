# LockPattern
```
[I'm a reference-style link][1]
```

This library provides developer with possibility of easy integration of lock pattern protection to his android application. A lot of items are available for customization.

<!--![alt text](screenshots/111222.gif "Description goes here")-->

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
 
 [1]**LPV_Interface**
 (This is tha main callback of LockPatternView)
 
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

 **SecondPassDialogInterface**
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
### Castomization in style
##### General Style
```
<item name="errorTimeOutDur">2000</item> //time in milis
<item name="secretModeEnable">false</item>
<item name="secondPassDialogEnable">true</item>
<item name="showPassStr">true</item>
```
##### Buttons Style
```
<item name="btnTextColor">#464646</item> 
<item name="btnBgResource">@drawable/some_btn_bg</item>
<item name="btnCancelStr">Cancel</item>
<item name="btnConfirmStr">Confirm</item>
<item name="btnRepeatStr">Repeat</item>
```

##### Forgot Password Style
``` 
<item name="forgotPassColor">#f2f2f0</item>
<item name="forgotPassTitleStr">Forgot patter?</item>
<item name="forgotPassFailedStr">Wrong answer!\nPatter recovery denied!</item>
<item name="forgotPassSuccessStr">Answer is correct!\nPlease set new pattern.</item>
```

##### Main View
```
<item name="dotAnimScaleMax">4f</item>
<item name="dotRadius">20</item>
<item name="dotLineWidth">10</item>
<item name="mBgRadius">10</item>
<item name="mainBgLineWidth">5</item>
<item name="dotMinCount">4</item>
<item name="dotVibrateDur">10</item>
<item name="errorVibrateDur">200</item>
<item name="toShortPatternStr">Pattern must contain at least <xliff:g id="count">%s</xliff:g> dots!</item>

<item name="dotNormalColor">#f2f2f0</item>
<item name="dotTouchedColor">#FF157B0B</item>
<item name="dotErrorColor">#ca3232</item>

<item name="mainIsBgEnable">false</item>
<item name="dotVibrateEnable">true</item>
<item name="errorVibrateEnable">true</item>
```

##### Status Text Style
```
<item name="statusNormalColor">#f2f2f0</item>
<item name="statusErrorColor">#f2f2f0</item>
<item name="statusSetNewPatternStr">Set new pattern</item>
<item name="statusConfirmPatternStr">Confirm pattern</item>
<item name="statusErrorPatternStr">Wrong Pattern</item>
```
##### Dialog Style
###### GeneralDialogStyle
```
<item name="dialogTitleColor"></item> //by default your primaryColor
<item name="dialogMessageColor">#f2f2f0</item> //by default your primaryColor
<item name="dialogTextColor">#464646</item>
<item name="dialogButtonsColor"></item> //by default your accentColor
<item name="dialogMinAnswerLength">4</item>
<item name="dialogMaxAnswerLength">20</item>
```
###### SecondPasswordDialogStyle
```
<item name="dialogRadioBtnColor"></item> //by default your accentColor
<item name="dialogSecondPassTitleStr">Second Password</item>
<item name="dialogSecondPassMessageStr">This password you will use in case current pattern recovery.</item>
<item name="dialogSecondPassPositiveStr">Confirm</item>
<item name="dialogSecondPassNegativeStr">Dismiss</item>
<item name="dialogQuestionsArray">@array/your_questions_array</item>
```

###### PasswordRestoreDialogStyle
```
<item name="dialogPassRestoreTitleStr">Forgot patter?</item>
<item name="dialogPassRestoreMessageStr">For patter reset please, answer next question:</item>
<item name="dialogPassRestorePositiveStr">Confirm</item>
<item name="dialogPassRestoreNegativeStr">Dismiss</item>
```
##### Logo Style
```
<item name="lpv_logo">@mipmap/your_logo</item>
```
