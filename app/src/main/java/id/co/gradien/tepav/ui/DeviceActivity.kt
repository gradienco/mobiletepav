 package id.co.gradien.tepav.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import id.co.gradien.tepav.utils.Tools
import kotlinx.android.synthetic.main.activity_device.*
import me.dm7.barcodescanner.zxing.ZXingScannerView


 class DeviceActivity : AppCompatActivity() {
    val TAG = "DEVICE ACTIVITY"
    private var deviceList = mutableListOf<DeviceModel>()
    private lateinit var deviceAdapter: DeviceAdapter

     companion object {
         const val ADD_DEVICE = "add_device"
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        btnCloseDevicesActivity.setOnClickListener { finish() }

        val failedScan = intent.getStringExtra(ADD_DEVICE)
        Log.d(TAG, "SCAN RESULT : $failedScan")
        when(failedScan) {
            "success" -> Tools.showPopUpDialog(this@DeviceActivity, "Berhasil", "Device berhasil didaftarkan")
            "failed" -> Tools.showPopUpDialogWaring(this@DeviceActivity, "Gagal", "Device telah didaftarkan")
        }
//        if(failedScan == "failed") {
//            Tools.showPopUpDialogWaring(this@DeviceActivity, "Gagal", "Device telah didaftarkan")
//        }
//
//        val successScan = intent.getStringExtra("Success_scan")
//        if (successScan == "success") {
//            Tools.showPopUpDialog(this@DeviceActivity, "Berhasil", "Device berhasil didaftarkan")
//        }

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
                        Log.d(TAG, "Device : $deviceList")
                    }
                    //Log.i(TAG, deviceList.toString())
                    deviceAdapter.setData(deviceList)
                    if (deviceList.isNullOrEmpty()) {
                        tvEmptyDevice.visibility = VISIBLE
                        recycleviewDevice.visibility = GONE
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }

        })

        btnAddDevice.setOnClickListener {
            finish()
            startActivity(Intent(this@DeviceActivity, ScanDeviceActivity::class.java))
        }

    }

}