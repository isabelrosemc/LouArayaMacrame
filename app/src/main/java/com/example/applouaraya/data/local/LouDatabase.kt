package com.example.applouaraya.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ClienteEntity::class, FavoritoEntity::class,
    SolicitudEntity::class, LecturaQREntity::class], version = 2, exportSchema = true)
abstract class LouDatabase : RoomDatabase() {
    abstract fun clienteDao(): ClienteDao
    abstract fun favoritoDao(): FavoritoDao
    abstract fun solicitudDao(): SolicitudDao
    abstract fun lecturaQRDao(): LecturaQRDao
    companion object {
        @Volatile private var instance: LouDatabase? = null
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""CREATE TABLE IF NOT EXISTS lecturas_qr (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    clienteId INTEGER NOT NULL,
                    productoId INTEGER NOT NULL,
                    fechaHora INTEGER NOT NULL,
                    FOREIGN KEY(clienteId) REFERENCES clientes(id) ON UPDATE NO ACTION ON DELETE CASCADE
                )""".trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS index_lecturas_qr_clienteId ON lecturas_qr(clienteId)")
            }
        }
        fun obtener(context: Context): LouDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context.applicationContext,
                    LouDatabase::class.java, "lou_araya_database")
                    .addMigrations(MIGRATION_1_2).build().also { instance = it }
            }
    }
}
