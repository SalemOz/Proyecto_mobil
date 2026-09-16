package com.example.mockup

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorialActivity : AppCompatActivity() {

    // Views
    private lateinit var rvSesiones: RecyclerView
    private lateinit var llEmptyState: LinearLayout
    private lateinit var adapter: SesionesAdapter

    // Bottom nav
    private lateinit var navHome: LinearLayout
    private lateinit var navHistory: LinearLayout
    private lateinit var navSettings: LinearLayout

    // State
    private var userName: String = "Usuario"
    private var selectedEar: String = "right"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Get data from intent
        userName = intent.getStringExtra("USER_NAME")?.trim().takeUnless { it.isNullOrEmpty() } ?: "Usuario"
        selectedEar = intent.getStringExtra("SELECTED_EAR") ?: "right"

        initViews()
        setupRecyclerView()
        setupBottomNav()
    }

    private fun initViews() {
        rvSesiones = findViewById(R.id.rv_sesiones)
        llEmptyState = findViewById(R.id.ll_empty_state)

        navHome = findViewById(R.id.nav_home)
        navHistory = findViewById(R.id.nav_history)
        navSettings = findViewById(R.id.nav_settings)
    }

    private fun setupRecyclerView() {
        rvSesiones.layoutManager = LinearLayoutManager(this)

        val sesiones = HistorialPreferences(this).obtenerSesiones()
        adapter = SesionesAdapter(sesiones)
        rvSesiones.adapter = adapter

        if (sesiones.isEmpty()) {
            llEmptyState.visibility = android.view.View.VISIBLE
            rvSesiones.visibility = android.view.View.GONE
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
            // Already on history
        }
        navSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("USER_NAME", userName)
                putExtra("SELECTED_EAR", selectedEar)
            }
            startActivity(intent)
            finish()
        }
    }
}
