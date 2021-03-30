package uz.fintech.uzbankcard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.payment_layout.view.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.formatDecimals
import uz.fintech.uzbankcard.common.inflate
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory

class SavePaymentAdapter() : ListAdapter<PaymentHistory,
        SavePaymentAdapter.VHPayment>(PaymentCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHPayment {
        val view =parent.inflate(LayoutInflater.from(parent.context),R.layout.payment_layout)
        return VHPayment(view)
    }

    override fun onBindViewHolder(holder: VHPayment, position: Int) {
        val pos = getItem(position)
        holder.onBind(pos)
    }

    class VHPayment(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun onBind(model: PaymentHistory) {
            itemView.tv_money_payment.text = "${model.moneyPt.toDouble().formatDecimals()}.00 so'm"
            itemView.tv_name_payment.text = model.namePt
        }
    }
}

class PaymentCallback : DiffUtil.ItemCallback<PaymentHistory>() {
    override fun areItemsTheSame(oldItem: PaymentHistory, newItem: PaymentHistory): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PaymentHistory, newItem: PaymentHistory): Boolean {
        return oldItem.ptId == newItem.ptId
    }

}