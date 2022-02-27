package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.roomDatabase.daoInterface.CurrencyDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyRoomRepoImpl @Inject constructor(private val currencyDao: CurrencyDao):
    CurrencyRoomRepository {

    override fun deleteAllValues() {
        currencyDao.deleteAllFromCurrency()
    }

    override suspend fun insertValues(list: List<CommercialRates>) {
        currencyDao.insertCurrency(list)
    }

    override fun getAllValues(): Flow<List<CommercialRates>> {
        return currencyDao.getCurrencyValues()
    }

//    val currencyValues = currencyDao.getCurrencyValues()
}