package com.example.mockup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioButton

class MainActivity : BaseActivity() {

    private var selectedEar: String = "right"

    private lateinit var flEarLeft: FrameLayout
    private lateinit var flEarRight: FrameLayout

    private lateinit var rbEarLeft: RadioButton
    private lateinit var rbEarRight: RadioButton

    private lateinit var etName: EditText
    private lateinit var btnContinue: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // La raíz es el ScrollView: sin su padding inferior la tarjeta del botón quedaría
        // debajo de la barra de navegación del sistema.
        prepararVentana(R.id.root, paddingInferiorEnRaiz = true)

        // Si ya hay sesión guardada, entrar directo a Home
        val userPrefs = UserPreferences(this)
        if (userPrefs.isLoggedIn()) {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_NAME", userPrefs.getUserName())
                putExtra("SELECTED_EAR", userPrefs.getSelectedEar())
            }
            startActivity(intent)
            finish()
            return
        }

        initViews()
        setupEarSelection()
        setupContinueButton()
    }

    private fun initViews() {
        flEarLeft = findViewById(R.id.fl_ear_left)
        flEarRight = findViewById(R.id.fl_ear_right)

        rbEarLeft = findViewById(R.id.rb_ear_left)
        rbEarRight = findViewById(R.id.rb_ear_right)

        etName = findViewById(R.id.et_name)
        btnContinue = findViewById(R.id.btn_continue)
    }

    private fun setupEarSelection() {
        flEarLeft.setOnClickListener { selectEar("left") }
        flEarRight.setOnClickListener { selectEar("right") }
        rbEarLeft.setOnClickListener { selectEar("left") }
        rbEarRight.setOnClickListener { selectEar("right") }

        selectEar(selectedEar)
    }

    /**
     * La tarjeta del oído elegido se queda seleccionada y el borde, el icono y la etiqueta se
     * pintan solos con el selector de color. Antes había que reiniciar fondos, tintes y colores
     * de seis vistas en cada toque.
     */
    private fun selectEar(ear: String) {
        selectedEar = ear
        val isLeft = ear == "left"

        flEarLeft.isSelected = isLeft
        flEarRight.isSelected = !isLeft
        rbEarLeft.isChecked = isLeft
        rbEarRight.isChecked = !isLeft
    }

    private fun setupContinueButton() {
        val nameRegex = "^[\\p{L}][\\p{L} '\\-]{1,49}$".toRegex()
        val consecutiveRegex = "(.)\\1".toRegex()
        val reservedNames = listOf("admin", "test", "usuario", "user", "root", "prueba")

        btnContinue.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                etName.error = getString(R.string.error_name_required)
                return@setOnClickListener
            }

            if (!nameRegex.matches(name)) {
                etName.error = getString(R.string.error_name_invalid)
                return@setOnClickListener
            }

            val stripped = name.lowercase().replace(" ", "").replace("-", "").replace("'", "")
            if (consecutiveRegex.containsMatchIn(stripped)) {
                etName.error = getString(R.string.error_name_repeated)
                return@setOnClickListener
            }

            if (reservedNames.contains(name.lowercase())) {
                etName.error = getString(R.string.error_name_reserved)
                return@setOnClickListener
            }

            // Guardar sesión del usuario y entrar a Home
            UserPreferences(this).guardarSesion(name, selectedEar)
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_NAME", name)
                putExtra("SELECTED_EAR", selectedEar)
            }
            startActivity(intent)
            finish()
        }
    }
}
