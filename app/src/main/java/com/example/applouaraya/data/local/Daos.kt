package com.example.applouaraya.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao interface ClienteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertar(cliente: ClienteEntity): Long
    @Query("SELECT * FROM clientes WHERE alias = :alias LIMIT 1") suspend fun porAlias(alias: String): ClienteEntity?
    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1") suspend fun porId(id: Int): ClienteEntity?
}
@Dao interface FavoritoDao {
    @Query("SELECT * FROM favoritos WHERE clienteId = :id") fun observar(id: Int): Flow<List<FavoritoEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertar(f: FavoritoEntity)
    @Query("DELETE FROM favoritos WHERE clienteId = :cliente AND productoId = :producto")
    suspend fun eliminar(cliente: Int, producto: Int)
}
@Dao interface SolicitudDao {
    @Query("SELECT * FROM solicitudes WHERE clienteId = :id ORDER BY id DESC")
    fun observar(id: Int): Flow<List<SolicitudEntity>>
    @Insert suspend fun insertar(s: SolicitudEntity): Long
}
@Dao interface LecturaQRDao {
    @Insert suspend fun insertar(l: LecturaQREntity)
    @Query("SELECT * FROM lecturas_qr WHERE clienteId = :id ORDER BY fechaHora DESC")
    fun observar(id: Int): Flow<List<LecturaQREntity>>
}
