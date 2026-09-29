package io.bbs.seva.vbbs004mobile.domain.model

data class ProxySettings(
    val isEnabled: Boolean,
    val protocol: String, // "HTTP" or "SOCKS"
    val host: String,
    val port: Int
)

data class AppSettings(
    val baseUrl: String,
    val proxy: ProxySettings
)
