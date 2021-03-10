package uz.fintech.uzbankcard.navui.databasae

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.fintech.uzbankcard.navui.database.HistoryDao
import uz.fintech.uzbankcard.navui.database.HistoryDB


@Database(entities = [HistoryDB::class],version = 1,exportSchema = false)
abstract class HistoryDatabase:RoomDatabase(){
    abstract fun getCardDao():HistoryDao
}