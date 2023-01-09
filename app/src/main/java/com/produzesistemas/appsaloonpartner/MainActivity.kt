package com.produzesistemas.appsaloonpartner

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.produzesistemas.appsaloonpartner.database.DataSourceUser
//import com.produzesistemas.appsaloonpartner.databinding.ActivityMainBinding
import com.produzesistemas.appsaloonpartner.viewmodel.ViewModelMain
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelMain: ViewModelMain
    private var datasource: DataSourceUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            val mTitle: TextView = toolbar.findViewById(R.id.toolbar_title);
            setSupportActionBar(toolbar)
            mTitle.text = toolbar.title
            supportActionBar?.setDisplayShowTitleEnabled(false)
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home,R.id.nav_my_account), drawerLayout)
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


            navView.menu.findItem(R.id.navigation_logout).setOnMenuItemClickListener {
                signOut()
                true
            }

            navView.menu.findItem(R.id.navigation_sair_do_app).setOnMenuItemClickListener {
                onBackPressed()
                true
            }

            datasource = DataSourceUser(this)
            var token = datasource?.get()!!
            if (token.token == "") {
                changeActivity()
                finish()
            }

            viewModelMain = ViewModelProvider(this).get(ViewModelMain::class.java)
            viewModelMain.title.observe(this, Observer {
                mTitle.text = it
            })

        } catch (e: SecurityException) {
            e.message?.let { Log.e("Exception: %s", it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun changeActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun signOut() {
        datasource?.deleteAll()
        onBackPressed()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}