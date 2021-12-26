package ge.nlatsabidze.walletfluent.ui.crypto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.cryptoModel.Exchanges
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

    private val _cryptoExchangedValues = MutableSharedFlow<List<Exchanges>>()
    val cryptoExchangedValues: MutableSharedFlow<List<Exchanges>> get() = _cryptoExchangedValues

    private var _showLoadingViewModelState = MutableStateFlow<Boolean>(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    fun getCryptoExchangeValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                cryptoService.getCryptoExchanges().collectLatest {
                    _cryptoExchangedValues.emit(it.data!!)
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