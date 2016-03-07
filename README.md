# LockPattern

This library provides developer with possibility of easy integration of lock pattern protection to his android application. A lot of items are available for customization.

# Usage
Add the dependencies to your gradle file:

    dependencies {
        compile 'com.github.pro100svitlo:lockpattern:0.9.2'
    }
Also you need to add next (bold and italic) line to your aplication into manifest:
  ```
  <application
        android:name=".utils.App_Application"
        android:allowBackup="true"
        android:fullBackupContent="true"
        tools:replace="label"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/gen_app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">```


![alt tag](https://github.com/pro100svitlo/LockPattern/blob/master/screenshots/111222.gif)
