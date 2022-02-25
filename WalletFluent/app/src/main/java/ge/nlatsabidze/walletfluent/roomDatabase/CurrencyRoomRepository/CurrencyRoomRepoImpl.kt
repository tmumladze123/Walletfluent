package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository

import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.roomDatabase.RoomRepository
import ge.nlatsabidze.walletfluent.roomDatabase.daoInterface.CurrencyDao
import javax.inject.Inject

class CurrencyRoomRepoImpl @Inject constructor(private val currencyDao: CurrencyDao): RoomRepository {

    override fun deleteAllValues() {
        currencyDao.deleteAllFromCurrency()
    }

    override suspend fun insertValues(list: List<CommercialRates>) {
        currencyDao.insertCurrency(list)
    }

    val currencyValues = currencyDao.getCurrencyValues()
}