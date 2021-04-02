package uz.fintech.uzbankcard.navui.onclikc

import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory

interface IPaymentHistoryDelete {
    fun onClickDelete(payment:PaymentHistory)
}