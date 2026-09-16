package com.example.mockup

import android.app.Activity
import android.content.Intent
import android.view.View

/**
 * Barra de navegación inferior compartida por Inicio, Historial y Ajustes.
 *
 * Las tres pantallas incluyen el mismo layout (`view_bottom_nav`), así que ni los ids ni la
 * apariencia de la barra se repiten por pantalla:
 *
 *  - Tocar la pestaña actual no navega: solo reaplica el resaltado.
 *  - Al cambiar de pestaña se reutiliza la pantalla destino si ya estaba en la pila
 *    (CLEAR_TOP | SINGLE_TOP) y la pantalla actual se cierra.
 *  - [raiz] marca la pantalla que no se cierra al salir: Inicio se queda viva porque conserva
 *    la amplificación y la sesión de uso en curso.
 *  - [marcarActiva] se reaplica al reanudar cada pantalla, porque una pantalla reutilizada
 *    conservaría el resaltado de la pestaña a la que se fue.
 *
 * El resaltado es el estado `selected` del contenedor de cada pestaña: el icono y la etiqueta lo
 * heredan (`duplicateParentState`) y se pintan solos con el selector `@color/nav_item_color`.
 * Antes había que buscar seis vistas y pintarlas una por una desde aquí.
 *
 * Llamar a [instalar] después de `setContentView`.
 */
class BottomNav(
    private val activity: Activity,
    private val tabActiva: String,
    private val userName: String,
    private val selectedEar: String,
    private val raiz: Boolean = false
) {

    companion object {
        const val INICIO = "home"
        const val HISTORIAL = "history"
        const val AJUSTES = "settings"
    }

    fun instalar() {
        activity.findViewById<View>(R.id.nav_home).setOnClickListener { seleccionar(INICIO) }
        activity.findViewById<View>(R.id.nav_history).setOnClickListener { seleccionar(HISTORIAL) }
        activity.findViewById<View>(R.id.nav_settings).setOnClickListener { seleccionar(AJUSTES) }
        marcarActiva()
    }

    fun marcarActiva() {
        activity.findViewById<View>(R.id.nav_home).isSelected = tabActiva == INICIO
        activity.findViewById<View>(R.id.nav_history).isSelected = tabActiva == HISTORIAL
        activity.findViewById<View>(R.id.nav_settings).isSelected = tabActiva == AJUSTES
    }

    private fun seleccionar(tab: String) {
        if (tab == tabActiva) {
            marcarActiva()
            return
        }

        val destino = when (tab) {
            INICIO -> HomeActivity::class.java
            HISTORIAL -> HistorialActivity::class.java
            else -> SettingsActivity::class.java
        }

        activity.startActivity(Intent(activity, destino).apply {
            putExtra("USER_NAME", userName)
            putExtra("SELECTED_EAR", selectedEar)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        })

        if (!raiz) {
            activity.finish()
        }
    }
}
