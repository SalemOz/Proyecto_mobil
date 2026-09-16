package com.example.mockup

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

/**
 * Base de las cuatro pantallas.
 *
 * Reúne lo que antes cada Activity resolvía por su cuenta y de forma desigual:
 *
 *  - Las barras del sistema: modo borde a borde con iconos oscuros, porque la app es clara
 *    aunque el móvil esté en modo oscuro. Ajustes era la única pantalla que las configuraba,
 *    y lo hacía a mano (statusBarColor/navigationBarColor + WindowInsetsControllerCompat).
 *  - Los insets: la raíz reserva el alto de la barra de estado y la barra inferior reserva el
 *    de la barra de gestos, que antes se ignoraba en las tres pantallas principales.
 *  - Los avisos al usuario: Snackbar en lugar de Toast, para que el mensaje sea parte de la
 *    pantalla y quede por encima de la barra de navegación.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = ESTILO_BARRA_CLARA,
            navigationBarStyle = ESTILO_BARRA_CLARA
        )
    }

    /**
     * Aplica los insets del sistema a la raíz de la pantalla y a su barra inferior.
     *
     * @param rootId id de la vista raíz del layout.
     * @param paddingInferiorEnRaiz true en el onboarding, que no tiene barra inferior y necesita
     *   que su contenido (el botón Continuar) quede por encima de la barra del sistema.
     */
    protected fun prepararVentana(@IdRes rootId: Int, paddingInferiorEnRaiz: Boolean = false) {
        val raiz = findViewById<View>(rootId)
        val barraInferior = raiz.findViewById<View>(R.id.bottom_nav_container)
        // Se guarda el padding del layout para poder sumarle el inset sin acumularlo en cada
        // llamada (el listener de insets se dispara más de una vez).
        val paddingInferiorOriginal = barraInferior?.paddingBottom ?: 0

        ViewCompat.setOnApplyWindowInsetsListener(raiz) { view, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                barras.left,
                barras.top,
                barras.right,
                if (paddingInferiorEnRaiz) barras.bottom else 0
            )
            barraInferior?.setPadding(
                barraInferior.paddingLeft,
                barraInferior.paddingTop,
                barraInferior.paddingRight,
                paddingInferiorOriginal + barras.bottom
            )
            insets
        }
    }

    /**
     * Da respuesta a los iconos de la barra superior. Todavía no llevan a ninguna pantalla, así
     * que antes eran iconos pulsables sin efecto y con la etiqueta de accesibilidad equivocada;
     * ahora informan de que la acción está pendiente.
     */
    protected fun conectarBarraSuperior() {
        listOf(R.id.iv_menu, R.id.iv_notifications).forEach { id ->
            findViewById<View>(id)?.setOnClickListener { mostrarMensaje(R.string.action_coming_soon) }
        }
    }

    /** Muestra un aviso breve por encima de la barra de navegación inferior. */
    protected fun mostrarMensaje(@StringRes mensajeRes: Int) {
        Snackbar.make(findViewById(R.id.root), mensajeRes, Snackbar.LENGTH_SHORT)
            .setAnchorView(R.id.bottom_nav_container)
            .show()
    }

    private companion object {
        /** Barras transparentes con iconos oscuros: la app siempre se ve en claro. */
        val ESTILO_BARRA_CLARA = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
    }
}
