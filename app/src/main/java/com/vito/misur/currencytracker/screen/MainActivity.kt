package com.vito.misur.currencytracker.screen

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.screen.home.HomeFragmentDirections
import org.koin.android.ext.android.inject

const val FIRST_CURRENCY_SELECTION = "FIRST_CURRENCY_SELECTION"

class MainActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** checks custom first start event
         * handles the start of users journey
         * does not let used proceed without selected main currency
         **/
        sharedPreferences.apply {
            if (getBoolean(FIRST_CURRENCY_SELECTION, true)) {
                (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController.navigate(
                    HomeFragmentDirections.toWelcome()
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}