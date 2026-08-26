package com.example.mockup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.RadioButton
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

    private lateinit var rbOidoDerecho: RadioButton
    private lateinit var rbOidoIzquierdo: RadioButton

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

        rbOidoDerecho = findViewById(R.id.rb_oido_derecho)
        rbOidoIzquierdo = findViewById(R.id.rb_oido_izquierdo)

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
        rbOidoIzquierdo.setOnClickListener { selectEar("left") }
        rbOidoDerecho.setOnClickListener { selectEar("right") }
    }

    private fun selectEar(ear: String) {
        selectedEar = ear

        if (ear == "left") {
            // Select left
            flOidoIzquierdo.setBackgroundResource(R.drawable.bg_ear_card_selected)
            ivOidoIzquierdo.setColorFilter(getColor(R.color.primary_blue))
            tvOidoIzquierdo.setTextColor(getColor(R.color.primary_blue))
            rbOidoIzquierdo.isChecked = true

            // Deselect right
            flOidoDerecho.setBackgroundResource(R.drawable.bg_ear_card)
            ivOidoDerecho.clearColorFilter()
            tvOidoDerecho.setTextColor(getColor(R.color.text_primary))
            rbOidoDerecho.isChecked = false
        } else {
            // Select right
            flOidoDerecho.setBackgroundResource(R.drawable.bg_ear_card_selected)
            ivOidoDerecho.setColorFilter(getColor(R.color.primary_blue))
            tvOidoDerecho.setTextColor(getColor(R.color.primary_blue))
            rbOidoDerecho.isChecked = true

            // Deselect left
            flOidoIzquierdo.setBackgroundResource(R.drawable.bg_ear_card)
            ivOidoIzquierdo.clearColorFilter()
            tvOidoIzquierdo.setTextColor(getColor(R.color.text_primary))
            rbOidoIzquierdo.isChecked = false
        }
    }

    private fun setupContinueButton() {
        val nameRegex = "^[\\p{L}][\\p{L} '\\-]{1,49}$".toRegex()
        val consecutiveRegex = "(.)\\1".toRegex()
        val reservedNames = listOf("admin", "test", "usuario", "user", "root", "prueba")

        btnContinuar.setOnClickListener {
            val name = etNombre.text.toString().trim()
            if (name.isEmpty()) {
                etNombre.error = "Por favor, ingresa tu nombre"
                return@setOnClickListener
            }

            if (!nameRegex.matches(name)) {
                etNombre.error = "Nombre no válido. Usa solo letras, espacios, guiones o apóstrofes (2-50 caracteres)"
                return@setOnClickListener
            }

            val stripped = name.lowercase().replace(" ", "").replace("-", "").replace("'", "")
            if (consecutiveRegex.containsMatchIn(stripped)) {
                etNombre.error = "El nombre no debe tener caracteres repetidos consecutivos"
                return@setOnClickListener
            }

            if (reservedNames.contains(name.lowercase())) {
                etNombre.error = "Este nombre no está permitido"
                return@setOnClickListener
            }

            // Navigate to HomeActivity with user data
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_NAME", name)
                putExtra("SELECTED_EAR", selectedEar)
            }
            startActivity(intent)
        }
    }
}
