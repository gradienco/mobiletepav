package id.co.gradien.tepav.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.*
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
import id.co.gradien.tepav.R
import id.co.gradien.tepav.adapter.PacketAdapter
import id.co.gradien.tepav.data.PacketModel
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val TAG = "HOME ACTIVITY"
    private var deviceId:String? = null
    private var packetList = mutableListOf<PacketModel>()
    private lateinit var packetAdapter: PacketAdapter

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftMillis: Long = 120000
    private var timerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        contentHome.visibility = INVISIBLE

        intent.getStringExtra("deviceId")?.let {
            deviceId = it
        }
        if (deviceId == null){
            startActivity(Intent(this@HomeActivity, DeviceActivity::class.java))
        } else {
            val database = FirebaseDatabase.getInstance().reference
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val device = database.child("device").child(deviceId!!)
            device.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    pbHome.visibility = GONE
                    contentHome.visibility = VISIBLE
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    //val value = dataSnapshot.value.toString()
                    //Log.d(TAG, "Value is: $value")

                    textNameDevice.text = dataSnapshot.child("name").value.toString()
                    val temperature = "${dataSnapshot.child("sensor").child("temperature").value.toString()}°"
                    textTemperature.text = temperature
                    textHumidity.text = dataSnapshot.child("sensor").child("humidity").value.toString()
                    textUV.text = dataSnapshot.child("sensor").child("uvIndex").value.toString()
                    val automaticMode = dataSnapshot.child("auto").value.toString()
                    val frontDoorLock = dataSnapshot.child("action").child("frontDoor").value.toString() == "1"
                    val backDoorLock = dataSnapshot.child("action").child("backDoor").value.toString() == "1"
                    Log.i(TAG, "Value Mode Automatic: $automaticMode")

                    // Sterilize
                    if(automaticMode == "0") {
                        tvManual.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorWhite))
                        layoutManual.setBackgroundResource(R.color.colorGreenLight)
                        layoutOtomatis.setBackgroundResource(R.color.colorWhite)
                        tvOtomatis.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorBlack))
                        btnSterilize.visibility = View.VISIBLE

                    } else {
                        tvOtomatis.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorWhite))
                        layoutOtomatis.setBackgroundResource(R.color.colorGreenLight)
                        layoutManual.setBackgroundResource(R.color.colorWhite)
                        tvManual.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorBlack))
                        btnSterilize.visibility = View.GONE
                    }

                    // BackDoor Lock & Unlock
                    if(backDoorLock) {
                        tvMessageBackDoor.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorRed))
                        tvMessageBackDoor.text = "Tertutup"
                        layoutLockBackDoor.setBackgroundResource(R.color.colorRed)
                        layoutUnlockBackDoor.setBackgroundResource(R.color.colorWhite)
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(ivLockBackDoor.drawable),
                                ContextCompat.getColor(this@HomeActivity, R.color.colorWhite)
                        )
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(ivUnlockBackDoor.drawable),
                                ContextCompat.getColor(this@HomeActivity, R.color.colorSoftGreenLight)
                        )
                    } else {
                        tvMessageBackDoor.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorGreenLight))
                        tvMessageBackDoor.text = "Terbuka"
                        layoutUnlockBackDoor.setBackgroundResource(R.color.colorGreenLight)
                        layoutLockBackDoor.setBackgroundResource(R.color.colorWhite)
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(ivUnlockBackDoor.drawable),
                                ContextCompat.getColor(this@HomeActivity, R.color.colorWhite)
                        )
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(ivLockBackDoor.drawable),
                                ContextCompat.getColor(this@HomeActivity, R.color.colorSoftRed)
                        )
                    }

                    // FrontDoor Lock & Unlock
                    if(frontDoorLock) {
                        tvMessageFrontDoor.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorRed))
                        tvMessageFrontDoor.text = "Tertutup"
                        layoutLockFrontDoor.setBackgroundResource(R.color.colorRed)
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(ivLockFrontDoor.drawable),
                                ContextCompat.getColor(this@HomeActivity, R.color.colorWhite2)
                        )
                        layoutUnlockFrontDoor.setBackgroundResource(R.color.colorWhite)
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(ivUnlockFrontDoor.drawable),
                                ContextCompat.getColor(this@HomeActivity, R.color.colorSoftGreenLight2)
                        )
                    } else {
                        tvMessageFrontDoor.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorGreenLight))
                        tvMessageFrontDoor.text = "Terbuka"
                        layoutUnlockFrontDoor.setBackgroundResource(R.color.colorGreenLight)
                        layoutLockFrontDoor.setBackgroundResource(R.color.colorWhite)
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(ivUnlockFrontDoor.drawable),
                                ContextCompat.getColor(this@HomeActivity, R.color.colorWhite)
                        )
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(ivLockFrontDoor.drawable),
                                ContextCompat.getColor(this@HomeActivity, R.color.colorSoftRed)
                        )
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

            btnOtomatis.setOnClickListener {
                device.child("auto").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        device.child("auto").setValue(1)
                    }
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e(TAG, "Error fetch data form database")
                    }
                })
            }

            btnManual.setOnClickListener {
                device.child("auto").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        device.child("auto").setValue(0)
                    }
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e(TAG, "Error fetch data form database")
                    }
                })
            }

            btnLockBackDoor.setOnClickListener {
                device.child("action").child("backDoor").setValue(1)
                Log.d(TAG, "Lock Tepav")
                tvMessageBackDoor.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivLockBackDoor.drawable),
                        ContextCompat.getColor(this, R.color.colorRed)
                )
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivUnlockBackDoor.drawable),
                        ContextCompat.getColor(this, R.color.colorGrey)
                )
            }
            btnUnlockBackDoor.setOnClickListener {
                device.child("action").child("backDoor").setValue(0)
                Log.d(TAG, "Unlock Tepav")
                tvMessageBackDoor.setTextColor(ContextCompat.getColor(this, R.color.colorGreenLight))
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivUnlockBackDoor.drawable),
                        ContextCompat.getColor(this, R.color.colorGreenLight)
                )
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivLockBackDoor.drawable),
                        ContextCompat.getColor(this, R.color.colorGrey)
                )
            }

            btnLockFrontDoor.setOnClickListener {
                device.child("action").child("frontDoor").setValue(1)
                Log.d(TAG, "Lock Tepav")
                tvMessageFrontDoor.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivLockFrontDoor.drawable),
                        ContextCompat.getColor(this, R.color.colorRed)
                )
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivUnlockFrontDoor.drawable),
                        ContextCompat.getColor(this, R.color.colorGrey)
                )
            }
            btnUnlockFrontDoor.setOnClickListener {
                device.child("action").child("frontDoor").setValue(0)
                Log.d(TAG, "Unlock Tepav")
                tvMessageFrontDoor.setTextColor(ContextCompat.getColor(this, R.color.colorGreenLight))
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivUnlockFrontDoor.drawable),
                        ContextCompat.getColor(this, R.color.colorGreenLight)
                )
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivLockFrontDoor.drawable),
                        ContextCompat.getColor(this, R.color.colorGrey)
                )
            }

            btnSterilize.setOnClickListener {
                device.child("action").child("manualSteril").setValue(1)
                Toast.makeText(this@HomeActivity, "Proses sterilisasi dimulai", Toast.LENGTH_LONG).show()
                device.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val sterilizeDuration = dataSnapshot.child("duration").value.toString()
                        timeLeftMillis = sterilizeDuration.toLong() * 60000
                        startTimer()
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Log.e(TAG, "Error fetch data form database")
                    }

                })

            }
            btnDevices.setOnClickListener {
                startActivity(Intent(this@HomeActivity, DeviceActivity::class.java))
            }
            btnAllPacket.setOnClickListener {
                startActivity(Intent(this@HomeActivity, PacketActivity::class.java))
            }
            btnSetting.setOnClickListener {
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java).putExtra("deviceId", deviceId))
            }

            val layoutManager = LinearLayoutManager(this)
            packetAdapter = PacketAdapter(packetList)
            recycleviewPacket.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
            recycleviewPacket.adapter = packetAdapter
            recycleviewPacket.layoutManager = layoutManager
            val packetData = FirebaseDatabase.getInstance().getReference("packet")
            val myPacket = packetData.orderByChild("user").equalTo(userId).limitToLast(2)
            myPacket.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    p0.let {
                        packetList.clear()
                        for (data in p0.children){
                            val packet = data.getValue(PacketModel::class.java)
                            packet!!.id = data.key.toString()
                            packetList.add(packet)
                        }
                        when {
                            packetList.size >= 2 -> {
                                val packetListLimit =  packetList.subList(0,2)
                                packetAdapter.setData(packetListLimit)
                            }
                            packetList.size == 1 -> {
                                val packetListLimit =  packetList.subList(0,1)
                                packetAdapter.setData(packetListLimit)
                            }
                            else -> {
                                tvEmptyPack.visibility = VISIBLE
                                recycleviewPacket.visibility = GONE
                            }
                        }
                        packetList.reverse()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e(TAG, "Error fetch data form database")
                }

            })


            device.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild("timerLeft")){
                        val timerSterilize = dataSnapshot.child("timerLeft").value.toString()
                        Log.d(TAG, "Time LEFT : $timerSterilize")
                        if (timerSterilize != "0") {
                            // val minutes = timeLeftMillis.toInt() % 60000 / 1000
                            // timeLeftMillis = timerSterilize.toLong() * 60000
                            val leftSecondDuration : Int = timerSterilize.toInt()
                            timeLeftMillis = leftSecondDuration.toLong() * 1000
                            Log.d("TIMER ", "$timeLeftMillis")
                            startTimer()
                        }
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                    Log.e(TAG, "Error fetch data form database")
                }
            })
        }

    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMillis = millisUntilFinished
                val minutes = timeLeftMillis.toInt() / 60000
                val second = timeLeftMillis.toInt() % 60000 / 1000
                btnSterilize.isEnabled = false
                updateTimer(minutes, second)
            }

            override fun onFinish() {
                tvScan.text = "Scan"
                btnSterilize.isEnabled = true
            }
        }.start()
        timerRunning = true
    }

    private fun updateTimer(minutes : Int, second : Int) {
        val secondLeft = timeLeftMillis.toInt() / 1000
        var timeLeftText: String = "$minutes:"
        if (second < 10) {
            timeLeftText += "0"
        }
        timeLeftText += second
        tvScan.text = timeLeftText

        val database = FirebaseDatabase.getInstance().reference
        val device = database.child("device").child(deviceId!!)
        device.child("timerLeft").setValue(secondLeft)
    }
}