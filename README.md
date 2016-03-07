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
        tools:replace="label"
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

