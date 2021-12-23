package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyPageViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) : ViewModel() {
    private val _commercialRates = MutableSharedFlow<List<CommercialRates>>()
    val commercialRates: MutableSharedFlow<List<CommercialRates>> get() = _commercialRates

    private var _showLoadingViewModelState = MutableStateFlow<Boolean>(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState


    fun getCommercialRates() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currencyRepository.getCountryCurrencies().collectLatest {
                    _commercialRates.emit(it.data?.commercialRatesList!!)
                }
            }
        }
    }

    fun showLoadingBar() {
        viewModelScope.launch {
            currencyRepository.showLoading.collectLatest {
                _showLoadingViewModelState.value = it
            }
        }
    }
}