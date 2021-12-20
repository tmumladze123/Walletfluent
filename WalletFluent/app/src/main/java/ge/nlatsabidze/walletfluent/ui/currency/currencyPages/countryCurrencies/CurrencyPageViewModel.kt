package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.CommercialRates
import ge.nlatsabidze.walletfluent.network.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyPageViewModel @Inject constructor(private val networkRepository: NetworkRepository) : ViewModel() {
    private val _commercialRates = MutableSharedFlow<List<CommercialRates>>()
    val commercialRates: MutableSharedFlow<List<CommercialRates>> get() = _commercialRates

    fun getCommercialRates() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                networkRepository.getCountryCurrencies().collectLatest {
                    _commercialRates.emit(it.data?.commercialRatesList!!)
                }
            }
        }
    }

}