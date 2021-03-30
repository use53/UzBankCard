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
        var cardcolor:Int=0,
        val color:Long=0L,
        val cardid:Long=0L
):Parcelable
