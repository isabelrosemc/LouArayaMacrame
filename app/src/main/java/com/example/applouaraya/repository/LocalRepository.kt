package com.example.applouaraya.repository

import com.example.applouaraya.data.local.*
import com.example.applouaraya.data.security.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalRepository(private val db: LouDatabase) {

    suspend fun registrar(
        nombre: String,
        alias: String,
        correo: String,
        telefono: String,
        direccion: String,
        clave: String
    ): Result<Long> = withContext(Dispatchers.IO) {
        runCatching {
            val normalizado = alias.trim().lowercase()
            require(db.clienteDao().porAlias(normalizado) == null) { "El alias ya está registrado" }
            val salt = PasswordUtils.salt()
            val hash = PasswordUtils.hash(clave, salt)
            db.clienteDao().insertar(
                ClienteEntity(
                    nombre = nombre.trim(),
                    alias = normalizado,
                    correo = correo.trim(),
                    telefono = telefono.trim(),
                    direccion = direccion.trim(),
                    avatar = 1,
                    claveHash = hash,
                    claveSalt = salt
                )
            )
        }
    }

    suspend fun entrar(alias: String, clave: String): ClienteEntity? =
        withContext(Dispatchers.IO) {
            val normalizado = alias.trim().lowercase()

            // Cuenta fija de Administrador requerida por el docente
            if (normalizado == "admin" && clave.trim() == "123") {
                return@withContext ClienteEntity(
                    id = 9999,
                    nombre = "Administrador Lou Araya",
                    alias = "admin",
                    correo = "admin@louaraya.cl",
                    telefono = "+56900000000",
                    direccion = "Taller Central",
                    avatar = 0,
                    claveHash = "123",
                    claveSalt = "admin_salt"
                )
            }

            val c = db.clienteDao().porAlias(normalizado)
            if (c != null && PasswordUtils.verificar(clave, c.claveSalt, c.claveHash)) c else null
        }

    suspend fun actualizarContrasena(clienteId: Int, nuevaClave: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val nuevoSalt = PasswordUtils.salt()
                val nuevoHash = PasswordUtils.hash(nuevaClave, nuevoSalt)
                db.clienteDao().actualizarContrasena(clienteId, nuevoHash, nuevoSalt)
                Unit
            }
        }

    fun favoritos(id: Int) = db.favoritoDao().observar(id)

    suspend fun cambiarFavorito(id: Int, producto: Int, presente: Boolean) {
        if (presente) db.favoritoDao().eliminar(id, producto)
        else db.favoritoDao().insertar(FavoritoEntity(id, producto))
    }

    fun solicitudes(id: Int) = db.solicitudDao().observar(id)

    fun todasLasSolicitudes(): Flow<List<SolicitudEntity>> = db.solicitudDao().observarTodas()

    suspend fun cambiarEstadoSolicitud(id: Int, nuevoEstado: String) = withContext(Dispatchers.IO) {
        db.solicitudDao().actualizarEstado(id, nuevoEstado)
    }

    suspend fun guardarSolicitud(s: SolicitudEntity) = db.solicitudDao().insertar(s)

    suspend fun registrarQR(id: Int, producto: Int) =
        db.lecturaQRDao().insertar(LecturaQREntity(clienteId = id, productoId = producto))
}