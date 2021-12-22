package ge.nlatsabidze.walletfluent.network.cryptoNetwork

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.cryptoModel.Exchanges
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CryptoRepository @Inject constructor(private var apiService: CryptoApi) {

    suspend fun getCryptoExchanges(): Flow<Resource<List<Exchanges>>>{
        return flow {
            emit(handleResponse {
                apiService.getCryptoExchanges()
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