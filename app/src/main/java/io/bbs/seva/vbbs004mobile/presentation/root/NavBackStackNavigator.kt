package io.bbs.seva.vbbs004mobile.presentation.root
// app/src/main/java/io/bbs/seva/vbbs004mobile/presentation/root/NavBackStackNavigator.kt

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.presentation.navigation.AppNavigator

class NavBackStackNavigator(
    private val backStack: NavBackStack<NavKey>     // ← exactly your inferred type
) : AppNavigator {
    override fun navigate(destination: NavKey) { backStack.add(destination) }        // ✓ T = NavKey
    override fun back() { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
}
