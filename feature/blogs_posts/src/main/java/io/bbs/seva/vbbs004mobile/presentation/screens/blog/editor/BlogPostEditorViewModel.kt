package io.bbs.seva.vbbs004mobile.presentation.screens.blog.editor
// feature/blogs_posts/src/main/java/io/bbs/seva/vbbs004mobile/presentation/screens/blog/detail/BlogPostEditorViewModel.kt


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toAppError
import io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts.CreateBlogPostUseCase
import io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts.GetBlogPostDetailUseCase
import io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts.UpdateBlogPostUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogPostEditorViewModel @Inject constructor(
    private val createBlogPostUseCase: CreateBlogPostUseCase,
    private val updateBlogPostUseCase: UpdateBlogPostUseCase,
    private val getBlogDetailUseCase: GetBlogPostDetailUseCase,
) : ViewModel() {


    /** null = create mode, non-null = edit mode targeting this slug. */
    private var originalSlug: String? = null
    private var modeSet = false

    private val _state = MutableStateFlow(BlogPostEditorUiState())
    val state: StateFlow<BlogPostEditorUiState> = _state

    private val _events = Channel<BlogPostEditorEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    /**
     * Called from LaunchedEffect(slug) in the screen.
     * Idempotent for the same slug.
     */
    fun setMode(slug: String?) {
        if (modeSet && originalSlug == slug) return
        modeSet = true
        originalSlug = slug

        if (slug == null) {
            _state.update { it.copy(mode = EditorMode.Create) }
        } else {
            _state.update { it.copy(mode = EditorMode.Edit, isLoading = true) }
            loadExisting(slug)
        }
    }

    // ---- field updates ----

    fun onTitleChange(v: String) {
        _state.update { current ->
            Log.d(TAG, "onTitleChange: current = $current")
            // Auto-derive slug in Create mode while the user hasn't customised it.
            val autoSlug = if (
                current.mode == EditorMode.Create &&
                (current.slug.isBlank() || current.slug == slugify(current.title))
            )
                slugify(v)
            else
                current.slug
            current.copy(title = v, slug = autoSlug, error = null)
        }
    }

    fun onSlugChange(v: String) =
        _state.update { it.copy(slug = v.lowercase(), error = null) }

    fun onContentChange(v: String) =
        _state.update { it.copy(content = v, error = null) }

    fun onExcerptChange(v: String) =
        _state.update { it.copy(excerpt = v, error = null) }

    fun onCoverImageUrlChange(v: String) =
        _state.update { it.copy(coverImageUrl = v, error = null) }

    fun onCommentsEnabledChange(v: Boolean) =
        _state.update { it.copy(commentsEnabled = v) }

    fun onPublishedChange(v: Boolean) =
        _state.update { it.copy(published = v) }

    // ---- actions ----

    fun save() {
        val s = _state.value
        if (!s.canSave) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }

            val result = if (s.mode == EditorMode.Create) {
                runCatching {
                    createBlogPostUseCase(
                        title = s.title.trim(),
                        slug = s.slug.trim(),
                        content = s.content.ifBlank { null },
                        excerpt = s.excerpt.ifBlank { null },
                        coverImageUrl = s.coverImageUrl.ifBlank { null },
                        commentsEnabled = s.commentsEnabled,
                        published = s.published,
                    )
                }
            } else {
                runCatching {
                    updateBlogPostUseCase(
                        idOrSlug = requireNotNull(originalSlug) { "Edit mode without slug" },
                        title = s.title.trim(),
                        slug = s.slug.trim(),
                        content = s.content,
                        excerpt = s.excerpt.ifBlank { null },
                        coverImageUrl = s.coverImageUrl.ifBlank { null },
                        commentsEnabled = s.commentsEnabled,
                        published = s.published,
                    )
                }
            }

            result
                .onSuccess { post ->
                    _state.update { it.copy(isSaving = false) }
                    _events.send(BlogPostEditorEvent.Saved(post.slug))
                }
                .onFailure { e ->
                    _state.update { it.copy(isSaving = false, error = e.toAppError()) }
                }
        }
    }

    fun cancel() {
        viewModelScope.launch { _events.send(BlogPostEditorEvent.Cancelled) }
    }

    // ---- internals ----

    private fun loadExisting(slug: String) {
        viewModelScope.launch {
            runCatching { getBlogDetailUseCase(slug) }
                .onSuccess { detail ->
                    val p = detail.post
                    _state.update {
                        it.copy(
                            isLoading = false,
                            title = p.title,
                            slug = p.slug,
                            content = p.content,
                            excerpt = p.excerpt.orEmpty(),
                            coverImageUrl = p.coverImageUrl.orEmpty(),
                            commentsEnabled = p.commentsEnabled,
                            published = p.published,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.toAppError()) }
                }
        }
    }

    private fun slugify(input: String): String =
        input.lowercase()
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .take(200)
            .trimEnd('-')
}

private const val TAG = "BlogPostEditorViewModel"