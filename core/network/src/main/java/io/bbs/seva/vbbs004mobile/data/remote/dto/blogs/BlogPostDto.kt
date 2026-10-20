package io.bbs.seva.vbbs004mobile.data.remote.dto.blogs


import io.bbs.seva.vbbs004mobile.domain.model.BlogPost
import io.bbs.seva.vbbs004mobile.domain.model.Page
import io.bbs.seva.vbbs004mobile.domain.model.Pagination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.OffsetDateTime

/**
{
            "id": "a8b5d78d-a2c1-47f9-bca5-e89166e9749f",
            "title": "mass and energy",
            "slug": "mc2",
            "excerpt": "E = mc²",
            "content": "E = mc², equation in German-born physicist Albert Einstein’s theory of special relativity that expresses the fact that mass and energy are the same physical entity and can be changed into each other. In the equation, the increased relativistic mass (m) of a body times the speed of light squared (c²) is equal to the kinetic energy (E) of that body.",
            "cover_image_url": "https://evuynobnxghhbbhhpqxd.supabase.co/storage/v1/object/public/user-uploads/bc82c0f1-c10e-4f81-a456-a70cb521b8c7/29a88335-4925-4a3f-a1df-813dcb93ab72.jpg",
            "published": true,
            "author_id": "bc82c0f1-c10e-4f81-a456-a70cb521b8c7",
            "created_at": "2026-07-22T09:31:49.352989+00:00",
            "updated_at": "2026-09-28T20:08:59.872711+00:00",
            "comments_enabled": true
        }
*/
@Serializable
data class BlogPostDto(
    @SerialName("author_id")
    val authorId: String, // = null, // bc82c0f1-c10e-4f81-a456-a70cb521b8c7
    @SerialName("author_email")
    val authorEmail: String? = null,
    @SerialName("author_full_name")
    val authorFullName: String? = null,
    @SerialName("comments_enabled")
    val commentsEnabled: Boolean = true, // ? = null, // true
    @SerialName("content")
    val content: String, // ? = null, // E = mc², equation in German-born physicist Albert Einstein’s theory of special relativity that expresses the fact that mass and energy are the same physical entity and can be changed into each other. In the equation, the increased relativistic mass (m) of a body times the speed of light squared (c²) is equal to the kinetic energy (E) of that body.
    @SerialName("cover_image_url")
    val coverImageUrl: String? = null, // https://evuynobnxghhbbhhpqxd.supabase.co/storage/v1/object/public/user-uploads/bc82c0f1-c10e-4f81-a456-a70cb521b8c7/29a88335-4925-4a3f-a1df-813dcb93ab72.jpg
    @SerialName("created_at")
    val createdAt: String, // ? = null, // 2026-07-22T09:31:49.352989+00:00
    @SerialName("excerpt")
    val excerpt: String? = null, // E = mc²
    @SerialName("id")
    val id: String, //? = null, // a8b5d78d-a2c1-47f9-bca5-e89166e9749f
    @SerialName("published")
    val published: Boolean = false, // true
    @SerialName("slug")
    val slug: String, // ? = null, // mc2
    @SerialName("title")
    val title: String, // ? = null, // mass and energy
    @SerialName("updated_at")
    val updatedAt: String, // ? = null // 2026-09-28T20:08:59.872711+00:00
)


fun BlogPostDto.toDomain(): BlogPost = BlogPost(
    id = id,
    title = title,
    slug = slug,
    excerpt = excerpt,
    content = content,
    commentsEnabled = commentsEnabled,
    coverImageUrl = coverImageUrl,
    published = published,
    authorId = authorId,
    authorEmail = authorEmail?:"",
    authorFullName = authorFullName?:"",
    createdAt = OffsetDateTime.parse(createdAt).toInstant(),//Instant.parse(createdAt),
    updatedAt = OffsetDateTime.parse(updatedAt).toInstant(),//Instant.parse(updatedAt),
)


@Serializable
data class ListBlogsResponse(
    val data: List<BlogPostDto>,
    val pagination: PaginationDto,
)

fun ListBlogsResponse.toDomainPage(): Page<BlogPost> = Page(
    items = data.map { it.toDomain() },
    pagination = Pagination(
        total = pagination.total,
        limit = pagination.limit,
        offset = pagination.offset,
        hasMore = pagination.hasMore,
    ),
)
@Serializable
data class PaginationDto(
    val total: Int,
    val limit: Int,
    val offset: Int,
    @SerialName("hasMore") val hasMore: Boolean,
)

@Serializable
data class CreateBlogPostRequest(
    val title: String,
    val slug: String,
    val content: String? = null,
    val excerpt: String? = null,
    @SerialName("cover_image_url") val coverImageUrl: String? = null,
    @SerialName("comments_enabled") val commentsEnabled: Boolean? = null,
    val published: Boolean = false,
)

@Serializable
data class UpdateBlogPostRequest(
    val title: String? = null,
    val slug: String? = null,
    val content: String? = null,
    val excerpt: String? = null,
    @SerialName("cover_image_url") val coverImageUrl: String? = null,
    @SerialName("comments_enabled") val commentsEnabled: Boolean? = null,
    val published: Boolean? = null,
)

@Serializable
data class CreateCommentRequest(val content: String)


@Serializable
data class DeleteBlogPostResponse(
    val success: Boolean,
    val count: Int,
)

