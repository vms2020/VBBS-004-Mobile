package io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts


import io.bbs.seva.vbbs004mobile.domain.model.BlogPostComment
import io.bbs.seva.vbbs004mobile.domain.repository.BlogRepository
import javax.inject.Inject

class AddBlogPostCommentUseCase @Inject constructor(
    private val repository: BlogRepository,
) {
    suspend operator fun invoke(postIdOrSlug: String, content: String): BlogPostComment {
        require(content.isNotBlank())          { "Comment cannot be empty" }
        require(content.length <= 2000)        { "Comment too long (max 2000)" }
        return repository.addComment(postIdOrSlug, content).getOrThrow()
    }
}