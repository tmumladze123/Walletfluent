package ge.nlatsabidze.walletfluent.model.valuteModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Converter(
    @Json(name = "amount")
    val amount: Double?,
    @Json(name = "from")
    val from: String?,
    @Json(name = "to")
    val to: String?,
    @Json(name = "value")
    val value: Double?
)
