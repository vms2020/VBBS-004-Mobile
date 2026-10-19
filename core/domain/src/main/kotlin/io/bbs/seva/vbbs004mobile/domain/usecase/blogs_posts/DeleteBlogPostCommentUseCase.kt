package io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts

import io.bbs.seva.vbbs004mobile.domain.repository.BlogRepository
import javax.inject.Inject

class DeleteBlogPostCommentUseCase @Inject constructor(
    private val repository: BlogRepository,
) {
    suspend operator fun invoke(commentId: String): Unit =
        repository.deleteComment(commentId).getOrThrow()
}
