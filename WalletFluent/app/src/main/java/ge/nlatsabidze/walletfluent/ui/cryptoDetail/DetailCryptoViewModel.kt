package ge.nlatsabidze.walletfluent.ui.cryptoDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepositoryImpl
import ge.nlatsabidze.walletfluent.useCases.GetChartValuesUseCase
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailCryptoViewModel @Inject constructor(private val getChartValuesUseCase: GetChartValuesUseCase): ViewModel() {

    suspend fun getValues(id: String) = getChartValuesUseCase(id).stateIn(viewModelScope)
}