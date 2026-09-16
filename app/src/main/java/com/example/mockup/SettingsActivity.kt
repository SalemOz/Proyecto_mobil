package com.example.mockup

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : BaseActivity() {

    // Views
    private lateinit var tvAvatarLetter: TextView
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEar: TextView
    private lateinit var switchNotifications: SwitchMaterial
    private lateinit var tvDefaultAmbientValue: TextView
    private lateinit var tvLanguageValue: TextView
    private lateinit var rowDefaultAmbient: LinearLayout
    private lateinit var rowLanguage: LinearLayout
    private lateinit var rowHelp: LinearLayout
    private lateinit var rowAbout: LinearLayout
    private lateinit var rowLogout: LinearLayout

    // Bottom nav
    private lateinit var bottomNav: BottomNav

    // State
    private var userName: String = "Usuario"
    private var selectedEar: String = "right"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        prepararVentana(R.id.root)
        conectarBarraSuperior()

        // Get data from intent
        userName = intent.getStringExtra("USER_NAME")?.trim().takeUnless { it.isNullOrEmpty() } ?: "Usuario"
        selectedEar = intent.getStringExtra("SELECTED_EAR") ?: "right"

        initViews()
        setupProfile()
        setupPreferences()
        setupSupportRows()

        // El resaltado de la barra inferior es el mismo en las tres pantallas (selector
        // @color/nav_item_color), así que Ajustes ya no pasa sus propios colores.
        bottomNav = BottomNav(this, BottomNav.AJUSTES, userName, selectedEar)
        bottomNav.instalar()
    }

    override fun onResume() {
        super.onResume()
        // Igual que en las otras pantallas: el resaltado se reaplica al reanudar.
        bottomNav.marcarActiva()
    }

    private fun initViews() {
        tvAvatarLetter = findViewById(R.id.tv_avatar_letter)
        tvProfileName = findViewById(R.id.tv_profile_name)
        tvProfileEar = findViewById(R.id.tv_profile_ear)
        switchNotifications = findViewById(R.id.switch_notifications)
        tvDefaultAmbientValue = findViewById(R.id.tv_default_ambient_value)
        tvLanguageValue = findViewById(R.id.tv_language_value)
        rowDefaultAmbient = findViewById(R.id.row_default_ambient)
        rowLanguage = findViewById(R.id.row_language)
        rowHelp = findViewById(R.id.row_help)
        rowAbout = findViewById(R.id.row_about)
        rowLogout = findViewById(R.id.row_logout)
    }

    private fun setupProfile() {
        // La inicial del avatar es decorativa: el nombre completo está justo al lado.
        tvAvatarLetter.text = if (userName.isNotEmpty()) userName.first().uppercaseChar().toString() else "?"
        tvProfileName.text = userName

        val earLabel = if (selectedEar == "left") {
            getString(R.string.settings_ear_left)
        } else {
            getString(R.string.settings_ear_right)
        }
        tvProfileEar.text = getString(R.string.settings_affected_ear, earLabel)
    }

    private fun setupPreferences() {
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            mostrarMensaje(
                if (isChecked) R.string.settings_notifications_on
                else R.string.settings_notifications_off
            )
        }

        rowDefaultAmbient.setOnClickListener {
            val modes = arrayOf(
                getString(R.string.home_ambient_low),
                getString(R.string.home_ambient_mid),
                getString(R.string.home_ambient_high)
            )
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_ambient_dialog_title)
                .setSingleChoiceItems(modes, modes.indexOf(tvDefaultAmbientValue.text.toString())) { dialog, which ->
                    tvDefaultAmbientValue.text = modes[which]
                    dialog.dismiss()
                }
                .show()
        }

        rowLanguage.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_language_dialog_title)
                .setSingleChoiceItems(arrayOf(getString(R.string.settings_spanish)), 0) { dialog, _ ->
                    tvLanguageValue.text = getString(R.string.settings_spanish)
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setupSupportRows() {
        rowHelp.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_help)
                .setMessage(R.string.settings_help_message)
                .setPositiveButton(R.string.settings_help_confirm, null)
                .show()
        }

        rowAbout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_about)
                .setMessage(R.string.settings_about_message)
                .setPositiveButton(R.string.settings_about_confirm, null)
                .show()
        }

        rowLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_logout)
                .setMessage(R.string.settings_logout_message)
                .setNegativeButton(R.string.settings_logout_cancel, null)
                .setPositiveButton(R.string.settings_logout) { _, _ ->
                    // Borrar solo la sesión del usuario, el historial se conserva
                    UserPreferences(this).cerrarSesion()
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                .show()
        }
    }
}
