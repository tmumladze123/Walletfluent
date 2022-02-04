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

class CurrencyRepository @Inject constructor(private var apiService: CurrencyApi) {

    private var _showLoading = MutableStateFlow<Boolean>(false)

    suspend fun getCountryCurrencies(): Flow<Resource<Currency>> {
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
        _showLoading.value = true
        try {
            val response = apiCall()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                _showLoading.value = false
                return Resource.Success(body)
            }
            return Resource.Error(response.errorBody().toString())

        }catch (e: Exception) {
            return Resource.Error("exception")
        }
    }

    fun showLoadingError(): MutableStateFlow<Boolean> {
        return _showLoading
    }
}