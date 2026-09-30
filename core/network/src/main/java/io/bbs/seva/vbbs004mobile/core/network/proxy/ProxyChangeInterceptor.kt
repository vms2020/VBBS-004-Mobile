package io.bbs.seva.vbbs004mobile.core.network.proxy

import io.bbs.seva.vbbs004mobile.domain.model.ProxySettings
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Interceptor
import okhttp3.Response

class ProxyChangeInterceptor(
    private val proxyState: StateFlow<ProxySettings>,
) : Interceptor {

    @Volatile
    private var lastProxy: ProxySettings? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val current = proxyState.value
        val changed = lastProxy != null && lastProxy != current
        lastProxy = current

        val request = if (changed) {
            chain.request().newBuilder()
                .header("Connection", "close")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}
