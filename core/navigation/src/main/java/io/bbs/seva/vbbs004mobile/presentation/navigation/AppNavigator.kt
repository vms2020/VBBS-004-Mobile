package io.bbs.seva.vbbs004mobile.presentation.navigation

import androidx.navigation3.runtime.NavKey

interface AppNavigator {
    fun navigate(destination: NavKey)
    fun back()
}
