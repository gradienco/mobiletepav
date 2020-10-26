package id.co.gradien.tepav.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.gradien.tepav.R
import id.co.gradien.tepav.adapter.PacketListAdapter
import kotlinx.android.synthetic.main.activity_packet.*

class PacketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packet)

        val layoutManager = LinearLayoutManager(this)
        recycleviewStatus.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recycleviewStatus.adapter = PacketListAdapter()
        recycleviewStatus.layoutManager = layoutManager
    }
}