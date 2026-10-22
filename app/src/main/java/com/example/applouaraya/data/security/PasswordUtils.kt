package com.example.applouaraya.data.security

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordUtils {
    fun salt(): String = ByteArray(16).also { SecureRandom().nextBytes(it) }
        .let { Base64.encodeToString(it, Base64.NO_WRAP) }
    fun hash(clave: String, salt: String): String {
        val chars = clave.toCharArray()
        val spec = PBEKeySpec(chars, Base64.decode(salt, Base64.NO_WRAP), 210000, 256)
        return try {
            Base64.encodeToString(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec).encoded, Base64.NO_WRAP)
        } finally { spec.clearPassword(); chars.fill('\u0000') }
    }
    fun verificar(clave: String, salt: String, hash: String): Boolean =
        MessageDigest.isEqual(Base64.decode(hash(clave, salt), Base64.NO_WRAP),
            Base64.decode(hash, Base64.NO_WRAP))
}
