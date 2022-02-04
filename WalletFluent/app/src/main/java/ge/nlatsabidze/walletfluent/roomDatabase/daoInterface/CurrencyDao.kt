package ge.nlatsabidze.walletfluent.roomDatabase.daoInterface

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marketItem: List<MarketsItem>)

    @Query("SELECT * FROM marketItem")
    fun getCryptoValues(): Flow<List<MarketsItem>>

    @Query("DELETE FROM marketItem")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(marketItem: List<CommercialRates>)

    @Query("SELECT * FROM commercial_table")
    fun getCurrencyValues(): Flow<List<CommercialRates>>

    @Query("DELETE FROM commercial_table")
    fun deleteAllFromCurrency()

}