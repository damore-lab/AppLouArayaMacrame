package cl.duoc.louarayamacrame.data

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.util.Base64

object SeguridadContrasena {

    fun generarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun generarHash(contrasena: String, salt: String): String {
        val saltBytes = Base64.getDecoder().decode(salt)
        val spec = PBEKeySpec(
            contrasena.toCharArray(),
            saltBytes,
            210_000,
            256
        )

        return try {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val hash = factory.generateSecret(spec).encoded
            Base64.getEncoder().encodeToString(hash)
        } finally {
            spec.clearPassword()
        }
    }

    fun verificar(
        contrasena: String,
        salt: String,
        hashGuardado: String
    ): Boolean {
        val hashCalculado = generarHash(contrasena, salt)

        return java.security.MessageDigest.isEqual(
            Base64.getDecoder().decode(hashCalculado),
            Base64.getDecoder().decode(hashGuardado)
        )
    }
}
