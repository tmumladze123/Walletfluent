package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import kotlinx.coroutines.flow.Flow

interface CurrencyRoomRepository {
    fun deleteAllValues()
    suspend fun insertValues(list: List<CommercialRates>)
    fun getAllValues(): Flow<List<CommercialRates>>
}