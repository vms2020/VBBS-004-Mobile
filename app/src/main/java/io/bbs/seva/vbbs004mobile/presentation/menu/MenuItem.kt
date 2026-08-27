package io.bbs.seva.vbbs004mobile.presentation.menu
// presentation/menu/MenuItem.kt

import androidx.compose.ui.graphics.vector.ImageVector
import io.bbs.seva.vbbs004mobile.presentation.navigation.Destination


data class MenuItem(
    val destination: Destination,
    val icon: ImageVector
)