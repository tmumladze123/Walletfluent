package ge.nlatsabidze.walletfluent.roomDatabase.roomDi

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ge.nlatsabidze.walletfluent.roomDatabase.LocalDataBase
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

}