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

    private lateinit var cardEarLeft: FrameLayout
    private lateinit var cardEarRight: FrameLayout
    private lateinit var ivEarLeft: ImageView
    private lateinit var ivEarRight: ImageView
    private lateinit var tvEarLeft: TextView
    private lateinit var tvEarRight: TextView

    private lateinit var checkRight: ImageView

    private lateinit var checkLeft: ImageView

    private lateinit var etName: EditText
    private lateinit var btnContinue: Button

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
        cardEarLeft = findViewById(R.id.cardEarLeft)
        cardEarRight = findViewById(R.id.cardEarRight)
        ivEarLeft = findViewById(R.id.ivEarLeft)
        ivEarRight = findViewById(R.id.ivEarRight)
        tvEarLeft = findViewById(R.id.tvEarLeft)
        tvEarRight = findViewById(R.id.tvEarRight)
        checkRight = findViewById(R.id.checkRight)
        checkLeft = findViewById(R.id.checkLeft)
        etName = findViewById(R.id.etName)
        btnContinue = findViewById(R.id.btnContinue)
    }

    private fun setupEarSelection() {
        cardEarLeft.setOnClickListener {
            selectEar("left")
        }

        cardEarRight.setOnClickListener {
            selectEar("right")
        }
    }

    private fun selectEar(ear: String) {
        selectedEar = ear

        if (ear == "left") {
            // Select left
            cardEarLeft.setBackgroundResource(R.drawable.bg_ear_card_selected)
            ivEarLeft.setColorFilter(getColor(R.color.primary_blue))
            tvEarLeft.setTextColor(getColor(R.color.primary_blue))
            checkLeft.visibility = android.view.View.VISIBLE

            // Deselect right
            cardEarRight.setBackgroundResource(R.drawable.bg_ear_card)
            ivEarRight.clearColorFilter()
            tvEarRight.setTextColor(getColor(R.color.text_primary))
            checkRight.visibility = android.view.View.GONE
        } else {
            // Select right
            cardEarRight.setBackgroundResource(R.drawable.bg_ear_card_selected)
            ivEarRight.setColorFilter(getColor(R.color.primary_blue))
            tvEarRight.setTextColor(getColor(R.color.primary_blue))
            checkRight.visibility = android.view.View.VISIBLE

            // Deselect left
            cardEarLeft.setBackgroundResource(R.drawable.bg_ear_card)
            ivEarLeft.clearColorFilter()
            tvEarLeft.setTextColor(getColor(R.color.text_primary))
            checkLeft.visibility = android.view.View.GONE
        }
    }

    private fun setupContinueButton() {
        btnContinue.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                etName.error = "Por favor, ingresa tu nombre"
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
