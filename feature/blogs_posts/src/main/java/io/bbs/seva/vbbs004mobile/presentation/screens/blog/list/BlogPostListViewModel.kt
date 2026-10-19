package io.bbs.seva.vbbs004mobile.presentation.screens.blog.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toAppError
import io.bbs.seva.vbbs004mobile.domain.model.AppError
import io.bbs.seva.vbbs004mobile.domain.model.BlogPost
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.domain.repository.BlogRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BlogListEvent {
    data class OpenPost(val slug: String) : BlogListEvent
    data object OpenEditor : BlogListEvent
}

@HiltViewModel
class BlogPostListViewModel @Inject constructor(
    private val blogRepository: BlogRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BlogListUiState())
    val state: StateFlow<BlogListUiState> = _state

    private val _events = Channel<BlogListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            authRepository.isAuthenticated.collect { authed ->
                _state.update {
                    it.copy(
                        isAuthenticated = authed,
                        mineOnly = if (authed) it.mineOnly else false,
                    )
                }
            }
        }
        load(showSpinner = true)
    }

    fun refresh() = load(showSpinner = false)
    fun retry() = load(showSpinner = true)

    fun loadMore() {
        val s = _state.value
        if (!s.hasMore || s.isLoading || s.isRefreshing || s.isLoadingMore) return
        load(showSpinner = false, offset = s.items.size)
    }

    fun setMineOnly(mine: Boolean) {
        if (_state.value.mineOnly == mine) return
        _state.update { it.copy(mineOnly = mine) }
        load(showSpinner = true)
    }

    fun onPostClicked(slug: String) {
        viewModelScope.launch { _events.send(BlogListEvent.OpenPost(slug)) }
    }

    fun onCreateClicked() {
        viewModelScope.launch { _events.send(BlogListEvent.OpenEditor) }
    }

    private fun load(showSpinner: Boolean, offset: Int = 0) {
        val isReset = offset == 0
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = showSpinner && isReset,
                    isRefreshing = !showSpinner && isReset,
                    isLoadingMore = !isReset,
                    errorAppError = null,
                )
            }
            runCatching {
                blogRepository.listPosts(
                    limit = PAGE_SIZE,
                    offset = offset,
                    mineOnly = _state.value.mineOnly,
                ).getOrThrow()
            }.onSuccess { page ->
                _state.update {
                    it.copy(
                        items = if (isReset) page.items else it.items + page.items,
                        hasMore = offset + page.items.size < page.pagination.total,
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                    )
                }
            }.onFailure { e ->
                android.util.Log.e(
                    "BlogPostList",
                    "load failed: ${e::class.simpleName}: ${e.message}",
                    e
                )
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                        errorAppError = e.toAppError(),
                    )
                }
            }
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}

data class BlogListUiState(
    val items: List<BlogPost> = emptyList(),
    val hasMore: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val mineOnly: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorAppError: AppError? = null,
) {
    val isEmpty: Boolean
        get() = !isLoading && items.isEmpty() && errorAppError == null
}



