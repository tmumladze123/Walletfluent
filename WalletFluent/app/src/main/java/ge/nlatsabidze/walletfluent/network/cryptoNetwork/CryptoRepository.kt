package ge.nlatsabidze.walletfluent.network.cryptoNetwork

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.cryptoModel.ChartItem
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    suspend fun getMarketValues(): Flow<Resource<List<MarketsItem>>>
    suspend fun getChartValues(id: String): Flow<Resource<ChartItem>>
}