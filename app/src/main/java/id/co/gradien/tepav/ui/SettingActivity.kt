package id.co.gradien.tepav.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private val TAG = "SETTING ACTIVITY"
    private lateinit var deviceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        intent.getStringExtra("deviceId")?.let {
            deviceId = it
        }
        val database = FirebaseDatabase.getInstance().reference
        val device = database.child("device").child(deviceId)
        device.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                inputSSID.hint = dataSnapshot.child("wifi").child("ssid").value.toString()
                inputPassword.hint = dataSnapshot.child("wifi").child("password").value.toString()
                inputDuration.hint = dataSnapshot.child("duration").value.toString()
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, "Failed to read value.", p0.toException())
            }
        })

        btnConnectBT.setOnClickListener {
            startActivity(Intent(this@SettingActivity, BluetoothActivity::class.java))
        }

        btnSaveSetting.setOnClickListener {

        }
    }
}