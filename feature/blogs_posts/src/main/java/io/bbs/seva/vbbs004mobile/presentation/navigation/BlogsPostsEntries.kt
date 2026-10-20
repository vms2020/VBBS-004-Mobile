package io.bbs.seva.vbbs004mobile.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.LocalTopBarController
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.TopBarAction
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.TopBarState
import io.bbs.seva.vbbs004mobile.presentation.screens.blog.detail.BlogPostDetailScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.blog.detail.BlogPostDetailViewModel
import io.bbs.seva.vbbs004mobile.presentation.screens.blog.editor.BlogPostEditorScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.blog.editor.BlogPostEditorViewModel
import io.bbs.seva.vbbs004mobile.presentation.screens.blog.editor.EditorMode
import io.bbs.seva.vbbs004mobile.presentation.screens.blog.list.BlogPostListScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.blog.list.BlogPostListViewModel

fun EntryProviderScope<NavKey>.blogsPostsEntryBuilder(navigator: AppNavigator) {
    entry<Destination.BlogsPosts> {
        val viewModel: BlogPostListViewModel = hiltViewModel()
        val topBar = LocalTopBarController.current
        val state by viewModel.state.collectAsStateWithLifecycle()

        SideEffect {
            topBar?.set(
                TopBarState(
                    title = "Blog Posts",
                    actions = listOf(
                        TopBarAction.MyTopBarActionIconButton(
                            icon = Icons.Default.Add,
                            contentDescription = "New Post",
                            onClick = { navigator.navigate(Destination.BlogPostEditor(slug = null)) },
                        ),
                    ),
                ),
                owner = Destination.BlogsPosts,
            )
        }
        DisposableEffect(Unit) {
            onDispose { topBar?.clear(Destination.BlogsPosts) }
        }

        BlogPostListScreen(
            viewModel = viewModel,
            onPostClick = { slug -> navigator.navigate(Destination.BlogPostDetail(slug)) },
        )
    }

    entry<Destination.BlogPostDetail> { key ->
        val viewModel: BlogPostDetailViewModel = hiltViewModel()
        val topBar = LocalTopBarController.current
        val state by viewModel.state.collectAsStateWithLifecycle()
        val titleText = state.post?.title ?: "Post"
        val canEdit = state.canEdit

        SideEffect {
            topBar?.set(
                TopBarState(
                    title = titleText, // state.post?.title ?: "Post",
                    actions = buildList {
                        add(
                            TopBarAction.MyTopBarActionIconButton(
                                icon = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                onClick = { viewModel.refresh() },
                            )
                        )
                        if (canEdit) {
                            add(
                                TopBarAction.MyTopBarActionIconButton(
                                    icon = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    onClick = { viewModel.onEditClicked() },
                                )
                            )
                            add(
                                TopBarAction.MyTopBarActionIconButton(
                                    icon = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    onClick = { viewModel.deletePost() },
                                )
                            )
                        }
                    },
                ),
                owner = key,
            )
        }
        DisposableEffect(Unit) {
            onDispose { topBar?.clear(key) }
        }

        BlogPostDetailScreen(
            slug = key.slug,
            viewModel = viewModel,
            onNavigateBack = { navigator.back() },
            onNavigateToEdit = { slug -> navigator.navigate(Destination.BlogPostEditor(slug)) },
        )
    }
    entry<Destination.BlogPostEditor> { key ->
        val viewModel: BlogPostEditorViewModel = hiltViewModel()
        val topBar = LocalTopBarController.current
        val state by viewModel.state.collectAsStateWithLifecycle()

        SideEffect {
            topBar?.set(
                TopBarState(
                    title = if (state.mode == EditorMode.Create) "New Post" else "Edit Post",
                    actions = emptyList(),
                ),
                owner = key,
            )
        }
        DisposableEffect(Unit) {
            onDispose { topBar?.clear(key) }
        }

        BlogPostEditorScreen(
            slug = key.slug,
            viewModel = viewModel,
            onNavigateBack = { navigator.back() },
            onSaved = { newSlug ->
                // Pop the editor, then show the post's detail.
                navigator.back()
                navigator.navigate(Destination.BlogPostDetail(newSlug))
            },
        )
    }
}


