package ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases

import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases.DeleteAllValuesUseCase
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases.GetAllValuesUseCase
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases.InsertValuesUseCase

data class CurrencyRoomUseCases(
    val deleteValues: DeleteValues,
    val getValuesUseCase: GetValuesUseCase,
    val insertValuesUseCase: InsertUseCases,
)

