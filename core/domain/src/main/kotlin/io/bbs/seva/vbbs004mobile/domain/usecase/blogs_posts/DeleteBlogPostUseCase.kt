package io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts

import io.bbs.seva.vbbs004mobile.domain.repository.BlogRepository
import javax.inject.Inject

class DeleteBlogPostUseCase @Inject constructor(
    private val repository: BlogRepository,
) {
    suspend operator fun invoke(idOrSlug: String): Unit =
        repository.deletePost(idOrSlug).getOrThrow()
}