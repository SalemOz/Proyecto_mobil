package com.example.mockup

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class HomeActivity : BaseActivity() {

    // Views
    private lateinit var tvGreeting: TextView
    private lateinit var tvEarType: TextView
    private lateinit var ivEarIllustration: ImageView
    private lateinit var switchAmplification: SwitchMaterial
    private lateinit var sliderVolume: Slider
    private lateinit var tvVolumePercent: TextView
    private lateinit var llVolumeSection: View
    private lateinit var llAmbientSection: View

    // Modo de ambiente
    private lateinit var btnAmbientLow: View
    private lateinit var btnAmbientMid: View
    private lateinit var btnAmbientHigh: View

    // Bottom nav
    private lateinit var bottomNav: BottomNav

    // State
    private var userName: String = "Usuario"
    private var selectedEar: String = "right"
    private var wasAmplificationActive: Boolean = true
    private var selectedAmbientMode: String = "low"

    // Sesión de uso en curso
    private var horaInicioSesion: Date? = null
    private var oidoSesionActual: String = "right"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        prepararVentana(R.id.root)
        conectarBarraSuperior()

        // Recuperar sesión de uso pendiente al recrear la Activity (rotación, etc.)
        if (savedInstanceState != null) {
            horaInicioSesion = savedInstanceState.getLong("HORA_INICIO", -1L).takeIf { it >= 0 }
                ?.let { Date(it) }
            oidoSesionActual = savedInstanceState.getString("OIDO_SESION") ?: "right"
        }

        // Get data from intent
        userName = intent.getStringExtra("USER_NAME")?.trim().takeUnless { it.isNullOrEmpty() } ?: "Usuario"
        selectedEar = intent.getStringExtra("SELECTED_EAR") ?: "right"

        initViews()
        setupGreeting()
        setupAmplificationToggle()
        setupVolumeSlider()
        setupAmbientMode()

        bottomNav = BottomNav(this, BottomNav.INICIO, userName, selectedEar, raiz = true)
        bottomNav.instalar()
    }

    private fun initViews() {
        tvGreeting = findViewById(R.id.tv_greeting)
        tvEarType = findViewById(R.id.tv_ear_type)
        ivEarIllustration = findViewById(R.id.iv_ear_illustration)
        switchAmplification = findViewById(R.id.switch_amplification)
        sliderVolume = findViewById(R.id.slider_volume)
        tvVolumePercent = findViewById(R.id.tv_volume_percent)
        llVolumeSection = findViewById(R.id.ll_volume_section)
        llAmbientSection = findViewById(R.id.ll_ambient_section)

        btnAmbientLow = findViewById(R.id.btn_ambient_low)
        btnAmbientMid = findViewById(R.id.btn_ambient_mid)
        btnAmbientHigh = findViewById(R.id.btn_ambient_high)
    }

    private fun setupGreeting() {
        tvGreeting.text = getString(R.string.home_greeting, userName)

        if (selectedEar == "left") {
            tvEarType.text = getString(R.string.home_ear_left)
            ivEarIllustration.setImageResource(R.drawable.ear_left)
        } else {
            tvEarType.text = getString(R.string.home_ear_right)
            ivEarIllustration.setImageResource(R.drawable.ear_right)
        }
    }

    private fun setupAmplificationToggle() {
        aplicarEstadoAmplificacion(switchAmplification.isChecked)

        switchAmplification.setOnCheckedChangeListener { _, isChecked ->
            val cambioReal = isChecked != wasAmplificationActive
            if (cambioReal) {
                if (isChecked) {
                    // Iniciar sesión de uso
                    iniciarSesionUso()
                } else {
                    // Finalizar y guardar la sesión de uso
                    finalizarSesionUso()
                }
            }
            wasAmplificationActive = isChecked
            aplicarEstadoAmplificacion(isChecked)
            mostrarMensaje(
                if (isChecked) R.string.home_amplification_on else R.string.home_amplification_off
            )
        }
    }

    /**
     * Con la amplificación apagada, el volumen y el modo de ambiente no se aplican: se atenúan
     * para que se note. Siguen siendo utilizables, porque el usuario puede dejarlos preparados.
     */
    private fun aplicarEstadoAmplificacion(activa: Boolean) {
        val alfa = if (activa) 1f else ALFA_SECCION_INACTIVA
        llVolumeSection.alpha = alfa
        llAmbientSection.alpha = alfa
    }

    private fun setupVolumeSlider() {
        tvVolumePercent.text = getString(R.string.home_volume_percent, sliderVolume.value.roundToInt())

        sliderVolume.setLabelFormatter { value ->
            getString(R.string.home_volume_percent, value.roundToInt())
        }
        sliderVolume.addOnChangeListener { _, value, _ ->
            tvVolumePercent.text = getString(R.string.home_volume_percent, value.roundToInt())
        }
    }

    private fun setupAmbientMode() {
        btnAmbientLow.setOnClickListener { selectAmbientMode("low") }
        btnAmbientMid.setOnClickListener { selectAmbientMode("medium") }
        btnAmbientHigh.setOnClickListener { selectAmbientMode("high") }
        selectAmbientMode(selectedAmbientMode)
    }

    /**
     * El botón activo se marca como seleccionado y el icono y la etiqueta se pintan solos con el
     * selector de color. Antes había que reiniciar fondos, colores de texto y tintes de los tres
     * botones (y sus hijos) a mano.
     */
    private fun selectAmbientMode(mode: String) {
        selectedAmbientMode = mode
        btnAmbientLow.isSelected = mode == "low"
        btnAmbientMid.isSelected = mode == "medium"
        btnAmbientHigh.isSelected = mode == "high"
    }

    override fun onResume() {
        super.onResume()
        // La amplificación puede estar activa sin que el usuario haya tocado el switch
        // (estado por defecto o restaurado al recrear la Activity): en ese caso la sesión
        // de uso empieza ahora, para que no se pierda el primer periodo de uso.
        if (switchAmplification.isChecked && horaInicioSesion == null) {
            iniciarSesionUso()
        }
        // Inicio se reutiliza al volver de otra pantalla, así que el resaltado se reaplica aquí.
        bottomNav.marcarActiva()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        horaInicioSesion?.let { outState.putLong("HORA_INICIO", it.time) }
        outState.putString("OIDO_SESION", oidoSesionActual)
    }

    override fun onDestroy() {
        // Al salir de la pantalla (botón atrás, cierre de sesión, ...) se guarda la sesión
        // que quedó en curso. Un cambio de configuración no la cierra: se restaura con la Activity.
        if (isFinishing) {
            finalizarSesionUso()
        }
        super.onDestroy()
    }

    private fun iniciarSesionUso() {
        horaInicioSesion = Date()
        oidoSesionActual = selectedEar
    }

    private fun finalizarSesionUso() {
        val inicio = horaInicioSesion
        horaInicioSesion = null
        if (inicio == null) return

        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formatoHora = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val oido = if (oidoSesionActual == "left") {
            getString(R.string.home_ear_left)
        } else {
            getString(R.string.home_ear_right)
        }
        val sesion = SesionUso(
            fecha = formato.format(inicio),
            horaInicio = formatoHora.format(inicio),
            horaFin = formatoHora.format(Date()),
            oido = oido
        )
        HistorialPreferences(this).agregarSesion(sesion)
    }

    private companion object {
        /** Alfa de las secciones que no aplican cuando la amplificación está apagada. */
        const val ALFA_SECCION_INACTIVA = 0.45f
    }
}
