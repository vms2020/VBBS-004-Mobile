package io.bbs.seva.vbbs004mobile.data.repository

import androidx.datastore.core.DataStore
import io.bbs.seva.vbbs004mobile.data.datastore.model.AppSettingsData
import io.bbs.seva.vbbs004mobile.data.datastore.model.ProxySettingsData
import io.bbs.seva.vbbs004mobile.data.datastore.model.toDomain
import io.bbs.seva.vbbs004mobile.di.AppSettingsDataStore
import io.bbs.seva.vbbs004mobile.di.DefaultBaseUrl
import io.bbs.seva.vbbs004mobile.domain.model.AppSettings
import io.bbs.seva.vbbs004mobile.domain.model.ProxySettings
import io.bbs.seva.vbbs004mobile.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    @AppSettingsDataStore private val appSettingsDataStore: DataStore<AppSettingsData>,
    @DefaultBaseUrl private val defaultBaseUrl: String
): AppSettingsRepository {
    override val appSettings: Flow<AppSettings>
        get() = appSettingsDataStore.data.map { data ->
            //data.toDomain()

            val resolvedDomain = data.toDomain()

            // If the domain url is empty or blank, override it with the default from local.properties
            resolvedDomain.copy(
                baseUrl = resolvedDomain.baseUrl.takeIf { it.isNotBlank() } ?: defaultBaseUrl
            )
        }

    override suspend fun saveBaseUrl(url: String) {
        appSettingsDataStore.updateData { currentData ->
            currentData.copy(
                baseUrl = url
            )
        }
    }

    override suspend fun saveProxySettings(proxy: ProxySettings) {
        appSettingsDataStore.updateData { currentData ->
            currentData.copy(
                proxy = ProxySettingsData(
                    isEnabled = proxy.isEnabled,
                    protocol = proxy.protocol,
                    host = proxy.host,
                    port = proxy.port
                )
            )
        }
    }

}
