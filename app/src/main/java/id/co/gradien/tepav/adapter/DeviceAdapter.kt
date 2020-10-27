package id.co.gradien.tepav.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.gradien.tepav.R
import id.co.gradien.tepav.data.DeviceModel
import id.co.gradien.tepav.ui.HomeActivity
import kotlinx.android.synthetic.main.item_device.view.*

class DeviceAdapter(list: List<DeviceModel>) : RecyclerView.Adapter<DeviceAdapter.DeviceVH>() {

    private lateinit var context: Context
    private var deviceList: MutableList<DeviceModel> = list as MutableList<DeviceModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceVH {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return DeviceVH(inflater, parent)
    }

    override fun onBindViewHolder(holder: DeviceVH, position: Int) {
        val device = deviceList[position]
        val view = holder.itemView

        view.textDeviceName.text = device.name
        view.layoutDevice.setOnClickListener {
            context.startActivity(
                Intent(context, HomeActivity::class.java)
                    .putExtra("deviceId", device.id)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            (context as Activity).finish()
        }
    }

    override fun getItemCount(): Int = deviceList.size

    internal fun setData(list: MutableList<DeviceModel>){
        deviceList = list
        notifyDataSetChanged()
    }

    class DeviceVH(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_device, parent, false))
}
