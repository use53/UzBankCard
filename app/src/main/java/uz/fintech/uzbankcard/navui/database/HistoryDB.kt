package uz.fintech.uzbankcard.navui.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class HistoryDB (
     val namedb:String,
     val surnamedb:String,
     val cardnumdb:String,
     val cardDate:String,
     @PrimaryKey(autoGenerate = true)
     val dbid:Long=0L
)