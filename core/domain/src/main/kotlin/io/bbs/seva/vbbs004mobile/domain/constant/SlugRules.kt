package io.bbs.seva.vbbs004mobile.domain.constant

/**
 * Single source of truth for blog slug format.
 * Mirrors the server-side Zod rule:
 *   slug: z.string().trim().min(1).max(200).regex(/^[a-z0-9]+(?:-[a-z0-9]+)*$/)
 */
object SlugRules {

    const val MAX_LENGTH = 200

    private val VALID = Regex("^[a-z0-9]+(?:-[a-z0-9]+)*$")

    fun isValid(slug: String): Boolean =
        slug.isNotEmpty() && slug.length <= MAX_LENGTH && VALID.matches(slug)

    fun requireValid(slug: String) {
        require(slug.isNotBlank())              { "Slug is required" }
        require(slug.length <= MAX_LENGTH)      { "Slug too long (max $MAX_LENGTH)" }
        require(VALID.matches(slug))            { "Slug must be lowercase alphanumeric with hyphens" }
    }

    /** Derive a slug from arbitrary text (used for auto-slug while typing a title). */
    fun slugify(input: String): String =
        input.lowercase()
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .take(MAX_LENGTH)
            .trimEnd('-')
}

