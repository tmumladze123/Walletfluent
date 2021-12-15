package ge.nlatsabidze.walletfluent.network

import ge.nlatsabidze.walletfluent.model.Currency
import retrofit2.Response
import retrofit2.http.GET

interface getCurrencyApi {
    @GET("80d25aee-d9a6-4e9c-b1d1-80d2a7c979bf")
    suspend fun getCurrencies() : Response<Currency>
}
