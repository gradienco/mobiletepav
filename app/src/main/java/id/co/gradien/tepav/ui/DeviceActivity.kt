 package id.co.gradien.tepav.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import id.co.gradien.tepav.adapter.DeviceAdapter
import id.co.gradien.tepav.data.DeviceModel
import kotlinx.android.synthetic.main.activity_device.*


class DeviceActivity : AppCompatActivity() {
    val TAG = "DEVICE ACTIVITY"
    private var deviceList = mutableListOf<DeviceModel>()
    private lateinit var deviceAdapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        btnCloseDevices.setOnClickListener { finish() }

        val layoutManager = LinearLayoutManager(this)
        deviceAdapter = DeviceAdapter(deviceList)
        recycleviewDevice.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recycleviewDevice.adapter = deviceAdapter
        recycleviewDevice.layoutManager = layoutManager

        val deviceData = FirebaseDatabase.getInstance().getReference("device")
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val myDevice = deviceData.orderByChild("user").equalTo(userId)
        myDevice.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.let {
                    deviceList.clear()
                    for (data in p0.children) {
                        val device = data.getValue(DeviceModel::class.java)
                        device!!.id = data.key.toString()
                        deviceList.add(device)
                    }
                    //Log.i(TAG, deviceList.toString())
                    deviceAdapter.setData(deviceList)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }

        })

        btnAddDevice.setOnClickListener {
            val macAddress = "30:AE:A4:07:0D:64" //This value should return form QR scanner

            deviceData.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(macAddress)) {
                        Log.i(TAG, "Device registered already!")
                    } else {
                        val newDevice = DeviceModel(mac = macAddress, name = "Tepav Device", user = userId)
                        deviceData.child(macAddress).setValue(newDevice)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e(TAG, "Error fetch data form database")
                }
            })
        }

    }


}