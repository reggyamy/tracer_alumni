package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.core.utils.SurveyPreferences
import com.reggya.traceralumni.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener,
    NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var backPressedTime : Long = 0

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contentMain.fab.setOnClickListener {
            startActivity(Intent(this, PostActivity::class.java))
        }

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_jobs, R.id.navigation_bookmark, R.id.navigation_profile), binding.drawerLayout)
        navController.addOnDestinationChangedListener(this)

        setSupportActionBar(binding.appBar.toolbar)
        val navView: BottomNavigationView = binding.contentMain.navView
        navView.background = null
        navView.setupWithNavController(navController)

        binding.drawerNavView.setupWithNavController(navController)
        binding.drawerNavView.setNavigationItemSelectedListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.btLogout.setOnClickListener {
            val snackBar = Snackbar.make(it, "Apa kamu yakin ingin keluar dari aplikasi ini?", Snackbar.LENGTH_LONG)
            snackBar.setAction("OK"){
                LoginPreference(this).isLogin(false)
                SurveyPreferences(this).isCompleted(false)
                setExit()
                finish()
            }
            snackBar.show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_password -> Toast.makeText(this, "On development", Toast.LENGTH_SHORT).show()
            R.id.item_translate -> Toast.makeText(this, "On development", Toast.LENGTH_SHORT).show()
            R.id.item_posting -> Toast.makeText(this, "On development", Toast.LENGTH_SHORT).show()
            R.id.item_privacy -> Toast.makeText(this, "On development", Toast.LENGTH_SHORT).show()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when(destination.id){
            R.id.navigation_home -> supportActionBar?.hide()
            R.id.navigation_jobs-> supportActionBar?.hide()
            R.id.navigation_bookmark -> supportActionBar?.hide()
            R.id.navigation_profile-> {
                supportActionBar?.show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()

    }

    private fun setExit() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}