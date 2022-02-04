package ge.nlatsabidze.walletfluent.model.cryptoModel

data class ChartItem(
    val market_caps: List<List<Double>>?,
    val prices: List<List<Double>>?,
    val total_volumes: List<List<Double>>?
)