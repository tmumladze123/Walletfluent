package ge.nlatsabidze.walletfluent.model.valuteModel


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "commercial_table")
@JsonClass(generateAdapter = true)
data class CommercialRates(
    @Json(name = "buy")
    val buy: Double?,
    @Json(name = "currency")
    val currency: String?,
    @Json(name = "sell")
    val sell: Double?
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}