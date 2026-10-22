package com.example.applouaraya.data.local

import androidx.room.*

@Entity(tableName = "clientes", indices = [Index(value = ["alias"], unique = true)])
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String, val alias: String, val correo: String,
    val telefono: String, val direccion: String, val avatar: Int,
    val claveHash: String, val claveSalt: String
)

@Entity(tableName = "favoritos", primaryKeys = ["clienteId", "productoId"],
    foreignKeys = [ForeignKey(entity = ClienteEntity::class, parentColumns = ["id"],
        childColumns = ["clienteId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("clienteId")])
data class FavoritoEntity(val clienteId: Int, val productoId: Int)

@Entity(tableName = "solicitudes",
    foreignKeys = [ForeignKey(entity = ClienteEntity::class, parentColumns = ["id"],
        childColumns = ["clienteId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("clienteId")])
data class SolicitudEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int, val productoId: Int?, val tipoEvento: String,
    val medidas: String, val material: String, val colores: String,
    val fecha: String, val observaciones: String, val estado: String = "Borrador local"
)

@Entity(tableName = "lecturas_qr", indices = [Index("clienteId")],
    foreignKeys = [ForeignKey(entity = ClienteEntity::class, parentColumns = ["id"],
        childColumns = ["clienteId"], onDelete = ForeignKey.CASCADE)])
data class LecturaQREntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int, val productoId: Int, val fechaHora: Long = System.currentTimeMillis()
)
