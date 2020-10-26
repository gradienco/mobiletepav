package id.co.gradien.tepav.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import id.co.gradien.tepav.data.PacketModel
import kotlinx.android.synthetic.main.activity_status_packet.*

class StatusPacketActivity : AppCompatActivity() {

    val TAG = "STATUS PACKET ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_packet)

        val packetId = intent.getStringExtra("id")
        val statusData = FirebaseDatabase.getInstance().getReference("packet").child(packetId!!)
        statusData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.let {
                    val packet = it.getValue(PacketModel::class.java)
                    packet!!.status.let { that -> textStatusPacket.text = that}
                    packet.receiveTime.let { that -> textReceiveTime.text = that}
                    packet.cleaningTime.let { that -> textCleanTime.text = that}
                    packet.sterilizedTime.let { that -> textSterilizedTime.text = that}
                    //Log.i(TAG, packet.toString())
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }

        })
    }
}