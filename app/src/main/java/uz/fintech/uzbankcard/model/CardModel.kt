package uz.fintech.uzbankcard.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardModel(
        val firstname: String? ="",
        val surname: String? ="",
        val cardname: String? ="",
        val money:Long=0L,
        val cardnum: String? ="",
        val cardid:Long=0L
):Parcelable
