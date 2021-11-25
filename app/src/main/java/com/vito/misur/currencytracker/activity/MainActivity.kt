package com.vito.misur.currencytracker.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.fragment.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val FIRST_CURRENCY_SELECTION = "FIRST_CURRENCY_SELECTION"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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