package io.bbs.seva.vbbs004mobile.domain.repository

// core/domain/src/main/kotlin/io/bbs/seva/vbbs004mobile/domain/repository/BlogRepository.kt


import io.bbs.seva.vbbs004mobile.domain.model.BlogPost
//import io.bbs.seva.vbbs004mobile.domain.model.BlogPostSummary
import io.bbs.seva.vbbs004mobile.domain.model.BlogPostComment
import io.bbs.seva.vbbs004mobile.domain.model.Page

interface BlogRepository {
    suspend fun listPosts(
        limit: Int = 20,
        offset: Int = 0,
        mineOnly: Boolean = false,
    ): Result<Page<BlogPost>>

    suspend fun getPost(idOrSlug: String): Result<BlogPost>

    suspend fun createPost(
        title: String,
        slug: String,
        content: String? = null,
        excerpt: String? = null,
        coverImageUrl: String? = null,
        commentsEnabled: Boolean? = null,
        published: Boolean = false,
    ): Result<BlogPost>

    suspend fun updatePost(
        idOrSlug: String,
        title: String? = null,
        slug: String? = null,
        content: String? = null,
        excerpt: String? = null,
        coverImageUrl: String? = null,
        commentsEnabled: Boolean? = null,
        published: Boolean? = null,
    ): Result<BlogPost>

    suspend fun deletePost(idOrSlug: String): Result<Unit>

    suspend fun listComments(postIdOrSlug: String): Result<List<BlogPostComment>>

    suspend fun addComment(postIdOrSlug: String, content: String): Result<BlogPostComment>

    suspend fun deleteComment(commentId: String): Result<Unit>
}
