package ge.nlatsabidze.walletfluent.networkmodule

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
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()))
            .build()
            .create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofitCrypto(): CryptoApi =
        Retrofit.Builder().baseUrl("https://api.coingecko.com/api/v3/")
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()))
            .build()
            .create(CryptoApi::class.java)


}
