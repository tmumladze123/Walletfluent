package ge.nlatsabidze.walletfluent.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyItem(
    @Json(name = "buy")
    val buy: Double?,
    @Json(name = "currency")
    val currency: String?,
    @Json(name = "sell")
    val sell: Double?
)
