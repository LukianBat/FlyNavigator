package com.lukianbat.flynavigator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lukianbat.coreui.utils.startActivity
import com.lukianbat.flynavigator.presentation.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity<MainActivity>()
        finish()
    }
}
