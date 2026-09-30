package io.bbs.seva.vbbs004mobile.core.network.proxy
// core/network/src/main/java/io/bbs/seva/vbbs004mobile/core/network/proxy/DynamicProxySelector.kt

import android.util.Log
import io.bbs.seva.vbbs004mobile.domain.model.ProxySettings
import io.bbs.seva.vbbs004mobile.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI

private const val TAG = "DynamicProxySelector"
class DynamicProxySelector(
    //private val settingsRepository: AppSettingsRepository
    private val proxyState: StateFlow<ProxySettings>,
) : ProxySelector() {

    override fun select(uri: URI?): List<Proxy> {
        // Safe context: executed on a background routing worker thread by OkHttp
        //val settings = runBlocking { settingsRepository.appSettings.first() }
        val settings = proxyState.value
        // If proxy is turned off or host is blank, return Java's official NO_PROXY definition
        if (!settings.isEnabled || settings.host.isBlank()) {
            Log.e(
                TAG,
                "select: proxy settings settings.isEnabled=${settings.isEnabled} " +
                        "or settings.host.isBlank() = ${settings.host.isBlank()}")
            return listOf(Proxy.NO_PROXY) // 👈 This utilizes the exact structure Java expects for DIRECT types
        }

        val r = try {
            val type = if (settings.protocol.uppercase() == "SOCKS") Proxy.Type.SOCKS else Proxy.Type.HTTP
            val socketAddress = InetSocketAddress(settings.host, settings.port)
            listOf(Proxy(type, socketAddress))
        } catch (e: Exception) {
            Log.e(TAG, "select: ERROR", e)
            listOf(Proxy.NO_PROXY) // Fallback safely if inputs are corrupt or invalid
        }
        Log.e(TAG, "uri=$uri → $r")
        return r
    }

    override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
        // Logging hook if a proxy server rejects connection strings
        Log.e(TAG, "connectFailed: uri=$uri sa=$sa", ioe)
    }
}
