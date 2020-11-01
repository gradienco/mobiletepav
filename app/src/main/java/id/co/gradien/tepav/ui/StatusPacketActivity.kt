package id.co.gradien.tepav.ui

import android.annotation.SuppressLint
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

        btnCloseDetailPacketActivity.setOnClickListener { finish() }

        val packetId = intent.getStringExtra("id")
        val statusData = FirebaseDatabase.getInstance().getReference("packet").child(packetId!!)
        statusData.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                p0.let {
                    val packet = it.getValue(PacketModel::class.java)

                    packet!!.status.let { that -> textStatusPacket.text = that}
                    if (packet.receiveTime!!.isNotEmpty()) {
                        packet.receiveTime.let { that ->
                            val date = that.subSequence(0,10).toString()
                            val time = that.substring(11, that.length - 3)
                            textReceiveTime.text = "$date, $time"
                            textReceiveTime.setTextColor(resources.getColor(R.color.colorBlack))
                            tvReceive.setTextColor(resources.getColor(R.color.colorBlack))
                            timelineReceived.marker = resources.getDrawable(R.drawable.ic_marker)
                            Log.d(TAG, "Receive Status")
                        }
                    }

                    if (packet.cleaningTime!!.isNotEmpty()) {
                        packet.cleaningTime.let { that ->
                            val date = that.subSequence(0,10).toString()
                            val time = that.substring(11, that.length - 3)
                            textCleanTime.text = "$date, $time"
                            timelineCleaning.marker = resources.getDrawable(R.drawable.ic_marker)
                            textCleanTime.setTextColor(resources.getColor(R.color.colorBlack))
                            tvCleaning.setTextColor(resources.getColor(R.color.colorBlack))
                            Log.d(TAG, "Cleaning Status")
                        }
                    }

                    if (packet.sterilizedTime!!.isNotEmpty()) {
                        packet.sterilizedTime.let { that ->
                            val date = that.subSequence(0,10).toString()
                            val time = that.substring(11, that.length - 3)
                            textSterilizedTime.text = "$date, $time"
                            textSterilizedTime.setTextColor(resources.getColor(R.color.colorBlack))
                            tvSterilized.setTextColor(resources.getColor(R.color.colorBlack))
                            timeLineSterilized.marker = resources.getDrawable(R.drawable.ic_marker)
                            Log.d(TAG, "Sterilized Status")
                        }
                    }


//                    if (packet.cleaningTime.isNullOrEmpty() && packet.sterilizedTime.isNullOrEmpty()) {
//                        packet.receiveTime.let { that ->
//                            val date = that?.subSequence(0,10).toString()
//                            val time = that?.substring(11, that.length - 3)
//                            textReceiveTime.text = "$date, $time"
//                            Log.d(TAG, "Receive Status")
//                        }
//                    } else if (packet.sterilizedTime.isNullOrEmpty()) {
//                        packet.receiveTime.let { that ->
//                            val date = that?.subSequence(0,10).toString()
//                            val time = that?.substring(11, that.length - 3)
//                            textReceiveTime.text = "$date, $time"
//                            Log.d(TAG, "Receive Status")
//                        }
//                        packet.cleaningTime.let { that ->
//                            val date = that?.subSequence(0,10).toString()
//                            val time = that?.substring(11, that.length - 3)
//                            textCleanTime.text = "$date, $time"
//                            Log.d(TAG, "Cleaning Status")
//                        }
//                    } else {
//                        packet.receiveTime.let { that ->
//                            val date = that?.subSequence(0,10).toString()
//                            val time = that?.substring(11, that.length - 3)
//                            textReceiveTime.text = "$date, $time"
//                            Log.d(TAG, "Receive Status")
//                        }
//                        packet.cleaningTime.let { that ->
//                            val date = that?.subSequence(0,10).toString()
//                            val time = that?.substring(11, that.length - 3)
//                            textCleanTime.text = "$date, $time"
//                            Log.d(TAG, "Cleaning Status")
//                        }
//                        packet.sterilizedTime.let { that ->
//                            val date = that?.subSequence(0,10).toString()
//                            val time = that?.substring(11, that.length - 3)
//                            textSterilizedTime.text = "$date, $time"
//                            Log.d(TAG, "Sterilized Status")
//                        }
//                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }

        })
    }
}