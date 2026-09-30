package io.bbs.seva.vbbs004mobile.domain.usecase

import io.bbs.seva.vbbs004mobile.domain.model.ProxySettings
import io.bbs.seva.vbbs004mobile.domain.repository.AppSettingsRepository
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val repository: AppSettingsRepository
) {
    suspend operator fun invoke(
        baseUrl: String,
        proxyEnabled: Boolean,
        proxyProtocol: String,
        proxyHost: String,
        proxyPortStr: String
    ): Result<Unit> {
        return runCatching {
            // Business Rule Validation 1: Base URL cannot be completely empty
            val cleanUrl = baseUrl.trim()
            if (cleanUrl.isBlank()) {
                throw IllegalArgumentException("Base URL cannot be empty")
            }

            // Business Rule Validation 2: Ensure port falls within appropriate network ranges
            val parsedPort = proxyPortStr.toIntOrNull() ?: 9150
            if (proxyEnabled && (parsedPort !in 1..65535)) {
                throw IllegalArgumentException("Invalid port number (must be 1-65535)")
            }

            // Write verified properties to the repository layers
            repository.saveBaseUrl(cleanUrl)
            repository.saveProxySettings(
                ProxySettings(
                    isEnabled = proxyEnabled,
                    protocol = proxyProtocol,
                    host = proxyHost.trim(),
                    port = parsedPort
                )
            )
        }
    }
}
