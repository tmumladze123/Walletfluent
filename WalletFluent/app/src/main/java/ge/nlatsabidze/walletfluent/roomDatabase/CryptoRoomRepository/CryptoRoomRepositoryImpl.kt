package ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyRoomRepository
import ge.nlatsabidze.walletfluent.roomDatabase.daoInterface.CurrencyDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CryptoRoomRepositoryImpl @Inject constructor(private val currencyDao: CurrencyDao):
    CryptoRoomRepository {

    override fun deleteAllValues() {
        currencyDao.deleteAll()
    }

    override suspend fun insertValues(list: List<MarketsItem>) {
        currencyDao.insert(list)
    }

    override fun getAllValues(): Flow<List<MarketsItem>> {
        return currencyDao.getCryptoValues()
    }

}