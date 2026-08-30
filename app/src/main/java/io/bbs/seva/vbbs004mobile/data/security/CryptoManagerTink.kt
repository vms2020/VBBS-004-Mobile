package io.bbs.seva.vbbs004mobile.data.security

//// data/security/CryptoManagerTink.kt
//import android.content.Context
//import com.google.crypto.tink.Aead
//import com.google.crypto.tink.KeyTemplates
//import com.google.crypto.tink.aead.AeadConfig
//import com.google.crypto.tink.integration.android.AndroidKeysetManager
//import java.util.Base64
//import javax.inject.Inject
//
//class CryptoManagerTink @Inject constructor(context: Context) {
//
//    private val aead: Aead
//
//    init {
//        AeadConfig.register()
//        aead = AndroidKeysetManager.Builder()
//            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
//            .withSharedPref(context, "tink_keyset", "tink_master_key")
//            .withMasterKeyUri("android-keystore://tink_token_key")
//            .build()
//            .keysetHandle
//            .getPrimitive(Aead::class.java)
//    }
//
//    fun encrypt(rawText: String): String {
//        val encryptedBytes = aead.encrypt(rawText.toByteArray(Charsets.UTF_8), null)
//        return Base64.getEncoder().encodeToString(encryptedBytes)
//    }
//
//    fun decrypt(encryptedText: String): String {
//        return try {
//            val decodedBytes = Base64.getDecoder().decode(encryptedText)
//            val decryptedBytes = aead.decrypt(decodedBytes, null)
//            String(decryptedBytes, Charsets.UTF_8)
//        } catch (e: Exception) {
//            "" // Gracefully handle corruption or missing keys
//        }
//    }
//}
