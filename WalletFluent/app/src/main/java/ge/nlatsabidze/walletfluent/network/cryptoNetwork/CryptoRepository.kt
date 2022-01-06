package ge.nlatsabidze.walletfluent.network.cryptoNetwork

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CryptoRepository @Inject constructor(private var apiService: CryptoApi) {

    private var _showLoading = MutableStateFlow<Boolean>(false)
    val showLoading: MutableStateFlow<Boolean> get() = _showLoading

    suspend fun getMarketValues(): Flow<Resource<List<MarketsItem>>> {
        return flow {
            emit(handleResponse {
                apiService.getMarketItems()
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
            return Resource.Error("exceptionMessage")

        }catch (e: Exception) {
            return Resource.Error("exception")
        }
    }
}