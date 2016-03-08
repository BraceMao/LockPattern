# Castomization in style

1. [General](#general-style)
2. [Main View](#main-view)
3. [Buttons](#buttons-style)
4. [Forgot Password](#forgot-password-style)
5. [Dialogs](#dialog-style)
⋅⋅* [General dialog style](#generaldialogstyle)
⋅⋅2. [Second Password Dialog](#secondpassworddialogstyle)
⋅⋅3. [Password Restore Dialog](#passwordrestoredialogstyle)
6. [Logo](#logo-style)
7.  Another item
⋅⋅* Unordered sub-list.

### General
```
<item name="errorTimeOutDur">2000</item> //time in milis
<item name="secretModeEnable">false</item>
<item name="secondPassDialogEnable">true</item>
<item name="showPassStr">true</item>
```

### Main View
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

### Buttons Style
```
<item name="btnTextColor">#464646</item> 
<item name="btnBgResource">@drawable/some_btn_bg</item>
<item name="btnCancelStr">Cancel</item>
<item name="btnConfirmStr">Confirm</item>
<item name="btnRepeatStr">Repeat</item>
```

### Forgot Password Style
``` 
<item name="forgotPassColor">#f2f2f0</item>
<item name="forgotPassTitleStr">Forgot patter?</item>
<item name="forgotPassFailedStr">Wrong answer!\nPatter recovery denied!</item>
<item name="forgotPassSuccessStr">Answer is correct!\nPlease set new pattern.</item>
```

### Status Text Style
```
<item name="statusNormalColor">#f2f2f0</item>
<item name="statusErrorColor">#f2f2f0</item>
<item name="statusSetNewPatternStr">Set new pattern</item>
<item name="statusConfirmPatternStr">Confirm pattern</item>
<item name="statusErrorPatternStr">Wrong Pattern</item>
```

### Dialog Style
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
### Logo Style
```
<item name="lpv_logo">@mipmap/your_logo</item>
```
