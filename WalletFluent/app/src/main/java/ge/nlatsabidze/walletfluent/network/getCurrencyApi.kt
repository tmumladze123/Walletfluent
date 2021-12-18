package ge.nlatsabidze.walletfluent.network

import ge.nlatsabidze.walletfluent.model.CommercialRates
import ge.nlatsabidze.walletfluent.model.Currency
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CurrencyApi {

    @Headers("apikey: HA0bikRGdUr9H9x3RO2godcdVP4uZAjH")
    @GET("exchange-rates/commercial")
    suspend fun getCurrencies() : Response<Currency>
}
