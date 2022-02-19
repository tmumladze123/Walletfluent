package ge.nlatsabidze.walletfluent.network.currencyNetwork

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    suspend fun getCountryCurrencies(): Flow<Resource<Currency>>
    suspend fun getConvertedValues(amount: Double, from: String, to: String): Flow<Resource<Converter>>
}