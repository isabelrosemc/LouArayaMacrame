package com.example.applouaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applouaraya.repository.LocalRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel : ViewModel() {
    private val _favoritos = MutableStateFlow<Set<Int>>(emptySet())
    val favoritos = _favoritos.asStateFlow()
    private var repo: LocalRepository? = null
    private var cliente: Int? = null
    private var trabajo: Job? = null
    fun configurar(r: LocalRepository) { repo = r }
    fun seleccionar(id: Int?) {
        trabajo?.cancel(); cliente = id; _favoritos.value = emptySet()
        val r = repo ?: return
        if (id != null) trabajo = viewModelScope.launch {
            r.favoritos(id).collect { lista -> _favoritos.value = lista.map { it.productoId }.toSet() }
        }
    }
    fun cambiar(id: Int) {
        val c = cliente ?: return
        val r = repo ?: return
        val presente = id in _favoritos.value
        viewModelScope.launch { r.cambiarFavorito(c, id, presente) }
    }
}
