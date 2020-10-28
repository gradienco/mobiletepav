package id.co.gradien.tepav.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import id.co.gradien.tepav.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val TAG = "HOME ACTIVITY"
    private var deviceId = "device001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        intent.getStringExtra("deviceId")?.let {
            deviceId = it
        }
        val database = FirebaseDatabase.getInstance().reference
        val device = database.child("device").child(deviceId)

        device.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //val value = dataSnapshot.value.toString()
                //Log.d(TAG, "Value is: $value")

                textTemperature.text = dataSnapshot.child("sensor").child("temperature").value.toString()
                textHumidity.text = dataSnapshot.child("sensor").child("humidity").value.toString()
                textUV.text = dataSnapshot.child("sensor").child("uvIndex").value.toString()
                textMode.text = dataSnapshot.child("mode").value.toString()

                if(dataSnapshot.child("action").child("backDoor").value.toString() == "1") {
                    tvLockMessage.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorRed))
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(ivLock.drawable),
                        ContextCompat.getColor(this@HomeActivity, R.color.colorRed)
                    )
                } else {
                    tvLockMessage.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorGreenLight))
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(ivUnlock.drawable),
                        ContextCompat.getColor(this@HomeActivity, R.color.colorGreenLight)
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })



        btnLock.setOnClickListener {
            device.child("action").child("backDoor").setValue(1)
            Log.d(TAG, "Lock Tepav")
            tvLockMessage.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
            DrawableCompat.setTint(
                DrawableCompat.wrap(ivLock.drawable),
                ContextCompat.getColor(this, R.color.colorRed)
            )
            DrawableCompat.setTint(
                DrawableCompat.wrap(ivUnlock.drawable),
                ContextCompat.getColor(this, R.color.colorGrey)
            )
        }
        btnUnlock.setOnClickListener {
            device.child("action").child("backDoor").setValue(0)
            Log.d(TAG, "Unlock Tepav")
            tvLockMessage.setTextColor(ContextCompat.getColor(this, R.color.colorGreenLight))
            DrawableCompat.setTint(
                DrawableCompat.wrap(ivUnlock.drawable),
                ContextCompat.getColor(this, R.color.colorGreenLight)
            )
            DrawableCompat.setTint(
                DrawableCompat.wrap(ivLock.drawable),
                ContextCompat.getColor(this, R.color.colorGrey)
            )
        }
        btnSterilize.setOnClickListener {
            device.child("action").child("manualSteril").setValue(1)
        }
        btnDevices.setOnClickListener {
            startActivity(Intent(this@HomeActivity, DeviceActivity::class.java))
        }
        btnAllPacket.setOnClickListener {
            startActivity(Intent(this@HomeActivity, PacketActivity::class.java))
        }
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        }

        val layoutManager = LinearLayoutManager(this)
        recycleviewPacket.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        //recycleviewPacket.adapter = PacketListAdapter()
        recycleviewPacket.layoutManager = layoutManager

        FirebaseMessaging.getInstance().subscribeToTopic("channelMain")
    }
}