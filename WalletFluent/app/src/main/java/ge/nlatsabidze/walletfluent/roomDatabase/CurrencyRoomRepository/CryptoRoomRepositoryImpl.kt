package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import ge.nlatsabidze.walletfluent.roomDatabase.RoomRepository
import ge.nlatsabidze.walletfluent.roomDatabase.daoInterface.CurrencyDao
import javax.inject.Inject

class CryptoRoomRepositoryImpl @Inject constructor(private val currencyDao: CurrencyDao):
    RoomRepository {

    override fun deleteAllValues() {
        currencyDao.deleteAll()
    }

    override suspend fun insert(marketItem: List<MarketsItem>) {
        currencyDao.insert(marketItem)
    }

    val cryptoValues = currencyDao.getCryptoValues()

}