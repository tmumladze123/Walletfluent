package ge.nlatsabidze.walletfluent.networkmodule

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoApi
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideRetrofitCurrency(): CurrencyApi =
        Retrofit.Builder().baseUrl("https://test-api.tbcbank.ge/v1/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofitCrypto(): CryptoApi =
        Retrofit.Builder().baseUrl("https://api.coingecko.com/api/v3/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(CryptoApi::class.java)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideDatabaseReference(): DatabaseReference =
        FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")

    @Provides
    @Singleton
    fun provideFirebaseUser(): FirebaseUser =
        FirebaseAuth.getInstance().currentUser!!

}
