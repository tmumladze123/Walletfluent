package ge.nlatsabidze.walletfluent.roomDatabase

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates

interface RoomRepository {
    fun deleteAllValues()
    suspend fun insertValues(list: List<CommercialRates>){}
    suspend fun insert(list: List<MarketsItem>){}
}