package id.co.gradien.tepav.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import id.co.gradien.tepav.data.PacketModel
import id.co.gradien.tepav.utils.Tools.parseTimeINSTANT
import kotlinx.android.synthetic.main.activity_status_packet.*

class StatusPacketActivity : AppCompatActivity() {

    val TAG = "STATUS PACKET ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_packet)
        btnCloseDetailPacketActivity.setOnClickListener { finish() }
        srDetailPacket.visibility = INVISIBLE

        getData()
        onSwipeRefresh()
    }

    private fun getData(){
        val packetId = intent.getStringExtra("id")
        val statusData = FirebaseDatabase.getInstance().getReference("packet").child(packetId!!)
        statusData.addValueEventListener(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                srDetailPacket.visibility = VISIBLE
                pbDetailPacket.visibility = GONE
                p0.let {
                    val packet = it.getValue(PacketModel::class.java)
                    Log.d(TAG, "Data Packet: $packet")
                    val receiveTime = parseTimeINSTANT(packet?.receiveTime)
                    val cleaningTime = parseTimeINSTANT(packet?.cleaningTime)
                    val sterilizedTime = parseTimeINSTANT(packet?.sterilizedTime)


                    Log.d(TAG, "Convert Time = Receive : $receiveTime, Cleaning : $cleaningTime, Sterilized : $sterilizedTime")

                    fun setReceivedUI() {
                        packet?.receiveTime.let {
                            textReceiveTime.text = "$receiveTime"
                            textReceiveTime.setTextColor(resources.getColor(R.color.colorBlack))
                            tvReceive.setTextColor(resources.getColor(R.color.colorBlack))
                            timelineReceived.setImageResource(R.drawable.ic_marker)
                            Log.d(TAG, "Receive Status")
                        }
                    }

                    fun setCleaningUI() {
                        packet?.cleaningTime.let {
                            textCleanTime.text = cleaningTime
                            textCleanTime.setTextColor(resources.getColor(R.color.colorBlack))
                            tvCleaning.setTextColor(resources.getColor(R.color.colorBlack))
                            timelineCleaning.setImageResource(R.drawable.ic_marker)
                            Log.d(TAG, "Cleaning Status")
                        }
                    }

                    fun setSterilizedUI() {
                        packet?.sterilizedTime.let {
                            textSterilizedTime.text = sterilizedTime
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
        srDetailPacket.setOnRefreshListener {
            getData()
            Handler().postDelayed({ srDetailPacket.isRefreshing = false }, 500)
        }
    }


}