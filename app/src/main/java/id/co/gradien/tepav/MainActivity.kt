package id.co.gradien.tepav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
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

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        val handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 2500)
    }
}