Usage:

- Let your Activity extends SlipBackActivity

  SlipBackActivity is extends FragmentActivity

- Add style in your styles.xml
```
<item name="android:windowIsTranslucent">true</item>
```
just like this,let your whole app use this style
```
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">

        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="android:windowIsTranslucent">true</item>
    </style>
```
- add dependencies in your app build.gradle
```
 compile 'com.daoxuehao.android:slipbackactivitylib:1.1.1'
 compile 'com.android.support:support-v4:19.1.0'
```

- some configure

 set is whether can slipback, default is true
```
setCanSlipBack(boolean)
```
- Contact

 royal8848@163.com
