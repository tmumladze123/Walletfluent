package ge.nlatsabidze.walletfluent.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Currency(
    @Json(name = "base")
    val base: String?,
    @Json(name = "commercialRatesList")
    val commercialRatesList: List<CurrencyItem>?
)
