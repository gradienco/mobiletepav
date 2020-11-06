package id.co.gradien.tepav.ui

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.co.gradien.tepav.R
import kotlinx.android.synthetic.main.activity_bluetooth.*


class BluetoothActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1
    private val TAG = "BLUETOOTH_ACTIVITY"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            finish()
        }

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        this.registerReceiver(mReceiver, filter)

        pairedDevices = bluetoothAdapter.bondedDevices
        val listDevice: ArrayList<BluetoothDevice> = ArrayList()
        val listDeviceName: ArrayList<String> = ArrayList()
        if (pairedDevices.isNotEmpty()) {
            for (device: BluetoothDevice in pairedDevices) {
                listDevice.add(device)
                listDeviceName.add(device.name)
                //Log.i(TAG, ""+device.name)
            }
        } else {
            Toast.makeText(this, "No paired device found!", Toast.LENGTH_SHORT).show()
        }

        listviewBT.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listDeviceName
        )
        listviewBT.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = listDevice[position]
            val address: String = device.address
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("address", address)
            startActivity(intent)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (bluetoothAdapter.isEnabled) {
                    Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth disabled", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling has been canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val mReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when {
                BluetoothDevice.ACTION_FOUND == action -> {//Device found
                }
                BluetoothDevice.ACTION_ACL_CONNECTED == action -> {
                    Toast.makeText(
                        this@BluetoothActivity,
                        "Device is now connected",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action -> {//Done searching
                }
                BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED == action -> {//Device is about to disconnect
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED == action -> {//Device has disconnected
                }
            }
        }
    }
}