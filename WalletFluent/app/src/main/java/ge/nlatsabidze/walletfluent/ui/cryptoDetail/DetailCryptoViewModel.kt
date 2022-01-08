package ge.nlatsabidze.walletfluent.ui.cryptoDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.cryptoModel.ChartItem
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailCryptoViewModel @Inject constructor(private val cryptoService: CryptoRepository): ViewModel() {

    private val _chartValues = MutableSharedFlow<List<List<Any>>>()
    val chartValues: MutableSharedFlow<List<List<Any>>> get() = _chartValues

    fun getChartValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                cryptoService.getChartValues().collect {
                    if (it.data?.prices != null) {
                        _chartValues.emit(it.data.prices)
                    }
                }
            }
        }
    }
}