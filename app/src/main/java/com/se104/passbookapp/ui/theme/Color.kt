package com.se104.passbookapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF8D4959)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFFFD9DF)
val onPrimaryContainerLight = Color(0xFF3A0817)

val secondaryLight = Color(0xFF75565C)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFFFD9DF)
val onSecondaryContainerLight = Color(0xFF3F0E19)

val backgroundLight = Color(0xFFFFFBFA)
val onBackgroundLight = Color(0xFF201A1B)
val surfaceLight = Color(0xFFFFFBFA)
val onSurfaceLight = Color(0xFF201A1B)

val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)

val inversePrimaryLight = Color(0xFFFFB1C1)
val outlineLight = Color(0xFF9E8C90)


val primaryDark = Color(0xFFFFB1C1)
val onPrimaryDark = Color(0xFF4B0D1E)
val primaryContainerDark = Color(0xFF6F2C3B)
val onPrimaryContainerDark = Color(0xFFFFD9DF)

val secondaryDark = Color(0xFFE1BBC3)
val onSecondaryDark = Color(0xFF3A1C23)
val secondaryContainerDark = Color(0xFF573F45)
val onSecondaryContainerDark = Color(0xFFFFD9DF)

val backgroundDark = Color(0xFF1D1B1E)
val onBackgroundDark = Color(0xFFEDE0E3)
val surfaceDark = Color(0xFF1D1B1E)
val onSurfaceDark = Color(0xFFEDE0E3)

val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)

val inversePrimaryDark = Color(0xFF8D4959)
val outlineDark = Color(0xFFAA8E96)

// Light mode
val buttonColorLight = Color(0xFFFF85A2)
val confirmColorLight = Color(0xFF18C07A)

// Dark mode
val buttonColorDark = Color(0xFFFFA8B9)
val confirmColorDark = Color(0xFF20D08C)


val ColorScheme.button: Color
    @Composable
    get() = if (isSystemInDarkTheme()) buttonColorDark else buttonColorLight

val ColorScheme.confirm: Color
    @Composable
    get() = if(isSystemInDarkTheme()) confirmColorDark else confirmColorLight