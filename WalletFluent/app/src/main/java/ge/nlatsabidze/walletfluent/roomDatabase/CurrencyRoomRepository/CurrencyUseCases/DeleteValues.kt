package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases

import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.CryptoRoomRepository
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyRoomRepository
import javax.inject.Inject

class DeleteValues @Inject constructor(
    private val currencyRoomRepository: CurrencyRoomRepository
) {
    operator fun invoke() {
        currencyRoomRepository.deleteAllValues()
    }
}