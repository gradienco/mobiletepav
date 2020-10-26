package id.co.gradien.tepav.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import id.co.gradien.tepav.adapter.PacketAdapter
import id.co.gradien.tepav.data.PacketModel
import kotlinx.android.synthetic.main.activity_packet.*

class PacketActivity : AppCompatActivity() {

    val TAG = "PACKET ACTIVITY"
    private var packetList = mutableListOf<PacketModel>()
    private lateinit var packetAdapter: PacketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packet)

        val layoutManager = LinearLayoutManager(this)
        packetAdapter = PacketAdapter(packetList)
        recycleviewStatus.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recycleviewStatus.adapter = packetAdapter
        recycleviewStatus.layoutManager = layoutManager

        val deviceData = FirebaseDatabase.getInstance().getReference("packet")
        deviceData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.let {
                    packetList.clear()
                    for (data in p0.children){
                        val packet = data.getValue(PacketModel::class.java)
                        packetList.add(packet!!)
                    }
                    Log.i(TAG, packetList.toString())
                    packetAdapter.setData(packetList)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }

        })
    }
}