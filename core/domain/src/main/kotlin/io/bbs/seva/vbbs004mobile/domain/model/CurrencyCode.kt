package io.bbs.seva.vbbs004mobile.domain.model


@JvmInline
value class CurrencyCode private constructor(val value: String) {
    companion object {
        fun of(raw: String): CurrencyCode {
            require(raw.length == 3) { "ISO 4217 expected" }
            return CurrencyCode(raw.uppercase())
        }
    }
}