package id.co.gradien.tepav.ui

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import kotlinx.android.synthetic.main.activity_setting.*
import java.io.IOException
import java.util.*


class SettingActivity : AppCompatActivity() {

    companion object {
        val TAG = "SETTING ACTIVITY"
        lateinit var deviceId: String
        var mUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var bluetoothSocket: BluetoothSocket? = null
        lateinit var progress: ProgressDialog
        lateinit var bluetoothAdapter: BluetoothAdapter
        var isConnected: Boolean = false
        lateinit var address: String
        lateinit var statusConnect: TextView
    }

    private val REQUEST_ENABLE_BLUETOOTH = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        statusConnect = this.findViewById(R.id.textConnect)

        intent.getStringExtra("deviceId")?.let {
            deviceId = it
        }
        intent.getStringExtra("address")?.let {
            address = it
            ConnectToDevice(this).execute()
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        val database = FirebaseDatabase.getInstance().reference
        val device = database.child("device").child(deviceId)
        device.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                inputSSID.setText(dataSnapshot.child("wifi").child("ssid").value.toString())
                inputPassword.setText(dataSnapshot.child("wifi").child("password").value.toString())
                inputDuration.setText(dataSnapshot.child("duration").value.toString())
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, "Failed to read value.", p0.toException())
            }
        })

        btnConnectBT.setOnClickListener {
            startActivity(Intent(this@SettingActivity, BluetoothActivity::class.java))
            finish()
        }
        btnDisconnectBT.setOnClickListener {
            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket!!.close()
                    bluetoothSocket = null
                    isConnected = false
                    statusConnect.text = "disconnected"
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        btnSaveSetting.setOnClickListener {
            val ssid = inputSSID.text
            val password = inputPassword.text
            val duration = inputDuration.text
            sendCommand("$ssid:$password;")
        }
    }

    private fun sendCommand(input: String) {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket!!.outputStream.write(input.toByteArray())
                Toast.makeText(this, "Tersimpan", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Perangkat belum terhubung", Toast.LENGTH_SHORT).show()
        }
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context = c

        override fun onPreExecute() {
            super.onPreExecute()
            progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg params: Void?): String? {
            try {
                if (bluetoothSocket == null || !isConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(mUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i(TAG, "Cannot connect")
            } else {
                isConnected = true
                statusConnect.text = "connected"
            }
            progress.dismiss()
        }

    }
}