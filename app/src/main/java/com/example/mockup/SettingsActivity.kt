package com.example.mockup

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

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
    private lateinit var navHome: LinearLayout
    private lateinit var navHistory: LinearLayout
    private lateinit var navSettings: LinearLayout
    private lateinit var ivNavHome: ImageView
    private lateinit var ivNavHistory: ImageView
    private lateinit var ivNavSettings: ImageView
    private lateinit var tvNavHome: TextView
    private lateinit var tvNavHistory: TextView
    private lateinit var tvNavSettings: TextView

    // State
    private var userName: String = "Della"
    private var selectedEar: String = "right"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Get data from intent
        userName = intent.getStringExtra("USER_NAME") ?: "Della"
        selectedEar = intent.getStringExtra("SELECTED_EAR") ?: "right"

        initViews()
        setupProfile()
        setupPreferences()
        setupSupportRows()
        setupBottomNav()
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

        navHome = findViewById(R.id.nav_home)
        navHistory = findViewById(R.id.nav_history)
        navSettings = findViewById(R.id.nav_settings)
        ivNavHome = findViewById(R.id.iv_nav_home)
        ivNavHistory = findViewById(R.id.iv_nav_history)
        ivNavSettings = findViewById(R.id.iv_nav_settings)
        tvNavHome = findViewById(R.id.tv_nav_home)
        tvNavHistory = findViewById(R.id.tv_nav_history)
        tvNavSettings = findViewById(R.id.tv_nav_settings)
    }

    private fun setupProfile() {
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
            val msg = if (isChecked) "Notificaciones activadas" else "Notificaciones desactivadas"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        rowDefaultAmbient.setOnClickListener {
            // TODO: Show ambient mode selector dialog
            Toast.makeText(this, "Seleccionar modo de ambiente", Toast.LENGTH_SHORT).show()
        }

        rowLanguage.setOnClickListener {
            // TODO: Show language selector dialog
            Toast.makeText(this, "Seleccionar idioma", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSupportRows() {
        rowHelp.setOnClickListener {
            // TODO: Navigate to help screen
            Toast.makeText(this, "Ayuda y soporte - Próximamente", Toast.LENGTH_SHORT).show()
        }

        rowAbout.setOnClickListener {
            // TODO: Navigate to about screen
            Toast.makeText(this, "Acerca de Oído+ - Próximamente", Toast.LENGTH_SHORT).show()
        }

        rowLogout.setOnClickListener {
            // TODO: Show logout confirmation dialog
            Toast.makeText(this, "Cerrar sesión - Próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNav() {
        navHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_NAME", userName)
                putExtra("SELECTED_EAR", selectedEar)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
        navHistory.setOnClickListener {
            Toast.makeText(this, "Historial - Próximamente", Toast.LENGTH_SHORT).show()
        }
        navSettings.setOnClickListener {
            // Already on settings
        }

        // Highlight active tab
        ivNavSettings.setColorFilter(getColor(R.color.brand_blue))
        tvNavSettings.setTextColor(getColor(R.color.brand_blue))
    }
}
