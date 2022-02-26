package ge.nlatsabidze.walletfluent.useCases

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.cryptoModel.ChartItem
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChartValuesUseCase @Inject constructor(
    private val currencyRepository: CryptoRepository
) {
    operator fun invoke(id: String): Flow<Resource<ChartItem>> = flow {
        emit(Resource.Loading())
        emit(currencyRepository.getChartValues(id))
    }
}