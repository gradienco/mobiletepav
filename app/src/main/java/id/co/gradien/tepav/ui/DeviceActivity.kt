package id.co.gradien.tepav.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.gradien.tepav.R
import id.co.gradien.tepav.adapter.DeviceListAdapter
import id.co.gradien.tepav.adapter.PacketListAdapter
import kotlinx.android.synthetic.main.activity_device.*

class DeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        btnAddDevice.setOnClickListener {  }

        val layoutManager = LinearLayoutManager(this)
        recycleviewDevice.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recycleviewDevice.adapter = DeviceListAdapter()
        recycleviewDevice.layoutManager = layoutManager
    }


}