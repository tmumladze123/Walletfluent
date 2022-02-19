package ge.nlatsabidze.walletfluent.network.cryptoNetwork

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.cryptoModel.ChartItem
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(private var apiService: CryptoApi): CryptoRepository {

    private var _showLoading = MutableStateFlow<Boolean>(false)
    val showLoading: MutableStateFlow<Boolean> get() = _showLoading

    override suspend fun getMarketValues(): Flow<Resource<List<MarketsItem>>> {
        return flow {
            emit(handleResponse {
                apiService.getMarketItems()
            })
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getChartValues(id: String): Flow<Resource<ChartItem>> {
        return flow {
            emit (handleResponse {
                apiService.getChartItems(id)
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
}