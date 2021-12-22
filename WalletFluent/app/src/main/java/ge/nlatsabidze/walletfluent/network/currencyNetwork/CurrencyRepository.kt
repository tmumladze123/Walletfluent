package ge.nlatsabidze.walletfluent.network.currencyNetwork
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private var apiService: CurrencyApi) {

    suspend fun getCountryCurrencies(): Flow<Resource<Currency>>{
        return flow {
            emit(handleResponse {
                apiService.getCurrencies()
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getConvertedValues(amount: Double, from: String, to: String): Flow<Resource<Converter>> {
        return flow {
            emit(handleResponse {
                apiService.getConverterValues(amount, from, to)
            })
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun <T> handleResponse(apiCall: suspend() -> Response<T>): Resource<T> {
        try {
            val response = apiCall()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return Resource.Success(body)
            }
            return Resource.Error("exception")

        }catch (e: Exception) {
            return Resource.Error("exception")
        }
    }
}