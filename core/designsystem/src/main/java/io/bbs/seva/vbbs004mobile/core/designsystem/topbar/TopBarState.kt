package io.bbs.seva.vbbs004mobile.core.designsystem.topbar

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarState(
    val title: String = "",
    val actions: List<TopBarAction> = emptyList(),
)

sealed interface TopBarAction {
    data class MyTopBarActionIconButton(
        val icon: ImageVector,
        val contentDescription: String,
        val onClick: () -> Unit,
    ) : TopBarAction
}

@Stable
class TopBarController {
    var state by mutableStateOf(TopBarState())
        private set

    private var owner: Any? = null

//    fun set(state: TopBarState) {
//        this.state = state
//    }

    fun set(state: TopBarState, owner: Any) {
        this.owner = owner
        this.state = state
    }

    fun clear(owner: Any) {
        if (this.owner === owner) {
            this.state = TopBarState()
            this.owner = null
        }
    }
}

val LocalTopBarController = compositionLocalOf<TopBarController?> { null }