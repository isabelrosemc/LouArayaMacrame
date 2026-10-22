package com.example.applouaraya.repository

import com.example.applouaraya.data.local.*
import com.example.applouaraya.data.security.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepository(private val db: LouDatabase) {
    suspend fun registrar(nombre: String, alias: String, correo: String,
        telefono: String, direccion: String, clave: String): Result<Long> =
        withContext(Dispatchers.IO) {
            runCatching {
                val normalizado = alias.trim().lowercase()
                require(db.clienteDao().porAlias(normalizado) == null) { "Alias ocupado" }
                val salt = PasswordUtils.salt()
                db.clienteDao().insertar(ClienteEntity(nombre = nombre.trim(), alias = normalizado,
                    correo = correo.trim(), telefono = telefono.trim(), direccion = direccion.trim(),
                    avatar = 0, claveHash = PasswordUtils.hash(clave, salt), claveSalt = salt))
            }
        }
    suspend fun entrar(alias: String, clave: String): ClienteEntity? =
        withContext(Dispatchers.IO) {
            val c = db.clienteDao().porAlias(alias.trim().lowercase())
            if (c != null && PasswordUtils.verificar(clave, c.claveSalt, c.claveHash)) c else null
        }
    fun favoritos(id: Int) = db.favoritoDao().observar(id)
    suspend fun cambiarFavorito(id: Int, producto: Int, presente: Boolean) {
        if (presente) db.favoritoDao().eliminar(id, producto)
        else db.favoritoDao().insertar(FavoritoEntity(id, producto))
    }
    fun solicitudes(id: Int) = db.solicitudDao().observar(id)
    suspend fun guardarSolicitud(s: SolicitudEntity) = db.solicitudDao().insertar(s)
    suspend fun registrarQR(id: Int, producto: Int) =
        db.lecturaQRDao().insertar(LecturaQREntity(clienteId = id, productoId = producto))
}
