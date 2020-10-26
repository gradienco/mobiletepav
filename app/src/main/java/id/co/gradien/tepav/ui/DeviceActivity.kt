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
import id.co.gradien.tepav.adapter.DeviceListAdapter
import id.co.gradien.tepav.adapter.PacketListAdapter
import id.co.gradien.tepav.data.DeviceModel
import kotlinx.android.synthetic.main.activity_device.*

class DeviceActivity : AppCompatActivity() {
    val TAG = "DEVICE ACTIVITY"
    private var deviceList = mutableListOf<DeviceModel>()
    private lateinit var deviceAdapter: DeviceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val layoutManager = LinearLayoutManager(this)
        deviceAdapter = DeviceListAdapter(deviceList)
        recycleviewDevice.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recycleviewDevice.adapter = deviceAdapter
        recycleviewDevice.layoutManager = layoutManager

        val deviceData = FirebaseDatabase.getInstance().getReference("device")
        deviceData.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.let {
                    deviceList.clear()
                    for (data in p0.children){
                        val device = data.getValue(DeviceModel::class.java)
                        deviceList.add(device!!)
                    }
                    //Log.i(TAG, deviceList.toString())
                    deviceAdapter.setData(deviceList)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }

        })

        btnAddDevice.setOnClickListener {  }


    }


}