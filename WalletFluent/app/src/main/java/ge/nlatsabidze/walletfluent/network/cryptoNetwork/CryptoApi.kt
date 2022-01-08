package ge.nlatsabidze.walletfluent.network.cryptoNetwork

import ge.nlatsabidze.walletfluent.model.cryptoModel.ChartItem
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import retrofit2.Response
import retrofit2.http.GET


interface CryptoApi {

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false")
    suspend fun getMarketItems(): Response<List<MarketsItem>>

    @GET("coins/bitcoin/market_chart?vs_currency=usd&days=1")
    suspend fun getChartItems(): Response<ChartItem>

}