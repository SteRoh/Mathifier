package com.zettl.mathifier.ui.theme

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Minimum touch target for buttons - large enough for young kids. */
const val KID_MIN_BUTTON_HEIGHT_DP = 72

/** Minimum touch target for icon buttons. */
const val KID_MIN_ICON_BUTTON_SIZE_DP = 64

/** Modifier for kid-friendly buttons: taller and easier to tap. */
fun Modifier.kidFriendlyButton() = this.then(
    Modifier.sizeIn(minWidth = 160.dp, minHeight = 72.dp)
)
