package id.co.gradien.tepav.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import id.co.gradien.tepav.adapter.PacketAdapter
import id.co.gradien.tepav.data.DeviceModel
import id.co.gradien.tepav.data.PacketModel
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val TAG = "HOME ACTIVITY"
    private var deviceId = "device001"
    private var packetList = mutableListOf<PacketModel>()
    private lateinit var packetAdapter: PacketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        intent.getStringExtra("deviceId")?.let {
            deviceId = it
        }
        val database = FirebaseDatabase.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val device = database.child("device").child(deviceId)
        device.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //val value = dataSnapshot.value.toString()
                //Log.d(TAG, "Value is: $value")

                val temperature = "${dataSnapshot.child("sensor").child("temperature").value.toString()}°"
                textTemperature.text = temperature
                textHumidity.text = dataSnapshot.child("sensor").child("humidity").value.toString()
                textUV.text = dataSnapshot.child("sensor").child("uvIndex").value.toString()
                var automaticMode = dataSnapshot.child("mode").value.toString()
                Log.i(TAG, "Value Mode Automatic: $automaticMode")
                if(automaticMode == "0") {
                    textMode.isChecked = false
                    btnSterilize.visibility = View.VISIBLE
                    textMode.setOnClickListener {
                        device.child("mode").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                device.child("mode").setValue(1)
                            }
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e(TAG, "Error fetch data form database")
                            }
                        })
                    }
                } else {
                    textMode.isChecked = true
                    btnSterilize.visibility = View.GONE
                    textMode.setOnClickListener {
                        device.child("mode").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                device.child("mode").setValue(0)

                            }
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e(TAG, "Error fetch data form database")
                            }
                        })
                    }
                }

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
            Toast.makeText(this@HomeActivity, "Proses sterilisasi dimulai", Toast.LENGTH_LONG).show()
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

        //recycleviewPacket.adapter = PacketListAdapter()
        packetAdapter = PacketAdapter(packetList)
        recycleviewPacket.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recycleviewPacket.adapter = packetAdapter
        recycleviewPacket.layoutManager = layoutManager
        val packetData = FirebaseDatabase.getInstance().getReference("packet")
        //FirebaseMessaging.getInstance().subscribeToTopic("channelMain")
        //val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val myPacket = packetData.orderByChild("user").equalTo(userId)
        myPacket.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.let {
                    packetList.clear()
                    for (data in p0.children){
                        val packet = data.getValue(PacketModel::class.java)
                        packet!!.id = data.key.toString()
                        packetList.add(packet!!)
                    }
                    packetAdapter.setData(packetList)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }

        })
    }
}