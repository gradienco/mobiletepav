package id.co.gradien.tepav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import id.co.gradien.tepav.ui.HomeActivity

/*
 * Copyright 2020 Gradien.co.
 *
 * ...
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        direct(HomeActivity::class.java)
    }

    private fun direct(cls: Class<*>) {
        val intent = Intent(this@MainActivity, cls)
        val handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 3000)
    }
}