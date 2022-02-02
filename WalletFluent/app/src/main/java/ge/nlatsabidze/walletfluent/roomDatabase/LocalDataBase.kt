package ge.nlatsabidze.walletfluent.roomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import ge.nlatsabidze.walletfluent.roomDatabase.daoInterface.CurrencyDao

@Database(entities = [MarketsItem::class, CommercialRates::class], version = 1)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun userDao(): CurrencyDao
}