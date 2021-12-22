package ge.nlatsabidze.walletfluent.model.cryptoModel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Exchanges(
    @Json(name = "country")
    val country: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "has_trading_incentive")
    val hasTradingIncentive: Boolean?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "trade_volume_24h_btc")
    val tradeVolume24hBtc: Double?,
    @Json(name = "trade_volume_24h_btc_normalized")
    val tradeVolume24hBtcNormalized: Double?,
    @Json(name = "trust_score")
    val trustScore: Int?,
    @Json(name = "trust_score_rank")
    val trustScoreRank: Int?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "year_established")
    val yearEstablished: Int?
)