package uz.fintech.uzbankcard.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class PaymentModel(
        val money:Long=0L,
        val payname:String="",
        val payimage:String="",
        val paymentId:Long=0L
)

