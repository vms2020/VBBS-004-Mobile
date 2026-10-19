package io.bbs.seva.vbbs004mobile.data.repository
// core/data/src/main/java/io/bbs/seva/vbbs004mobile/data/repository/BlogRepositoryImpl.kt

import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.BlogPostDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.BlogPostCommentDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.CreateBlogPostRequest
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.CreateCommentRequest
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.DeleteBlogPostCommentResponse
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.DeleteBlogPostResponse
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.ListBlogsResponse
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.UpdateBlogPostRequest
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.toDomain
import io.bbs.seva.vbbs004mobile.data.remote.dto.blogs.toDomainPage
import io.bbs.seva.vbbs004mobile.domain.model.BlogPost
import io.bbs.seva.vbbs004mobile.domain.model.BlogPostComment
import io.bbs.seva.vbbs004mobile.domain.model.Page
import io.bbs.seva.vbbs004mobile.domain.repository.BlogRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import javax.inject.Inject
import kotlin.collections.map

class BlogRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : BlogRepository {

    // ---------- posts ----------

    override suspend fun listPosts(
        limit: Int,
        offset: Int,
        mineOnly: Boolean,
    ): Result<Page<BlogPost>> = runCatching {
        val response: ListBlogsResponse = httpClient.get("blogs") {
            parameter("limit", limit)
            parameter("offset", offset)
            if (mineOnly) parameter("mine", true)
        }.body()
        response.toDomainPage()
    }

    override suspend fun getPost(idOrSlug: String): Result<BlogPost> = runCatching {
        val dto: BlogPostDto = httpClient.get("blogs/$idOrSlug").body()
        dto.toDomain()
    }

    override suspend fun createPost(
        title: String,
        slug: String,
        content: String?,
        excerpt: String?,
        coverImageUrl: String?,
        commentsEnabled: Boolean?,
        published: Boolean,
    ): Result<BlogPost> = runCatching {
        val dto: BlogPostDto = httpClient.post("blogs") {
            setBody(
                CreateBlogPostRequest(
                    title = title,
                    slug = slug,
                    content = content,
                    excerpt = excerpt,
                    coverImageUrl = coverImageUrl,
                    commentsEnabled = commentsEnabled,
                    published = published,
                )
            )
        }.body()
        dto.toDomain()
    }

    override suspend fun updatePost(
        idOrSlug: String,
        title: String?,
        slug: String?,
        content: String?,
        excerpt: String?,
        coverImageUrl: String?,
        commentsEnabled: Boolean?,
        published: Boolean?,
    ): Result<BlogPost> = runCatching {
        val dto: BlogPostDto = httpClient.put("blogs/$idOrSlug") {
            setBody(
                UpdateBlogPostRequest(
                    title = title,
                    slug = slug,
                    content = content,
                    excerpt = excerpt,
                    coverImageUrl = coverImageUrl,
                    commentsEnabled = commentsEnabled,
                    published = published,
                )
            )
        }.body()
        dto.toDomain()
    }

    override suspend fun deletePost(idOrSlug: String): Result<Unit> = runCatching {
        val response: DeleteBlogPostResponse = httpClient.delete("blogs/$idOrSlug").body()
        check(response.success) { "Post delete not acknowledged by server" }
    }

    // ---------- comments ----------

    override suspend fun listComments(postIdOrSlug: String): Result<List<BlogPostComment>> = runCatching {
        val dtos: List<BlogPostCommentDto> = httpClient.get("blogs/$postIdOrSlug/comments").body()
        dtos.map { it.toDomain() }
    }

    override suspend fun addComment(
        postIdOrSlug: String,
        content: String,
    ): Result<BlogPostComment> = runCatching {
        val dto: BlogPostCommentDto = httpClient.post("blogs/$postIdOrSlug/comments") {
            setBody(CreateCommentRequest(content = content))
        }.body()
        dto.toDomain()
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> = runCatching {
        val response: DeleteBlogPostCommentResponse = httpClient.delete("blogs/comments/$commentId").body()
        check(response.success) { "Comment delete not acknowledged by server" }
    }
}
