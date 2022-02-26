package ge.nlatsabidze.walletfluent.useCases

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetConvertedValuesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    operator fun invoke(amount: Double, from: String, to: String): Flow<Resource<Converter>> = flow {
        emit(Resource.Loading())
        emit(currencyRepository.getConvertedValues(amount, from, to))
    }
}