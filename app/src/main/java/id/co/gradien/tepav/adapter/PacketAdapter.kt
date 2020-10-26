package id.co.gradien.tepav.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.gradien.tepav.R
import id.co.gradien.tepav.data.PacketModel
import kotlinx.android.synthetic.main.item_packet.view.*

class PacketAdapter(list: List<PacketModel>) : RecyclerView.Adapter<PacketAdapter.PacketVH>() {

    private lateinit var context: Context
    private var packetList: MutableList<PacketModel> = list as MutableList<PacketModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacketVH {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return PacketVH(inflater, parent)
    }

    override fun getItemCount(): Int = packetList.size

    override fun onBindViewHolder(holder: PacketVH, position: Int) {
        val packet = packetList[position]
        val view = holder.itemView

        view.textPacketDate.text = packet.receiveTime
        view.textPacketStatus.text = packet.status
    }

    internal fun setData(list: MutableList<PacketModel>){
        packetList = list
        notifyDataSetChanged()
    }

    class PacketVH(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_packet, parent, false))
}
