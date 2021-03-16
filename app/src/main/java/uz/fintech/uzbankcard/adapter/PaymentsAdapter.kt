package uz.fintech.uzbankcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.payment_item_layout.view.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.model.PaymentModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeOnClick


class PaymentsAdapter(val iHomeOnClick: IHomeOnClick): ListAdapter<PaymentModel,
        PaymentsAdapter.PaymentVH>(ItemAdapterCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentVH {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.payment_item_layout,parent,false)
        return PaymentVH(view,iHomeOnClick)
    }

    override fun onBindViewHolder(holder: PaymentVH, position: Int) {
        val item=getItem(position)
        holder.onBind(item)
    }
    class PaymentVH(view:View,val iHomeOnClick: IHomeOnClick)
        :RecyclerView.ViewHolder(view), View.OnClickListener {
            var paymentModel:PaymentModel?=null
        init {
            itemView.setOnClickListener(this)
        }

        fun onBind(paymentModel:PaymentModel){
            
            this.paymentModel=paymentModel
           itemView.tv_payment.text=paymentModel.payname
            Picasso.get()
                .load(paymentModel.payimage)
                .resize(50, 50)
                .centerCrop()
                .into(itemView.image_payment)
        }

        override fun onClick(v: View?) {
            iHomeOnClick.onClickListener(paymentModel!!)
        }
    }

}

class ItemAdapterCallback:DiffUtil.ItemCallback<PaymentModel>(){
    override fun areItemsTheSame(oldItem: PaymentModel, newItem: PaymentModel): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: PaymentModel, newItem: PaymentModel): Boolean {
        return oldItem.paymentId==newItem.paymentId
    }

}