package io.bbs.seva.vbbs004mobile.domain.usecase.blogs_posts
// core/domain/src/main/kotlin/io/bbs/seva/vbbs004mobile/domain/usecase/blogs_posts/CreateBlogPostUseCase.kt

import io.bbs.seva.vbbs004mobile.domain.constant.SlugRules
import io.bbs.seva.vbbs004mobile.domain.model.BlogPost
import io.bbs.seva.vbbs004mobile.domain.repository.BlogRepository
import javax.inject.Inject

class CreateBlogPostUseCase @Inject constructor(
    private val repository: BlogRepository,
) {
    suspend operator fun invoke(
        title: String,
        slug: String,
        content: String?,
        excerpt: String?,
        coverImageUrl: String?,
        commentsEnabled: Boolean,
        published: Boolean,
    ): BlogPost {
        require(title.isNotBlank()) { "Title is required" }
        require(slug.isNotBlank()) { "Slug is required" }
        return repository.createPost(
            title = title,
            slug = slug,
            content = content,
            excerpt = excerpt,
            coverImageUrl = coverImageUrl,
            commentsEnabled = commentsEnabled,
            published = published,
        ).getOrThrow()
    }
}

