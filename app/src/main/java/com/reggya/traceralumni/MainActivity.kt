package com.reggya.traceralumni

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.databinding.ActivityMainBinding
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var preferences: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBar.toolbar2)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main2)

        navView.background = null
        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_jobs, R.id.navigation_bookmark, R.id.navigation_profile), binding.drawerLayout)

        navController.addOnDestinationChangedListener(this)

        binding.fab.setOnClickListener {
            supportFragmentManager.beginTransaction()
                startActivity(Intent(this, PostActivity::class.java))
        }

    }

    private fun setDrawerLayout() {
        supportActionBar?.title = this.getString(R.string.title_profile)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBar.toolbar2,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        binding.drawerNavView.setNavigationItemSelectedListener(this)

        preferences = LoginPreference(this)
        binding.btLogout.setOnClickListener {
            preferences.isLogin(false)
            exitProcess(0)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_password -> Toast.makeText(this, "nice", Toast.LENGTH_SHORT).show()
            R.id.item_translate -> {}
            R.id.item_posting -> {}
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
                setDrawerLayout()
            }
        }
    }
}