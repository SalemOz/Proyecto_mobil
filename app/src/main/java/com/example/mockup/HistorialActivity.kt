package com.example.mockup

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorialActivity : BaseActivity() {

    // Views
    private lateinit var rvSessions: RecyclerView
    private lateinit var llEmptyState: LinearLayout
    private lateinit var adapter: SesionesAdapter

    // Bottom nav
    private lateinit var bottomNav: BottomNav

    // State
    private var userName: String = "Usuario"
    private var selectedEar: String = "right"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)
        prepararVentana(R.id.root)
        conectarBarraSuperior()

        // Get data from intent
        userName = intent.getStringExtra("USER_NAME")?.trim().takeUnless { it.isNullOrEmpty() } ?: "Usuario"
        selectedEar = intent.getStringExtra("SELECTED_EAR") ?: "right"

        initViews()
        setupRecyclerView()

        bottomNav = BottomNav(this, BottomNav.HISTORIAL, userName, selectedEar)
        bottomNav.instalar()
    }

    override fun onResume() {
        super.onResume()
        // Igual que en las otras pantallas: el resaltado se reaplica al reanudar.
        bottomNav.marcarActiva()
    }

    private fun initViews() {
        rvSessions = findViewById(R.id.rv_sessions)
        llEmptyState = findViewById(R.id.ll_empty_state)
    }

    private fun setupRecyclerView() {
        rvSessions.layoutManager = LinearLayoutManager(this)

        val sesiones = HistorialPreferences(this).obtenerSesiones()
        adapter = SesionesAdapter(sesiones)
        rvSessions.adapter = adapter

        // La lista y el estado vacío son excluyentes.
        val haySesiones = sesiones.isNotEmpty()
        llEmptyState.visibility = if (haySesiones) View.GONE else View.VISIBLE
        rvSessions.visibility = if (haySesiones) View.VISIBLE else View.GONE
    }
}
