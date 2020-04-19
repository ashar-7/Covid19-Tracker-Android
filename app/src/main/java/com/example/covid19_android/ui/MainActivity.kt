package com.example.covid19_android.ui

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.covid19_android.R
import com.mapbox.mapboxsdk.Mapbox
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        setContentView(R.layout.activity_main)

        val navController = nav_host.findNavController()

        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(bottom_navigation, navController)

        // Switch to night mode if system night mode is ON despite of the app's settings
        val isNightModeOn =
            when(resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> true

                else -> getPreferences(Context.MODE_PRIVATE)
                    .getBoolean(getString(R.string.night_mode_prefs_key), false)
            }

        delegate.localNightMode = when(isNightModeOn) {
            true -> AppCompatDelegate.MODE_NIGHT_YES
            false -> AppCompatDelegate.MODE_NIGHT_NO
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
