package io.bbs.seva.vbbs004mobile.presentation.screens.blog.detail
// feature/blogs_posts/src/main/java/io/bbs/seva/vbbs004mobile/presentation/screens/blog/detail/BlogPostDetailViewModel.kt


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toAppError
import io.bbs.seva.vbbs004mobile.domain.model.AppError
import io.bbs.seva.vbbs004mobile.domain.model.BlogPost
import io.bbs.seva.vbbs004mobile.domain.model.BlogPostComment
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts.AddBlogPostCommentUseCase
import io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts.DeleteBlogPostUseCase
import io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts.DeleteBlogPostCommentUseCase
import io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts.GetBlogPostDetailUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogPostDetailViewModel @Inject constructor(
    private val getBlogDetailUseCase: GetBlogPostDetailUseCase,
    private val addCommentUseCase: AddBlogPostCommentUseCase,
    private val deleteCommentUseCase: DeleteBlogPostCommentUseCase,
    private val deleteBlogPostUseCase: DeleteBlogPostUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    // No SavedStateHandle. Slug is set from the screen via setSlug().
    private var idOrSlug: String? = null

    private val _state = MutableStateFlow(BlogPostDetailUiState())
    val state: StateFlow<BlogPostDetailUiState> = _state

    private val _events = Channel<BlogDetailEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        _state.update { it.copy(currentUserId = authRepository.userProfile.value?.id) }
        viewModelScope.launch {
            authRepository.userProfile.collect { user ->
                _state.update { it.copy(currentUserId = user?.id) }
            }
        }
    }

    /** Called from LaunchedEffect(slug) in the screen. Idempotent. */
    fun setSlug(slug: String) {
        if (idOrSlug == slug) return
        idOrSlug = slug
        load(showSpinner = true)
    }

    fun refresh() = load(showSpinner = false)
    fun retry()   = load(showSpinner = true)

    fun onDraftCommentChange(text: String) {
        _state.update { it.copy(draftComment = text) }
    }

    fun submitComment() {
        val slug = idOrSlug ?: return
        val text = _state.value.draftComment.trim()
        if (text.isEmpty() || _state.value.isSubmittingComment) return

        viewModelScope.launch {
            _state.update { it.copy(isSubmittingComment = true, error = null) }
            runCatching { addCommentUseCase(slug, text) }
                .onSuccess { comment ->
                    _state.update {
                        it.copy(
                            comments = it.comments + comment,
                            draftComment = "",
                            isSubmittingComment = false,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(isSubmittingComment = false, error = e.toAppError()) }
                }
        }
    }

    fun deleteComment(commentId: String) {
        viewModelScope.launch {
            runCatching { deleteCommentUseCase(commentId) }
                .onSuccess {
                    _state.update { s -> s.copy(comments = s.comments.filterNot { it.id == commentId }) }
                }
                .onFailure { e ->
                    _events.send(BlogDetailEvent.ShowToast(e.toAppError().toString()))
                }
        }
    }

    fun deletePost() {
        val slug = idOrSlug ?: return
        if (_state.value.isDeleting) return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, error = null) }
            runCatching { deleteBlogPostUseCase(slug) }
                .onSuccess { _events.send(BlogDetailEvent.NavigateBack) }
                .onFailure { e ->
                    _state.update { it.copy(isDeleting = false, error = e.toAppError()) }
                }
        }
    }

    fun onEditClicked() {
        _state.value.post?.let { p ->
            viewModelScope.launch { _events.send(BlogDetailEvent.NavigateToEdit(p.slug)) }
        }
    }

    private fun load(showSpinner: Boolean) {
        val slug = idOrSlug ?: return
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingPost = showSpinner,
                    isLoadingComments = showSpinner,
                    isRefreshing = !showSpinner,
                    error = null,
                )
            }
            runCatching { getBlogDetailUseCase(slug) }
                .onSuccess { detail ->
                    _state.update {
                        it.copy(
                            post = detail.post,
                            comments = detail.comments,
                            isLoadingPost = false,
                            isLoadingComments = false,
                            isRefreshing = false,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoadingPost = false,
                            isLoadingComments = false,
                            isRefreshing = false,
                            error = e.toAppError(),
                        )
                    }
                }
        }
    }
}

data class BlogPostDetailUiState(
    val post: BlogPost? = null,
    val comments: List<BlogPostComment> = emptyList(),
    val currentUserId: String? = null,
    val isLoadingPost: Boolean = false,
    val isLoadingComments: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSubmittingComment: Boolean = false,
    val isDeleting: Boolean = false,
    val draftComment: String = "",
    val error: AppError? = null,
) {
    val canComment: Boolean get() = post?.commentsEnabled == true && post.published
    val canEdit: Boolean get() = post != null && currentUserId != null && post.authorId == currentUserId
    val canDelete: Boolean get() = canEdit
}

sealed interface BlogDetailEvent {
    data object NavigateBack : BlogDetailEvent
    data class NavigateToEdit(val slug: String) : BlogDetailEvent
    data class ShowToast(val message: String) : BlogDetailEvent
}
