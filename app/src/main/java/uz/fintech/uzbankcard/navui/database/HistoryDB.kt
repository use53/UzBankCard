package uz.fintech.uzbankcard.navui.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class HistoryDB(
        @PrimaryKey(autoGenerate = true)
        val dbid: Long = 0L,
        val cardnumdb: String,
        val cardcolor:Int=0

)