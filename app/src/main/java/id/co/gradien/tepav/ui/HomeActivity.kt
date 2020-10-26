package id.co.gradien.tepav.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import id.co.gradien.tepav.R
import id.co.gradien.tepav.adapter.PacketListAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val database = FirebaseDatabase.getInstance().reference
        val deviceId = "device001"

        btnLock.setOnClickListener {
            database.child("device").child(deviceId).child("action").child("back_door").setValue(1)
        }
        btnUnlock.setOnClickListener {
            database.child("device").child(deviceId).child("action").child("back_door").setValue(0)
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