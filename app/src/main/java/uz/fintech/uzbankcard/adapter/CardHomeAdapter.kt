package uz.fintech.uzbankcard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeOnClick

class CardHomeAdapter(ctx:Context,val list:MutableList<CardModel>,val iHomeOnClick: IHomeOnClick):RecyclerView.Adapter<CardHomeAdapter.VH>(){

    private val infaleter= LayoutInflater.from(ctx)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
      val view=infaleter.inflate(R.layout.card_item,parent,false)
      return VH(view,iHomeOnClick)
    }

    override fun getItemCount()=list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val id=list[position]
        holder.onBind(id)

    }

    fun itemList(cardModel: CardModel) {
        list.clear()
        list.add(cardModel)
        notifyItemInserted(list.size)
    }



    class VH(override val containerView: View, iHomeOnClick: IHomeOnClick):
            RecyclerView.ViewHolder(containerView),
            LayoutContainer{

        private var cardModel:CardModel?=null

        init {
               containerView.setOnClickListener{
                   cardModel?.let { it1 -> iHomeOnClick.onClickListener(it1) }
               }
        }

        fun onBind(id: CardModel) {
            cardModel=id
        }
    }
}