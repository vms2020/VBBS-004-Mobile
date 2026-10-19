package io.bbs.seva.vbbs004mobile.domain.model

// core/domain/src/main/kotlin/io/bbs/seva/vbbs004mobile/domain/model/Pagination.kt
data class Pagination(
    val total: Int,
    val limit: Int,
    val offset: Int,
    val hasMore: Boolean,
)

data class Page<T>(
    val items: List<T>,
    val pagination: Pagination,
)
