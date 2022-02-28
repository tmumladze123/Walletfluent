package ge.nlatsabidze.walletfluent.network.cryptoNetwork

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.cryptoModel.ChartItem
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(private var apiService: CryptoApi) :
    CryptoRepository {

    override suspend fun getMarketValues(): Resource<List<MarketsItem>> {
        return handleResponse {
            apiService.getMarketItems()
        }
    }

    override suspend fun getChartValues(id: String): Resource<ChartItem> {
        return handleResponse {
            apiService.getChartItems(id)
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