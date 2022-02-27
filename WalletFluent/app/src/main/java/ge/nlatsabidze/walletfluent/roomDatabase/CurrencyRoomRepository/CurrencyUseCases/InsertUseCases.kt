package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases

import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertUseCases @Inject constructor(
    private val currencyRoomRepository: CurrencyRoomRepository
) {
    suspend operator fun invoke(list: List<CommercialRates>) {
        currencyRoomRepository.insertValues(list)
    }
}