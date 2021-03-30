package uz.fintech.uzbankcard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_item.view.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.cardDateUtils
import uz.fintech.uzbankcard.common.cardNumber
import uz.fintech.uzbankcard.common.formatDecimals
import uz.fintech.uzbankcard.common.inflate
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeCardOnClick

class CardHomeAdapter(val iHomeCardOnClick: IHomeCardOnClick) : RecyclerView.Adapter<CardHomeAdapter.VH>() {

    val list = mutableListOf<CardModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =parent.inflate(LayoutInflater.from(parent.context),R.layout.card_item)
       // val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return VH(view,iHomeCardOnClick)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val id = list[position]
        holder.onBind(id)


    }

    fun itemList(cardModel: CardModel) {
  list.add(cardModel)
       notifyDataSetChanged()
    }

    fun submitList(cardList: MutableList<CardModel>) {
        list.clear()
        list.addAll(cardList)
        notifyDataSetChanged()
       // notifyItemRangeInserted(0, cardList.size)
    }


    class VH(override val containerView: View,val iHomeCardOnClick: IHomeCardOnClick) :
            RecyclerView.ViewHolder(containerView),
            LayoutContainer, View.OnClickListener {
            var cardModel:CardModel?=null

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun onBind(id: CardModel) {
            cardModel=id
            containerView.rec_add_number.text= cardNumber(id.cardnum!!.substring(0,16))
            containerView.rec_add_money.text="${id.money.toString().toDouble().formatDecimals()}.00 so'm"
            containerView.rec_add_date_year.text = cardDateUtils(id.cardnum.substring(16, 20))
            containerView.rec_firstname.text = id.firstname
            containerView.rec_surname.text = id.surname
        }

        override fun onClick(v: View?) {
            iHomeCardOnClick.onClickItem(cardModel!!)
        }
    }
}

