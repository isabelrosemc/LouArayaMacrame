package com.example.applouaraya.repository

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ProductoRepositoryTest {

    private val repository = ProductoRepository()

    @Test
    fun `productosLocales retorna los 3 productos base esperados`() {
        val locales = repository.productosLocales()

        assertEquals(3, locales.size)
        assertEquals("Arco de Macramé", locales.first().nombre)
        assertEquals(120000, locales.first().precio)
    }

    @Test
    fun `existeProducto retorna true cuando el id existe en el catalogo local sin red`() = runTest {
        val existe = repository.existeProducto(2)
        val noExiste = repository.existeProducto(9999)

        assertTrue(existe)
        assertFalse(noExiste)
    }
}