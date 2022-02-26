package ge.nlatsabidze.walletfluent.useCases

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.valuteModel.Currency
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCountryCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    operator fun invoke(): Flow<Resource<Currency>> = flow {
        emit(Resource.Loading())
        emit(currencyRepository.getCountryCurrencies())
    }
}