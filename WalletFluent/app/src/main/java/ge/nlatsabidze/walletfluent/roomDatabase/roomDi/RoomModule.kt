package ge.nlatsabidze.walletfluent.roomDatabase.roomDi

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoApi
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepository
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepositoryImpl
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.CryptoRoomRepository
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.CryptoRoomRepositoryImpl
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases.CryptoRoomUseCases
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases.DeleteAllValuesUseCase
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases.GetAllValuesUseCase
import ge.nlatsabidze.walletfluent.roomDatabase.CryptoRoomRepository.cryptoUseCases.InsertValuesUseCase
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyRoomRepoImpl
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyRoomRepository
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases.CurrencyRoomUseCases
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases.DeleteValues
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases.GetValuesUseCase
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases.InsertUseCases
import ge.nlatsabidze.walletfluent.roomDatabase.LocalDataBase
import ge.nlatsabidze.walletfluent.roomDatabase.daoInterface.CurrencyDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        LocalDataBase::class.java,
        "db_name"
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(db: LocalDataBase) = db.userDao()


    @Provides
    @Singleton
    fun provideCryptoRoomRepository(api: CurrencyDao): CryptoRoomRepository {
        return CryptoRoomRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCryptoUseCases(api: CryptoRoomRepository): CryptoRoomUseCases {
        return CryptoRoomUseCases(
            deleteAllValuesUseCase = DeleteAllValuesUseCase(api),
            insertValuesUseCase = InsertValuesUseCase(api),
            getAllValuesUseCase = GetAllValuesUseCase(api)
        )
    }

    @Provides
    @Singleton
    fun provideCurrencyUseCases(api: CurrencyRoomRepository): CurrencyRoomUseCases {
        return CurrencyRoomUseCases(
            deleteValues = DeleteValues(api),
            getValuesUseCase = GetValuesUseCase(api),
            insertValuesUseCase = InsertUseCases(api)
        )
    }

    @Provides
    @Singleton
    fun provideCryptoRoomRepository1(api: CurrencyDao): CurrencyRoomRepository {
        return CurrencyRoomRepoImpl(api)
    }

}