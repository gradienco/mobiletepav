package id.co.gradien.tepav.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val TAG = "HOME ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val database = FirebaseDatabase.getInstance().reference
        val deviceId = "device001"
        val device = database.child("device").child(deviceId)

        device.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //val value = dataSnapshot.value.toString()
                //Log.d(TAG, "Value is: $value")

                textTemperature.text = dataSnapshot.child("sensor").child("temperature").value.toString()
                textHumidity.text = dataSnapshot.child("sensor").child("humidity").value.toString()
                textUV.text = dataSnapshot.child("sensor").child("uv_index").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        btnLock.setOnClickListener {
            device.child("action").child("back_door").setValue(1)
        }
        btnUnlock.setOnClickListener {
            device.child("action").child("back_door").setValue(0)
        }
        btnDevices.setOnClickListener {
            startActivity(Intent(this@HomeActivity, DeviceActivity::class.java))
        }
        btnAllPacket.setOnClickListener {
            startActivity(Intent(this@HomeActivity, PacketActivity::class.java))
        }

        val layoutManager = LinearLayoutManager(this)
        recycleviewPacket.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        //recycleviewPacket.adapter = PacketListAdapter()
        recycleviewPacket.layoutManager = layoutManager


    }
}