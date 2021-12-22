package ge.nlatsabidze.walletfluent.model.valuteModel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommercialRates(
    @Json(name = "buy")
    val buy: Double?,
    @Json(name = "currency")
    val currency: String?,
    @Json(name = "sell")
    val sell: Double?
)