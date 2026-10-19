package io.bbs.seva.vbbs004mobile.data.remote.dto.blogs


import io.bbs.seva.vbbs004mobile.domain.model.BlogPostComment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.OffsetDateTime

/**
{
  "id": "7f8dab97-2864-4631-a570-c55f3cdb3d7d",
  "blog_post_id": "d2c0ca5e-7a4f-4176-9541-920a17f2edb5",
  "author_id": "e1d3eb7f-7943-46a2-aa34-ea57af7be497",
  "content": "The second half of the 1970s to the early 1980s.",
  "created_at": "2026-10-14T01:13:31.914716+00:00"
}
*/
@Serializable
data class BlogPostCommentDto(
    @SerialName("author_id")
    val authorId: String, // ? = null, // e1d3eb7f-7943-46a2-aa34-ea57af7be497
    @SerialName("blog_post_id")
    val blogPostId: String, // ? = null, // d2c0ca5e-7a4f-4176-9541-920a17f2edb5
    @SerialName("content")
    val content: String, // ? = null, // The second half of the 1970s to the early 1980s.
    @SerialName("created_at")
    val createdAt: String, // ? = null, // 2026-10-14T01:13:31.914716+00:00
    @SerialName("id")
    val id: String, // ? = null // 7f8dab97-2864-4631-a570-c55f3cdb3d7d
)

fun BlogPostCommentDto.toDomain(): BlogPostComment = BlogPostComment(
    id = id,
    blogPostId = blogPostId,
    authorId = authorId,
    content = content,
    createdAt = OffsetDateTime.parse(createdAt).toInstant(),//Instant.parse(createdAt),
)

@Serializable
data class DeleteBlogPostCommentResponse(
    val success: Boolean,
)
