package io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts

import io.bbs.seva.vbbs004mobile.domain.model.BlogPost
import io.bbs.seva.vbbs004mobile.domain.model.BlogPostComment
import io.bbs.seva.vbbs004mobile.domain.repository.BlogRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

data class BlogDetail(
    val post: BlogPost,
    val comments: List<BlogPostComment>,
)

class GetBlogPostDetailUseCase @Inject constructor(
    private val repository: BlogRepository,
) {
    suspend operator fun invoke(idOrSlug: String): BlogDetail = coroutineScope {
        // Post is critical: if it fails, the whole screen fails.
        val postDeferred = async { repository.getPost(idOrSlug).getOrThrow() }
        // Comments degrade gracefully: an error just means an empty list,
        // the post itself still renders.
        val commentsDeferred = async { repository.listComments(idOrSlug) }

        BlogDetail(
            post = postDeferred.await(),
            comments = commentsDeferred.await().getOrElse { emptyList() },
        )
    }
}