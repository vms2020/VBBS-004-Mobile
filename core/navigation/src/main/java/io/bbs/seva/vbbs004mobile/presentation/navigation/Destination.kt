package io.bbs.seva.vbbs004mobile.presentation.navigation
// presentation/navigation/Destination.kt

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed class Destination : NavKey {
    // Existing
    @Serializable
    data object Login : Destination()

    @Serializable
    data object Home : Destination()

    @Serializable
    data object Weather : Destination()

    @Serializable
    class GeoLocationDest(val lat: Double, val lon: Double): Destination()

    // New
    @Serializable
    data object CurrencyRates : Destination()

    @Serializable
    data object Signup : Destination()

    @Serializable
    data object Logout : Destination()   // not a real screen, handled separately

    @Serializable
    data object EditProfile : Destination()

    @Serializable
    data object Blogs : Destination()

    @Serializable
    data object Shops : Destination()

    @Serializable
    data object Chats : Destination()

    // Metadata
    val title: String
        get() = when (this) {
            Login -> "Login"
            Home -> "Home"
            Weather -> "Weather"
            is GeoLocationDest -> "Geo Location"
            CurrencyRates -> "Currency Rates"
            Signup -> "Sign Up"
            Logout -> "Logout"
            EditProfile -> "Edit Profile"
            Blogs -> "Blogs"
            Shops -> "Shops"
            Chats -> "Chats"
        }

    val requiresAuth: Boolean
        get() = when (this) {
            Login, Signup, CurrencyRates -> false
            else -> true
        }

    companion object {
//        val all: List<Destination> by lazy {
//            listOf(
//                Login,
//                Home,
//                Weather,
//                CurrencyRates,
//                Signup,
//                EditProfile,
//                Blogs,
//                Shops,
//                Chats,
//                Logout
//            ).also { list ->
//                Log.i("DEST_CHECK", "all created: $list")
//                list.forEachIndexed { i, d ->
//                    Log.i("DEST_CHECK", "index $i -> ${d.javaClass.name} (hash=${d.hashCode()})")
//                }
//            }
//        }

        val all: List<Destination> by lazy {
            Destination::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .toMutableList()
                .apply {
                    // Add a default GeoLocationDest for the menu
                    // (You will pass the real GeoLocation when navigating)
                    add(GeoLocationDest(0.0, 0.0))
                }
        }

    }
}


/*

//@Serializable
//sealed interface Destination : NavKey {
//
//    @Serializable
//    data object Login : Destination
//
//    @Serializable
//    data object Home : Destination
//
//    @Serializable
//    data object Weather : Destination
//
//    @Serializable
//    data class Profile(val userId: String) : Destination
//
//}

*/
