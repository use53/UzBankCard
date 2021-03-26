package uz.fintech.uzbankcard.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize



@Parcelize
class SelectionModel(
        val title: String? = "",
        val photo: String? = "",
        val slid: Long = 0L
) : Parcelable