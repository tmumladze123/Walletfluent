package ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases

import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.CryptoRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllValuesUseCase @Inject constructor(
    private val cryptoRoomRepository: CryptoRoomRepository
) {
    operator fun invoke(): Flow<List<MarketsItem>> {
        return cryptoRoomRepository.getAllValues()
    }
}