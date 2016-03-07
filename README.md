# LockPattern

This library provides developer with possibility of easy integration of lock pattern protection to his android application. A lot of items are available for customization.

![alt text](screenshots/111222.gif "Description goes here")

# Usage
Add the dependencies to your gradle file:
```sh
    dependencies {
        compile 'com.github.pro100svitlo:lockpattern:0.9.2'
    }
```
Also you need few line in your manifest file:
```sh
  <application
        tools:replace="label"
        >
```
```sh
  <uses-permission android:name="android.permission.VIBRATE" />
```
