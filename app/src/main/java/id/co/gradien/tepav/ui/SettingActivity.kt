package id.co.gradien.tepav.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
                textSSID.text = dataSnapshot.child("wifi").child("ssid").value.toString()
                textLastConn.text = dataSnapshot.child("wifi").child("lastConnect").value.toString()
                inputDuration.setText(dataSnapshot.child("duration").value.toString())
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, "Failed to read value.", p0.toException())
            }
        })

        btnSaveSetting.setOnClickListener {
            device.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    device.child("duration").setValue(inputDuration.text.toString())
                    Toast.makeText(this@SettingActivity, "Pengaturan Disimpan", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e(TAG, "Error fetch data form database")
                }
            })
        }
    }
}