package com.example.applouaraya.viewmodel

import com.example.applouaraya.model.Producto
import com.example.applouaraya.repository.ProductoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val sampleProductos = listOf(
        Producto(1, "Arco de Macramé", "Matrimonios", "Arco artesanal.", "Algodón", "200 x 180 cm", 120000, true),
        Producto(2, "Colgante Festival", "Festivales", "Decoración eventos.", "Cordón", "80 x 50 cm", 45000, true)
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarProductos exitoso actualiza uiState con productos y sin error`() = runTest(dispatcher) {
        val repoMock = mockk<ProductoRepository>()
        coEvery { repoMock.obtenerProductos() } returns sampleProductos

        val vm = ProductoViewModel(repository = repoMock)
        advanceUntilIdle()

        assertFalse(vm.uiState.value.isLoading)
        assertEquals(sampleProductos, vm.uiState.value.productos)
        assertNull(vm.uiState.value.errorMessage)
        assertEquals(sampleProductos.first(), vm.obtener(1))
    }

    @Test
    fun `cargarProductos con error usa catalogo local de respaldo y muestra mensaje`() = runTest(dispatcher) {
        val repoMock = mockk<ProductoRepository>()
        coEvery { repoMock.obtenerProductos() } throws RuntimeException("Sin red")
        every { repoMock.productosLocales() } returns sampleProductos

        val vm = ProductoViewModel(repository = repoMock)
        advanceUntilIdle()

        assertFalse(vm.uiState.value.isLoading)
        assertEquals(sampleProductos, vm.uiState.value.productos)
        assertNotNull(vm.uiState.value.errorMessage)
        assertTrue(vm.uiState.value.errorMessage!!.contains("Sin conexión con el servidor"))
    }

    @Test
    fun `buscar y filtrar actualizan sus respectivos flujos`() = runTest(dispatcher) {
        val repoMock = mockk<ProductoRepository>()
        coEvery { repoMock.obtenerProductos() } returns sampleProductos

        val vm = ProductoViewModel(repository = repoMock)
        advanceUntilIdle()

        vm.buscar("Arco")
        vm.filtrar("Matrimonios")

        assertEquals("Arco", vm.busqueda.value)
        assertEquals("Matrimonios", vm.categoria.value)
    }

    @Test
    fun `agregarProducto agrega el producto creado a la lista`() = runTest(dispatcher) {
        val repoMock = mockk<ProductoRepository>()
        val nuevo = Producto(3, "Tapiz Bohemio", "Interiores", "Tapiz hogar.", "Algodón", "120 x 80 cm", 65000, false)
        coEvery { repoMock.obtenerProductos() } returns sampleProductos
        coEvery { repoMock.crearProducto(nuevo) } returns nuevo

        val vm = ProductoViewModel(repository = repoMock)
        advanceUntilIdle()

        vm.agregarProducto(nuevo)
        advanceUntilIdle()

        assertEquals(3, vm.uiState.value.productos.size)
        assertTrue(vm.uiState.value.productos.any { it.id == 3 })
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun `agregarProducto con error actualiza errorMessage`() = runTest(dispatcher) {
        val repoMock = mockk<ProductoRepository>()
        val nuevo = Producto(3, "Tapiz Bohemio", "Interiores", "Tapiz hogar.", "Algodón", "120 x 80 cm", 65000, false)
        coEvery { repoMock.obtenerProductos() } returns sampleProductos
        coEvery { repoMock.crearProducto(nuevo) } throws RuntimeException("Error POST")

        val vm = ProductoViewModel(repository = repoMock)
        advanceUntilIdle()

        vm.agregarProducto(nuevo)
        advanceUntilIdle()

        assertEquals(2, vm.uiState.value.productos.size)
        assertNotNull(vm.uiState.value.errorMessage)
        assertTrue(vm.uiState.value.errorMessage!!.contains("No se pudo crear el producto"))
    }

    @Test
    fun `actualizarProducto y eliminarProducto actualizan la lista correctamente`() = runTest(dispatcher) {
        val repoMock = mockk<ProductoRepository>()
        val actualizado = sampleProductos.first().copy(precio = 135000)
        coEvery { repoMock.obtenerProductos() } returns sampleProductos
        coEvery { repoMock.actualizarProducto(actualizado) } returns actualizado
        coEvery { repoMock.eliminarProducto(2) } returns Unit

        val vm = ProductoViewModel(repository = repoMock)
        advanceUntilIdle()

        vm.actualizarProducto(actualizado)
        advanceUntilIdle()
        assertEquals(135000, vm.obtener(1)?.precio)

        vm.eliminarProducto(2)
        advanceUntilIdle()
        assertEquals(1, vm.uiState.value.productos.size)
        assertNull(vm.obtener(2))
        coVerify(exactly = 1) { repoMock.eliminarProducto(2) }
    }

    @Test
    fun `actualizarProducto y eliminarProducto con error reportan errorMessage`() = runTest(dispatcher) {
        val repoMock = mockk<ProductoRepository>()
        val actualizado = sampleProductos.first().copy(precio = 135000)
        coEvery { repoMock.obtenerProductos() } returns sampleProductos
        coEvery { repoMock.actualizarProducto(actualizado) } throws RuntimeException("Error PUT")
        coEvery { repoMock.eliminarProducto(2) } throws RuntimeException("Error DELETE")

        val vm = ProductoViewModel(repository = repoMock)
        advanceUntilIdle()

        vm.actualizarProducto(actualizado)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.errorMessage!!.contains("No se pudo actualizar el producto"))

        vm.eliminarProducto(2)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.errorMessage!!.contains("No se pudo eliminar el producto"))
    }
}