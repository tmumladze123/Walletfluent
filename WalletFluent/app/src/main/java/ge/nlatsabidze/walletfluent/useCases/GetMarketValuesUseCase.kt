package ge.nlatsabidze.walletfluent.useCases

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepository
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMarketValuesUseCase @Inject constructor(
    private val currencyRepository: CryptoRepository
) {
    operator fun invoke(): Flow<Resource<List<MarketsItem>>> = flow {
        emit(Resource.Loading())
        emit(currencyRepository.getMarketValues())
    }
}