package io.bbs.seva.vbbs004mobile.presentation.navigation
// core/navigation/src/main/java/io/bbs/seva/vbbs004mobile/presentation/navigation/AppNavigator.kt

import androidx.navigation3.runtime.NavKey

interface AppNavigator {
    fun navigate(destination: NavKey)
    fun back()
}
