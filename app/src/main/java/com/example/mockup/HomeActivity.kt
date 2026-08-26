package com.example.mockup

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.content.Intent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class HomeActivity : AppCompatActivity() {

    // Views
    private lateinit var tvGreeting: TextView
    private lateinit var tvEarType: TextView
    private lateinit var ivEarIllustration: ImageView
    private lateinit var switchAmplification: SwitchMaterial
    private lateinit var tvMicStatus: TextView
    private lateinit var viewStatusDot: android.view.View
    private lateinit var seekBarVolume: SeekBar
    private lateinit var tvVolumePercent: TextView

    // Ambient mode buttons
    private lateinit var btnAmbientLow: LinearLayout
    private lateinit var btnAmbientMid: LinearLayout
    private lateinit var btnAmbientHigh: LinearLayout

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
    private var userName: String = "Delia"
    private var selectedEar: String = "right"
    private var isAmplificationActive: Boolean = true
    private var selectedAmbientMode: String = "low"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Get data from intent
        userName = intent.getStringExtra("USER_NAME") ?: "Delia"
        selectedEar = intent.getStringExtra("SELECTED_EAR") ?: "right"

        initViews()
        setupGreeting()
        setupAmplificationToggle()
        setupVolumeSlider()
        setupAmbientMode()
        setupBottomNav()
    }

    private fun initViews() {
        tvGreeting = findViewById(R.id.tv_greeting)
        tvEarType = findViewById(R.id.tv_ear_type)
        ivEarIllustration = findViewById(R.id.iv_ear_illustration)
        switchAmplification = findViewById(R.id.switch_amplification)
        tvMicStatus = findViewById(R.id.tv_mic_status)
        viewStatusDot = findViewById(R.id.view_status_dot)
        seekBarVolume = findViewById(R.id.seekbar_volume)
        tvVolumePercent = findViewById(R.id.tv_volume_percent)

        btnAmbientLow = findViewById(R.id.btn_ambient_low)
        btnAmbientMid = findViewById(R.id.btn_ambient_mid)
        btnAmbientHigh = findViewById(R.id.btn_ambient_high)

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

    private fun setupGreeting() {
        tvGreeting.text = getString(R.string.home_greeting, userName)

        if (selectedEar == "left") {
            tvEarType.text = getString(R.string.home_ear_left)
            ivEarIllustration.setImageResource(R.drawable.ic_ear_hero)
        } else {
            tvEarType.text = getString(R.string.home_ear_right)
            ivEarIllustration.setImageResource(R.drawable.ic_ear_hero)
        }
    }

    private fun setupAmplificationToggle() {
        switchAmplification.setOnCheckedChangeListener { _, isChecked ->
            isAmplificationActive = isChecked
            if (isChecked) {
                switchAmplification.trackTintList =
                    android.content.res.ColorStateList.valueOf(getColor(R.color.toggle_track_on))
                Toast.makeText(this, "Amplificación activada", Toast.LENGTH_SHORT).show()
            } else {
                switchAmplification.trackTintList =
                    android.content.res.ColorStateList.valueOf(getColor(R.color.toggle_track_off))
                Toast.makeText(this, "Amplificación desactivada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupVolumeSlider() {
        tvVolumePercent.text = getString(R.string.home_volume_percent, seekBarVolume.progress)

        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvVolumePercent.text = getString(R.string.home_volume_percent, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupAmbientMode() {
        selectAmbientMode("low")

        btnAmbientLow.setOnClickListener { selectAmbientMode("low") }
        btnAmbientMid.setOnClickListener { selectAmbientMode("medium") }
        btnAmbientHigh.setOnClickListener { selectAmbientMode("high") }
    }

    private fun selectAmbientMode(mode: String) {
        selectedAmbientMode = mode

        // Reset all buttons
        btnAmbientLow.setBackgroundResource(R.drawable.bg_ambient_button)
        btnAmbientMid.setBackgroundResource(R.drawable.bg_ambient_button)
        btnAmbientHigh.setBackgroundResource(R.drawable.bg_ambient_button)

        // Reset text colors
        (btnAmbientLow.getChildAt(1) as TextView).setTextColor(getColor(R.color.text_secondary))
        (btnAmbientMid.getChildAt(1) as TextView).setTextColor(getColor(R.color.text_secondary))
        (btnAmbientHigh.getChildAt(1) as TextView).setTextColor(getColor(R.color.text_secondary))

        // Update icon tints
        updateAmbientIconTint(btnAmbientLow, R.color.text_secondary)
        updateAmbientIconTint(btnAmbientMid, R.color.text_secondary)
        updateAmbientIconTint(btnAmbientHigh, R.color.text_secondary)

        // Select the active one
        when (mode) {
            "low" -> {
                btnAmbientLow.setBackgroundResource(R.drawable.bg_ambient_button_selected)
                (btnAmbientLow.getChildAt(1) as TextView).setTextColor(getColor(R.color.brand_blue))
                updateAmbientIconTint(btnAmbientLow, R.color.brand_blue)
            }
            "medium" -> {
                btnAmbientMid.setBackgroundResource(R.drawable.bg_ambient_button_selected)
                (btnAmbientMid.getChildAt(1) as TextView).setTextColor(getColor(R.color.brand_blue))
                updateAmbientIconTint(btnAmbientMid, R.color.brand_blue)
            }
            "high" -> {
                btnAmbientHigh.setBackgroundResource(R.drawable.bg_ambient_button_selected)
                (btnAmbientHigh.getChildAt(1) as TextView).setTextColor(getColor(R.color.brand_blue))
                updateAmbientIconTint(btnAmbientHigh, R.color.brand_blue)
            }
        }
    }

    private fun updateAmbientIconTint(button: LinearLayout, colorRes: Int) {
        val icon = button.getChildAt(0) as? ImageView
        icon?.setColorFilter(getColor(colorRes))
    }

    private fun setupBottomNav() {
        navHome.setOnClickListener {
            selectNavItem("home")
        }
        navHistory.setOnClickListener {
            selectNavItem("history")
            Toast.makeText(this, "Historial - Próximamente", Toast.LENGTH_SHORT).show()
        }
        navSettings.setOnClickListener {
            selectNavItem("settings")
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("USER_NAME", userName)
            intent.putExtra("SELECTED_EAR", selectedEar)
            startActivity(intent)
        }
    }

    private fun selectNavItem(item: String) {
        // Reset all nav items
        ivNavHome.setImageResource(R.drawable.ic_home)
        ivNavHome.clearColorFilter()
        ivNavHistory.setImageResource(R.drawable.ic_history)
        ivNavHistory.clearColorFilter()
        ivNavSettings.setImageResource(R.drawable.ic_settings)
        ivNavSettings.clearColorFilter()

        tvNavHome.setTextColor(getColor(R.color.nav_inactive))
        tvNavHistory.setTextColor(getColor(R.color.nav_inactive))
        tvNavSettings.setTextColor(getColor(R.color.nav_inactive))

        // Select active item
        when (item) {
            "home" -> {
                ivNavHome.setImageResource(R.drawable.ic_home)
                ivNavHome.setColorFilter(getColor(R.color.nav_active))
                tvNavHome.setTextColor(getColor(R.color.nav_active))
            }
            "history" -> {
                ivNavHistory.setImageResource(R.drawable.ic_history)
                ivNavHistory.setColorFilter(getColor(R.color.nav_active))
                tvNavHistory.setTextColor(getColor(R.color.nav_active))
            }
            "settings" -> {
                ivNavSettings.setImageResource(R.drawable.ic_settings)
                ivNavSettings.setColorFilter(getColor(R.color.nav_active))
                tvNavSettings.setTextColor(getColor(R.color.nav_active))
            }
        }
    }
}
