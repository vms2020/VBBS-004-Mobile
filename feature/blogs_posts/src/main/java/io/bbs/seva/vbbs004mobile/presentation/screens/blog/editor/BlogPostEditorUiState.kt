package io.bbs.seva.vbbs004mobile.presentation.screens.blog.editor
// feature/blogs_posts/src/main/java/io/bbs/seva/vbbs004mobile/presentation/screens/blog/editor/BlogPostEditorUiState.kt

import io.bbs.seva.vbbs004mobile.domain.model.AppError

data class BlogPostEditorUiState(
    val mode: EditorMode = EditorMode.Create,
    val title: String = "",
    val slug: String = "",
    val content: String = "",
    val excerpt: String = "",
    val coverImageUrl: String = "",
    val commentsEnabled: Boolean = true,
    val published: Boolean = false,
    val isLoading: Boolean = false,      // fetching existing post in Edit mode
    val isSaving: Boolean = false,       // POST/PUT in flight
    val error: AppError? = null,
) {
    val isFormValid: Boolean
        get() = title.isNotBlank() && slug.isNotBlank()

    val canSave: Boolean
        get() = isFormValid && !isSaving && !isLoading
}

enum class EditorMode { Create, Edit }

sealed interface BlogPostEditorEvent {
    data class Saved(val slug: String) : BlogPostEditorEvent
    data object Cancelled : BlogPostEditorEvent
}
