package uz.fintech.uzbankcard.navui.database.paymentsave

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class PaymentHistory (
    val namePt:String,
    val moneyPt:Long,
    val datePt:String,
    @PrimaryKey(autoGenerate = true)
    val ptId:Long=0L
)