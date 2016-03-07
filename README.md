# LockPattern

This library provides developer with possibility of easy integration of lock pattern protection to his android application. A lot of items are available for customization.

<!--![alt text](screenshots/111222.gif "Description goes here")-->

# Usage
Add the dependencies to your gradle file:
```sh
    dependencies {
        compile 'com.github.pro100svitlo:lockpattern:0.9.2'
    }
```
Also you need few line in your manifest file:
```sh
  <uses-permission android:name="android.permission.VIBRATE" />
  <application
        ...
        tools:replace="label"
        ...
        >
```
In your layout xml you need add next code:

**Note: your activity must be without toolbar or etc! Just LockPatternView as main and single layout!**
```sh
  <com.pro100svitlo.lockpattern.LockPatternView
        android:id="@+id/lpv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
```
Inside your activity or fragment:
```sh
    LockPatternView lockPatternView = (LockPatternView) findViewById(R.id.lpv);
    lockPatternView.initLockPatternView(getActivity(), this, this);
```
thats preaty mutch all what you need for start work!

Inside your activity/fragment you have next callbacks for managing LockPatternView:
```sh
    **LPV_Interface**
    @Override
    public void isPatternExist(boolean isExist) {
        if (isExist){
            //do some staff here if need it...
        } else{
            //do some other staff here if need it...
        }
    }
    
    @Override
    public void patternConfirmed(boolean isFirstEnter, String patternPass) {
        if (isFirstEnter){
            //do some staff here if need it...
            //it mean that user just setup pattern and enter first time
        }
        
        //here you can make some intent into inner activity of your app...
        
        //patternPass - by default retirn null
        //if you need to know which password user create, than add style flag "showPatternPassStr = true"
    }
```
