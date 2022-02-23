package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.roomDatabase.daoInterface.CurrencyDao
import javax.inject.Inject

class CurrencyRoomRepo @Inject constructor(private val currencyDao: CurrencyDao) {

    fun deleteAll() {
        currencyDao.deleteAllFromCurrency()
    }

    suspend fun insert(commercialRates: List<CommercialRates>) {
        currencyDao.insertCurrency(commercialRates)
    }

    val currencyValues = currencyDao.getCurrencyValues()
}