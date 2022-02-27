package ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.CryptoRoomRepository
import javax.inject.Inject

class InsertValuesUseCase @Inject constructor(
    private val cryptoRoomRepository: CryptoRoomRepository
) {
    suspend operator fun invoke(list: List<MarketsItem>) {
        cryptoRoomRepository.insertValues(list)
    }
}