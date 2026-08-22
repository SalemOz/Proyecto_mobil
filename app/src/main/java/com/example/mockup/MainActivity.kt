package com.example.mockup

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var selectedEar: String = "right"

    private lateinit var flOidoIzquierdo: FrameLayout
    private lateinit var flOidoDerecho: FrameLayout

    private lateinit var ivOidoIzquierdo: ImageView
    private lateinit var ivOidoDerecho: ImageView

    private lateinit var tvOidoIzquierdo: TextView
    private lateinit var tvOidoDerecho: TextView

    private lateinit var ivCheckDerecho: ImageView
    private lateinit var ivCheckIzquierdo: ImageView

    private lateinit var etNombre: EditText
    private lateinit var btnContinuar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupEarSelection()
        setupContinueButton()
    }

    private fun initViews() {

        flOidoIzquierdo = findViewById(R.id.fl_oido_izquierdo)
        flOidoDerecho = findViewById(R.id.fl_oido_derecho)

        ivOidoIzquierdo = findViewById(R.id.iv_oido_izquierdo)
        ivOidoDerecho = findViewById(R.id.iv_oido_derecho)

        tvOidoIzquierdo = findViewById(R.id.tv_oido_izquierdo)
        tvOidoDerecho = findViewById(R.id.tv_oido_derecho)

        ivCheckDerecho = findViewById(R.id.iv_check_derecho)
        ivCheckIzquierdo = findViewById(R.id.iv_check_izquierdo)

        etNombre = findViewById(R.id.et_nombre)
        btnContinuar = findViewById(R.id.btn_continuar)
    }

    private fun setupEarSelection() {
        flOidoIzquierdo.setOnClickListener {
            selectEar("left")
        }

        flOidoDerecho.setOnClickListener {
            selectEar("right")
        }
    }

    private fun selectEar(ear: String) {
        selectedEar = ear

        if (ear == "left") {
            // Select left
            flOidoIzquierdo.setBackgroundResource(R.drawable.bg_ear_card_selected)
            ivOidoIzquierdo.setColorFilter(getColor(R.color.primary_blue))
            tvOidoIzquierdo.setTextColor(getColor(R.color.primary_blue))
            ivCheckIzquierdo.visibility = android.view.View.VISIBLE

            // Deselect right
            flOidoDerecho.setBackgroundResource(R.drawable.bg_ear_card)
            ivOidoDerecho.clearColorFilter()
            tvOidoDerecho.setTextColor(getColor(R.color.text_primary))
            ivCheckDerecho.visibility = android.view.View.GONE
        } else {
            // Select right
            flOidoDerecho.setBackgroundResource(R.drawable.bg_ear_card_selected)
            ivOidoDerecho.setColorFilter(getColor(R.color.primary_blue))
            tvOidoDerecho.setTextColor(getColor(R.color.primary_blue))
            ivCheckDerecho.visibility = android.view.View.VISIBLE

            // Deselect left
            flOidoIzquierdo.setBackgroundResource(R.drawable.bg_ear_card)
            ivOidoIzquierdo.clearColorFilter()
            tvOidoIzquierdo.setTextColor(getColor(R.color.text_primary))
            ivCheckIzquierdo.visibility = android.view.View.GONE
        }
    }

    private fun setupContinueButton() {
        btnContinuar.setOnClickListener {
            val name = etNombre.text.toString().trim()
            if (name.isEmpty()) {
                etNombre.error = "Por favor, ingresa tu nombre"
                return@setOnClickListener
            }

            val earText = if (selectedEar == "left") "Izquierdo" else "Derecho"
            Toast.makeText(
                this,
                "Hola $name. Oído afectado: $earText",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
