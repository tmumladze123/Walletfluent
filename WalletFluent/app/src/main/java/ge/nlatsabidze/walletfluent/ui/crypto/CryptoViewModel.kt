package ge.nlatsabidze.walletfluent.ui.crypto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.cryptoModel.ChartItem
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(private val cryptoService: CryptoRepository) : ViewModel() {

    private val _marketItemsValues = MutableSharedFlow<List<MarketsItem>>()
    val marketItems: MutableSharedFlow<List<MarketsItem>> get() = _marketItemsValues

    private var _showLoadingViewModelState = MutableStateFlow<Boolean>(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    fun getCryptoExchangeValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                cryptoService.getMarketValues().collectLatest {
                    if (it.data != null) {
                        _marketItemsValues.emit(it.data)
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