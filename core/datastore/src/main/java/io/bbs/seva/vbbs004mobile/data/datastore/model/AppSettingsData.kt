package io.bbs.seva.vbbs004mobile.data.datastore.model

import io.bbs.seva.vbbs004mobile.domain.model.AppSettings
import io.bbs.seva.vbbs004mobile.domain.model.ProxySettings
import kotlinx.serialization.Serializable

@Serializable
data class AppSettingsData(
    val baseUrl: String? = null,
    val proxy: ProxySettingsData? = null
)

@Serializable
data class ProxySettingsData(
    val isEnabled: Boolean? = null,
    val protocol: String? = null, // "HTTP" or "SOCKS"
    val host: String? = null,
    val port: Int? = null
)

fun ProxySettingsData.toDomain(): ProxySettings {
    return ProxySettings(
        isEnabled = this.isEnabled ?: false,
        protocol = this.protocol ?: "SOCKS",
        host = this.host ?: "127.0.0.1",
        port = this.port ?: 9150
    )
}

fun AppSettingsData.toDomain(): AppSettings {

    return AppSettings(
        baseUrl = this.baseUrl ?: "",
        proxy = this.proxy?.toDomain() ?: ProxySettings(
            isEnabled = false,
            protocol = "SOCKS",
            host = "127.0.0.1",
            port = 9159
        )
    )
}

