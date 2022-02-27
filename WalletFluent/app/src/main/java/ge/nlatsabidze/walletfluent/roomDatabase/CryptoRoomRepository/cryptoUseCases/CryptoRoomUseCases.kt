package ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases

data class CryptoRoomUseCases(
    val deleteAllValuesUseCase: DeleteAllValuesUseCase,
    val insertValuesUseCase: InsertValuesUseCase,
    val getAllValuesUseCase: GetAllValuesUseCase
)