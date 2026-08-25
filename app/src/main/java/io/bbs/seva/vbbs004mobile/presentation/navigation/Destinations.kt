package io.bbs.seva.vbbs004mobile.presentation.navigation

// presentation/navigation/Destinations.kt
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination : NavKey {

    @Serializable
    data object Login : Destination

    @Serializable
    data object Home : Destination

    @Serializable
    data object Weather : Destination

    @Serializable
    data class Profile(val userId: String) : Destination

}
