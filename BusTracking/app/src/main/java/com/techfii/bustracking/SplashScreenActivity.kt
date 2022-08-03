package com.techfii.bustracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import com.techfii.bustracking.utilities.SavePreferences

class SplashScreenActivity : AppCompatActivity() {
    lateinit var preferences: SavePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        preferences = SavePreferences(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            if(preferences.getLogin()==false)
            { UserSelectionActivity.startIntent(this)
            finish()}else{
                goToActivity()
            }
        }, 2000)
    }
    fun goToActivity() {
        if (preferences.getAdmin() == true) {
            AdminActivity.startIntent(this)
        }
        if (preferences.getStudent() == true) {
            StudentActivity.startIntent(this)
        }
        if (preferences.getDriver() == true) {
            DriverTrackingActivity.startIntent(this)
        }
    }
}