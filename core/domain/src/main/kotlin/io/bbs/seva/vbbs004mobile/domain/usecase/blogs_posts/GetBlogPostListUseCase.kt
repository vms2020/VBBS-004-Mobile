package io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts

import io.bbs.seva.vbbs004mobile.domain.model.BlogPost
import io.bbs.seva.vbbs004mobile.domain.model.Page
import io.bbs.seva.vbbs004mobile.domain.repository.BlogRepository
import javax.inject.Inject

class GetBlogPostListUseCase @Inject constructor(
    private val repository: BlogRepository,
) {
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0,
        mineOnly: Boolean = false,
    ): Page<BlogPost> =
        repository.listPosts(limit, offset, mineOnly).getOrThrow()
}
