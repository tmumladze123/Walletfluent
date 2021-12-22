package ge.nlatsabidze.walletfluent.ui.crypto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.cryptoModel.Exchanges
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(private val cryptoService: CryptoRepository) : ViewModel() {

    private val _cryptoExchangedValues = MutableSharedFlow<List<Exchanges>>()
    val cryptoExchangedValues: MutableSharedFlow<List<Exchanges>> get() = _cryptoExchangedValues

    fun getCryptoExchangeValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                cryptoService.getCryptoExchanges().collectLatest {
                    _cryptoExchangedValues.emit(it.data!!)
                }
            }
        }
    }
}