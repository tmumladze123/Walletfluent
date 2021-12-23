package ge.nlatsabidze.walletfluent.network.cryptoNetwork

import ge.nlatsabidze.walletfluent.model.cryptoModel.Exchanges
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface CryptoApi {

    @GET("exchanges?per_page=20&page=1")
    suspend fun getCryptoExchanges(): Response<List<Exchanges>>
}