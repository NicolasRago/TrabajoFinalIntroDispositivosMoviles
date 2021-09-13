package com.example.myapplication.usecases.home

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        val host = (supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment?)!!
        this.navController = host.navController

        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        /* Le seteo el nav controller al toolbar para que muestre el label del fragment seleccionado */
        /* El appBarConfiguration define todos los fragments como "top level" para evitar que la toolbar dibuje flechitas de retroceso */
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.cowFragment, R.id.herdFragment)).build()
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }
}