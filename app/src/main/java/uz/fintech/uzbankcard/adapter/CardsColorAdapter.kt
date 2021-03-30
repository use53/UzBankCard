package uz.fintech.uzbankcard.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.ListAdapter
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_color_item.view.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.inflate
import uz.fintech.uzbankcard.model.CardColorModel
import uz.fintech.uzbankcard.navui.onclikc.ICardColorOnClick

class CardsColorAdapter(val iCardColorOnClick: ICardColorOnClick):ListAdapter<CardColorModel,CardsColorAdapter.CardVH>(ColorCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardVH {
        val view =parent.inflate(LayoutInflater.from(parent.context), R.layout.card_color_item)
      return CardVH(view,iCardColorOnClick)
    }

    override fun onBindViewHolder(holder: CardVH, position: Int) {
        val pos=getItem(position)
        holder.onBind(pos)
    }

    class CardVH(view: View,iCardColorOnClick: ICardColorOnClick)
        :RecyclerView.ViewHolder(view){
             private var cardColorModel:CardColorModel?=null
        init {
            itemView.setOnClickListener {
                cardColorModel?.let { it1 -> iCardColorOnClick.colorOnClickListener(it1) }   
            }
        }
        
        fun onBind(pos: CardColorModel) {
            cardColorModel=pos
          itemView.image_color.setBackgroundColor(pos.colors)
           itemView.tv_color_title.text=pos.colortitle
        }
    }

}

class ColorCallback:DiffUtil.ItemCallback<CardColorModel>(){
    override fun areItemsTheSame(oldItem: CardColorModel, newItem: CardColorModel): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: CardColorModel, newItem: CardColorModel): Boolean {
        return oldItem.colorid==newItem.colorid
    }

}