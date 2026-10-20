package io.bbs.seva.vbbs004mobile.domain.model

// domain/model/BlogPost.kt

import java.time.Instant

data class BlogPost(
    val id: String,                 // UUID as string; consider a @JvmInline value class
    val title: String,
    val slug: String,
    val excerpt: String?,
    val content: String,
    val commentsEnabled: Boolean,
    val coverImageUrl: String?,
    val published: Boolean,
    val authorId: String,
    val authorEmail: String,
    val authorFullName: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class BlogPostComment(
    val id: String,
    val blogPostId: String,
    val authorId: String,
    val content: String,
    val createdAt: Instant,
)

