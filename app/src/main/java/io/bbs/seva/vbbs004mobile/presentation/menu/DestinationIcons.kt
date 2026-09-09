package io.bbs.seva.vbbs004mobile.presentation.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import io.bbs.seva.vbbs004mobile.presentation.navigation.Destination

fun getDestinationIcon(destination: Destination): ImageVector {
    return when (destination) {
        Destination.Home -> Icons.Default.Home
        Destination.Weather -> Icons.Default.WbSunny
        Destination.CurrencyRates -> Icons.Default.AttachMoney
        Destination.Login -> Icons.AutoMirrored.Filled.Login
        Destination.Signup -> Icons.Default.PersonAdd
        Destination.Logout -> Icons.AutoMirrored.Filled.Logout
        Destination.EditProfile -> Icons.Default.Edit
        Destination.Blogs -> Icons.AutoMirrored.Filled.Article
        Destination.Shops -> Icons.Default.ShoppingCart
        Destination.Chats -> Icons.AutoMirrored.Filled.Chat
        is Destination.GeoLocationDest -> Icons.Default.Map
        //else -> Icons.Default.Star
    }
}
