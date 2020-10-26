package id.co.gradien.tepav.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.co.gradien.tepav.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            val username = inputUser.text.toString()
            val password = inputPassword.text.toString()
            //
        }
    }
}