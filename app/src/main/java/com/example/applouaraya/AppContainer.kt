package com.example.applouaraya

import android.content.Context
import com.example.applouaraya.data.local.LouDatabase
import com.example.applouaraya.repository.LocalRepository

class AppContainer(context: Context) {
    val database = LouDatabase.obtener(context)
    val local = LocalRepository(database)
}
