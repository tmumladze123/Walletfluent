package ge.nlatsabidze.walletfluent.ui.crypto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import ge.nlatsabidze.walletfluent.network.cryptoNetwork.CryptoRepositoryImpl
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CryptoRoomRepositoryImpl
import ge.nlatsabidze.walletfluent.useCases.GetMarketValuesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val getMarketValuesUseCase: GetMarketValuesUseCase,
    private val currencyRoomRepository: CryptoRoomRepositoryImpl
) : ViewModel() {

    private var _showLoadingViewModelState = MutableStateFlow<Boolean>(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    private val _marketValues = MutableStateFlow(listOf<MarketsItem>())
    val marketValues: MutableStateFlow<List<MarketsItem>> get() = _marketValues

    val getCryptoValues = currencyRoomRepository.cryptoValues

    fun getCryptoExchangeValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getMarketValuesUseCase().collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            _showLoadingViewModelState.value = false
                            _marketValues.value = it.data!!
                            currencyRoomRepository.deleteAllValues()
                            currencyRoomRepository.insert(it.data)
                        }
                        is Resource.Loading -> {
                            _showLoadingViewModelState.value = true
                        }
                    }
                }
            }
        }
    }
}