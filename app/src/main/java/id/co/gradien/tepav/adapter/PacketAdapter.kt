package id.co.gradien.tepav.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import id.co.gradien.tepav.R
import id.co.gradien.tepav.data.PacketModel
import id.co.gradien.tepav.ui.HomeActivity
import id.co.gradien.tepav.ui.StatusPacketActivity
import id.co.gradien.tepav.utils.Tools.parseTimeINSTANT
import kotlinx.android.synthetic.main.item_packet.view.*
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class PacketAdapter(list: List<PacketModel>) : RecyclerView.Adapter<PacketAdapter.PacketVH>() {

    private lateinit var context: Context
    private var packetList: MutableList<PacketModel> = list as MutableList<PacketModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacketVH {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return PacketVH(inflater, parent)
    }

    override fun getItemCount(): Int = packetList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PacketVH, position: Int) {
        val packet = packetList[position]
        val view = holder.itemView

        val receiveTime = parseTimeINSTANT(packet.receiveTime)
        Log.d("ADAPTER", "Parse Date : ${packet.receiveTime}")
        Log.d("ADAPTER", "Convert Date : $receiveTime")


        val date = receiveTime?.subSequence(0,10).toString()
        val time = receiveTime?.substring(12, receiveTime.length)

        view.textPacketDate.text = "Tanggal : $date"
        view.textPacketTime.text = "Waktu : $time"

        view.textPacketStatus.text = packet.status

        view.layoutPacket.setOnClickListener {
            context.startActivity(
                    Intent(context, StatusPacketActivity::class.java)
                    .putExtra("id", packet.id))
        }
    }

    internal fun setData(list: MutableList<PacketModel>){
        packetList = list
        notifyDataSetChanged()
    }

    class PacketVH(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_packet, parent, false))
}
