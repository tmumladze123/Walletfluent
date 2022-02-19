package ge.nlatsabidze.walletfluent.ui.crypto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepositoryImpl
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CryptoRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val cryptoService: CryptoRepositoryImpl,
    private val currencyRoomRepository: CryptoRoomRepository
) : ViewModel() {

    private var _showLoadingViewModelState = MutableStateFlow<Boolean>(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    private val _marketValues = MutableStateFlow(listOf<MarketsItem>())
    val marketValues: MutableStateFlow<List<MarketsItem>> get() = _marketValues

    val getCryptoValues = currencyRoomRepository.cryptoValues

    fun getCryptoExchangeValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                cryptoService.getMarketValues().collectLatest {
                    if (it.data != null) {
                        _marketValues.value = it.data
                        currencyRoomRepository.deleteAll()
                        currencyRoomRepository.insert(it.data)
                    }
                }
            }
        }
    }

    fun showLoadingBar() {
        viewModelScope.launch {
            cryptoService.showLoading.collectLatest {
                _showLoadingViewModelState.value = it
            }
        }
    }
}