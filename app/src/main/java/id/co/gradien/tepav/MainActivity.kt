package id.co.gradien.tepav

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import id.co.gradien.tepav.ui.HomeActivity
import id.co.gradien.tepav.ui.LoginActivity

/*
 * Copyright 2020 Gradien.co.
 *
 * ...
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 21) {
//            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            window.statusBarColor = ContextCompat.getColor(this,R.color.colorAccent); //status bar or the time bar at the top
        }

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        val handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 2500)
    }
}