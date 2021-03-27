package uz.fintech.uzbankcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.map_layout.view.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.model.MapModel

class MapAdapter : ListAdapter<MapModel, MapAdapter.MapVH>(MapCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.map_layout, parent, false)
        return MapVH(view)
    }
    override fun onBindViewHolder(holder: MapVH, position: Int) {
        val item = getItem(position)
        holder.mapBind(item)
    }

    class MapVH(view: View) : RecyclerView.ViewHolder(view) {

        fun mapBind(mapModel: MapModel) {

            itemView.tv_map.text=mapModel.title
            Picasso.get()
                .load(mapModel.imagetext)
                .resize(50, 50)
                .centerCrop()
                .into(itemView.image_map)
        }
    }
}

class MapCallback : DiffUtil.ItemCallback<MapModel>() {
    override fun areItemsTheSame(oldItem: MapModel, newItem: MapModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MapModel, newItem: MapModel): Boolean {
        return oldItem.mapid == newItem.mapid
    }
}