package id.co.gradien.tepav.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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

        getData()
        onSwipeRefresh()
    }

    private fun getData(){
        val packetId = intent.getStringExtra("id")
        val statusData = FirebaseDatabase.getInstance().getReference("packet").child(packetId!!)
        statusData.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                p0.let {
                    val packet = it.getValue(PacketModel::class.java)
                    Log.d(TAG, "Data Packet: $packet")

                    fun setReceivedUI(){
                        packet?.receiveTime.let { that ->
                            val date = that?.subSequence(0,10).toString()
                            val time = that?.substring(11, that.length - 3)
                            textReceiveTime.text = "$date, $time"
                            textReceiveTime.setTextColor(resources.getColor(R.color.colorBlack))
                            tvReceive.setTextColor(resources.getColor(R.color.colorBlack))
                            timelineReceived.setImageResource(R.drawable.ic_marker)
                            Log.d(TAG, "Receive Status")
                        }
                    }

                    fun setCleaningUI(){
                        packet?.cleaningTime.let { that ->
                            val date = that?.subSequence(0,10).toString()
                            val time = that?.substring(11, that.length - 3)
                            textCleanTime.text = "$date, $time"
                            textCleanTime.setTextColor(resources.getColor(R.color.colorBlack))
                            tvCleaning.setTextColor(resources.getColor(R.color.colorBlack))
                            timelineCleaning.setImageResource(R.drawable.ic_marker)
                            Log.d(TAG, "Cleaning Status")
                        }
                    }

                    fun setSterilizedUI(){
                        packet?.sterilizedTime.let { that ->
                            val date = that?.subSequence(0,10).toString()
                            val time = that?.substring(11, that.length - 3)
                            textSterilizedTime.text = "$date, $time"
                            textSterilizedTime.setTextColor(resources.getColor(R.color.colorBlack))
                            tvSterilized.setTextColor(resources.getColor(R.color.colorBlack))
                            timeLineSterilized.setImageResource(R.drawable.ic_marker)
                            Log.d(TAG, "Sterilized Status")
                        }
                    }

                    if (packet?.cleaningTime.isNullOrEmpty() && packet?.sterilizedTime.isNullOrEmpty()) {
                        setReceivedUI()
                    } else if (packet?.sterilizedTime.isNullOrEmpty()) {
                        setReceivedUI()
                        setCleaningUI()
                    } else {
                        setReceivedUI()
                        setCleaningUI()
                        setSterilizedUI()
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }

        })
    }

    private fun onSwipeRefresh(){
        srStatusPacket.setOnRefreshListener {
            getData()
            Handler().postDelayed({ srStatusPacket.isRefreshing = false }, 500)
        }
    }
}