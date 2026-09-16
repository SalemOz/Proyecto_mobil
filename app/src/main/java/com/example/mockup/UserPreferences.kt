package com.example.mockup

import android.content.Context

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Guardar sesión del usuario
    fun guardarSesion(nombre: String, oido: String) {
        prefs.edit()
            .putString("USER_NAME", nombre)
            .putString("SELECTED_EAR", oido)
            .putBoolean("IS_LOGGED_IN", true)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("IS_LOGGED_IN", false)

    fun getUserName(): String = prefs.getString("USER_NAME", "") ?: ""

    fun getSelectedEar(): String = prefs.getString("SELECTED_EAR", "right") ?: "right"

    // Borrar solo la sesión del usuario, el historial queda intacto
    fun cerrarSesion() {
        prefs.edit()
            .remove("USER_NAME")
            .remove("SELECTED_EAR")
            .putBoolean("IS_LOGGED_IN", false)
            .apply()
    }
}
