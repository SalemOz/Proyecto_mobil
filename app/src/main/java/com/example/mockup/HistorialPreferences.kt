package com.example.mockup

import android.content.Context
import org.json.JSONArray

class HistorialPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("historial_prefs", Context.MODE_PRIVATE)

    // Guardar historial
    fun guardarSesiones(sesiones: List<SesionUso>) {
        prefs.edit().putString("sesiones", SesionUso.listToJson(sesiones).toString()).apply()
    }

    // Recuperar historial
    fun obtenerSesiones(): List<SesionUso> {
        val json = prefs.getString("sesiones", null) ?: return emptyList()
        return try {
            SesionUso.listFromJson(JSONArray(json))
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun agregarSesion(sesion: SesionUso) {
        val lista = obtenerSesiones().toMutableList()
        lista.add(0, sesion)
        guardarSesiones(lista)
    }
}
