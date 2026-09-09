package io.bbs.seva.vbbs004mobile.data.security
// data/security/CryptoManager.kt

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object CryptoManager {
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    private const val KEY_ALIAS = "auth_token_key"

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    private fun getSecretKey(): SecretKey {
        val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM, "AndroidKeyStore").apply {
            init(
                KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(bytes: ByteArray, outputStream: OutputStream) {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        // Write the Initialization Vector (IV) first, so we can use it to decrypt later
        outputStream.write(cipher.iv.size)
        outputStream.write(cipher.iv)
        outputStream.write(cipher.doFinal(bytes))
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        val ivSize = inputStream.read()
        val iv = ByteArray(ivSize).apply { inputStream.read(this) }

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))

        val encryptedData = inputStream.readBytes()
        return cipher.doFinal(encryptedData)
    }

    ///////////////////// z variant
//    fun decrypt(inputStream: InputStream): ByteArray {
//        DataInputStream(inputStream).use { dis ->
//            val ivSize = dis.read()
//            require(ivSize >= 0) { "Corrupted input: empty or truncated stream" }
//            val iv = ByteArray(ivSize)
//            dis.readFully(iv)          // guaranteed to fill or throw EOFException
//            val cipher = Cipher.getInstance(TRANSFORMATION)
//            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
//            return cipher.doFinal(dis.readBytes())
//        }
//    }
}
