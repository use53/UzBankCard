package uz.fintech.uzbankcard.navui.onclikc

import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.model.PaymentModel

interface IPaymentOnClick {
    fun onClickListener(paymentModel: PaymentModel)
}