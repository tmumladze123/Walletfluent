package ge.nlatsabidze.walletfluent.network.currencyNetwork

import ge.nlatsabidze.walletfluent.constants.get.GetResponse.API_KEY
import ge.nlatsabidze.walletfluent.constants.get.GetResponse.GET_COMMERCIAL
import ge.nlatsabidze.walletfluent.constants.get.GetResponse.GET_CONVERTER
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyApi {

    @Headers(API_KEY)
    @GET(GET_COMMERCIAL)
    suspend fun getCurrencies() : Response<Currency>

    @Headers(API_KEY)
    @GET(GET_CONVERTER)
    suspend fun getConverterValues(
        @Query("amount") amount: Double,
        @Query("from") from: String,
        @Query("to") to: String
    ) : Response<Converter>
}
