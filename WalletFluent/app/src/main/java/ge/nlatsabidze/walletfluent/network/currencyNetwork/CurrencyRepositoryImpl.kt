package ge.nlatsabidze.walletfluent.network.currencyNetwork

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private var apiService: CurrencyApi) :
    CurrencyRepository {

    override suspend fun getCountryCurrencies(): Resource<Currency> {
        return handleResponse {
            apiService.getCurrencies()
        }
    }

    override suspend fun getConvertedValues(
        amount: Double,
        from: String,
        to: String
    ): Resource<Converter> {
        return handleResponse {
            apiService.getConverterValues(amount, from, to)
        }
    }

    private suspend fun <T> handleResponse(apiCall: suspend () -> Response<T>): Resource<T> {
        try {
            val response = apiCall()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return Resource.Success(body)
            }
            return Resource.Error(response.errorBody().toString())

        } catch (e: Exception) {
            return Resource.Error("exception")
        }
    }

}