package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases

import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetValuesUseCase @Inject constructor(
    private val currencyRoomRepository: CurrencyRoomRepository
) {
    operator fun invoke(): Flow<List<CommercialRates>> {
        return currencyRoomRepository.getAllValues()
    }
}