package ge.nlatsabidze.walletfluent.ui.currency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.CommercialRates
import ge.nlatsabidze.walletfluent.model.Currency
import ge.nlatsabidze.walletfluent.network.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(private val networkRepository: NetworkRepository): ViewModel() {

    private val _commercialRates = MutableStateFlow<List<CommercialRates>>(mutableListOf())
    val commercialRates: MutableStateFlow<List<CommercialRates>> get() = _commercialRates

    fun getCommercialRates() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                networkRepository.getCountryCurrencies().collectLatest {
                    _commercialRates.value = it.data?.commercialRatesList!!
                }
            }
        }
    }
}