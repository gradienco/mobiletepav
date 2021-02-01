package id.co.gradien.tepav.ui

import android.Manifest
import android.bluetooth.BluetoothClass
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import kotlinx.android.synthetic.main.activity_scan_device.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.Result
import id.co.gradien.tepav.data.DeviceModel
import id.co.gradien.tepav.utils.Tools

class ScanDeviceActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private lateinit var scannerView: ZXingScannerView
    val TAG = "SCAN DEVICE ACTIVITY"
    val deviceData = FirebaseDatabase.getInstance().getReference("device")
    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_device)
        tbAddDevices.setOnClickListener { finish() }
        setScannerView()
    }

    private fun setScannerView() {
        scannerView = ZXingScannerView(this@ScanDeviceActivity)
        scannerView.setAutoFocus(true)
        scannerView.setResultHandler(this)
        fl_barcode.addView(scannerView)
    }

    override fun onStart() {
        initPermission()
        scannerView.startCamera()
        super.onStart()
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
                setScannerView()
                scannerView.startCamera()
            }
            else -> {

            }
        }
    }

    override fun onPause() {
        scannerView.stopCamera()
        super.onPause()
    }

    override fun handleResult(rawResult: Result?) {
        scannerView.resumeCameraPreview(this)
        if (rawResult != null) {
            onPause()
        }
        val qrResult = rawResult?.text
        //Log.d("MODEL ", "qrCode ${qrResult!!}")

        deviceData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "Child : ${snapshot.hasChild(qrResult!!)}")
                if (snapshot.hasChild(qrResult)) {
//                  [option A] block if device already registered
//                    Log.i(TAG, "Device registered already!")
//                    Log.d(TAG, "Child True = ${snapshot.hasChild(qrResult)}")
//                    finish()
//                    startActivity(Intent(this@ScanDeviceActivity, DeviceActivity::class.java).putExtra(DeviceActivity.ADD_DEVICE, "failed"))

//                  [option B] update paired device with this one
                    deviceData.child(qrResult).child("user").setValue(userId)
                    Log.d(TAG, "Child True = ${snapshot.hasChild(qrResult)}")
                    finish()
                    startActivity(Intent(this@ScanDeviceActivity, DeviceActivity::class.java).putExtra(DeviceActivity.ADD_DEVICE, "updated"))
                } else {
                    val newDevice = DeviceModel(mac = qrResult, name = "Tepav Device", user = userId, duration = 30)
                    deviceData.child(qrResult).setValue(newDevice)
                    Log.d(TAG, "Child False = ${snapshot.hasChild(qrResult)}")
                    finish()
                    startActivity(Intent(this@ScanDeviceActivity, DeviceActivity::class.java)
                        .putExtra(DeviceActivity.ADD_DEVICE, "success").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Error fetch data form database")
            }
        })
    }

}