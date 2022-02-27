package ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases

import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.CryptoRoomRepository
import javax.inject.Inject

class DeleteAllValuesUseCase @Inject constructor(
    private val cryptoRoomRepository: CryptoRoomRepository
) {
    operator fun invoke() {
        cryptoRoomRepository.deleteAllValues()
    }
}