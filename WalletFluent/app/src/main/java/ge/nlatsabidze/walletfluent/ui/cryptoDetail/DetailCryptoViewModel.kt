package ge.nlatsabidze.walletfluent.ui.cryptoDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepositoryImpl
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailCryptoViewModel @Inject constructor(private val cryptoService: CryptoRepositoryImpl): ViewModel() {

    suspend fun getValues(id: String) =
        cryptoService.getChartValues(id).stateIn(viewModelScope)
}