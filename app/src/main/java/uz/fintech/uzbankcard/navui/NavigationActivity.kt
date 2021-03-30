package uz.fintech.uzbankcard.navui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.navui.twoui.home.HomeViewModel
import uz.fintech.uzbankcard.ui.SplashActivity

class NavigationActivity : AppCompatActivity() {


private val homeViewModel:HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

    }


    override fun onStop() {
        super.onStop()
        val intent=Intent(this,SplashActivity::class.java)
        startActivity(intent)
    }
}