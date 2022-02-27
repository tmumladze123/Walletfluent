package ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import kotlinx.coroutines.flow.Flow

interface CryptoRoomRepository {
    fun deleteAllValues()
    suspend fun insertValues(list: List<MarketsItem>)
    fun getAllValues(): Flow<List<MarketsItem>>
}