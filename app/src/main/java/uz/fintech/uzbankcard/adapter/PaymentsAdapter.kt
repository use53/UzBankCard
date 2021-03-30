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
import uz.fintech.uzbankcard.common.inflate
import uz.fintech.uzbankcard.model.PaymentModel
import uz.fintech.uzbankcard.navui.onclikc.IPaymentOnClick


class PaymentsAdapter(val iHomeOnClick: IPaymentOnClick): ListAdapter<PaymentModel,
        PaymentsAdapter.PaymentVH>(ItemAdapterCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentVH {
        val view =parent.inflate(LayoutInflater.from(parent.context),R.layout.payment_item_layout)
        return PaymentVH(view,iHomeOnClick)
    }

    override fun onBindViewHolder(holder: PaymentVH, position: Int) {
        val item=getItem(position)
        holder.onBind(item)
    }
    class PaymentVH(view:View,val iHomeOnClick: IPaymentOnClick)
        :RecyclerView.ViewHolder(view) {
            var paymentModel:PaymentModel?=null
        init {
            itemView.setOnClickListener {
                paymentModel?.let { it1 -> iHomeOnClick.onClickListener(it1) }
            }
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