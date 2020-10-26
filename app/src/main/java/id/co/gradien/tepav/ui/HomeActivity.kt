package id.co.gradien.tepav.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.gradien.tepav.R
import id.co.gradien.tepav.adapter.PacketListAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnLock.setOnClickListener {  }
        btnUnlock.setOnClickListener {  }
        btnDevices.setOnClickListener {  }
        btnAllPacket.setOnClickListener {  }

        val layoutManager = LinearLayoutManager(this)
        recycleviewPacket.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recycleviewPacket.adapter = PacketListAdapter()
        recycleviewPacket.layoutManager = layoutManager
    }
}