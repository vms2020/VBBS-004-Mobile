package io.bbs.seva.vbbs004mobile.domain.repository

import io.bbs.seva.vbbs004mobile.domain.model.AppSettings
import io.bbs.seva.vbbs004mobile.domain.model.ProxySettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    val appSettings: Flow<AppSettings>
    suspend fun saveBaseUrl(url: String)
    suspend fun saveProxySettings(proxy: ProxySettings)
}